package cn.asany.cms.content.service;

import cn.asany.cms.content.dao.VideoContentDao;
import cn.asany.cms.content.domain.VideoContent;
import cn.asany.cms.content.domain.enums.ContentType;
import cn.asany.storage.api.FileObject;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class VideoContentService implements ArticleContentHandler<VideoContent> {
  private final VideoContentDao videoContentDao;

  public VideoContentService(VideoContentDao videoContentDao) {
    this.videoContentDao = videoContentDao;
  }

  @Override
  public boolean supports(ContentType type) {
    return type == ContentType.VIDEO;
  }

  @Override
  public VideoContent save(VideoContent body) {
    return this.videoContentDao.save(body);
  }

  @Override
  public VideoContent update(Long id, VideoContent body) {
    body.setId(id);
    return this.videoContentDao.update(body);
  }

  @Override
  public void delete(Long id) {
    this.videoContentDao.deleteById(id);
  }

  @Override
  public VideoContent parse(Map<String, Object> content) {
    Long id = (Long) content.get("id");
    String url = (String) content.get("url");
    String title = (String) content.get("title");
    String description = (String) content.get("description");
    FileObject video = (FileObject) content.get("video");
    return VideoContent.builder()
        .url(url)
        .title(title)
        .description(description)
        .id(id)
        .video(video)
        .build();
  }
}
