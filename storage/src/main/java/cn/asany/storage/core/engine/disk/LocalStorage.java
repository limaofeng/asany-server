package cn.asany.storage.core.engine.disk;

import cn.asany.storage.api.*;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalStorage implements Storage {

    protected String defaultDir;

    public LocalStorage() {
        super();
    }

    public LocalStorage(String defaultDir) {
        super();
        this.defaultDir = defaultDir;
    }

    @Override
    public void readFile(String remotePath, String localPath) throws IOException {
        readFile(remotePath, new FileOutputStream(localPath));
    }

    @Override
    public void readFile(String remotePath, OutputStream out) throws IOException {
        StreamUtil.copyThenClose(getInputStream(remotePath), out);
    }

    @Override
    public void writeFile(String remotePath, File file) throws IOException {
        writeFile(remotePath, new FileInputStream(file));
    }

    @Override
    public void writeFile(String remotePath, InputStream in) throws IOException {
        StreamUtil.copyThenClose(in, getOutputStream(remotePath));
    }

    @Override
    public InputStream readFile(String remotePath) throws IOException {
        return getInputStream(remotePath);
    }

    public void setDefaultDir(String defaultDir) {
        FileUtil.createFolder(new File(defaultDir));
        this.defaultDir = defaultDir;
    }

    @Override
    public OutputStream writeFile(String remotePath) throws IOException {
        return getOutputStream(remotePath);
    }

    private OutputStream getOutputStream(String absolutePath) throws IOException {
        return new FileOutputStream(createFile(absolutePath));
    }

    private String filterRemotePath(String remotePath) {
        return StringUtil.defaultValue(remotePath, "").startsWith("/") ? remotePath : ("/" + remotePath);
    }

    private InputStream getInputStream(String remotePath) throws IOException {
        File file = new File(this.defaultDir + filterRemotePath(remotePath));
        if (!file.exists()) {
            throw new FileNotFoundException("文件:" + remotePath + "不存在!");
        }
        return new FileInputStream(file);
    }

    private File createFile(String remotePath) {
        return FileUtil.createFile(this.defaultDir + filterRemotePath(remotePath));
    }

    @Override
    public FileObject getFileItem(String remotePath) {
        return retrieveFileItem(filterRemotePath(remotePath));
    }

    private FileObject root() {
        FileObject root = retrieveFileItem(File.separator);
        assert root != null;
        return root;
    }

    private FileObject retrieveFileItem(String absolutePath) {
        final File file = new File(this.defaultDir + absolutePath);
        if (!file.getAbsolutePath().startsWith(new File(this.defaultDir).getAbsolutePath()) || !file.exists()) {
            return null;
        }
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(FileObjectMetadata.CONTENT_TYPE, FileUtil.getMimeType(file));
        return file.isDirectory() ? new LocalFileObject(this, file) : new LocalFileObject(this, file, new FileObjectMetadata(metadata));
    }

    FileObject retrieveFileItem(final File file) {
        if (!file.getAbsolutePath().startsWith(new File(this.defaultDir).getAbsolutePath()) || !file.exists()) {
            return null;
        }
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(FileObjectMetadata.CONTENT_TYPE, FileUtil.getMimeType(file));
        return file.isDirectory() ? new LocalFileObject(this, file) : new LocalFileObject(this, file, new FileObjectMetadata(metadata));
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
        return fileObject == null ? Collections.emptyList() : fileObject.listFiles();
    }

    @Override
    public List<FileObject> listFiles(String remotePath, FileItemFilter fileItemFilter) {
        FileObject fileObject = retrieveFileItem(remotePath);
        return fileObject == null ? Collections.emptyList() : fileObject.listFiles(fileItemFilter);
    }

    @Override
    public List<FileObject> listFiles(String remotePath, FileItemSelector selector) {
        return getFileItem(remotePath).listFiles(selector);
    }

    @Override
    public void removeFile(String remotePath) {
        FileUtil.delFile(this.defaultDir + remotePath);
    }

}