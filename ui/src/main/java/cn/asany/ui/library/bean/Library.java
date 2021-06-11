package cn.asany.ui.library.bean;

import cn.asany.ui.library.bean.enums.LibraryType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "UI_LIBRARY")
public class Library extends BaseBusEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 20, nullable = false)
    private LibraryType type;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 60)
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

}
