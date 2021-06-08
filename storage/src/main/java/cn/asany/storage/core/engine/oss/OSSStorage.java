package cn.asany.storage.core.engine.oss;

import cn.asany.storage.api.*;
import cn.asany.storage.core.AbstractFileObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OSSStorage implements Storage {

    private AccessKey accessKey;
    private String bucketName;
    private String endpoint;
    private OSSClient client;

    public OSSStorage(String endpoint, AccessKey accessKey, String bucketName) {
        this.accessKey = accessKey;
        this.bucketName = bucketName;
        this.endpoint = endpoint;
        this.client = new OSSClient(this.endpoint, this.accessKey.getId(), this.accessKey.getSecret());
    }

    @Override
    public void writeFile(String remotePath, File file) throws IOException {
        createFolder(remotePath.endsWith("/") ? remotePath : RegexpUtil.replace(remotePath, "[^/]+[/]{0,1}$", ""));
        client.putObject(this.bucketName, RegexpUtil.replace(remotePath, "^/", ""), file);
    }

    @Override
    public void writeFile(String remotePath, InputStream in) throws IOException {
        createFolder(remotePath.endsWith("/") ? remotePath : RegexpUtil.replace(remotePath, "[^/]+[/]{0,1}$", ""));
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
        return this.listFiles("/");
    }

    @Override
    public List<FileObject> listFiles(String remotePath) {
        FileObject fileObject = retrieveFileItem(remotePath);
        return fileObject == null ? Collections.<FileObject>emptyList() : fileObject.listFiles();
    }

    @Override
    public List<FileObject> listFiles(FileItemSelector selector) {
        return this.listFiles("/", selector);
    }

    @Override
    public List<FileObject> listFiles(String remotePath, FileItemSelector selector) {
        FileObject fileObject = retrieveFileItem(remotePath);
        return fileObject == null ? Collections.<FileObject>emptyList() : fileObject.listFiles(selector);
    }

    @Override
    public List<FileObject> listFiles(FileItemFilter filter) {
        return this.listFiles("/", filter);
    }

    @Override
    public List<FileObject> listFiles(String remotePath, FileItemFilter filter) {
        FileObject fileObject = retrieveFileItem(remotePath);
        return fileObject == null ? Collections.<FileObject>emptyList() : fileObject.listFiles(filter);
    }

    @Override
    public FileObject getFileItem(String remotePath) {
        return retrieveFileItem(remotePath);
    }

    private FileObject retrieveFileItem(OSSObjectSummary objectSummary) {
        String absolutePath = "/" + objectSummary.getKey();
        if (absolutePath.endsWith("/")) {
            return new OSSFileObject(this, absolutePath, client.getObjectMetadata(bucketName, objectSummary.getKey()));
        } else {
            return new OSSFileObject(this, absolutePath, objectSummary.getSize(), objectSummary.getLastModified(), client.getObjectMetadata(bucketName, objectSummary.getKey()));
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
            return new OSSFileObject(this, absolutePath, objectMetadata.getContentLength(), objectMetadata.getLastModified(), objectMetadata);
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
        private String ossAbsolutePath;
        private OSSStorage fileManager;

        protected OSSFileObject(OSSStorage fileManager, String absolutePath, ObjectMetadata objectMetadata) {
            super(absolutePath, new FileObjectMetadata(objectMetadata.getRawMetadata(), objectMetadata.getUserMetadata()));
            this.ossAbsolutePath = RegexpUtil.replace(absolutePath, "^/", "");
            this.fileManager = fileManager;
        }

        protected OSSFileObject(OSSStorage fileManager, String absolutePath, long size, Date lastModified, ObjectMetadata objectMetadata) {
            super(absolutePath, size, lastModified, new FileObjectMetadata(objectMetadata.getRawMetadata(), objectMetadata.getUserMetadata()));
            this.ossAbsolutePath = RegexpUtil.replace(absolutePath, "^/", "");
            this.fileManager = fileManager;
        }

        @Override
        public FileObject getParentFile() {
            return this.fileManager.retrieveFileItem(RegexpUtil.replace(this.getAbsolutePath(), "[^/]+[/]{0,1}$", ""));
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
                fileObjects.add(this.fileManager.retrieveFileItem("/" + commonPrefix));
            }
            for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
                if (objectSummary.getKey().equals(ossAbsolutePath)) {
                    continue;
                }
                fileObjects.add(this.fileManager.retrieveFileItem(objectSummary));
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
        public InputStream getInputStream() throws IOException {
            if (this.isDirectory()) {
                throw new IgnoreException("当前对象为一个目录,不能获取 InputStream ");
            }
            return client.getObject(bucketName, ossAbsolutePath).getObjectContent();
        }
    }

    public static class AccessKey {
        private String id;
        private String secret;

        public AccessKey(String id, String secret) {
            this.id = id;
            this.secret = secret;
        }

        public String getId() {
            return id;
        }

        public String getSecret() {
            return secret;
        }
    }

}
