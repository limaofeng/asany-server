package cn.asany.website.landing.graphql.input;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class LandingPageCreateInput {
  private String name;
  private String description;
  private Date start;
  private Date end;
  private Long poster;
  private List<Long> stores;
}
