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
package cn.asany.storage.core.engine.oss;

import cn.asany.storage.api.*;
import cn.asany.storage.core.AbstractFileObject;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import net.asany.jfantasy.framework.error.IgnoreException;
import net.asany.jfantasy.framework.util.common.StreamUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.framework.util.common.file.FileUtil;
import net.asany.jfantasy.framework.util.regexp.RegexpUtil;

/**
 * OSS 存储器
 *
 * @author limaofeng
 */
public class OSSStorage implements Storage {

  private final String id;
  private final String bucketName;
  private final OSSClient client;
  private final IMultipartUpload multipartUpload;

  public OSSStorage(
      String id, String endpoint, String accessKeyId, String accessKeySecret, String bucketName) {
    this.id = id;
    this.bucketName = bucketName;
    CredentialsProvider credsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);
    ClientConfiguration configuration = new ClientConfiguration();
    this.client = new OSSClient(endpoint, credsProvider, configuration);
    this.multipartUpload = new OSSMultipartUpload(this.client, bucketName);
  }

  @Override
  public String getId() {
    return this.id;
  }

  protected String getStorePath(String remotePath) {
    String path = RegexpUtil.replace(remotePath, "[^/]+[/]{0,1}$", "");
    return RegexpUtil.replace(remotePath, "^/", "");
  }

  @Override
  public void writeFile(String remotePath, File file) throws IOException {
    String path = getStorePath(remotePath);
    createFolder(path);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(FileUtil.getMimeType(file));
    PutObjectRequest request = new PutObjectRequest(bucketName, path, file);
    request.setMetadata(metadata);
    client.putObject(request);
  }

  @Override
  public void writeFile(String remotePath, InputStream in) throws IOException {
    createFolder(
        remotePath.endsWith("/")
            ? remotePath
            : RegexpUtil.replace(remotePath, "[^/]+[/]{0,1}$", ""));
    ObjectMetadata meta = new ObjectMetadata();
    meta.setContentLength(in.available());
    client.putObject(this.bucketName, RegexpUtil.replace(remotePath, "^/", ""), in, meta);
  }

  private void createFolder(String folders) {
    String path = "";
    for (String folder : StringUtil.tokenizeToStringArray(folders, "/")) {
      if (!client.doesObjectExist(bucketName, path += folder + "/")) {
        ObjectMetadata objectMeta = new ObjectMetadata();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        objectMeta.setContentLength(0);
        try {
          client.putObject(bucketName, path, in, objectMeta);
        } finally {
          StreamUtil.closeQuietly(in);
        }
      }
    }
  }

  @Override
  public OutputStream writeFile(String remotePath) throws IOException {
    throw new IOException(" OSS 不支持直接获取输入流对象操作文件,可以通过自定义 OutputStream 实现该方法!");
  }

  @Override
  public void readFile(String remotePath, String localPath) throws IOException {
    readFile(remotePath, new FileOutputStream(localPath));
  }

  @Override
  public void readFile(String remotePath, OutputStream out) throws IOException {
    StreamUtil.copyThenClose(readFile(remotePath), out);
  }

  @Override
  public InputStream readFile(String remotePath) throws IOException {
    FileObject fileObject = this.retrieveFileItem(remotePath);
    if (fileObject == null) {
      throw new FileNotFoundException("文件:" + remotePath + "不存在!");
    }
    return fileObject.getInputStream();
  }

  @Override
  public List<FileObject> listFiles() {
    return this.listFiles(FileObject.ROOT_PATH);
  }

  @Override
  public List<FileObject> listFiles(String remotePath) {
    FileObject fileObject = retrieveFileItem(remotePath);
    return fileObject == null ? Collections.emptyList() : fileObject.listFiles();
  }

  @Override
  public List<FileObject> listFiles(FileItemSelector selector) {
    return this.listFiles(FileObject.ROOT_PATH, selector);
  }

  @Override
  public List<FileObject> listFiles(String remotePath, FileItemSelector selector) {
    FileObject fileObject = retrieveFileItem(remotePath);
    return fileObject == null ? Collections.emptyList() : fileObject.listFiles(selector);
  }

  @Override
  public List<FileObject> listFiles(FileItemFilter filter) {
    return this.listFiles(FileObject.ROOT_PATH, filter);
  }

  @Override
  public List<FileObject> listFiles(String remotePath, FileItemFilter filter) {
    FileObject fileObject = retrieveFileItem(remotePath);
    return fileObject == null ? Collections.emptyList() : fileObject.listFiles(filter);
  }

  @Override
  public FileObject getFileItem(String remotePath) {
    return retrieveFileItem(remotePath);
  }

  @Override
  public IMultipartUpload multipartUpload() {
    return this.multipartUpload;
  }

  private FileObject retrieveFileItem(OSSObjectSummary objectSummary) {
    String absolutePath = FileObject.ROOT_PATH + objectSummary.getKey();
    if (absolutePath.endsWith(FileObject.SEPARATOR)) {
      return new OSSFileObject(
          this, absolutePath, client.getObjectMetadata(bucketName, objectSummary.getKey()));
    } else {
      return new OSSFileObject(
          this,
          absolutePath,
          objectSummary.getSize(),
          objectSummary.getLastModified(),
          client.getObjectMetadata(bucketName, objectSummary.getKey()));
    }
  }

  private FileObject retrieveFileItem(String absolutePath) {
    ObjectMetadata objectMetadata;
    if ("/".equals(absolutePath)) {
      objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(0);
      objectMetadata.setContentType("application/ostet-stream");
    } else {
      String ossAbsolutePath = RegexpUtil.replace(absolutePath, "^/", "");
      if (!client.doesObjectExist(bucketName, ossAbsolutePath)) {
        return null;
      }
      objectMetadata = client.getObjectMetadata(bucketName, ossAbsolutePath);
    }
    if (absolutePath.endsWith("/")) {
      return new OSSFileObject(this, absolutePath, objectMetadata);
    } else {
      return new OSSFileObject(
          this,
          absolutePath,
          objectMetadata.getContentLength(),
          objectMetadata.getLastModified(),
          objectMetadata);
    }
  }

  @Override
  public void removeFile(String remotePath) {
    if (remotePath.endsWith("/")) {
      ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
      String prefix = RegexpUtil.replace(remotePath, "^/", "");
      if (StringUtil.isNotBlank(prefix)) {
        listObjectsRequest.setPrefix(prefix);
      }
      ObjectListing listing = client.listObjects(listObjectsRequest);
      for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
        client.deleteObject(bucketName, objectSummary.getKey());
      }
      for (String commonPrefix : listing.getCommonPrefixes()) {
        if (client.doesObjectExist(bucketName, commonPrefix)) {
          client.deleteObject(bucketName, commonPrefix);
        }
      }
    } else {
      client.deleteObject(bucketName, RegexpUtil.replace(remotePath, "^/", ""));
    }
  }

  public class OSSFileObject extends AbstractFileObject {
    private final String ossAbsolutePath;
    private final OSSStorage storage;

    protected OSSFileObject(
        OSSStorage storage, String absolutePath, ObjectMetadata objectMetadata) {
      super(
          absolutePath,
          new FileObjectMetadata(
              objectMetadata.getRawMetadata(), objectMetadata.getUserMetadata()));
      this.ossAbsolutePath = RegexpUtil.replace(absolutePath, "^/", "");
      this.storage = storage;
    }

    protected OSSFileObject(
        OSSStorage storage,
        String absolutePath,
        long size,
        Date lastModified,
        ObjectMetadata objectMetadata) {
      super(
          absolutePath,
          size,
          lastModified,
          new FileObjectMetadata(
              objectMetadata.getRawMetadata(), objectMetadata.getUserMetadata()));
      this.ossAbsolutePath = RegexpUtil.replace(absolutePath, "^/", "");
      this.storage = storage;
    }

    @Override
    public FileObject getParentFile() {
      if (FileObject.ROOT_PATH.equals(this.getPath())) {
        return null;
      }
      return this.storage.retrieveFileItem(
          RegexpUtil.replace(this.getPath(), "[^/]+[/]{0,1}$", ""));
    }

    @Override
    public List<FileObject> listFiles() {
      if (!this.isDirectory()) {
        return Collections.emptyList();
      }
      ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
      listObjectsRequest.setDelimiter("/");
      String prefix = ossAbsolutePath;
      if (StringUtil.isNotBlank(prefix)) {
        listObjectsRequest.setPrefix(prefix);
      }
      ObjectListing listing = client.listObjects(listObjectsRequest);
      List<FileObject> fileObjects = new ArrayList<>();
      for (String commonPrefix : listing.getCommonPrefixes()) {
        if (commonPrefix.equals(ossAbsolutePath)) {
          continue;
        }
        fileObjects.add(this.storage.retrieveFileItem(FileObject.ROOT_PATH + commonPrefix));
      }
      for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
        if (objectSummary.getKey().equals(ossAbsolutePath)) {
          continue;
        }
        fileObjects.add(this.storage.retrieveFileItem(objectSummary));
      }
      return fileObjects;
    }

    @Override
    public List<FileObject> listFiles(FileItemFilter filter) {
      if (!this.isDirectory()) {
        return Collections.emptyList();
      }
      List<FileObject> fileObjects = new ArrayList<>();
      for (FileObject item : listFiles()) {
        if (filter.accept(item)) {
          fileObjects.add(item);
        }
      }
      return fileObjects;
    }

    @Override
    public List<FileObject> listFiles(FileItemSelector selector) {
      if (!this.isDirectory()) {
        return Collections.emptyList();
      }
      return FileObject.Util.flat(this.listFiles(), selector);
    }

    @Override
    public Storage getStorage() {
      return this.storage;
    }

    @Override
    public InputStream getInputStream() throws IOException {
      if (this.isDirectory()) {
        throw new IgnoreException("当前对象为一个目录,不能获取 InputStream ");
      }
      return client.getObject(bucketName, ossAbsolutePath).getObjectContent();
    }

    @Override
    public InputStream getInputStream(long start, long end) throws IOException {
      if (this.isDirectory()) {
        throw new IgnoreException("当前对象为一个目录,不能获取 InputStream ");
      }
      GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, ossAbsolutePath);
      getObjectRequest.setRange(start, end);
      return client.getObject(getObjectRequest).getObjectContent();
    }
  }
}
