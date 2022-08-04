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
public class AccountCheckRequestBody extends GeneralRequest {
  private List<String> checkUserIDList;

  public abstract static class AccountCheckRequestBodyBuilder<
          C extends AccountCheckRequestBody, B extends AccountCheckRequestBodyBuilder<C, B>>
      extends GeneralRequest.GeneralRequestBuilder<C, B> {
    public AccountCheckRequestBodyBuilder<?, ?> checkUserID(String userId) {
      if (this.checkUserIDList == null) {
        this.checkUserIDList = new ArrayList<>();
      }
      this.checkUserIDList.add(userId);
      return this;
    }
  }
}
