package cn.asany.nuwa.app.bean;

import cn.asany.nuwa.template.bean.ApplicationTemplate;
import lombok.*;
import org.hibernate.annotations.Where;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;

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
    @Column(name = "ID", length = 40)
    private String id;
    @Column(name = "NAME", length = 50)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_TEMPLATE_ID", foreignKey = @ForeignKey(name = "FK_ROUTESPACE_APP_TEMP_ID"), nullable = false)
    private ApplicationTemplate applicationTemplate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "NUWA_APPLICATION_ROUTESPACE",
        joinColumns = @JoinColumn(name = "ROUTESPACE_ID"),
        inverseJoinColumns = @JoinColumn(name = "APPLICATION_ID"),
        foreignKey = @ForeignKey(name = "FK_APPLICATION_ROUTESPACE_SPACEID")
    )
    private List<Application> applications;

}
