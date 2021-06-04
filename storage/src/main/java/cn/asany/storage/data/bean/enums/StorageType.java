package cn.asany.storage.data.bean.enums;

/**
 * 存储类型
 *
 * @author limaofeng
 */
public enum StorageType {
    DISK("本地文件系统"), OSS("阿里开放存储服务"), FTP("FTP文件系统"), jdbc("数据库存储"), MINIO("上传文件存储");

    private String name;

    StorageType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
