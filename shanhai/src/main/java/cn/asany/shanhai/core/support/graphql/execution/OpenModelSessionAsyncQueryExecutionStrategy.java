package cn.asany.shanhai.core.support.graphql.execution;

import cn.asany.shanhai.core.support.dao.ManualTransactionManager;
import graphql.ExecutionResult;
import graphql.execution.ExecutionContext;
import graphql.execution.ExecutionStrategyParameters;
import graphql.execution.NonNullableFieldWasNullException;
import java.util.concurrent.CompletableFuture;
import org.jfantasy.graphql.execution.AsyncQueryExecutionStrategy;
import org.springframework.transaction.annotation.Transactional;

public class OpenModelSessionAsyncQueryExecutionStrategy extends AsyncQueryExecutionStrategy {

  private final ManualTransactionManager transactionManager;

  public OpenModelSessionAsyncQueryExecutionStrategy(ManualTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  @Override
  @Transactional(readOnly = true)
  public CompletableFuture<ExecutionResult> execute(
      ExecutionContext executionContext, ExecutionStrategyParameters parameters)
      throws NonNullableFieldWasNullException {
    boolean built = transactionManager.bindSession();
    return super.execute(executionContext, parameters)
        .whenComplete(
            (t, e) -> {
              if (built) {
                transactionManager.unbindSession();
              }
            });
  }
}
