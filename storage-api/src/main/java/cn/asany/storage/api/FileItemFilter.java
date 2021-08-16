package cn.asany.storage.api;

/**
 * 文件匹配过滤器
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-8 下午4:49:48
 */
public interface FileItemFilter {

  boolean accept(FileObject item);
}
