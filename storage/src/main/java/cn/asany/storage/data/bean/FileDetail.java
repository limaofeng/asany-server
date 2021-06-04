package cn.asany.storage.data.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 文件信息表
 *
 * @author 软件
 */
@Data
@Entity
@Table(name = "STORAGE_FILEOBJECT")
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "folder", "real_path", "namespace", "md5"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FileDetail extends BaseBusEntity implements Cloneable {


    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 虚拟文件路径
     */
    @Column(name = "PATH", nullable = false, updatable = false, length = 250)
    private String path;
    /**
     * 文件类型
     */
    @Column(name = "MIME_TYPE", length = 50, nullable = false)
    private String mimeType;

    /**
     * 文件名称
     */
    @Column(name = "NAME", length = 50, nullable = false)
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 150, nullable = false)
    private String description;
    /**
     * 文件长度
     */
    @Column(name = "LENGTH")
    private Long size;
    /**
     * 文件MD5码
     */
    @Column(name = "MD5", length = 50, nullable = false)
    private String md5;
    /**
     * 文件真实路径
     */
    @Column(name = "REAL_PATH", length = 150, nullable = false)
    private String realPath;

    @JoinColumn(name = "FOLDER_ID", insertable = true, updatable = false, foreignKey = @ForeignKey(name = "FK_STORAGE_FILEOBJECT_FID"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Folder folder;
    /**
     * 文件命名空间
     */
    @Column(name = "STORAGE_ID", nullable = false, updatable = false, length = 50)
    private String storage;

}