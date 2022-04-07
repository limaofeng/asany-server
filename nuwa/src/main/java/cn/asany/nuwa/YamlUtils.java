package cn.asany.nuwa;

import cn.asany.base.IModuleProperties;
import cn.asany.nuwa.app.service.dto.NativeApplication;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.*;

/**
 * Yaml 解析工具类
 *
 * @author limaofeng
 */
@Slf4j
public abstract class YamlUtils {

  private static Yaml yaml;
  private static final Map<String, Class<? extends IModuleProperties>> moduleClasses =
      new HashMap<>();

  static {
    Constructor constructor = new Constructor(NativeApplication.class);
    TypeDescription moduleTypeDescription = new ModuleTypeDescription();
    constructor.addTypeDescription(moduleTypeDescription);
    YamlUtils.yaml = new Yaml(constructor);
  }

  public static NativeApplication load(InputStream inputStream) {
    return yaml.load(inputStream);
  }

  public static void addModuleClass(String type, Class<? extends IModuleProperties> clazz) {
    moduleClasses.put(type, clazz);
  }

  public static class ModuleTypeDescription extends TypeDescription {

    public ModuleTypeDescription() {
      super(IModuleProperties.class);
    }

    @Override
    public Object newInstance(Node node) {
      List<NodeTuple> nodeTuples = ((MappingNode) node).getValue();
      NodeTuple nodeTuple = ObjectUtil.find(nodeTuples, "keyNode.value", "type");
      if (nodeTuple == null) {
        return super.newInstance(node);
      }
      String type = ((ScalarNode) nodeTuple.getValueNode()).getValue();
      try {
        Class beanType = moduleClasses.get(type);
        node.setType(beanType);
        return beanType.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        log.error(e.getMessage());
        return super.newInstance(node);
      }
    }
  }
}
