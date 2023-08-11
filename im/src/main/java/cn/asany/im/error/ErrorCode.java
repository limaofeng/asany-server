package cn.asany.im.error;

/**
 * 异常编码
 *
 * @author limaofeng
 */
public enum ErrorCode {
  SERVER_INTERNAL_ERROR(500, "服务器内部错误，通常为内部网络错误，需要检查服务器各节点运行是否正常"),
  PARAMETER_ERROR(1001, "参数错误，需要检查 body 参数以及 header 参数是否正确"),
  INSUFFICIENT_PERMISSIONS(1002, "权限不足，一般为 header 参数中携带 token 不正确，或者权限越级操作"),
  DUPLICATE_PRIMARY_KEY(1003, "数据库主键重复"),
  RECORD_NOT_FOUND(1004, "数据库记录未找到"),
  USER_ID_NOT_EXIST(1101, "用户 ID 不存在"),
  USER_ALREADY_REGISTERED(1102, "用户已经注册"),
  GROUP_NOT_EXIST(1201, "群不存在"),
  GROUP_ALREADY_EXIST(1202, "群已存在"),
  USER_NOT_IN_GROUP(1203, "用户不在群组中"),
  GROUP_DISBANDED(1204, "群组已解散"),
  GROUP_APPLICATION_PROCESSED(1205, "群申请已经被处理，不需要重复处理"),
  CANNOT_ADD_SELF_AS_FRIEND(1301, "不能添加自己为好友"),
  BLOCKED_BY_OTHER_USER(1302, "已被对方拉黑"),
  NOT_FRIEND_WITH_OTHER_USER(1303, "对方不是自己的好友"),
  ALREADY_FRIENDS(1304, "已经是好友关系，不需要重复申请"),
  READ_RECEIPT_DISABLED(1401, "消息已读功能被关闭"),
  MUTED_IN_GROUP(1402, "你已被禁言，不能在群里发言"),
  GROUP_MUTED(1403, "群已被禁言，不能发言"),
  MESSAGE_REVOKED(1404, "该消息已被撤回"),
  AUTHORIZATION_EXPIRED(1405, "授权过期"),
  TOKEN_EXPIRED(1501, "token 已经过期"),
  INVALID_TOKEN(1502, "token 无效"),
  MALFORMED_TOKEN(1503, "token 格式错误"),
  TOKEN_NOT_YET_VALID(1504, "token 还未生效"),
  UNKNOWN_TOKEN_ERROR(1505, "未知 token 错误"),
  INVALID_KICKED_OUT_TOKEN(1506, "被踢出的 token，无效"),
  TOKEN_NOT_FOUND(1507, "token 不存在"),
  EXCEEDED_GATEWAY_CONNECTION_LIMIT(1601, "连接数超过网关最大限制"),
  INVALID_CONNECTION_HANDSHAKE_PARAMETERS(1602, "连接握手参数错误"),
  FILE_UPLOAD_EXPIRED(1701, "文件上传过期");

  private final int code;
  private final String description;

  ErrorCode(int code, String description) {
    this.code = code;
    this.description = description;
  }

  public int getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }
}
