package cn.asany.myjob.factory.domain;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectsConverter;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.jfantasy.framework.dao.BaseBusEntity;

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

  @Column(name = "BOUND_IP", unique = true)
  private String BoundIp;

  @Type(type = "file")
  @Column(name = "STATUS_IMAGE_STORE", columnDefinition = "JSON")
  private FileObject statusImage;

  @Type(type = "file")
  @Column(name = "INFO_STORE", columnDefinition = "JSON")
  private FileObject info;

  @Type(type = "file")
  @Column(name = "OPERATOR1_STORE", columnDefinition = "JSON")
  private FileObject operator1;

  @Type(type = "file")
  @Column(name = "OPERATOR2_STORE", columnDefinition = "JSON")
  private FileObject operator2;

  @Type(type = "file")
  @Column(name = "OPERATOR3_STORE", columnDefinition = "JSON")
  private FileObject operator3;

  @Convert(converter = FileObjectsConverter.class)
  @Column(name = "DOCUMENTS_STORE", columnDefinition = "JSON")
  private List<FileObject> documents;
}
