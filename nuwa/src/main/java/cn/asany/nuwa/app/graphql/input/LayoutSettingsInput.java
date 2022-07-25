package cn.asany.nuwa.app.graphql.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LayoutSettingsInput {
  private Boolean pure;
  private Boolean hideMenu;
}
