package cn.asany.nuwa.app.bean;

import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 客户端凭证
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "AUTH_CLIENT_SECRET")
public class ClientSecret extends BaseBusEntity {

    @Id
    @Column(name = "ID", updatable = false)
    private Long id;
    /**
     * 密钥
     */
    @Column(name = "SECRET", length = 40, updatable = false)
    private String secret;
    /**
     * 客户端
     */
    @JoinColumn(name = "CLIENT_ID",table = "NUWA_APPLICATION", referencedColumnName = "CLIENT_ID", foreignKey = @ForeignKey(name = "FK_CLIENT_SECRET_CLIENT"), updatable = false, nullable = false)
    private String client;

}
