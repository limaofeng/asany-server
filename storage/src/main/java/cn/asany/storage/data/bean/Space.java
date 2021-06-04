package cn.asany.storage.data.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 文件上传时，为其指定的上传目录
 * <p/>
 * 通过Key获取上传的目录及文件管理器
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-23 上午10:30:06
 */
@Data
@Entity
@Table(name = "STORAGE_SPACE")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Space extends BaseBusEntity {

    private static final long serialVersionUID = 8150927437017643578L;

    @Id
    @Column(name = "DIR_KEY", nullable = false, insertable = true, updatable = false, length = 50, unique = true)
    private String key;
    /**
     * 对应的文件管理器
     */
    @JoinColumn(name = "STORAGE_ID", insertable = true, updatable = false, foreignKey = @ForeignKey(name = "FK_STORAGE_SPACE_STORAGE"))
    @ManyToOne(fetch = FetchType.LAZY)
    private StorageConfig storage;
    /**
     * 对应的默认目录
     */
    @Column(name = "DIR_PATH", length = 250)
    private String dirPath;
    /**
     * 目录名称
     */
    @Column(name = "DIR_NAME", length = 250)
    private String name;

}
