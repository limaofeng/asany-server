package cn.asany.shanhai.core.support.dao;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecuteOperationParameters;

public class CustomGraphQLTransactionInstrumentation extends SimpleInstrumentation {

  //    private final PlatformTransactionManager transactionManager;
  //
  //    public CustomGraphQLTransactionInstrumentation(PlatformTransactionManager
  // transactionManager) {
  //        super(transactionManager);
  //    }

  @Override
  public InstrumentationContext<ExecutionResult> beginExecuteOperation(
      InstrumentationExecuteOperationParameters parameters) {
    return super.beginExecuteOperation(parameters);
  }
}
