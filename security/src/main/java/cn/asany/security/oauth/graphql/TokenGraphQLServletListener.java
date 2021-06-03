package cn.asany.security.oauth.graphql;

import graphql.kickstart.servlet.core.GraphQLServletListener;
import lombok.SneakyThrows;
import org.jfantasy.framework.error.ErrorResponse;
import org.jfantasy.framework.error.ErrorUtils;
import org.jfantasy.framework.jackson.JSON;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class TokenGraphQLServletListener implements GraphQLServletListener {
    @Override
    public RequestCallback onRequest(HttpServletRequest request, HttpServletResponse response) {
        return new RequestCallback() {

            @Override
            @SneakyThrows
            public void onError(HttpServletRequest request, HttpServletResponse response, Throwable throwable) {
                ErrorResponse errorResponse = new ErrorResponse();
                if (Exception.class.isAssignableFrom(throwable.getClass())) {
                    ErrorUtils.fill(errorResponse, (Exception) throwable);
                }
                response.setHeader("Content-Type", "application/json;charset=utf-8");
                PrintWriter printWriter = response.getWriter();
                printWriter.print(JSON.serialize(errorResponse));
                response.setStatus(401);
                response.flushBuffer();
            }
        };
    }
}
