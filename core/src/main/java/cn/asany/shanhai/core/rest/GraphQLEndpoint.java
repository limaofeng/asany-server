package cn.asany.shanhai.core.rest;

import cn.asany.shanhai.core.support.graphql.GraphQLServer;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.jackson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class GraphQLEndpoint {

    @Autowired
    private GraphQLServer graphQLServer;

    @PostMapping("/dynamic_graphql")
    public Map<String, Object> graphql(@RequestBody String body) {
        log.debug("请求体:" + body);
        ReadContext context = JsonPath.parse(body);
        body = JSON.getObjectMapper().convertValue(context.read("$.query"), String.class);
        return graphQLServer.execute(body);
    }

}
