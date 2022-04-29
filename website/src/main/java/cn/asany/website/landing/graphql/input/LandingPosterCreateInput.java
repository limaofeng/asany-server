package cn.asany.website.landing.graphql.input;

import cn.asany.storage.api.FileObject;
import lombok.Data;

@Data
public class LandingPosterCreateInput {
  private String name;
  private FileObject music;
  private FileObject background;
  private String description;
}
