package cn.asany.storage.data.rest;

import cn.asany.storage.core.FileUploadService;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.FilePart;
import cn.asany.storage.data.service.FilePartService;
import cn.asany.storage.data.service.FileService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 文件上传接口 */
@RestController
@RequestMapping("/files")
public class FileController {

  private final FileService fileService;
  private final FileUploadService fileUploadService;
  private final FilePartService filePartService;

  @Autowired
  public FileController(
      FileService fileService,
      FileUploadService fileUploadService,
      FilePartService filePartService) {
    this.fileService = fileService;
    this.fileUploadService = fileUploadService;
    this.filePartService = filePartService;
  }

  //    /**
  //     * 上传文件<br/>
  //     * 单独的文件上传接口，返回 FileDetail 对象
  //     *
  //     * @param file 要上传的文件
  //     * @param info 上传的目录标识
  //     * @return {String} 返回文件信息
  //     * @throws IOException 文件上传异常
  //     */
  //    @RequestMapping(method = RequestMethod.POST)
  //    public FileDetail upload(@RequestParam(value = "attach", required = false) MultipartFile
  // file, @Validated(RESTful.POST.class) Info info) throws IOException {
  //        if (StringUtil.isBlank(info.getUrl()) && file == null) {
  //            throw new ValidationException("attach 与 url 必须指定其中一个参数");
  //        }
  //        if (StringUtil.isBlank(info.getUrl())) {
  //            return fileUploadService.upload(file, info);
  //        } else {
  //            try {
  //                HttpResponse<InputStream> response = Unirest.get(info.getUrl()).asBinary();
  //                Long length = Long.valueOf(response.getHeaders().getFirst("Content-Length"));
  //                String contentType = response.getHeaders().getFirst("Content-Type");
  //                return fileUploadService.upload(response.getBody(), contentType,
  // StringUtil.defaultValue(info.getName(), ""), length, info.getSpace());
  //            } catch (UnirestException e) {
  //                throw new IOException(e.getMessage(), e);
  //            }
  //        }
  //    }

  /**
   * 分段上传查询
   *
   * @param hash hash
   * @return Map<Object>
   */
  @RequestMapping(value = "/{hash}/pass", method = RequestMethod.GET)
  public Map<String, Object> pass(@PathVariable("hash") String hash) {
    List<FilePart> parts = filePartService.find(hash);
    Map<String, Object> data = new HashMap<>();
    FilePart part = ObjectUtil.remove(parts, "index", 0);
    if (part != null) {
      data.put("fileDetail", fileService.findByPath(part.getPath()));
    }
    data.put("parts", parts);
    return data;
  }

  /** 查询文件信息 */
  @RequestMapping(method = RequestMethod.GET)
  public FileDetail view(@RequestParam("path") String path) {
    return fileService.findByPath(
        path.contains(":") ? path.substring(path.indexOf(":") + 1) : path);
  }
}
