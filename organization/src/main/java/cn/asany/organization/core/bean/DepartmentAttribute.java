package cn.asany.organization.core.bean;

import cn.asany.organization.core.bean.databind.DepartmentDeserializer;
import cn.asany.organization.core.bean.databind.DepartmentSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: 部门
 * @date 2019-03-11 14:35
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_DEPARTMENT_ATTRIBUTE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "children", "employees", "positions", "roles", "links", "grants"})
public class DepartmentAttribute extends BaseBusEntity {

    @Id
    @Column(name = "ID", precision = 22)
    @GeneratedValue(generator = "org_department_attribute_gen")
    @TableGenerator(name = "org_department_attribute_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "org_department_attribute:id", valueColumnName = "gen_value")
    private Long id;
    /**
     * 部门
     */
    @JsonSerialize(using = DepartmentSerializer.class)
    @JsonDeserialize(using = DepartmentDeserializer.class)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "FK_AUTH_DEPARTMENT_ATTRIBUTE_ID"), nullable = false, updatable = false)
    private Department department;

    /**
     * 属性名称
     */
    @Column(name = "attribute_name", length = 50)
    private String attributeName;
    /**
     * 属性值
     */
    @Column(name = "attribute_value", length = 50)
    private String attributeValue;
    /**
     * 描述信息
     */
    @Column(name = "DESCRIPTION", length = 150)
    private String description;
}