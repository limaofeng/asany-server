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
package cn.asany.weixin.framework.factory;

import cn.asany.weixin.framework.account.WeixinAppService;
import cn.asany.weixin.framework.core.WeixinCoreHelper;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.session.WeixinSession;

/** 微信会话工厂 */
public interface WeixinSessionFactory {

  /**
   * 第三方工具类
   *
   * @return Signature
   */
  WeixinCoreHelper getWeixinCoreHelper();

  /**
   * 获取当前的 WeiXinSession
   *
   * @return WeiXinSession
   */
  WeixinSession getCurrentSession() throws WeixinException;

  /**
   * 返回一个 WeiXinSession 对象，如果当前不存在，则创建一个新的session对象
   *
   * @return WeiXinSession
   */
  WeixinSession openSession(String appid) throws WeixinException;

  /**
   * 获取微信账号存储服务
   *
   * @return AccountDetailsService
   */
  WeixinAppService getWeixinAppService();

  /**
   * 处理接收到的请求
   *
   * @param message http response
   * @return WeiXinMessage
   */
  WeixinMessage<?> execute(WeixinMessage<?> message) throws WeixinException;
}
