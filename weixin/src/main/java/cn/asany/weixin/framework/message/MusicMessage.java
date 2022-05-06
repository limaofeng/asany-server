package cn.asany.weixin.framework.message;

import cn.asany.weixin.framework.message.content.Music;

/** 音乐消息 */
public class MusicMessage extends AbstractWeixinMessage<Music> {

  public MusicMessage(Music content) {
    super(content);
  }
}
