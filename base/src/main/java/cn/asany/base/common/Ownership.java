package cn.asany.base.common;

import java.io.Serializable;

/**
 * 所有者接口
 *
 * @author limaofeng
 */
public interface Ownership extends Serializable {

  Long getId();

  String getName();

  String getOwnerType();
}
