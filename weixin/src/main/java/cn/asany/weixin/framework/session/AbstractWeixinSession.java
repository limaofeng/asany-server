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
package cn.asany.weixin.framework.session;

import cn.asany.weixin.framework.core.Jsapi;
import cn.asany.weixin.framework.core.Openapi;
import cn.asany.weixin.framework.core.WeixinCoreHelper;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.content.*;
import cn.asany.weixin.framework.message.user.User;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.error.IgnoreException;

/** 微信 session 抽象实现 */
@Slf4j
public abstract class AbstractWeixinSession implements WeixinSession {
  private final String id;

  private final WeixinApp weixinApp;
  private final WeixinCoreHelper weixinCoreHelper;
  private static final ExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

  public AbstractWeixinSession(WeixinApp weixinApp, WeixinCoreHelper weixinCoreHelper) {
    this.weixinApp = weixinApp;
    this.id = weixinApp.getId();
    this.weixinCoreHelper = weixinCoreHelper;
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void sendImageMessage(final Image content, final String... toUsers) {
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendImageMessage(
                AbstractWeixinSession.this, content, toUsers);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public void sendImageMessage(final Image content, final long toGroup) {
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendImageMessage(
                AbstractWeixinSession.this, content, toGroup);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public void sendVoiceMessage(final Voice content, final String... toUsers) {
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendVoiceMessage(
                AbstractWeixinSession.this, content, toUsers);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public void sendVoiceMessage(final Voice content, final long toGroup) {
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendVoiceMessage(
                AbstractWeixinSession.this, content, toGroup);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public void sendVideoMessage(final Video content, final String... toUsers) {
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendVideoMessage(
                AbstractWeixinSession.this, content, toUsers);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public void sendVideoMessage(final Video content, final long toGroup) {
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendVideoMessage(
                AbstractWeixinSession.this, content, toGroup);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public void sendMusicMessage(final Music content, final String toUser) {
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendMusicMessage(
                AbstractWeixinSession.this, content, toUser);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public void sendNewsMessage(final List<Article> content, final String... toUsers) {
    if (content.isEmpty()) {
      return;
    }
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendNewsMessage(
                AbstractWeixinSession.this, content, toUsers);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public void sendNewsMessage(final List<News> content, final String toUser) {
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendNewsMessage(
                AbstractWeixinSession.this, content, toUser);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public void sendNewsMessage(final List<Article> content, final long toGroup) {
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendNewsMessage(
                AbstractWeixinSession.this, content, toGroup);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public void sendTextMessage(final String content, final String... toUsers) {
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendTextMessage(
                AbstractWeixinSession.this, content, toUsers);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public void sendTextMessage(final String content, final long toGroup) {
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendTextMessage(
                AbstractWeixinSession.this, content, toGroup);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public void sendTemplateMessage(final Template content, final String toUser) {
    EXECUTOR.execute(
        () -> {
          try {
            AbstractWeixinSession.this.weixinCoreHelper.sendTemplateMessage(
                AbstractWeixinSession.this, content, toUser);
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          }
        });
  }

  @Override
  public User getUser(String userId) {
    try {
      if (getWeixinApp().getType() == WeixinAppType.open) {
        return this.weixinCoreHelper.getOpenapi(this).getUser(userId);
      } else {
        return this.weixinCoreHelper.getUser(this, userId);
      }
    } catch (WeixinException e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public List<User> getUsers() {
    try {
      return this.weixinCoreHelper.getUsers(this);
    } catch (WeixinException e) {
      log.error(e.getMessage(), e);
      return Collections.emptyList();
    }
  }

  @Override
  public void refreshMenu(Menu... menus) {
    try {
      this.weixinCoreHelper.refreshMenu(this, menus);
    } catch (WeixinException e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public List<Menu> getMenus() {
    try {
      return this.weixinCoreHelper.getMenus(this);
    } catch (WeixinException e) {
      log.error(e.getMessage(), e);
      throw new IgnoreException(e.getMessage());
    }
  }

  @Override
  public void clearMenu() {
    try {
      this.weixinCoreHelper.clearMenu(this);
    } catch (WeixinException e) {
      log.error(e.getMessage(), e);
    }
  }

  public WeixinApp getWeixinApp() {
    return this.weixinApp;
  }

  @Override
  public Jsapi getJsapi() {
    try {
      return this.weixinCoreHelper.getJsapi(this);
    } catch (WeixinException e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public Openapi getOpenapi() {
    try {
      return this.weixinCoreHelper.getOpenapi(this);
    } catch (WeixinException e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
