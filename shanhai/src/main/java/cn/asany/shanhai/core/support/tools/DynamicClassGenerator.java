package cn.asany.shanhai.core.support.tools;

import static org.objectweb.asm.Opcodes.*;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.FantasyClassLoader;
import org.jfantasy.framework.util.asm.AsmUtil;
import org.jfantasy.framework.util.asm.MethodInfo;
import org.jfantasy.framework.util.asm.Property;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.graphql.inputs.WhereInput;
import org.jfantasy.graphql.types.BaseConnection;
import org.objectweb.asm.*;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DynamicClassGenerator {

  @Autowired FieldTypeRegistry fieldTypeRegistry;

  public Class<GraphQLResolver<?>> makeQueryResolver(
      String namespace,
      String entityName,
      String getMethodName,
      String listMethodName,
      String connectionMethodName) {

    String _namespace = namespace.replace(".", "/");
    String className = _namespace + "/" + entityName + "GraphQLQueryResolver";
    String whereInputClassName = _namespace + "/" + entityName + "WhereInput";
    String orderByClassName = _namespace + "/" + entityName + "OrderBy";
    String entityClassName = _namespace + "/" + entityName;
    String connectionClassName = _namespace + "/" + entityName + "Connection";

    ClassWriter classWriter = new ClassWriter(0);
    FieldVisitor fieldVisitor;
    MethodVisitor methodVisitor;

    classWriter.visit(
        V1_8,
        ACC_PUBLIC | ACC_SUPER,
        className,
        null,
        "java/lang/Object",
        new String[] {"graphql/kickstart/tools/GraphQLQueryResolver"});

    {
      fieldVisitor =
          classWriter.visitField(
              ACC_PRIVATE | ACC_FINAL,
              "service",
              "Lcn/asany/shanhai/core/support/model/CustomModelService;",
              null,
              null);
      fieldVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              ACC_PUBLIC,
              "<init>",
              "(Lcn/asany/shanhai/core/support/model/CustomModelService;)V",
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
          className,
          "service",
          "Lcn/asany/shanhai/core/support/model/CustomModelService;");
      Label label2 = new Label();
      methodVisitor.visitLabel(label2);
      methodVisitor.visitLineNumber(16, label2);
      methodVisitor.visitInsn(RETURN);
      Label label3 = new Label();
      methodVisitor.visitLabel(label3);
      methodVisitor.visitLocalVariable("this", "L" + className + ";", null, label0, label3, 0);
      methodVisitor.visitLocalVariable(
          "service",
          "Lcn/asany/shanhai/core/support/model/CustomModelService;",
          null,
          label0,
          label3,
          1);
      methodVisitor.visitMaxs(2, 2);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              ACC_PUBLIC, getMethodName, "(Ljava/lang/Long;)L" + entityClassName + ";", null, null);
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitLineNumber(19, label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD,
          className,
          "service",
          "Lcn/asany/shanhai/core/support/model/CustomModelService;");
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitMethodInsn(
          INVOKEINTERFACE,
          "cn/asany/shanhai/core/support/model/CustomModelService",
          "get",
          "(Ljava/lang/Long;)Ljava/lang/Object;",
          true);
      methodVisitor.visitTypeInsn(CHECKCAST, "" + entityClassName + "");
      methodVisitor.visitInsn(ARETURN);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLocalVariable("this", "L" + className + ";", null, label0, label1, 0);
      methodVisitor.visitLocalVariable("id", "Ljava/lang/Long;", null, label0, label1, 1);
      methodVisitor.visitMaxs(2, 2);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              ACC_PUBLIC,
              listMethodName,
              "(L"
                  + whereInputClassName
                  + ";IIL"
                  + orderByClassName
                  + ";Lgraphql/schema/DataFetchingEnvironment;)Ljava/util/List;",
              "(L"
                  + whereInputClassName
                  + ";IIL"
                  + orderByClassName
                  + ";Lgraphql/schema/DataFetchingEnvironment;)Ljava/util/List<L"
                  + entityClassName
                  + ";>;",
              null);
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitLineNumber(28, label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD,
          className,
          "service",
          "Lcn/asany/shanhai/core/support/model/CustomModelService;");
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitMethodInsn(
          INVOKEVIRTUAL, whereInputClassName, "build", "()Ljava/util/List;", false);
      methodVisitor.visitVarInsn(ILOAD, 2);
      methodVisitor.visitVarInsn(ILOAD, 3);
      methodVisitor.visitVarInsn(ALOAD, 4);
      methodVisitor.visitMethodInsn(
          INVOKESTATIC,
          "cn/asany/shanhai/core/support/model/CustomModelService",
          "toOrderBy",
          "(Ljava/lang/Enum;)Lorg/jfantasy/framework/dao/OrderBy;",
          true);
      methodVisitor.visitMethodInsn(
          INVOKEINTERFACE,
          "cn/asany/shanhai/core/support/model/CustomModelService",
          "findAll",
          "(Ljava/util/List;IILorg/jfantasy/framework/dao/OrderBy;)Ljava/util/List;",
          true);
      methodVisitor.visitInsn(ARETURN);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLocalVariable("this", "L" + className + ";", null, label0, label1, 0);
      methodVisitor.visitLocalVariable(
          "where", "L" + whereInputClassName + ";", null, label0, label1, 1);
      methodVisitor.visitLocalVariable("offset", "I", null, label0, label1, 2);
      methodVisitor.visitLocalVariable("limit", "I", null, label0, label1, 3);
      methodVisitor.visitLocalVariable(
          "orderBy", "L" + orderByClassName + ";", null, label0, label1, 4);
      methodVisitor.visitLocalVariable(
          "environment", "Lgraphql/schema/DataFetchingEnvironment;", null, label0, label1, 5);
      methodVisitor.visitMaxs(5, 6);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              ACC_PUBLIC,
              connectionMethodName,
              "(L"
                  + whereInputClassName
                  + ";IIL"
                  + orderByClassName
                  + ";Lgraphql/schema/DataFetchingEnvironment;)L"
                  + connectionClassName
                  + ";",
              null,
              null);
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitLineNumber(37, label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD,
          className,
          "service",
          "Lcn/asany/shanhai/core/support/model/CustomModelService;");
      methodVisitor.visitVarInsn(ILOAD, 2);
      methodVisitor.visitVarInsn(ILOAD, 3);
      methodVisitor.visitVarInsn(ALOAD, 4);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLineNumber(39, label1);
      methodVisitor.visitMethodInsn(
          INVOKESTATIC,
          "cn/asany/shanhai/core/support/model/CustomModelService",
          "toOrderBy",
          "(Ljava/lang/Enum;)Lorg/jfantasy/framework/dao/OrderBy;",
          true);
      methodVisitor.visitMethodInsn(
          INVOKESTATIC,
          "org/jfantasy/framework/dao/Page",
          "of",
          "(IILorg/jfantasy/framework/dao/OrderBy;)Lorg/jfantasy/framework/dao/Page;",
          true);
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitMethodInsn(
          INVOKEVIRTUAL, "" + whereInputClassName + "", "build", "()Ljava/util/List;", false);
      Label label2 = new Label();
      methodVisitor.visitLabel(label2);
      methodVisitor.visitLineNumber(38, label2);
      methodVisitor.visitMethodInsn(
          INVOKEINTERFACE,
          "cn/asany/shanhai/core/support/model/CustomModelService",
          "findPage",
          "(Lorg/jfantasy/framework/dao/Page;Ljava/util/List;)Lorg/jfantasy/framework/dao/Page;",
          true);
      methodVisitor.visitLdcInsn(Type.getType("L" + connectionClassName + ";"));
      Label label3 = new Label();
      methodVisitor.visitLabel(label3);
      methodVisitor.visitLineNumber(37, label3);
      methodVisitor.visitMethodInsn(
          INVOKESTATIC,
          "org/jfantasy/graphql/util/Kit",
          "connection",
          "(Lorg/jfantasy/framework/dao/Page;Ljava/lang/Class;)Lorg/jfantasy/graphql/Connection;",
          false);
      methodVisitor.visitTypeInsn(CHECKCAST, "" + entityClassName + "Connection");
      methodVisitor.visitInsn(ARETURN);
      Label label4 = new Label();
      methodVisitor.visitLabel(label4);
      methodVisitor.visitLocalVariable("this", "L" + className + ";", null, label0, label4, 0);
      methodVisitor.visitLocalVariable(
          "where", "L" + whereInputClassName + ";", null, label0, label4, 1);
      methodVisitor.visitLocalVariable("page", "I", null, label0, label4, 2);
      methodVisitor.visitLocalVariable("pageSize", "I", null, label0, label4, 3);
      methodVisitor.visitLocalVariable(
          "orderBy", "L" + orderByClassName + ";", null, label0, label4, 4);
      methodVisitor.visitLocalVariable(
          "environment", "Lgraphql/schema/DataFetchingEnvironment;", null, label0, label4, 5);
      methodVisitor.visitMaxs(4, 6);
      methodVisitor.visitEnd();
    }
    classWriter.visitEnd();

    return AsmUtil.loadClass(
        namespace.concat(".").concat(entityName).concat("GraphQLQueryResolver"),
        classWriter.toByteArray());
  }

  public Class<GraphQLResolver<?>> makeMutationResolver(
      String namespace,
      String entityName,
      String createMethodName,
      String updateMethodName,
      String deleteMethodName,
      String deleteManyMethodName) {

    String _namespace = namespace.replace(".", "/");
    String className = _namespace + "/" + entityName + "GraphQLMutationResolver";
    String createInputType = _namespace + "/" + entityName + "CreateInput";
    String updateInputType = _namespace + "/" + entityName + "UpdateInput";
    String whereInputType = _namespace + "/" + entityName + "WhereInput";
    String orderByClassName = _namespace + "/" + entityName + "OrderBy";
    String entityClassName = _namespace + "/" + entityName;

    ClassWriter classWriter = new ClassWriter(0);
    FieldVisitor fieldVisitor;
    RecordComponentVisitor recordComponentVisitor;
    MethodVisitor methodVisitor;
    AnnotationVisitor annotationVisitor0;

    classWriter.visit(
        V1_8,
        ACC_PUBLIC | ACC_SUPER,
        className,
        null,
        "java/lang/Object",
        new String[] {"graphql/kickstart/tools/GraphQLMutationResolver"});

    {
      fieldVisitor =
          classWriter.visitField(
              ACC_PRIVATE | ACC_FINAL,
              "service",
              "Lcn/asany/shanhai/core/support/model/CustomModelService;",
              null,
              null);
      fieldVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              ACC_PUBLIC,
              "<init>",
              "(Lcn/asany/shanhai/core/support/model/CustomModelService;)V",
              null,
              null);
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitLineNumber(12, label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLineNumber(13, label1);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitFieldInsn(
          PUTFIELD,
          className,
          "service",
          "Lcn/asany/shanhai/core/support/model/CustomModelService;");
      Label label2 = new Label();
      methodVisitor.visitLabel(label2);
      methodVisitor.visitLineNumber(14, label2);
      methodVisitor.visitInsn(RETURN);
      Label label3 = new Label();
      methodVisitor.visitLabel(label3);
      methodVisitor.visitLocalVariable("this", "L" + className + ";", null, label0, label3, 0);
      methodVisitor.visitLocalVariable(
          "service",
          "Lcn/asany/shanhai/core/support/model/CustomModelService;",
          null,
          label0,
          label3,
          1);
      methodVisitor.visitMaxs(2, 2);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              0,
              createMethodName,
              "(L" + createInputType + ";)L" + entityClassName + ";",
              null,
              null);
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitLineNumber(17, label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD,
          className,
          "service",
          "Lcn/asany/shanhai/core/support/model/CustomModelService;");
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitTypeInsn(NEW, entityClassName);
      methodVisitor.visitInsn(DUP);
      methodVisitor.visitMethodInsn(INVOKESPECIAL, entityClassName, "<init>", "()V", false);
      methodVisitor.visitInsn(ICONST_0);
      methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/String");
      methodVisitor.visitMethodInsn(
          INVOKESTATIC,
          "org/jfantasy/framework/util/common/ObjectUtil",
          "copy",
          "(Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/String;)Ljava/lang/Object;",
          false);
      methodVisitor.visitMethodInsn(
          INVOKEINTERFACE,
          "cn/asany/shanhai/core/support/model/CustomModelService",
          "save",
          "(Ljava/lang/Object;)Ljava/lang/Object;",
          true);
      methodVisitor.visitTypeInsn(CHECKCAST, entityClassName);
      methodVisitor.visitInsn(ARETURN);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLocalVariable("this", "L" + className + ";", null, label0, label1, 0);
      methodVisitor.visitLocalVariable(
          "input", "L" + createInputType + ";", null, label0, label1, 1);
      methodVisitor.visitMaxs(4, 2);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              0,
              updateMethodName,
              "(Ljava/lang/Long;L" + updateInputType + ";Z)L" + entityClassName + ";",
              null,
              null);
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitLineNumber(21, label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD,
          className,
          "service",
          "Lcn/asany/shanhai/core/support/model/CustomModelService;");
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitVarInsn(ALOAD, 2);
      methodVisitor.visitTypeInsn(NEW, entityClassName);
      methodVisitor.visitInsn(DUP);
      methodVisitor.visitMethodInsn(INVOKESPECIAL, entityClassName, "<init>", "()V", false);
      methodVisitor.visitInsn(ICONST_0);
      methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/String");
      methodVisitor.visitMethodInsn(
          INVOKESTATIC,
          "org/jfantasy/framework/util/common/ObjectUtil",
          "copy",
          "(Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/String;)Ljava/lang/Object;",
          false);
      methodVisitor.visitVarInsn(ILOAD, 3);
      methodVisitor.visitMethodInsn(
          INVOKEINTERFACE,
          "cn/asany/shanhai/core/support/model/CustomModelService",
          "update",
          "(Ljava/lang/Long;Ljava/lang/Object;Z)Ljava/lang/Object;",
          true);
      methodVisitor.visitTypeInsn(CHECKCAST, entityClassName);
      methodVisitor.visitInsn(ARETURN);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLocalVariable("this", "L" + className + ";", null, label0, label1, 0);
      methodVisitor.visitLocalVariable("id", "Ljava/lang/Long;", null, label0, label1, 1);
      methodVisitor.visitLocalVariable(
          "input", "L" + updateInputType + ";", null, label0, label1, 2);
      methodVisitor.visitLocalVariable("merge", "Z", null, label0, label1, 3);
      methodVisitor.visitMaxs(5, 4);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              0, deleteMethodName, "(Ljava/lang/Long;)L" + entityClassName + ";", null, null);
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitLineNumber(25, label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD,
          className,
          "service",
          "Lcn/asany/shanhai/core/support/model/CustomModelService;");
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitMethodInsn(
          INVOKEINTERFACE,
          "cn/asany/shanhai/core/support/model/CustomModelService",
          "delete",
          "(Ljava/lang/Long;)Ljava/lang/Object;",
          true);
      methodVisitor.visitTypeInsn(CHECKCAST, entityClassName);
      methodVisitor.visitInsn(ARETURN);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLocalVariable("this", "L" + className + ";", null, label0, label1, 0);
      methodVisitor.visitLocalVariable("id", "Ljava/lang/Long;", null, label0, label1, 1);
      methodVisitor.visitMaxs(2, 2);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              0,
              deleteManyMethodName,
              "(L" + entityClassName + "WhereInput;)Ljava/util/List;",
              "(L" + entityClassName + "WhereInput;)Ljava/util/List<L" + entityClassName + ";>;",
              null);
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitLineNumber(29, label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD,
          className,
          "service",
          "Lcn/asany/shanhai/core/support/model/CustomModelService;");
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitMethodInsn(
          INVOKEVIRTUAL, whereInputType, "build", "()Ljava/util/List;", false);
      methodVisitor.visitMethodInsn(
          INVOKEINTERFACE,
          "cn/asany/shanhai/core/support/model/CustomModelService",
          "deleteMany",
          "(Ljava/util/List;)Ljava/util/List;",
          true);
      methodVisitor.visitInsn(ARETURN);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLocalVariable("this", "L" + className + ";", null, label0, label1, 0);
      methodVisitor.visitLocalVariable(
          "where", "L" + whereInputType + ";", null, label0, label1, 1);
      methodVisitor.visitMaxs(2, 2);
      methodVisitor.visitEnd();
    }
    classWriter.visitEnd();

    return AsmUtil.loadClass(
        namespace.concat(".").concat(entityName).concat("GraphQLMutationResolver"),
        classWriter.toByteArray());
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
    return AsmUtil.makeClass(classname, toProperties(model, classname, model.getFields()));
  }

  public Class<?> makeWhereClass(String namespace, Class<?> entityClass, Model model) {
    String classname = namespace.concat(".").concat(model.getCode());
    return AsmUtil.makeClass(
        classname,
        "Lorg/jfantasy/graphql/inputs/WhereInput<L"
            + classname.replace(".", "/")
            + ";L"
            + entityClass.getName().replace(".", "/")
            + ";>;",
        WhereInput.class.getName(),
        new Class[] {},
        new Property[] {},
        new MethodInfo[0]);
  }

  public Class<?> makeEdgeClass(String namespace, Class<?> entityClass, Model model) {
    String classname = namespace + "." + model.getCode();
    Property[] properties = toProperties(model, classname, model.getFields());
    return AsmUtil.makeClass(
        classname,
        "Ljava/lang/Object;Lorg/jfantasy/graphql/Edge<L"
            + entityClass.getName().replace(".", "/")
            + ";>;",
        Object.class.getName(),
        new Class[] {org.jfantasy.graphql.Edge.class},
        properties,
        new MethodInfo[] {
          MethodInfo.builder()
              .symbolTable(ACC_PUBLIC | ACC_BRIDGE | ACC_SYNTHETIC)
              .methodName("setNode")
              .methodDescriptor("(Ljava/lang/Object;)V")
              .methodCreator(
                  mv -> {
                    mv.visitCode();
                    Label label0 = new Label();
                    mv.visitLabel(label0);
                    mv.visitLineNumber(5, label0);
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitTypeInsn(CHECKCAST, entityClass.getName().replace(".", "/"));
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        classname.replace(".", "/"),
                        "setNode",
                        "(L" + entityClass.getName().replace(".", "/") + ";)V",
                        false);
                    mv.visitInsn(RETURN);
                    Label label1 = new Label();
                    mv.visitLabel(label1);
                    mv.visitLocalVariable(
                        "this", "L" + classname.replace(".", "/") + ";", null, label0, label1, 0);
                    mv.visitMaxs(2, 2);
                  })
              .build()
        });
  }

  public Class<?> makeConnectionClass(String namespace, Class<?> entityClass, Model model) {
    String classname = namespace + "." + model.getCode();
    String edgeClasName = entityClass.getName() + "Edge";
    return AsmUtil.makeClass(
        classname,
        "Lorg/jfantasy/graphql/types/BaseConnection<L"
            + edgeClasName.replace(".", "/")
            + ";L"
            + entityClass.getName().replace(".", "/")
            + ";>;",
        BaseConnection.class.getName(),
        new Class[] {},
        new Property[] {
          Property.builder()
              .name("edges")
              .descriptor("Ljava/util/List;")
              .signature("Ljava/util/List<L" + edgeClasName.replace(".", "/") + ";>;")
              .build()
        },
        new MethodInfo[0]);
  }

  public Class<?> makeEnumClass(String namespace, Model type) {
    String classname = namespace.concat(".").concat(type.getCode());
    return AsmUtil.makeEnum(
        classname, type.getFields().stream().map(ModelField::getCode).toArray(String[]::new));
  }

  private Property[] toProperties(Model model, String classname, Set<ModelField> fields) {
    return fields.stream()
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
                String javaType = fieldTypeRegistry.getType(item.getType()).getJavaType(metadata);
                try {
                  Class<?> javaTypeClass = FantasyClassLoader.getClassLoader().loadClass(javaType);
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
        .toArray(Property[]::new);
  }
}
