package cn.asany.storage.data.rest;

import cn.asany.storage.api.FileItemSelector;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.core.engine.virtual.VirtualStorage;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.util.CompressionOptions;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.data.util.ZipUtil;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.framework.util.web.ServletUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DownloadController {

  private final FileService fileService;
  private final StorageResolver storageResolver;

  public DownloadController(FileService fileService, StorageResolver storageResolver) {
    this.fileService = fileService;
    this.storageResolver = storageResolver;
  }

  @GetMapping("/thumbnail/{id}")
  public void thumbnail(@PathVariable("id") String id) {
    System.out.println(id);
  }

  @GetMapping("/download")
  public void download(
      @RequestParam("fidlist") String fidlistString,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {

    String[] fidlist = StringUtil.tokenizeToStringArray(fidlistString, ",");

    if (fidlist.length == 0) {
      response.sendError(404, "请求的文件不存在");
      return;
    }

    IdUtils.FileKey firstFileKey = IdUtils.parseKey(fidlist[0]);
    Space space = firstFileKey.getSpace();
    Storage innerStorage = space.getStorage();

    VirtualStorage storage = new VirtualStorage(space, innerStorage, fileService, true);

    FileObject fileObject =
        fidlist.length > 1
            ? compressPacking(space, storage, fidlist)
            : storage.getFileItem(firstFileKey.getFile().getPath());

    response.setContentType(fileObject.getMimeType());
    response.setCharacterEncoding("UTF-8");

    ServletUtils.setContentDisposition(fileObject.getName(), request, response);

    String rangeString = ServletUtils.getRange(request);

    String etag = fileObject.getMetadata().getETag();
    Date lastModified = fileObject.lastModified();
    long size = fileObject.getSize();

    if (ServletUtils.cacheable(request)) {
      if (ServletUtils.checkCache(etag, lastModified, request)) {
        if (rangeString == null) {
          response.setStatus(304);
          return;
        }
      } else if (rangeString != null) {
        rangeString = "bytes=0-";
      }
    }

    if (ServletUtils.isKeepAlive(request)) {

      response.setStatus(206);

      long[] range = ServletUtils.getRange(rangeString, size);

      long start = range[0];
      long end = range[1];

      long contentLength = end - start + 1;

      response.setContentLengthLong(Math.min(contentLength, size));

      ServletUtils.setKeepAlive(start, end, size, response);
      ServletUtils.setCache(etag, lastModified, response);

      response.flushBuffer();

      InputStream in = fileObject.getInputStream();
      OutputStream out = response.getOutputStream();

      int loadLength = (int) contentLength;
      int bufferSize = 2048;

      byte[] buf = new byte[bufferSize];

      int bytesRead = in.read(buf, 0, Math.min(loadLength, bufferSize));
      while (bytesRead != -1 && loadLength > 0) {
        loadLength -= bytesRead;
        out.write(buf, 0, bytesRead);
        bytesRead = in.read(buf, 0, Math.min(loadLength, bufferSize));
      }
      StreamUtil.closeQuietly(in);
      out.flush();
    } else {
      response.setContentLengthLong(size);
      ServletUtils.setCache(etag, lastModified, response);

      try {
        StreamUtil.copy(fileObject.getInputStream(), response.getOutputStream());
      } catch (FileNotFoundException e) {
        log.error(e.getMessage(), e);
        response.sendError(404);
      }
    }
  }

  public FileObject compressPacking(Space space, VirtualStorage storage, String[] fidlist)
      throws IOException {
    Set<String> parentPath = new HashSet<>();

    log.debug("开始查询需要打包的数据:" + Arrays.toString(fidlist));
    List<FileObject> files = new ArrayList<>();
    for (String fid : fidlist) {
      IdUtils.FileKey fileKey = IdUtils.parseKey(fid);
      FileObject file = fileKey.getFile().toFileObject(storage);

      parentPath.add(
          file.isDirectory()
              ? file.getPath().replaceFirst("[^/]+/$", "")
              : file.getPath().replaceFirst("[^/]+$", ""));

      if (file.isDirectory()) {
        List<FileObject> fileObjects = file.listFiles(new FileItemSelector() {});
        files.addAll(
            fileObjects.stream().filter(item -> !item.isDirectory()).collect(Collectors.toList()));
      } else {
        files.add(file);
      }
    }

    log.debug("查询需要打包的数据:" + Arrays.toString(fidlist) + ", 共" + files.size() + "个文件");
    String name =
        StringUtil.md5(
            files.stream()
                .map(item -> item.getMetadata().getETag())
                .sorted()
                .collect(Collectors.joining(",")));

    String filename = "download-" + name + ".zip";
    String tempPath = storage.getRootPath() + ".temp/" + filename;

    FileDetail tempFolder = fileService.getTempFolder(space.getVFolder().getId());

    FileObject tempZip = storage.getFileItem(tempPath);

    if (tempZip != null) {
      log.debug("打包的数据:" + Arrays.toString(fidlist) + ", 存在缓存,返回缓存数据");
      return tempZip;
    }

    log.debug("打包的数据:" + Arrays.toString(fidlist) + ", 没有缓存,执行打包操作");
    File tmp = FileUtil.tmp();
    FileOutputStream tempOutput = new FileOutputStream(tmp);
    try {

      String relative = null;
      boolean noFolder = false;
      if (parentPath.size() == 1) {
        relative = parentPath.stream().findFirst().get();
      } else {
        noFolder = true;
      }

      ZipUtil.compress(
          files,
          tempOutput,
          CompressionOptions.builder()
              .encoding("utf-8")
              .relative(relative)
              .noFolder(noFolder)
              .build());

      log.debug("打包的数据:" + Arrays.toString(fidlist) + ", 完成压缩打包操作");

      storage.getInnerStorage().writeFile(tempPath, tmp);

      FileObject storeFile = storage.getInnerStorage().getFileItem(tempPath);

      log.debug("打包的数据:" + Arrays.toString(fidlist) + ", 完成压缩包上传操作");
      return fileService
          .createFile(
              tempFolder.getPath() + filename,
              "【批量下载】" + files.get(0).getName() + " 等 (" + files.size() + ").zip",
              "application/zip",
              storeFile.getSize(),
              storeFile.getMetadata().getETag(),
              storage.getInnerStorage().getId(),
              storeFile.getPath(),
              "",
              tempFolder.getId())
          .toFileObject(storage);
    } finally {
      StreamUtil.closeQuietly(tempOutput);
      FileUtil.delFile(tmp);
    }
  }
}
