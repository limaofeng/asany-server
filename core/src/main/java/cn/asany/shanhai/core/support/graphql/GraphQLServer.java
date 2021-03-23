package cn.asany.shanhai.core.support.graphql;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.bean.enums.ModelEndpointType;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.dao.ModelRepository;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GraphQLServer implements InitializingBean {

    private Template template;

    private String scheme;
    private Map<Long, Model> modelMap = new ConcurrentHashMap<>();
    private Map<Long, List<Long>> modelEndpointMap = new ConcurrentHashMap<>();
    private Map<Long, ModelEndpoint> queries = new ConcurrentHashMap();
    private Map<Long, ModelEndpoint> mutations = new ConcurrentHashMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        template = HandlebarsTemplateUtils.template("/scheme");
    }

    @SneakyThrows
    @Transactional
    public String buildScheme() {
        List<Model> models = new ArrayList<>(modelMap.values());
        List<ModelEndpoint> queries = new ArrayList<>(this.queries.values());
        List<ModelEndpoint> mutations = new ArrayList<>(this.mutations.values());
        return scheme = template.apply(new TemplateRootData(models, queries, mutations));
    }

    public void buildResolver(Model model, ModelRepository repository) {
        List<Long> endpointIds = new ArrayList<>();
        modelMap.put(model.getId(), model);
        modelEndpointMap.put(model.getId(), endpointIds);
        for (ModelEndpoint endpoint : model.getEndpoints()) {
            endpointIds.add(endpoint.getId());
            if (endpoint.getType() == ModelEndpointType.QUERY) {
                queries.put(endpoint.getId(), endpoint);
            } else {
                mutations.put(endpoint.getId(), endpoint);
            }
            new ModelDataFetcher(endpoint, repository);
        }
    }

    static class TemplateRootData {
        private final List<ModelEndpoint> queries;
        private final List<ModelEndpoint> mutations;
        private List<Model> models;

        public TemplateRootData(List<Model> models, List<ModelEndpoint> queries, List<ModelEndpoint> mutations) {
            this.models = models;
            this.queries = queries;
            this.mutations = mutations;
        }

        public List getMutations() {
            return queries.stream().map(item -> new TemplateDataOfEndpoint(item)).collect(Collectors.toList());
        }

        public List getQueries() {
            return queries.stream().map(item -> new TemplateDataOfEndpoint(item)).collect(Collectors.toList());
        }

        public List<TemplateDataOfModel> getTypes() {
            return models.stream().map(item -> new TemplateDataOfModel(item)).collect(Collectors.toList());
        }
    }

}
