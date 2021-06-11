package cn.asany.ui.resources.bean.toy;

import lombok.Data;

import java.util.Map;

@Data
public class ComponentData {
    private String key;
    private Map<String,Object> props;
}
