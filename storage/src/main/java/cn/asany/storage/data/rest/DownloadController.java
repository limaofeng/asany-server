package cn.asany.storage.data.rest;

import cn.asany.storage.api.Storage;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.rest.input.DownloadForm;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.util.IdUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfantasy.framework.util.common.StreamUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DownloadController {

  private final FileService fileService;
  private final StorageResolver storageResolver;

  public DownloadController(FileService fileService, StorageResolver storageResolver) {
    this.fileService = fileService;
    this.storageResolver = storageResolver;
  }

  @PostMapping("/download")
  public void download(
      @RequestBody DownloadForm form, HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    List<String> ids = form.getIds();

    InputStream input = null;
    long contentLength = 0;
    if (ids.size() == 1) {
      IdUtils.FileKey fileKey = IdUtils.parseKey(ids.get(0));

      Storage storage = storageResolver.resolve(fileKey.getStorage());
      FileDetail file = fileService.getFileById(fileKey.getFileId());

      if (fileKey.isFile()) {
        contentLength += file.getSize();
        input = storage.readFile(file.getStorePath());
      } else {

      }
    }

    assert input != null;
    response.setHeader("Content-Length", String.valueOf(contentLength));
    StreamUtil.copy(input, response.getOutputStream());
  }
}
