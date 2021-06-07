package cn.asany.storage.data.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;


/**
 * 文件部分
 *
 * @author limaofeng
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@Table(name = "STORAGE_FILEPART", uniqueConstraints = {@UniqueConstraint(columnNames = {"ENTIRE_FILE_HASH", "PART_FILE_HASH"})})
public class FilePart extends BaseBusEntity {

    @Id
    @Column(name = "ABSOLUTE_PATH", nullable = false, updatable = false, length = 250)
    private String path;

    @Column(name = "FILE_MANAGER_CONFIG_ID", nullable = false, updatable = false, length = 50)
    private String namespace;
    /**
     * 完整文件的hash值
     */
    @Column(name = "ENTIRE_FILE_HASH")
    private String entireFileHash;
    /**
     * 片段文件的hash值
     */
    @Column(name = "PART_FILE_HASH")
    private String partFileHash;
    /**
     * 总的段数
     */
    @Column(name = "PAER_TOTAL")
    private Integer total;
    /**
     * 当前段数
     */
    @Column(name = "PAER_INDEX")
    private Integer index;
}
