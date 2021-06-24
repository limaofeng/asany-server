package cn.asany.ui.library.bean;

import cn.asany.ui.resources.UIResource;
import cn.asany.ui.resources.bean.Component;
import cn.asany.ui.resources.bean.Icon;
import lombok.*;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.MetaValue;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "UI_LIBRARY_ITEM")
public class LibraryItem extends BaseBusEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIBRARY_ID", foreignKey = @ForeignKey(name = "FK_LIBRARY_ITEM_LID"), nullable = false)
    private Library library;

    @Column(name = "RESOURCE_ID", length = 20)
    private Long resourceId;

    @Column(name = "RESOURCE_TYPE", length = 10)
    private String resourceType;

    @Any(
        metaColumn = @Column(name = "RESOURCE_TYPE", length = 10, insertable = false, updatable = false),
        fetch = FetchType.LAZY
    )
    @AnyMetaDef(
        idType = "long", metaType = "string",
        metaValues = {
            @MetaValue(targetEntity = Icon.class, value = Icon.RESOURCE_NAME),
            @MetaValue(targetEntity = Component.class, value = Component.RESOURCE_NAME)
        }
    )
    @JoinColumn(name = "RESOURCE_ID", insertable = false, updatable = false)
    private UIResource resource;


}
