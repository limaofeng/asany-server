package cn.asany.storage.core.engine.disk;

import cn.asany.storage.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.file.FileUtil;

@Slf4j
public class LocalStorage implements Storage {

  private final String id;
  protected String defaultDir;

  @SneakyThrows
  public LocalStorage(String id, String defaultDir) {
    super();
    this.id = id;
    this.setDefaultDir(defaultDir);
  }

  @Override
  public void readFile(String remotePath, String localPath) throws IOException {
    readFile(remotePath, Files.newOutputStream(Paths.get(localPath)));
  }

  @Override
  public void readFile(String remotePath, OutputStream out) throws IOException {
    StreamUtil.copyThenClose(getInputStream(remotePath), out);
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void writeFile(String remotePath, File file) throws IOException {
    writeFile(remotePath, Files.newInputStream(file.toPath()));
  }

  @Override
  public void writeFile(String remotePath, InputStream in) throws IOException {
    StreamUtil.copyThenClose(in, getOutputStream(remotePath));
  }

  @Override
  public InputStream readFile(String remotePath) throws IOException {
    return getInputStream(remotePath);
  }

  public void setDefaultDir(String defaultDir) throws IOException {
    FileUtil.mkdir(new File(defaultDir).toPath());
    this.defaultDir = defaultDir;
  }

  @Override
  public OutputStream writeFile(String remotePath) throws IOException {
    return getOutputStream(remotePath);
  }

  private OutputStream getOutputStream(String absolutePath) throws IOException {
    return Files.newOutputStream(createFile(absolutePath).toPath());
  }

  private String filterRemotePath(String remotePath) {
    return remotePath.startsWith("/") ? remotePath : ("/" + remotePath);
  }

  private InputStream getInputStream(String remotePath) throws IOException {
    File file = new File(this.defaultDir + filterRemotePath(remotePath));
    if (!file.exists()) {
      throw new FileNotFoundException("文件:" + remotePath + "不存在!");
    }
    return Files.newInputStream(file.toPath());
  }

  private File createFile(String remotePath) throws IOException {
    return FileUtil.createFile(Paths.get(this.defaultDir + filterRemotePath(remotePath))).toFile();
  }

  @Override
  public FileObject getFileItem(String remotePath) {
    return retrieveFileItem(filterRemotePath(remotePath));
  }

  private FileObject root() {
    return retrieveFileItem(File.separator);
  }

  private FileObject retrieveFileItem(String absolutePath) {
    final File file = new File(this.defaultDir + absolutePath);
    if (!file.exists()) {
      throw new RuntimeException("文件不存在");
    }
    Map<String, Object> metadata = new HashMap<>();
    metadata.put(FileObjectMetadata.CONTENT_TYPE, FileUtil.getMimeType(file));
    return file.isDirectory()
        ? new LocalFileObject(this, file)
        : new LocalFileObject(this, file, new FileObjectMetadata(metadata));
  }

  FileObject retrieveFileItem(final File file) {
    if (!file.getAbsolutePath().startsWith(new File(this.defaultDir).getAbsolutePath())
        || !file.exists()) {
      return null;
    }
    Map<String, Object> metadata = new HashMap<>();
    metadata.put(FileObjectMetadata.CONTENT_TYPE, FileUtil.getMimeType(file));
    return file.isDirectory()
        ? new LocalFileObject(this, file)
        : new LocalFileObject(this, file, new FileObjectMetadata(metadata));
  }

  @Override
  public List<FileObject> listFiles() {
    return root().listFiles();
  }

  @Override
  public List<FileObject> listFiles(FileItemFilter filter) {
    return root().listFiles(filter);
  }

  @Override
  public List<FileObject> listFiles(FileItemSelector selector) {
    return root().listFiles(selector);
  }

  @Override
  public List<FileObject> listFiles(String remotePath) {
    FileObject fileObject = retrieveFileItem(remotePath);
    return fileObject.listFiles();
  }

  @Override
  public List<FileObject> listFiles(String remotePath, FileItemFilter fileItemFilter) {
    FileObject fileObject = retrieveFileItem(remotePath);
    return fileObject.listFiles(fileItemFilter);
  }

  @Override
  public List<FileObject> listFiles(String remotePath, FileItemSelector selector) {
    return getFileItem(remotePath).listFiles(selector);
  }

  @Override
  public void removeFile(String remotePath) throws IOException {
    FileUtil.rm(Paths.get(this.defaultDir + remotePath));
  }
}
