package cn.asany.shanhai.core.support;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;

import java.util.ArrayList;
import java.util.List;

/**
 * Model Save 上下文对象
 *
 * @author limaofeng
 */
public class ModelSaveContext {

    private List<Model> models = new ArrayList<>();
    private final List<ModelField> fields = new ArrayList<>();

    private final static ThreadLocal<ModelSaveContext> HOLDER = new ThreadLocal<>();

    public static ModelSaveContext newInstance() {
        ModelSaveContext context = HOLDER.get();
        if (context == null) {
            return setContext(new ModelSaveContext());
        }
        return HOLDER.get();
    }

    public static ModelSaveContext getContext() {
        ModelSaveContext context = HOLDER.get();
        if (context == null) {
            return null;
        }
        return HOLDER.get();
    }

    public static ModelSaveContext setContext(ModelSaveContext context) {
        clear();
        HOLDER.set(context);
        return context;
    }

    public static void clear() {
        ModelSaveContext prevContext = HOLDER.get();
        if (prevContext != null) {
            HOLDER.remove();
        }
    }

    public void addField(ModelField field) {
        this.fields.add(field);
    }

    public List<ModelField> getFields() {
        return this.fields;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public List<Model> getModels() {
        return models;
    }
}
