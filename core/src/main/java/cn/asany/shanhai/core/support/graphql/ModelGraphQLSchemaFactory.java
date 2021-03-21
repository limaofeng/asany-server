package cn.asany.shanhai.core.support.graphql;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.bean.enums.ModelEndpointType;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.utils.TemplateDataOfEndpoint;
import cn.asany.shanhai.core.utils.TemplateDataOfModel;
import com.github.jknack.handlebars.Template;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.HandlebarsTemplateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelGraphQLSchemaFactory implements InitializingBean {

    @Autowired
    public ModelService modelService;

    private Template template;

    @Override
    public void afterPropertiesSet() throws Exception {
        template = HandlebarsTemplateUtils.template("/scheme");
    }


    @SneakyThrows
    @Transactional
    public String buildScheme() {
        List<Model> models = modelService.findAll();
        return template.apply(new TemplateRootData(models));
    }

    static class TemplateRootData {
        private List<Model> models;

        public TemplateRootData(List<Model> models) {
            this.models = models;
        }

        public List getQueries() {
            List<ModelEndpoint> endpoints = new ArrayList<>();
            for (Model model : this.models) {
                endpoints.addAll(model.getEndpoints().stream().filter(item -> item.getType() == ModelEndpointType.QUERY).collect(Collectors.toList()));
            }
            return endpoints.stream().map(item -> new TemplateDataOfEndpoint(item)).collect(Collectors.toList());
        }

        public List<TemplateDataOfModel> getModels() {
            return models.stream().map(item -> new TemplateDataOfModel(item)).collect(Collectors.toList());
        }
    }

}
