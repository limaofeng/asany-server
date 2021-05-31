package cn.asany.security.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringArrayConverter;

import javax.persistence.*;

/**
 * @author liumeng
 * @Description: 业务角色数据(这里用一句话描述这个类的作用)
 * @date 11:35  2020-04-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORG_LOCAL_ROLE_DATA")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LocalRoleData extends BaseBusEntity {

    @Id
    @Column(name = "ID", length = 32)
    private String id;

    @Column(name = "ROLE_ID",length = 32)
    private String 	roleId;

    @Column(name = "BUSINESS_ID",length = 200)
    private String  businessId;

    /**
     * 对应数据
     */
    @Column(name = "COUNTERPART", precision = 300)
    @Convert(converter = StringArrayConverter.class)
    private String[] counterpart;
}
