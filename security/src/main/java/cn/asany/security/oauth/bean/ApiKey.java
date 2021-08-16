package cn.asany.security.oauth.bean;
//
// import lombok.Data;
// import org.hibernate.annotations.Any;
// import org.hibernate.annotations.AnyMetaDef;
// import org.hibernate.annotations.GenericGenerator;
// import org.hibernate.annotations.MetaValue;
// import org.jfantasy.framework.dao.BaseBusEntity;
// import org.jfantasy.framework.dao.hibernate.converter.MapConverter;
//
// import javax.persistence.*;
// import java.util.Date;
// import java.util.Map;
//
/// **
// * API 授权
// *
// * @author limaofeng
// */
// @Data
// @Entity
// @Table(name = "OAUTH_APIKEY")
// public class ApiKey extends BaseBusEntity {
//
//    /**
//     * 调用 api key
//     */
//    @Id
//    @Column(name = "_KEY", updatable = false)
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid")
//    private String key;
//    /**
//     * 描述
//     */
//    @Column(name = "DESCRIPTION")
//    private String description;
//    /**
//     * api 配置的一些额外信息
//     */
//    @Column(name = "PROPERTIES", columnDefinition = "Text")
//    @Convert(converter = MapConverter.class)
//    private Map<String, Object> attrs;
//    /**
//     * 对应的应用
//     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "APP_ID", updatable = false, foreignKey = @ForeignKey(name =
// "FK_OAUTH_APPKEY_APPID"))
//    private Application application;
//    /**
//     * 到期时间
//     */
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "EXPIRES_AT")
//    private Date expiresAt;
//
//    /**
//     * 平台简码(web/app)
//     */
//    @Column(name = "PLATFORM")
//    private String platform;
//
//    @Any(
//        metaColumn = @Column(name = "OWNERSHIP_TYPE", length = 10, insertable = false, updatable =
// false),
//        fetch = FetchType.LAZY
//    )
//    @AnyMetaDef(
//        idType = "long", metaType = "string",
//        metaValues = {
//            @MetaValue(targetEntity = Application.class, value = "APPLICATION"),
////            @MetaValue(targetEntity = User.class, value = "PERSONAL")
//        }
//    )
//    @JoinColumn(name = "OWNERSHIP_ID", insertable = false, updatable = false)
//    private Ownership ownership;
//
// }
