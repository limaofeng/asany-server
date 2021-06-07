package cn.asany.storage.data.web;

import cn.asany.storage.core.FileObject;
import cn.asany.storage.core.FileUploadService;
import cn.asany.storage.core.Storage;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.service.FileService;
import org.jfantasy.framework.util.common.ImageUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.framework.util.web.ServletUtils;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.framework.util.web.WebUtil.Browser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;

@Component
public class FileFilter extends GenericFilterBean {

    @Autowired
    private FileService fileService;
    @Autowired
    private FileUploadService fileUploadService;

    private String[] allowHosts;

    private static final String regex = "_(\\d+)x(\\d+)[.]([^.]+)$";

    @Override
    protected void initFilterBean() throws ServletException {
//        this.addRequiredProperty("allowHosts");
        this.setAllowHosts("");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if(allowHosts.length != 0 && !ObjectUtil.exists(allowHosts,request.getHeader("host"))){
            chain.doFilter(request, response);
            return;
        }

        final String url = request.getRequestURI().replaceAll("^" + request.getContextPath(), "");
        FileDetail fileDetail = FileFilter.this.fileService.findByPath(url);
        if (fileDetail != null) {
            Storage Storage = null;//FileManagerFactory.getInstance().getFileManager(fileDetail.getStorage());
            FileObject fileObject = Storage.getFileItem(fileDetail.getPath());
            if (fileObject != null) {
                writeFile(request, response, fileObject);
                return;
            }
        }
        if (RegexpUtil.find(url, regex)) {
            final String srcUrl = RegexpUtil.replace(url, regex, ".$3");
            FileDetail srcFileDetail = FileFilter.this.fileService.findByPath(srcUrl);
            if (srcFileDetail == null) {
                chain.doFilter(request, response);
                return;
            }
            // 查找源文件
            Storage Storage =  null;// FileManagerFactory.getInstance().getFileManager(srcFileDetail.getStorage());
            FileObject fileObject = Storage.getFileItem(srcFileDetail.getPath());
            if (fileObject == null) {
                chain.doFilter(request, response);
                return;
            }
            // 只自动缩放 image/jpeg 格式的图片
            if (!fileObject.getContentType().contains("image/")) {
                chain.doFilter(request, response);
                return;
            }
            RegexpUtil.Group group = RegexpUtil.parseFirstGroup(url, regex);
            // 图片缩放
            assert group != null;
            BufferedImage image = ImageUtil.reduce(fileObject.getInputStream(), Integer.valueOf(group.$(1)), Integer.valueOf(group.$(2)));
            // 创建临时文件
            File tmp = FileUtil.tmp();
            ImageUtil.write(image, tmp);
            fileDetail = fileUploadService.upload(tmp, url, "haolue-upload");
            // 删除临时文件
            FileUtil.delFile(tmp);
            writeFile(request, response, Storage.getFileItem(fileDetail.getPath()));
        }else{
            chain.doFilter(request,response);
        }

    }

    private void writeFile(HttpServletRequest request, HttpServletResponse response, FileObject fileObject) throws IOException {
        if ("POST".equalsIgnoreCase(WebUtil.getMethod(request))) {
            response.setContentType(fileObject.getContentType());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentLength((int) fileObject.getSize());

            String fileName = Browser.mozilla == WebUtil.browser(request) ? new String(fileObject.getName().getBytes("UTF-8"), "iso8859-1") : URLEncoder.encode(fileObject.getName(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        } else {
            ServletUtils.setExpiresHeader(response, 1000 * 60 * 5L);
            ServletUtils.setLastModifiedHeader(response, fileObject.lastModified().getTime());
        }
        if (fileObject.getContentType().startsWith("video/")) {
            response.addHeader("Accept-Ranges", "bytes");
            response.addHeader("Cneonction", "close");

            String range = StringUtil.defaultValue(request.getHeader("Range"), "bytes=0-");
            if ("keep-alive".equals(request.getHeader("connection"))) {
                long fileLength = fileObject.getSize();

                response.setStatus(206);
                String bytes = WebUtil.parseQuery(range).get("bytes")[0];
                String[] sf = bytes.split("-");
                int start = 0;
                int end = 0;
                if (sf.length == 2) {
                    start = Integer.valueOf(sf[0]);
                    end = Integer.valueOf(sf[1]);
                } else if (bytes.startsWith("-")) {
                    start = 0;
                    end = (int) (fileLength - 1);
                } else if (bytes.endsWith("-")) {
                    start = Integer.valueOf(sf[0]);
                    end = (int) (fileLength - 1);
                }
                int contentLength = end - start + 1;

                response.setHeader("Connection", "keep-alive");
                response.setHeader("Content-Type", fileObject.getContentType());
                response.setHeader("Cache-Control", "max-age=1024");
                ServletUtils.setLastModifiedHeader(response, fileObject.lastModified().getTime());
                response.setHeader("Content-Length", Long.toString(contentLength > fileLength ? fileLength : contentLength));
                response.setHeader("Content-Range", "bytes " + start + "-" + (end != 1 && end >= fileLength ? end - 1 : end) + "/" + fileLength);

                InputStream in = fileObject.getInputStream();
                OutputStream out = response.getOutputStream();

                int loadLength = contentLength, bufferSize = 2048;

                byte[] buf = new byte[bufferSize];

                int bytesRead = in.read(buf, 0, loadLength > bufferSize ? bufferSize : loadLength);
                while (bytesRead != -1 && loadLength > 0) {
                    loadLength -= bytesRead;
                    out.write(buf, 0, bytesRead);
                    bytesRead = in.read(buf, 0, loadLength > bufferSize ? bufferSize : loadLength);
                }
                StreamUtil.closeQuietly(in);
                out.flush();
            } else {
                try {
                    StreamUtil.copy(fileObject.getInputStream(), response.getOutputStream());
                } catch (FileNotFoundException var5) {
                    logger.error(var5.getMessage(),var5);
                    response.sendError(404);
                }
            }
        } else {
            if (ServletUtils.checkIfModifiedSince(request, response, fileObject.lastModified().getTime())) {
                try {
                    response.setHeader("Content-Type", fileObject.getContentType());
                    StreamUtil.copy(fileObject.getInputStream(), response.getOutputStream());
                } catch (FileNotFoundException e) {
                    logger.error(e.getMessage(),e);
                    response.sendError(404);
                }
            }
        }
    }

    public void setAllowHosts(String allowHosts) {
        this.allowHosts = StringUtil.tokenizeToStringArray(allowHosts);
    }

}