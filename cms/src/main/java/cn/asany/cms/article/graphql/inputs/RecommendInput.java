package cn.asany.cms.article.graphql.inputs;

import cn.asany.cms.article.bean.Recommend;

/** @Description @Author ChenWenJie @Data 2020/10/22 11:31 上午 */
public class RecommendInput {
  private Recommend.RecommendBuilder builder = Recommend.builder();

  private void setName(String name) {
    this.builder.name(name);
  }

  private void setDescription(String description) {
    this.builder.description(description);
  }

  private void setEnableProcess(Boolean enableProcess) {
    this.builder.enableProcess(enableProcess);
  }

  private void setOrganization(String organization) {
    this.builder.organization(organization);
  }

  private void setCode(String code) {
    this.builder.code(code);
  }

  public Recommend build() {
    return this.builder.build();
  }
}
