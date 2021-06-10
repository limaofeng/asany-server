package cn.asany.nuwa.template.bean;

import cn.asany.nuwa.app.bean.ApplicationRoute;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "NUWA_APPLICATION_TEMPLATE")
public class ApplicationTemplate extends BaseBusEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    @OneToMany(mappedBy = "application", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ApplicationRoute> routes;
}
