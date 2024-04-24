package cn.asany.storage.data.web;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.service.FileService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Optional;
import net.asany.jfantasy.framework.util.common.ClassUtil;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StreamUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.framework.util.regexp.RegexpUtil;
import net.asany.jfantasy.framework.util.web.ServletUtils;
import net.asany.jfantasy.framework.util.web.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

/**
 * 文件转发
 *
 * @author limaofeng
 */
public class FileFilter extends GenericFilterBean {

  private static final String REGEX = "_(\\d+)x(\\d+)[.]([^.]+)$";

  @Autowired private FileService fileService;

  @Autowired private StorageResolver storageResolver;

  private String[] allowHosts = new String[] {};

  @Override
  protected void initFilterBean() {
    //    this.setAllowHosts("");
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    if (allowHosts.length != 0 && !ObjectUtil.exists(allowHosts, request.getHeader("host"))) {
      chain.doFilter(request, response);
      return;
    }

    final String url = request.getRequestURI().replaceAll("^" + request.getContextPath(), "");
    Optional<FileDetail> optionalFile = fileService.findByPath(url);
    if (optionalFile.isPresent()) {
      FileDetail file = optionalFile.get();
      String id = file.getStorageConfig().getId();
      Storage storage = storageResolver.resolve(id);
      chain.doFilter(request, response);
      return;
      //      FileObject fileObject = with(storage.getFileItem(file.getPath()),
      // file.toFileObject(null));
      //      writeFile(request, response, fileObject);
    }
    if (RegexpUtil.find(url, REGEX)) {
      final String srcUrl = RegexpUtil.replace(url, REGEX, ".$3");
      optionalFile = fileService.findByPath(srcUrl);
      if (!optionalFile.isPresent()) {
        chain.doFilter(request, response);
        return;
      }
      // 查找源文件
      FileDetail file = optionalFile.get();
      String id = file.getStorageConfig().getId();
      Storage storage = storageResolver.resolve(id);
      FileObject fileObject = null;
      // with(storage.getFileItem(file.getPath()), file.toFileObject(null));
      if (fileObject == null) {
        chain.doFilter(request, response);
        return;
      }
      // 只自动缩放 image/jpeg 格式的图片
      if (!fileObject.getMimeType().contains("image/")) {
        chain.doFilter(request, response);
        return;
      }
      RegexpUtil.Group group = RegexpUtil.parseFirstGroup(url, REGEX);
      // 图片缩放
      assert group != null;
      // TODO: 缺少缩放逻辑
      //      BufferedImage image =
      //          ImageUtil.reduce(
      //              fileObject.getInputStream(),
      //              Integer.valueOf(group.$(1)),
      //              Integer.valueOf(group.$(2)));
      //      // 创建临时文件
      //      File tmp = FileUtil.tmp();
      //      ImageUtil.write(image, tmp);
      //            fileDetail = fileUploadService.upload(tmp, url, "haolue-upload");
      // 删除临时文件
      //      FileUtil.delFile(tmp);
      //      writeFile(request, response, Storage.getFileItem(fileDetail.getPath()));
    } else {
      chain.doFilter(request, response);
    }
  }

  private FileObject with(FileObject fileObject, FileObject file) {
    ClassUtil.setValue(fileObject, "lastModified", file.lastModified());
    fileObject.getMetadata().setContentType(file.getMimeType());
    return fileObject;
  }

  private void writeFile(
      HttpServletRequest request, HttpServletResponse response, FileObject fileObject)
      throws IOException {
    if ("POST".equalsIgnoreCase(WebUtil.getMethod(request))) {
      setDownloadFileHeader(request, response, fileObject);
    } else {
      //      ServletUtils.setCache(
      //          fileObject.getMetadata().getETag(), fileObject.lastModified(), response);
    }
    if (fileObject.getMimeType().startsWith("video/")) {
      writeVideo(request, response, fileObject);
    } else {
      if (ServletUtils.checkCache(
          fileObject.getMetadata().getETag(), fileObject.lastModified(), request)) {
        response.setStatus(304);
      } else {
        try {
          response.setHeader("Content-Type", fileObject.getMimeType());
          StreamUtil.copy(fileObject.getInputStream(), response.getOutputStream());
        } catch (FileNotFoundException e) {
          logger.error(e.getMessage(), e);
          response.sendError(404);
        }
      }
    }
  }

  private void setDownloadFileHeader(
      HttpServletRequest request, HttpServletResponse response, FileObject fileObject)
      throws UnsupportedEncodingException {
    response.setContentType(fileObject.getMimeType());
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Expires", "0");
    response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
    response.setHeader("Pragma", "public");
    response.setContentLength((int) fileObject.getSize());

    response.setHeader(
        "Content-Disposition",
        "attachment;filename=" + WebUtil.filename(fileObject.getName(), request));
  }

  private void writeVideo(
      HttpServletRequest request, HttpServletResponse response, FileObject fileObject)
      throws IOException {
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
        start = Integer.parseInt(sf[0]);
        end = Integer.parseInt(sf[1]);
      } else if (bytes.startsWith("-")) {
        start = 0;
        end = (int) (fileLength - 1);
      } else if (bytes.endsWith("-")) {
        start = Integer.parseInt(sf[0]);
        end = (int) (fileLength - 1);
      }
      int contentLength = end - start + 1;

      response.setHeader("Connection", "keep-alive");
      response.setHeader("Content-Type", fileObject.getMimeType());

      ServletUtils.setCache(1024, response);

      response.setHeader(
          "Content-Length", Long.toString(contentLength > fileLength ? fileLength : contentLength));
      response.setHeader(
          "Content-Range",
          "bytes "
              + start
              + "-"
              + (end != 1 && end >= fileLength ? end - 1 : end)
              + "/"
              + fileLength);

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
        logger.error(var5.getMessage(), var5);
        response.sendError(404);
      }
    }
  }

  public void setAllowHosts(String allowHosts) {
    this.allowHosts = StringUtil.tokenizeToStringArray(allowHosts);
  }
}
