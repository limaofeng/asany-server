package org.jfantasy.graphql.context;

import graphql.kickstart.execution.context.DefaultGraphQLContextBuilder;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.jfantasy.framework.security.AuthenticationException;
import org.jfantasy.framework.security.AuthenticationManager;
import org.jfantasy.framework.security.SecurityContextHolder;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.authentication.AuthenticationDetailsSource;
import org.jfantasy.framework.security.authentication.AuthenticationManagerResolver;
import org.jfantasy.framework.security.oauth2.server.BearerTokenAuthenticationToken;
import org.jfantasy.framework.security.oauth2.server.web.BearerTokenResolver;
import org.jfantasy.framework.security.oauth2.server.web.DefaultBearerTokenResolver;
import org.jfantasy.framework.security.web.WebAuthenticationDetailsSource;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * 从请求头中获取 Token 并转化未用户
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-04-14 14:13
 */
@Slf4j
@Component
public class SecurityGraphQLContextBuilder extends DefaultGraphQLContextBuilder implements GraphQLServletContextBuilder {

    private BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    @Autowired
    private AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

    @Override
    public GraphQLContext build(HttpServletRequest req, HttpServletResponse response) {
        SecurityContextHolder.clear();

        AuthorizationGraphQLServletContext context = new AuthorizationGraphQLServletContext(req, response);
        context.setDataLoaderRegistry(buildDataLoaderRegistry());

        GraphQLContextHolder.setContext(context);

        String token = bearerTokenResolver.resolve(req);

        if (token == null) {
            log.trace("Did not process request since did not find bearer token");
            return context;
        }

        String authorization = req.getHeader("Authorization");

        if (StringUtil.isBlank(authorization) || !authorization.startsWith("bearer ")) {
            return context;
        }

        BearerTokenAuthenticationToken authenticationRequest = new BearerTokenAuthenticationToken(token);
        authenticationRequest.setDetails(this.authenticationDetailsSource.buildDetails(req));

        try {
            AuthenticationManager authenticationManager = this.authenticationManagerResolver.resolve(req);
            Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);

            if (log.isDebugEnabled()) {
                log.debug(LogMessage.format("Set SecurityContextHolder to %s", authenticationResult).toString());
            }
        } catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();
            log.trace("Failed to process authentication request", failed);
            throw failed;
        }
        return context;
    }

    @Override
    public GraphQLContext build(Session session, HandshakeRequest request) {
        AuthorizationGraphQLServletContext context = new AuthorizationGraphQLServletContext(session, request);
        context.setDataLoaderRegistry(buildDataLoaderRegistry());
        return context;
    }

    private DataLoaderRegistry buildDataLoaderRegistry() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        dataLoaderRegistry.register("customerDataLoader",
            new DataLoader<Integer, String>(customerIds ->
                CompletableFuture.supplyAsync(() -> {
                    System.out.println(customerIds);
                    return new ArrayList<>();
                })));
        return dataLoaderRegistry;
    }

}