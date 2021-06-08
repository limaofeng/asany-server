package cn.asany.nuwa.app.bean;

import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 区分不同的端
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "NUWA_ROUTESPACE")
public class Routespace extends BaseBusEntity {
    @Id
    @Column(name = "ID", length = 50, updatable = false)
    private String id;
    @Column(name = "NAME", length = 50)
    private String name;

}
