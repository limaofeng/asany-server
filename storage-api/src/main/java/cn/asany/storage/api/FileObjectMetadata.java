package cn.asany.storage.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jfantasy.framework.util.common.DateUtil;

/**
 * 文件对象元数据
 *
 * @author limaofeng
 */
public class FileObjectMetadata implements Serializable {
  public static final String AUTHORIZATION = "Authorization";
  public static final String CACHE_CONTROL = "Cache-Control";
  public static final String CONTENT_DISPOSITION = "Content-Disposition";
  public static final String CONTENT_ENCODING = "Content-Encoding";
  public static final String CONTENT_LENGTH = "Content-Length";
  public static final String CONTENT_MD5 = "Content-MD5";
  public static final String CONTENT_TYPE = "Content-Type";
  public static final String TRANSFER_ENCODING = "Transfer-Encoding";
  public static final String DATE = "Date";
  public static final String ETAG = "ETag";
  public static final String EXPIRES = "Expires";
  public static final String HOST = "Host";
  public static final String LAST_MODIFIED = "Last-Modified";
  public static final String RANGE = "Range";
  public static final String LOCATION = "Location";
  public static final String IS_DIR = "IS_DIR";

  public static FileObjectMetadata ROOT =
      FileObjectMetadata.builder().dir(true).contentLength(0).contentType("").build();

  /** 用户自定义的元数据，表示以x-oss-meta-为前缀的请求头。 */
  private final Map<String, Object> userMetadata = new HashMap<>();
  /** 非用户自定义的元数据 */
  private final Map<String, Object> metadata = new HashMap<>();

  public static final long DEFAULT_FILE_SIZE_LIMIT = 5 * 1024 * 1024 * 1024L;

  public FileObjectMetadata(Map<String, Object> metadata) {
    if (metadata != null && !metadata.isEmpty()) {
      this.metadata.putAll(metadata);
    }
  }

  public boolean hasMetadataByKey(String key) {
    return metadata.containsKey(key);
  }

  public FileObjectMetadata(Map<String, Object> metadata, Map<String, String> userMetadata) {
    this(metadata);
    this.setUserMetadata(userMetadata);
  }

  public FileObjectMetadata() {}

  public static FileObjectMetadataBuilder builder() {
    return new FileObjectMetadataBuilder();
  }

  /**
   * 获取用户自定义的元数据。
   *
   * <p>同时，元数据字典的键名是不区分大小写的，并且在从服务器端返回时会全部以小写形式返回，
   * 即使在设置时给定了大写字母。比如键名为：MyUserMeta，通过getObjectMetadata接口 返回时键名会变为：myusermeta。
   *
   * @return 用户自定义的元数据。
   */
  public Map<String, Object> getUserMetadata() {
    return userMetadata;
  }

  /**
   * 设置用户自定义的元数据，表示以x-oss-meta-为前缀的请求头。
   *
   * @param userMetadata 用户自定义的元数据。
   */
  public void setUserMetadata(Map<String, String> userMetadata) {
    this.userMetadata.clear();
    if (userMetadata != null && !userMetadata.isEmpty()) {
      this.userMetadata.putAll(userMetadata);
    }
  }

  /**
   * 设置请求头（内部使用）。
   *
   * @param key 请求头的Key。
   * @param value 请求头的Value。
   */
  public void setHeader(String key, Object value) {
    metadata.put(key, value);
  }

  /**
   * 添加一个用户自定义的元数据。
   *
   * @param key 请求头的Key。 这个Key不需要包含OSS要求的前缀，即不需要加入“x-oss-meta-”。
   * @param value 请求头的Value。
   */
  public void addUserMetadata(String key, Object value) {
    this.userMetadata.put(key, value);
  }

  /**
   * 获取Last-Modified请求头的值，表示Object最后一次修改的时间。
   *
   * @return Object最后一次修改的时间。
   */
  public Date getLastModified() {
    return (Date) metadata.get(FileObjectMetadata.LAST_MODIFIED);
  }

  /**
   * 设置Last-Modified请求头的值，表示Object最后一次修改的时间（内部使用）。
   *
   * @param lastModified Object最后一次修改的时间。
   */
  public void setLastModified(Date lastModified) {
    metadata.put(FileObjectMetadata.LAST_MODIFIED, lastModified);
  }

  public void setDir(boolean dir) {
    metadata.put(FileObjectMetadata.IS_DIR, dir);
  }

  /**
   * 获取Expires请求头。 如果Object没有定义过期时间，则返回null。
   *
   * @return Expires请求头。
   */
  public Date getExpirationTime() {
    return (Date) metadata.get(FileObjectMetadata.EXPIRES);
  }

  /**
   * 设置Expires请求头。
   *
   * @param expirationTime 过期时间。
   */
  public void setExpirationTime(Date expirationTime) {
    metadata.put(FileObjectMetadata.EXPIRES, DateUtil.formatRfc822Date(expirationTime));
  }

  /**
   * 获取Content-Length请求头，表示Object内容的大小。
   *
   * @return Object内容的大小。
   */
  public long getContentLength() {
    Long contentLength = (Long) metadata.get(FileObjectMetadata.CONTENT_LENGTH);
    return contentLength == null ? 0 : contentLength;
  }

  /**
   * 设置Content-Length请求头，表示Object内容的大小。 当上传Object到OSS时，请总是指定正确的content length。
   *
   * @param contentLength Object内容的大小。
   * @throws IllegalArgumentException Object内容的长度大小大于最大限定值：5G字节。
   */
  public void setContentLength(long contentLength) {
    if (contentLength > DEFAULT_FILE_SIZE_LIMIT) {
      throw new IllegalArgumentException("内容长度不能超过5G字节。");
    }
    metadata.put(FileObjectMetadata.CONTENT_LENGTH, contentLength);
  }

  /**
   * 获取Content-Type请求头，表示Object内容的类型，为标准的MIME类型。
   *
   * @return Object内容的类型，为标准的MIME类型。
   */
  public String getContentType() {
    return (String) metadata.get(FileObjectMetadata.CONTENT_TYPE);
  }

  /**
   * 获取Content-Type请求头，表示Object内容的类型，为标准的MIME类型。
   *
   * @param contentType Object内容的类型，为标准的MIME类型。
   */
  public void setContentType(String contentType) {
    metadata.put(FileObjectMetadata.CONTENT_TYPE, contentType);
  }

  /**
   * 获取Content-Encoding请求头，表示Object内容的编码方式。
   *
   * @return Object内容的编码方式。
   */
  public String getContentEncoding() {
    return (String) metadata.get(FileObjectMetadata.CONTENT_ENCODING);
  }

  /**
   * 设置Content-Encoding请求头，表示Object内容的编码方式。
   *
   * @param encoding 表示Object内容的编码方式。
   */
  public void setContentEncoding(String encoding) {
    metadata.put(FileObjectMetadata.CONTENT_ENCODING, encoding);
  }

  /**
   * 获取Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为。
   *
   * @return Cache-Control请求头。
   */
  public String getCacheControl() {
    return (String) metadata.get(FileObjectMetadata.CACHE_CONTROL);
  }

  /**
   * 设置Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为。
   *
   * @param cacheControl Cache-Control请求头。
   */
  public void setCacheControl(String cacheControl) {
    metadata.put(FileObjectMetadata.CACHE_CONTROL, cacheControl);
  }

  /**
   * 获取Content-Disposition请求头，表示MIME用户代理如何显示附加的文件。
   *
   * @return Content-Disposition请求头
   */
  public String getContentDisposition() {
    return (String) metadata.get(FileObjectMetadata.CONTENT_DISPOSITION);
  }

  /**
   * 设置Content-Disposition请求头，表示MIME用户代理如何显示附加的文件。
   *
   * @param disposition Content-Disposition请求头
   */
  public void setContentDisposition(String disposition) {
    metadata.put(FileObjectMetadata.CONTENT_DISPOSITION, disposition);
  }

  /**
   * 获取一个值表示与Object相关的hex编码的128位MD5摘要。
   *
   * @return 与Object相关的hex编码的128位MD5摘要。
   */
  public String getETag() {
    return (String) metadata.get(FileObjectMetadata.ETAG);
  }

  public void setETag(String etag) {
    metadata.put(FileObjectMetadata.ETAG, etag);
  }

  /**
   * 返回内部保存的请求头的元数据（内部使用）。
   *
   * @return 内部保存的请求头的元数据（内部使用）。
   */
  public Map<String, Object> getRawMetadata() {
    return Collections.unmodifiableMap(metadata);
  }

  public boolean isDir() {
    return (boolean) metadata.get(IS_DIR);
  }

  public static class FileObjectMetadataBuilder {

    private final FileObjectMetadata metadata = new FileObjectMetadata();

    public FileObjectMetadataBuilder etag(String etag) {
      this.metadata.setETag(etag);
      return this;
    }

    public FileObjectMetadataBuilder contentLength(long size) {
      this.metadata.setContentLength(size);
      return this;
    }

    public FileObjectMetadataBuilder contentType(String contentType) {
      this.metadata.setContentType(contentType);
      return this;
    }

    public FileObjectMetadataBuilder lastModified(Date date) {
      this.metadata.setLastModified(date);
      return this;
    }

    public FileObjectMetadataBuilder dir(boolean dir) {
      this.metadata.setDir(dir);
      return this;
    }

    public FileObjectMetadataBuilder addUserMetadata(String key, String value) {
      this.metadata.addUserMetadata(key, value);
      return this;
    }

    public FileObjectMetadata build() {
      return this.metadata;
    }
  }
}
