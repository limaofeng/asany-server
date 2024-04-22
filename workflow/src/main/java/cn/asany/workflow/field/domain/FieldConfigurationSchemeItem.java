package cn.asany.workflow.field.domain;

import cn.asany.pm.type.bean.IssueType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;

import jakarta.persistence.*;
import java.util.Objects;

/**
 *
 * 字段方案明显
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "FSM_FIELD_CONFIGURATION_SCHEME_ITEM")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldConfigurationSchemeItem extends BaseBusEntity {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22)
    @GeneratedValue(generator = "field_configuration_scheme_item_gen")
    @TableGenerator(name = "field_configuration_scheme_item_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "field_configuration_scheme_item:id", valueColumnName = "gen_value")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ISSUE_TYPE_ID", foreignKey = @ForeignKey(name = "FK_FIELD_CONFIGURATION_SCHEME_ITEM_ISSUE_TYPE"))
    @ToString.Exclude
    private IssueType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONFIG_ID", foreignKey = @ForeignKey(name = "FK_FIELD_CONFIGURATION_SCHEME_ITEM_CONFIG"))
    @ToString.Exclude
    private FieldConfiguration fieldConfiguration;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEME_ID", foreignKey = @ForeignKey(name = "FK_FIELD_CONFIGURATION_SCHEME_ITEM_SCHEME"))
    @ToString.Exclude
    private FieldConfiguration fieldConfigurationScheme;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FieldConfigurationSchemeItem that = (FieldConfigurationSchemeItem) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
