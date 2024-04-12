package cn.asany.cms.content.service;

import cn.asany.cms.content.dao.ImageContentDao;
import cn.asany.cms.content.domain.ImageContent;
import cn.asany.cms.content.domain.ImageItem;
import cn.asany.cms.content.domain.enums.ContentType;
import cn.asany.cms.content.graphql.input.ImageContentItemInput;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ImageContentService implements ArticleContentHandler<ImageContent> {
  private final ImageContentDao imageContentDao;

  public ImageContentService(ImageContentDao imageContentDao) {
    this.imageContentDao = imageContentDao;
  }

  @Override
  public boolean supports(ContentType type) {
    return type == ContentType.IMAGE;
  }

  @Override
  public ImageContent save(ImageContent content) {
    for (ImageItem item : content.getImages()) {
      item.setIndex(content.getImages().indexOf(item));
      item.setImageContent(content);
    }
    return this.imageContentDao.save(content);
  }

  @Override
  public ImageContent update(Long id, ImageContent content) {
    content.setId(id);
    for (ImageItem item : content.getImages()) {
      item.setIndex(content.getImages().indexOf(item));
      item.setImageContent(content);
    }
    return this.imageContentDao.update(content);
  }

  @Override
  public void delete(Long id) {
    this.imageContentDao.deleteById(id);
  }

  @Override
  public ImageContent parse(Map<String, Object> content) {
    Long id = (Long) content.get("id");
    List<ImageContentItemInput> images = (List<ImageContentItemInput>) content.get("images");
    return ImageContent.builder()
        .id(id)
        .images(
            images.stream()
                .map(ImageContentItemInput::toImageContentItem)
                .collect(Collectors.toList()))
        .build();
  }
}
