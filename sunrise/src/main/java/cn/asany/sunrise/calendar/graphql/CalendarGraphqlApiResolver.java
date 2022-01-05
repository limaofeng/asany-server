package cn.asany.sunrise.calendar.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

/**
 * 日历 接口
 *
 * @author limaofeng
 */
@Component
public class CalendarGraphqlApiResolver implements GraphQLQueryResolver, GraphQLMutationResolver {}
