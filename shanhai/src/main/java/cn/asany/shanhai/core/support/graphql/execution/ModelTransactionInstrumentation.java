package cn.asany.shanhai.core.support.graphql.execution;

import cn.asany.shanhai.core.support.dao.ManualTransactionManager;
import cn.asany.shanhai.core.support.dao.TransactionStatus;
import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecuteOperationParameters;
import graphql.language.OperationDefinition;

public class ModelTransactionInstrumentation extends SimpleInstrumentation {

  private final ManualTransactionManager transactionManager;

  public ModelTransactionInstrumentation(ManualTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  @Override
  public InstrumentationContext<ExecutionResult> beginExecuteOperation(
      InstrumentationExecuteOperationParameters parameters) {
    OperationDefinition.Operation operation =
        parameters.getExecutionContext().getOperationDefinition().getOperation();
    if (!OperationDefinition.Operation.MUTATION.equals(operation)) {
      return super.beginExecuteOperation(parameters);
    }
    TransactionStatus transaction = transactionManager.getTransaction();
    return SimpleInstrumentationContext.whenCompleted(
        (t, e) -> {
          if (!t.getErrors().isEmpty() || e != null) {
            transaction.rollback();
          } else {
            transaction.commit();
          }
        });
  }
}
