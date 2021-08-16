package cn.asany.security.oauth.graphql.input;

import lombok.Data;

@Data
public class PersonalAccessTokenCreateInput {
  private String clientId;
  private String name;
}
