package cn.asany.weixin.graphql.execution;

import cn.asany.weixin.framework.factory.WeixinSessionUtils;
import graphql.ExecutionResult;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.ExecutionContext;
import graphql.execution.ExecutionStrategyParameters;
import graphql.execution.NonNullableFieldWasNullException;
import java.util.concurrent.CompletableFuture;

/**
 * 请求结束后移除上下问中的 WeixinSession
 *
 * @author limaofeng
 */
public class WeixinSessionAsyncExecutionStrategy extends AsyncExecutionStrategy {

  @Override
  public CompletableFuture<ExecutionResult> execute(
      ExecutionContext executionContext, ExecutionStrategyParameters parameters)
      throws NonNullableFieldWasNullException {

    CompletableFuture<ExecutionResult> overallResult = super.execute(executionContext, parameters);

    return overallResult.handleAsync(
        (ExecutionResult results, Throwable exception) -> {
          WeixinSessionUtils.closeSession();
          return results;
        });
  }
}
