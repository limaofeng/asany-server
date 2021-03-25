package cn.asany.shanhai.core.bean;

import cn.asany.shanhai.core.bean.enums.InterfaceProtocol;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 服务发现
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_SERVICE")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class NameServer extends BaseBusEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 编码
     */
    @Column(name = "CODE", length = 50, unique = true)
    private String code;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 50)
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 200)
    private String description;
    /**
     * 名称
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PROTOCOL", length = 50)
    private InterfaceProtocol protocol;
    /**
     * 端点
     */
    @Column(name = "ENDPOINT", length = 100)
    private String endpoint;

}
