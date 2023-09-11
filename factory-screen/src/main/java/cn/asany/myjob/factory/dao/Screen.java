package cn.asany.myjob.factory.dao;

import cn.asany.storage.api.converter.FileObjectsConverter;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.jfantasy.framework.dao.BaseBusEntity;
import cn.asany.storage.api.FileObject;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MY_FACTORY_SCREEN")
public class Screen extends BaseBusEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS", length = 10)
    private String status;

    @Type(type = "file")
    @Column(name = "INFO_STORE", columnDefinition = "JSON")
    private FileObject statusImage;

    @Type(type = "file")
    @Column(name = "INFO_STORE", columnDefinition = "JSON")
    private FileObject info;

    @Convert(converter = FileObjectsConverter.class)
    @Column(name = "OPERATORS_STORE", columnDefinition = "JSON")
    private List<FileObject> operators;

    @Convert(converter = FileObjectsConverter.class)
    @Column(name = "DOCUMENTS_STORE", columnDefinition = "JSON")
    private List<FileObject> documents;

}
