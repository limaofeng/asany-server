package cn.asany.storage.data.rest;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.util.IdUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.web.ServletUtils;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping("/download")
  public void download(
      @RequestParam("fidlist") String fidlistString,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {

    String[] fidlist = StringUtil.tokenizeToStringArray(fidlistString, ",");

    String fid = fidlist[0];

    IdUtils.FileKey fileKey = IdUtils.parseKey(fid);

    Storage storage = storageResolver.resolve(fileKey.getStorage());
    FileDetail file = fileService.getFileById(fileKey.getFileId());

    FileObject fileObject = storage.getFileItem(file.getStorePath());

    response.setContentType(fileObject.getMimeType());
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Expires", "0");
    response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
    response.setHeader("Pragma", "public");
    response.setContentLength((int) fileObject.getSize());

    String fileName = URLEncoder.encode(fileObject.getName(), "UTF-8");

    if (WebUtil.Browser.mozilla == WebUtil.browser(request)) {
      byte[] bytes = fileObject.getName().getBytes(StandardCharsets.UTF_8);
      fileName = new String(bytes, StandardCharsets.ISO_8859_1);
    }

    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

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
        end = (int) (fileLength - 1);
      } else if (bytes.endsWith("-")) {
        start = Integer.parseInt(sf[0]);
        end = (int) (fileLength - 1);
      }
      int contentLength = end - start + 1;

      response.setHeader("Connection", "keep-alive");
      response.setHeader("Content-Type", fileObject.getMimeType());
      response.setHeader("Cache-Control", "max-age=1024");
      ServletUtils.setLastModifiedHeader(response, fileObject.lastModified().getTime());
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

      response.flushBuffer();

      InputStream in = fileObject.getInputStream();
      OutputStream out = response.getOutputStream();

      int loadLength = contentLength, bufferSize = 2048;

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
      try {
        StreamUtil.copy(fileObject.getInputStream(), response.getOutputStream());
      } catch (FileNotFoundException e) {
        log.error(e.getMessage(), e);
        response.sendError(404);
      }
    }
  }
}
