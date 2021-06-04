package cn.asany.storage.core;

import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.FilePart;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.FilePartService;
import cn.asany.storage.data.service.FileService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.spring.SpELUtil;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
@Transactional
public class FileUploadService {

    private static final Log LOG = LogFactory.getLog(FileUploadService.class);

    private static final String separator = "/";//NOSONAR
    private final FileService fileService;
    private final FilePartService filePartService;
    private final StorageResolver storageResolver;

    private static final Map<String, String> EXTENSIONS = new HashMap<>();

    static {
        EXTENSIONS.put("image/jpeg", "jpg");
        EXTENSIONS.put("image/gif", "gif");
        EXTENSIONS.put("image/png", "png");
        EXTENSIONS.put("image/bmp", "bmp");
    }

    @Autowired
    public FileUploadService(FileService fileService, FilePartService filePartService, StorageResolver storageResolver) {
        this.fileService = fileService;
        this.filePartService = filePartService;
        this.storageResolver = storageResolver;
    }

    private FileDetail uploadPart(FilePart part, FilePart filePart, List<FilePart> fileParts, String contentType, Info info, Storage Storage) throws IOException {
        FileDetail fileDetail = null;
        if (part == null) {
            List<FilePart> joinFileParts = new ArrayList<>();
            ObjectUtil.join(joinFileParts, fileParts, "index");

            if (joinFileParts.size() == info.getTotal()) {
                //临时文件
                File tmp = FileUtil.tmp();
                //合并 Part 文件
                try (FileOutputStream out = new FileOutputStream(tmp)) {
                    for (FilePart filesPart : joinFileParts) {
                        InputStream in = Storage.readFile(filesPart.getPath());
                        StreamUtil.copy(in, out);
                        StreamUtil.closeQuietly(in);
                        Storage.removeFile(filesPart.getPath());
                        ObjectUtil.remove(fileParts, SpELUtil.getExpression(" absolutePath == #value.getAbsolutePath() and fileManagerId == #value.getFileManagerId() "), filePart);
                    }
                }

                //保存合并后的新文件
                fileDetail = this.upload(tmp, contentType, info.getEntireFileName(), info.getEntireFileDir());

                //删除临时文件
                FileUtil.delFile(tmp);

                //删除 Part 文件
                for (FilePart filesPart : fileParts) {
                    Storage.removeFile(filesPart.getPath());
                }

                //在File_PART 表冗余一条数据 片段为 0
                filePartService.save(fileDetail.getPath(), fileDetail.getStorage(), info.getEntireFileHash(), info.getEntireFileHash(), info.getTotal(), 0);
            }
        } else {
            //删除 Part 文件
            for (FilePart filesPart : fileParts) {
                Storage.removeFile(filesPart.getPath());
            }
        }
        return fileDetail;
    }

    public FileDetail upload(MultipartFile file, Info info) throws IOException {
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        try {
            //判断是否为分段上传
            boolean isPart = info.isPart();
            //生成分段上传的文件名
            if (isPart && "blob".equalsIgnoreCase(fileName)) {
                fileName = info.getPartName();
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("上传文件参数:{fileName:" + contentType + ",contentType:" + fileName + ",dir:" + info.getDir() + ",isPart:" + isPart + "}");
            }

            //上传文件信息
            FileDetail fileDetail;

            if (isPart) {//如果为分段上传
                //获取文件上传目录的配置信息
                Space directory = fileService.getDirectory(info.getDir());
                Storage Storage = storageResolver.resolve(directory.getStorage().getId());

                FilePart filePart = filePartService.findByPartFileHash(info.getEntireFileHash(), info.getPartFileHash());
                if (filePart == null || (fileDetail = fileService.get(filePart.getPath())) == null) {//分段已上传信息
                    fileDetail = this.upload(file, info.getDir());
                    filePartService.save(fileDetail.getPath(), fileDetail.getStorage(), info.getEntireFileHash(), info.getPartFileHash(), info.getTotal(), info.getIndex());
                }
                //查询上传的片段
                List<FilePart> fileParts = filePartService.find(info.getEntireFileHash());
                FilePart part = ObjectUtil.remove(fileParts, "index", 0);
                FileDetail entireFileDetail = this.uploadPart(part, filePart, fileParts, contentType, info, Storage);
                if (entireFileDetail != null) {
                    fileDetail = entireFileDetail;
                }
            } else {
                fileDetail = this.upload(file, info.getDir());
            }
            return fileDetail;
        } catch (RuntimeException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 文件上传方法
     * <br/>dir 往下为分段上传参数
     *
     * @param attach      附件信息
     * @param contentType 附件类型
     * @param name        附件名称
     * @param info        附加信息
     * @return FileDetail
     * @throws IOException IO 异常
     */
    public FileDetail upload(File attach, String contentType, String name, Info info) throws IOException {
        try {
            //判断是否为分段上传
            boolean isPart = info.isPart();
            //生成分段上传的文件名
            String fileName = isPart && "blob".equalsIgnoreCase(name) ? info.getPartName() : name;

            if (LOG.isDebugEnabled()) {
                LOG.debug("上传文件参数:{fileName:" + contentType + ",contentType:" + fileName + ",dir:" + info.getDir() + ",isPart:" + isPart + "}");
            }

            //上传文件信息
            FileDetail fileDetail;

            if (isPart) {//如果为分段上传
                //获取文件上传目录的配置信息
                Space directory = fileService.getDirectory(info.getDir());
                Storage Storage = null;// fileManagerFactory.getFileManager(directory.getStorage().getId());

                FilePart filePart = filePartService.findByPartFileHash(info.getEntireFileHash(), info.getPartFileHash());
                if (filePart == null || (fileDetail = fileService.get(filePart.getPath())) == null) {//分段已上传信息
                    fileDetail = this.upload(attach, contentType, fileName, info.getDir());
                    filePartService.save(fileDetail.getPath(), fileDetail.getStorage(), info.getEntireFileHash(), info.getPartFileHash(), info.getTotal(), info.getIndex());
                }
                //查询上传的片段
                List<FilePart> fileParts = filePartService.find(info.getEntireFileHash());
                FilePart part = ObjectUtil.remove(fileParts, "index", 0);
                FileDetail entireFileDetail = this.uploadPart(part, filePart, fileParts, contentType, info, Storage);
                if (entireFileDetail != null) {
                    fileDetail = entireFileDetail;
                }
            } else {
                fileDetail = this.upload(attach, contentType, fileName, info.getDir());
            }
            return fileDetail;
        } catch (RuntimeException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

    private FileDetail upload(MultipartFile file, String dir) throws IOException {
        InputStream input = file.getInputStream();
        if (input.markSupported()) {
            return upload(input, file.getContentType(), file.getOriginalFilename(), file.getSize(), dir);
        } else {
            File temp = null;
            try {
                temp = FileUtil.tmp();
                StreamUtil.copyThenClose(input, new FileOutputStream(temp));
                return this.upload(temp, file.getContentType(), file.getOriginalFilename(), dir);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
                throw e;
            } finally {
                if (temp != null) {
                    FileUtil.delFile(temp);
                }
            }
        }
    }

    public FileDetail upload(InputStream input, String contentType, String fileName, long size, String dir) throws IOException {
        Space directory = this.fileService.getDirectory(dir);
        // 设置 mask
        input.mark((int) size + 1);
        // 获取文件Md5码
        String md5 = DigestUtils.md5DigestAsHex(input);
        // 获取MimeType
        input.reset();
        String mimeType = FileUtil.getMimeType(input);
        // 获取虚拟目录
        String absolutePath = directory.getDirPath() + separator + DateUtil.format("yyyyMMdd") + separator + StringUtil.hexTo64("0" + UUID.randomUUID().toString().replaceAll("-", "")) + "." + StringUtil.defaultValue(EXTENSIONS.get(mimeType), WebUtil.getExtension(fileName));
        // 文件类型
        FileDetail fileDetail;
        // 获取真实目录
        String realPath;

        String fileManagerId = directory.getStorage().getId();

        Storage Storage = null;//fileManagerFactory.getFileManager(fileManagerId);

        fileDetail = fileService.getFileDetailByMd5(md5, fileManagerId);
        if (fileDetail == null || Storage.getFileItem(fileDetail.getRealPath()) == null) {
            realPath = separator + mimeType + separator + StringUtil.hexTo64("0" + md5) + "." + StringUtil.defaultValue(EXTENSIONS.get(mimeType), WebUtil.getExtension(fileName));
            input.reset();
//            TODO 中断
//            fileManager.writeFile(fileManager instanceof OSSFileManager ? absolutePath : realPath, input);
        } else {
            realPath = fileDetail.getRealPath();
        }
        return fileService.saveFileDetail(absolutePath, fileName, contentType, size, md5, realPath, fileManagerId, "");
    }

    public FileDetail upload(File attach, String contentType, String fileName, String dir) throws IOException {
        Space directory = this.fileService.getDirectory(dir);
        //获取文件Md5码
        String md5 = DigestUtils.md5DigestAsHex(new FileInputStream(attach));// 获取文件MD5

        String mimeType = FileUtil.getMimeType(attach);

        // 获取虚拟目录
        String absolutePath = directory.getDirPath() + separator + DateUtil.format("yyyyMMdd") + separator + StringUtil.hexTo64("0" + UUID.randomUUID().toString().replaceAll("-", "")) + "." + StringUtil.defaultValue(EXTENSIONS.get(mimeType), WebUtil.getExtension(fileName));
        // 文件类型
        FileDetail fileDetail;
        // 获取真实目录
        String realPath;

        String fileManagerId = directory.getStorage().getId();

        Storage Storage = null;// fileManagerFactory.getFileManager(fileManagerId);

        fileDetail = fileService.getFileDetailByMd5(md5, fileManagerId);
        if (fileDetail == null || Storage.getFileItem(fileDetail.getRealPath()) == null) {
            realPath = separator + mimeType + separator + StringUtil.hexTo64("0" + md5) + "." + StringUtil.defaultValue(EXTENSIONS.get(mimeType), WebUtil.getExtension(fileName));
//            TODO 中断
//            fileManager.writeFile(fileManager instanceof OSSFileManager ? absolutePath : realPath, attach);
        } else {
            return fileDetail;
        }
        return fileService.saveFileDetail(absolutePath, fileName, contentType, attach.length(), md5, realPath, fileManagerId, "");
    }

    public FileDetail upload(File attach, String absolutePath, String fileManagerId) throws IOException {
        //获取文件Md5码
        String md5 = DigestUtils.md5DigestAsHex(new FileInputStream(attach));// 获取文件MD5

        String mimeType = FileUtil.getMimeType(attach);

        // 文件类型
        FileDetail fileDetail;
        // 获取真实目录
        String realPath;

        String fileName = RegexpUtil.parseFirst(absolutePath, "[^/]+$");

        Storage Storage = null;// ileManagerFactory(fileManagerId);

        fileDetail = fileService.getFileDetailByMd5(md5, fileManagerId);
        if (fileDetail == null || Storage.getFileItem(fileDetail.getRealPath()) == null) {
            realPath = separator + mimeType + separator + StringUtil.hexTo64("0" + md5) + "." + StringUtil.defaultValue(EXTENSIONS.get(mimeType), WebUtil.getExtension(fileName));
//            TODO 中断
//            fileManager.writeFile(fileManager instanceof OSSFileManager ? absolutePath : realPath, attach);
        } else {
            return fileDetail;
        }
        return fileService.saveFileDetail(absolutePath, fileName, mimeType, attach.length(), md5, realPath, fileManagerId, "");
    }

}