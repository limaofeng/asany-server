package cn.asany.shanhai.core.support.graphql.execution;

import graphql.ExecutionResult;
import graphql.execution.ExecutionContext;
import graphql.execution.ExecutionStrategyParameters;
import graphql.execution.NonNullableFieldWasNullException;
import java.util.concurrent.CompletableFuture;
import org.jfantasy.graphql.execution.AsyncMutationExecutionStrategy;
import org.springframework.transaction.annotation.Transactional;

public class OpenModelSessionAsyncMutationExecutionStrategy extends AsyncMutationExecutionStrategy {

  @Override
  @Transactional
  public CompletableFuture<ExecutionResult> execute(
      ExecutionContext executionContext, ExecutionStrategyParameters parameters)
      throws NonNullableFieldWasNullException {
    return super.execute(executionContext, parameters);
  }
}
