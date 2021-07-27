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
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(exclude = {"resource", "icon", "library"})
@NamedEntityGraph(
    name = "Graph.LibraryItem.FetchIcon",
    attributeNodes = {
        @NamedAttributeNode(value = "tags"),
        @NamedAttributeNode(value = "icon")
    }
)
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

    /**
     * 标签
     */
    @ElementCollection
    @CollectionTable(name = "UI_LIBRARY_ITEM_TAGS", foreignKey = @ForeignKey(name = "FK_LIBRARY_ITEM_TAG"), joinColumns = @JoinColumn(name = "ITEM_ID"))
    @Column(name = "TAG")
    private List<String> tags;

    /**
     * 资源ID
     */
    @Column(name = "RESOURCE_ID", length = 20)
    private Long resourceId;

    /**
     * 资源类型
     */
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

    /**
     * 用于级联加载
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "RESOURCE_ID", foreignKey = @ForeignKey(name = "NONE", value = ConstraintMode.NO_CONSTRAINT), insertable = false, updatable = false)
    private Icon icon;

    public <T extends UIResource> T getResource(Class<T> resourceClass) {
        return (T) this.resource;
    }
}
