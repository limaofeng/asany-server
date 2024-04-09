package cn.asany.cms.content.graphql.input;

import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.Data;

@Data
public class ArticleContentInput {
  private Long id;
  /** 标题 */
  private String title;
  /** 描述 */
  private String description;
  /** 引用地址 */
  private String url;
  /** * HTML 类型：HTML, MARKDOWN */
  private String type;
  /** 文本 */
  private String text;
  /** 文件大小 */
  private Long size;
  /** 图片集 */
  private List<ImageContentItemInput> images;
  /** 视频 */
  private FileObject video;
  /** 文档 */
  private FileObject document;
}
