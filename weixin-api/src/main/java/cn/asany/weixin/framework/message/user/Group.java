package cn.asany.weixin.framework.message.user;

import java.util.ArrayList;
import java.util.List;
import net.asany.jfantasy.framework.util.common.ObjectUtil;

/** 用户分组 */
public class Group {
  private long id;
  private String name;
  private long count;
  private List<User> users;

  public Group(long id, String name, long count) {
    this.id = id;
    this.name = name;
    this.count = count;
    this.users = new ArrayList<>((int) count);
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public void addUser(User user) {
    if (ObjectUtil.find(this.users, "getOpenId()", user.getOpenId()) == null) {
      this.users.add(user);
    }
  }

  @Override
  public String toString() {
    return "Group{" + "id=" + id + ", name='" + name + '\'' + ", count=" + count + '}';
  }
}
