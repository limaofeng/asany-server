package cn.asany.storage.data.rest;

import cn.asany.storage.api.FileItemSelector;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.core.engine.virtual.VirtualStorage;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.domain.Space;
import cn.asany.storage.data.domain.Thumbnail;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.ThumbnailService;
import cn.asany.storage.data.util.BandwidthLimiter;
import cn.asany.storage.data.util.CompressionOptions;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.data.util.ZipUtil;
import cn.asany.storage.data.web.wrapper.DownloadLimitServletOutputStream;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.framework.util.web.ServletUtils;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DownloadController {

  private final FileService fileService;
  private final StorageResolver storageResolver;
  private final ThumbnailService thumbnailService;

  public DownloadController(
      FileService fileService, StorageResolver storageResolver, ThumbnailService thumbnailService) {
    this.fileService = fileService;
    this.storageResolver = storageResolver;
    this.thumbnailService = thumbnailService;
  }

  @GetMapping("/preview/{fid}")
  public void preview(
      @PathVariable("fid") String id, HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    download(id, request, response);
  }

  @GetMapping("/thumbnail/{fid}")
  public void thumbnail(
      @PathVariable("fid") String id,
      @RequestParam("size") String size,
      HttpServletResponse response)
      throws IOException {
    IdUtils.FileKey fileKey = IdUtils.parseKey(id);

    FileDetail fileDetail = fileKey.getFile();

    if (!(fileDetail.getMimeType().startsWith("image/")
        || fileDetail.getMimeType().startsWith("video/"))) {
      response.sendError(404, "??????????????????????????????????????????");
      return;
    }

    Optional<Thumbnail> thumbnailOptional = thumbnailService.findBySize(size, fileDetail.getId());

    Thumbnail thumbnail =
        thumbnailOptional.orElseGet(() -> thumbnailService.generate(size, fileDetail.getId()));

    FileDetail thumbFile = fileService.getFileById(thumbnail.getFile().getId());

    Storage storage = storageResolver.resolve(thumbFile.getStorageConfig().getId());

    response.setContentType(thumbFile.getMimeType());
    response.setContentLengthLong(thumbFile.getSize());
    ServletUtils.setCache(
        3600,
        thumbFile.getMd5(),
        thumbFile.getLastModified(),
        CacheControl.maxAge(3600, TimeUnit.SECONDS).cachePrivate(),
        response);

    storage.readFile(thumbFile.getStorePath(), response.getOutputStream());
  }

  @GetMapping("/download")
  public void download(
      @RequestParam("fidlist") String fidlistString,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {

    String[] fidlist = StringUtil.tokenizeToStringArray(fidlistString, ",");

    if (fidlist.length == 0) {
      response.sendError(404, "????????????????????????");
      return;
    }

    IdUtils.FileKey firstFileKey = IdUtils.parseKey(fidlist[0]);
    Space space = firstFileKey.getSpace();

    VirtualStorage storage = new VirtualStorage(space, storageResolver, fileService);

    FileObject fileObject =
        fidlist.length > 1
            ? compressPacking(space, storage, fidlist)
            : storage.getFileItem(firstFileKey.getFile().toFileObject(storage).getPath());

    response.setContentType(fileObject.getMimeType());
    ServletUtils.setContentDisposition("inline", fileObject.getName(), request, response);

    String rangeString = ServletUtils.getRange(request);

    String etag = fileObject.getMetadata().getETag();
    Date lastModified = fileObject.lastModified();
    long size = fileObject.getSize();

    BandwidthLimiter limiter = new BandwidthLimiter(1024);
    OutputStream out = new DownloadLimitServletOutputStream(response.getOutputStream(), limiter);

    if (ServletUtils.isRange(request)) {
      response.setStatus(206);

      long[] range = ServletUtils.getRange(rangeString, size);
      long contentLength = range[1] - range[0] + 1;

      ServletUtils.setContentRange(range[0], range[1], size, response);
      ServletUtils.setCache(
          3600,
          etag,
          lastModified,
          CacheControl.maxAge(3600, TimeUnit.SECONDS).cachePrivate(),
          response);
      response.setContentLengthLong(Math.min(contentLength, size));

      out.flush();

      InputStream in = fileObject.getInputStream(range[0], range[1]);

      try {
        StreamUtil.copy(in, out);
      } finally {
        StreamUtil.closeQuietly(in);
      }
    } else {
      response.setContentLengthLong(size);
      ServletUtils.setContentRange(0, size - 1, size, response);
      ServletUtils.setCache(
          3600,
          etag,
          lastModified,
          CacheControl.maxAge(3600, TimeUnit.SECONDS).cachePrivate(),
          response);

      out.flush();

      InputStream in = fileObject.getInputStream();
      try {
        StreamUtil.copy(in, out);
      } finally {
        StreamUtil.closeQuietly(in);
      }
    }
  }

  public FileObject compressPacking(Space space, VirtualStorage storage, String[] fidlist)
      throws IOException {
    Set<String> parentPath = new HashSet<>();

    log.debug("?????????????????????????????????:" + Arrays.toString(fidlist));
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

    log.debug("???????????????????????????:" + Arrays.toString(fidlist) + ", ???" + files.size() + "?????????");
    String name =
        StringUtil.md5(
            files.stream()
                .map(item -> item.getMetadata().getETag())
                .sorted()
                .collect(Collectors.joining(",")));

    String filename = "download-" + name + ".zip";

    FileObject tempFolder =
        fileService.getTempFolder(space.getVFolder().getId()).toFileObject(storage);

    String tempPath = tempFolder.getPath() + filename;

    FileObject tempZip = storage.getFileItem(tempPath);

    if (tempZip != null) {
      log.debug("???????????????:" + Arrays.toString(fidlist) + ", ????????????,??????????????????");
      return tempZip;
    }

    log.debug("???????????????:" + Arrays.toString(fidlist) + ", ????????????,??????????????????");
    File tmp = FileUtil.tmp();
    FileOutputStream tempOutput = new FileOutputStream(tmp);
    try {

      String relative = null;
      boolean noFolder = false;
      if (parentPath.size() == 1) {
        relative = parentPath.stream().findFirst().get();
        relative = ((VirtualFileObject) storage.getFileItem(relative)).getNamePath();
      } else {
        noFolder = true;
      }

      boolean finalNoFolder = noFolder;
      String finalRelative = relative;
      CompressionOptions options =
          CompressionOptions.builder()
              .encoding("utf-8")
              .forward(
                  file -> {
                    if (finalNoFolder) {
                      return file.getName();
                    }
                    if (finalRelative != null) {
                      return ((VirtualFileObject) file)
                          .getNamePath()
                          .substring(finalRelative.length());
                    }
                    return ((VirtualFileObject) file).getNamePath();
                  })
              .build();

      ZipUtil.compress(files, tempOutput, options);

      log.debug("???????????????:" + Arrays.toString(fidlist) + ", ????????????????????????");

      String zipPath = tempFolder.getPath() + filename;
      String showName = "??????????????????" + files.get(0).getName() + " ??? (" + files.size() + ").zip";

      storage.writeFile(zipPath, tmp, showName);

      log.debug("???????????????:" + Arrays.toString(fidlist) + ", ???????????????????????????");
      return storage.getFileItem(zipPath);
    } finally {
      StreamUtil.closeQuietly(tempOutput);
      FileUtil.delFile(tmp);
    }
  }
}
