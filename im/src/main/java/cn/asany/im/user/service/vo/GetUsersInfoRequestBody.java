package cn.asany.im.user.service.vo;

import cn.asany.im.utils.GeneralRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class GetUsersInfoRequestBody extends GeneralRequest {
  private List<String> userIDList;

  public abstract static class GetUsersInfoRequestBodyBuilder<
          C extends GetUsersInfoRequestBody, B extends GetUsersInfoRequestBodyBuilder<C, B>>
      extends GeneralRequest.GeneralRequestBuilder<C, B> {
    public GetUsersInfoRequestBodyBuilder<?, ?> userID(String userId) {
      if (this.userIDList == null) {
        this.userIDList = new ArrayList<>();
      }
      this.userIDList.add(userId);
      return this;
    }
  }
}
