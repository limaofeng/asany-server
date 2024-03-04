package cn.asany.myjob.factory.domain;

import cn.asany.storage.api.FileObject;
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
  private String boundIp;

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

  @Type(type = "file")
  @Column(name = "DOCUMENT1_STORE", columnDefinition = "JSON")
  private FileObject document1;

  @Type(type = "file")
  @Column(name = "DOCUMENT2_STORE", columnDefinition = "JSON")
  private FileObject document2;

  @Type(type = "file")
  @Column(name = "DOCUMENT3_STORE", columnDefinition = "JSON")
  private FileObject document3;

  @Type(type = "file")
  @Column(name = "DOCUMENT4_STORE", columnDefinition = "JSON")
  private FileObject document4;

  @Type(type = "file")
  @Column(name = "DOCUMENT5_STORE", columnDefinition = "JSON")
  private FileObject document5;

  @Type(type = "file")
  @Column(name = "DOCUMENT6_STORE", columnDefinition = "JSON")
  private FileObject document6;

  @Type(type = "file")
  @Column(name = "DOCUMENT7_STORE", columnDefinition = "JSON")
  private FileObject document7;

  @Type(type = "file")
  @Column(name = "DOCUMENT8_STORE", columnDefinition = "JSON")
  private FileObject document8;

  @Type(type = "file")
  @Column(name = "DOCUMENT9_STORE", columnDefinition = "JSON")
  private FileObject document9;

  @Type(type = "file")
  @Column(name = "DOCUMENT10_STORE", columnDefinition = "JSON")
  private FileObject document10;
}
