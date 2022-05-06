package cn.asany.weixin.framework.intercept;

import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.WeixinMessage;

public interface Invocation {

  WeixinMessage invoke() throws WeixinException;
}
