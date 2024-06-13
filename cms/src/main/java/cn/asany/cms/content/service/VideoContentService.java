/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.cms.content.service;

import cn.asany.cms.content.dao.VideoContentDao;
import cn.asany.cms.content.domain.VideoContent;
import cn.asany.cms.content.domain.enums.ContentType;
import cn.asany.cms.content.graphql.input.ArticleContentInput;
import cn.asany.storage.api.FileObject;
import java.util.Optional;
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
  public VideoContent parse(ArticleContentInput content) {
    Long id = content.getId();
    String url = content.getUrl();
    String title = content.getTitle();
    String description = content.getDescription();
    FileObject video = content.getVideo();
    return VideoContent.builder()
        .url(url)
        .title(title)
        .description(description)
        .id(id)
        .video(video)
        .build();
  }

  @Override
  public Optional<VideoContent> findById(Long id) {
    return this.videoContentDao.findById(id);
  }
}
