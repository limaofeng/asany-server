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
package cn.asany.weixin.framework.core;

import cn.asany.weixin.framework.exception.WeixinException;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/** 微信 Jsapi */
public interface Jsapi {

  String getTicket() throws WeixinException;

  String getTicket(boolean forceRefresh) throws WeixinException;

  Signature signature(String url) throws WeixinException;

  /** JS API 签名 */
  @Setter
  @Getter
  class Signature implements Serializable {

    /** 随机字符串 * */
    private String noncestr;

    /** appid * */
    private String appid;

    /** 时间戳 * */
    private long timestamp;

    /** URL * */
    private String url;

    /** 签名串 * */
    private String signature;

    Signature(String noncestr, String appid, long timestamp, String url, String signature) {
      this.noncestr = noncestr;
      this.appid = appid;
      this.timestamp = timestamp;
      this.url = url;
      this.signature = signature;
    }
  }
}
