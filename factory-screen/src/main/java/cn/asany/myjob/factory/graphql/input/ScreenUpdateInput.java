package cn.asany.myjob.factory.graphql.input;

import cn.asany.storage.api.FileObject;
import lombok.Data;

@Data
public class ScreenUpdateInput {
  private String description;

  private String status;

  private FileObject statusImage;

  private FileObject info;

  private FileObject operator1;

  private FileObject operator2;

  private FileObject operator3;

  private FileObject document1;
  private FileObject document2;
  private FileObject document3;
  private FileObject document4;
  private FileObject document5;
  private FileObject document6;
  private FileObject document7;
  private FileObject document8;
  private FileObject document9;
  private FileObject document10;
}
