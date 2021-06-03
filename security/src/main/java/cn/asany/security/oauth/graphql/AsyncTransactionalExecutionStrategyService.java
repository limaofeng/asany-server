package cn.asany.security.oauth.graphql;

import graphql.ErrorClassification;
import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.GraphQLError;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.ExecutionContext;
import graphql.execution.ExecutionStrategyParameters;
import graphql.execution.NonNullableFieldWasNullException;
import graphql.language.SourceLocation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Service
public class AsyncTransactionalExecutionStrategyService extends AsyncExecutionStrategy {

    @Override
    public CompletableFuture<ExecutionResult> execute(ExecutionContext executionContext, ExecutionStrategyParameters parameters) throws NonNullableFieldWasNullException {
        CompletableFuture<ExecutionResult> overallResult = super.execute(executionContext, parameters);

        return overallResult.handleAsync((ExecutionResult results, Throwable exception) -> {
             return new ExecutionResultImpl(new GraphQLError() {
                @Override
                public String getMessage() {
                    return "1111";
                }

                @Override
                public List<SourceLocation> getLocations() {
                    return null;
                }

                @Override
                public ErrorClassification getErrorType() {
                    return null;
                }
            });
        });
    }

    @Override
    protected BiConsumer<List<ExecutionResult>, Throwable> handleResults(ExecutionContext executionContext, List<String> fieldNames, CompletableFuture<ExecutionResult> overallResult) {
        return super.handleResults(executionContext, fieldNames, overallResult);
    }
}
