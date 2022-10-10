package cn.asany.shanhai.core.support.tools;

import static org.objectweb.asm.Opcodes.*;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import graphql.kickstart.tools.GraphQLResolver;
import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.FantasyClassLoader;
import org.jfantasy.framework.util.asm.AsmUtil;
import org.jfantasy.framework.util.asm.Property;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.PathUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.objectweb.asm.*;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DynamicClassGenerator {

  @Autowired FieldTypeRegistry fieldTypeRegistry;

  public Class<GraphQLResolver<?>> makeQueryResolver(
      String namespace, String entityName, String nameOfList) {
    ClassWriter classWriter = new ClassWriter(0);
    FieldVisitor fieldVisitor;
    RecordComponentVisitor recordComponentVisitor;
    MethodVisitor methodVisitor;
    AnnotationVisitor annotationVisitor0;

    String _namespace = namespace.replace(".", "/");

    classWriter.visit(
        V1_8,
        ACC_PUBLIC | ACC_SUPER,
        _namespace + "/" + entityName + "GraphQLQueryResolver",
        null,
        "java/lang/Object",
        new String[] {"graphql/kickstart/tools/GraphQLQueryResolver"});

    {
      fieldVisitor =
          classWriter.visitField(
              ACC_PRIVATE | ACC_FINAL,
              "repository",
              "Lcn/asany/shanhai/core/support/dao/ModelRepository;",
              null,
              null);
      fieldVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              ACC_PUBLIC,
              "<init>",
              "(Lcn/asany/shanhai/core/support/dao/ModelRepository;)V",
              null,
              null);
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitLineNumber(14, label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLineNumber(15, label1);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitFieldInsn(
          PUTFIELD,
          _namespace + "/" + entityName + "GraphQLQueryResolver",
          "repository",
          "Lcn/asany/shanhai/core/support/dao/ModelRepository;");
      Label label2 = new Label();
      methodVisitor.visitLabel(label2);
      methodVisitor.visitLineNumber(16, label2);
      methodVisitor.visitInsn(RETURN);
      Label label3 = new Label();
      methodVisitor.visitLabel(label3);
      methodVisitor.visitLocalVariable(
          "this",
          "L" + _namespace + "/" + entityName + "GraphQLQueryResolver;",
          null,
          label0,
          label3,
          0);
      methodVisitor.visitLocalVariable(
          "repository",
          "Lcn/asany/shanhai/core/support/dao/ModelRepository;",
          null,
          label0,
          label3,
          1);
      methodVisitor.visitMaxs(2, 2);
      methodVisitor.visitEnd();
    }
    {
      String OrderByClass = namespace + "." + entityName + "OrderBy";

      methodVisitor =
          classWriter.visitMethod(
              ACC_PUBLIC,
              nameOfList,
              "(L"
                  + _namespace
                  + "/"
                  + entityName
                  + "WhereInput;IIL"
                  + OrderByClass.replace(".", "/")
                  + ";Lgraphql/schema/DataFetchingEnvironment;)Ljava/util/List;",
              "(L"
                  + _namespace
                  + "/"
                  + entityName
                  + "WhereInput;IIL"
                  + OrderByClass.replace(".", "/")
                  + ";Lgraphql/schema/DataFetchingEnvironment;)Ljava/util/List<L"
                  + _namespace
                  + "/"
                  + entityName
                  + ";>;",
              null);
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitLineNumber(24, label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD,
          _namespace + "/" + entityName + "GraphQLQueryResolver",
          "repository",
          "Lcn/asany/shanhai/core/support/dao/ModelRepository;");
      methodVisitor.visitMethodInsn(
          INVOKEVIRTUAL,
          "cn/asany/shanhai/core/support/dao/ModelRepository",
          "findAll",
          "()Ljava/util/List;",
          false);
      methodVisitor.visitInsn(ARETURN);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLocalVariable(
          "this",
          "L" + _namespace + "/" + entityName + "GraphQLQueryResolver;",
          null,
          label0,
          label1,
          0);
      methodVisitor.visitLocalVariable(
          "where", "L" + _namespace + "/" + entityName + "WhereInput;", null, label0, label1, 1);
      methodVisitor.visitLocalVariable("offset", "I", null, label0, label1, 2);
      methodVisitor.visitLocalVariable("limit", "I", null, label0, label1, 3);
      methodVisitor.visitLocalVariable(
          "orderBy", "L" + OrderByClass.replace(".", "/") + ";", null, label0, label1, 4);
      methodVisitor.visitLocalVariable(
          "environment", "Lgraphql/schema/DataFetchingEnvironment;", null, label0, label1, 5);
      methodVisitor.visitMaxs(1, 6);
      methodVisitor.visitEnd();
    }
    classWriter.visitEnd();

    return loadClass(
        namespace.concat(".").concat(entityName).concat("GraphQLQueryResolver"),
        classWriter.toByteArray());
  }

  protected static <T> Class<T> loadClass(String className, byte[] bytes) {
    try {
      FileUtil.writeFile(
          bytes, PathUtil.classes() + "/" + className.replace(".", File.separator) + ".class");
      return FantasyClassLoader.getClassLoader().loadClass(PathUtil.classes(), className);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      try {
        return FantasyClassLoader.getClassLoader().loadClass(bytes, className);
      } catch (ClassNotFoundException ex) {
        log.error(e.getMessage(), ex);
        throw new IgnoreException(e.getMessage(), ex);
      }
    }
  }

  public Class<?> makeEntityClass(String namespace, Model model) {
    String classname = namespace.concat(".").concat(model.getCode());
    return AsmUtil.makeClass(
        classname,
        BaseBusEntity.class.getName(),
        model.getFields().stream()
            .map(
                item -> {
                  String javaType =
                      fieldTypeRegistry.getType(item.getType()).getJavaType(item.getMetadata());
                  return Property.builder()
                      .name(item.getCode())
                      .type(ClassUtil.forName(javaType))
                      .build();
                })
            .toArray(Property[]::new));
  }

  public Class<?> makeBeanClass(String namespace, Model model) {
    String classname = namespace.concat(".").concat(model.getCode());
    return AsmUtil.makeClass(
        classname,
        model.getFields().stream()
            .map(
                item -> {
                  Property.PropertyBuilder builder = Property.builder().name(item.getCode());
                  if (item.getRealType() == model) {
                    if (item.getList()) {
                      builder
                          .descriptor("Ljava/util/List;")
                          .signature("Ljava/util/List<L" + classname.replace(".", "/") + ";>;");
                    } else {
                      builder.descriptor("L" + classname.replace(".", "/") + ";");
                    }
                  } else {
                    ModelFieldMetadata metadata =
                        item.getMetadata() == null
                            ? ModelFieldMetadata.builder().build()
                            : item.getMetadata();
                    metadata.setField(item);
                    String javaType =
                        fieldTypeRegistry.getType(item.getType()).getJavaType(metadata);
                    try {
                      Class<?> javaTypeClass =
                          FantasyClassLoader.getClassLoader().loadClass(javaType);
                      if (item.getList()) {
                        builder.type(List.class).genericTypes(new Class[] {javaTypeClass});
                      } else {
                        builder.type(javaTypeClass);
                      }
                    } catch (ClassNotFoundException e) {
                      throw new RuntimeException(e);
                    }
                  }
                  return builder.build();
                })
            .toArray(Property[]::new));
  }
}
