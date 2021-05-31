package cn.asany.security.core.graphql.inputs;

import lombok.Data;

@Data
public class ScopeContext {

    private Boolean analysis;

    private static ThreadLocal threadLocal = ThreadLocal.withInitial(() -> new ScopeContext());

    public static void create(ScopeContext scopeContext) {
        threadLocal.set(scopeContext);
    }

    public static ScopeContext get() {
       return (ScopeContext) threadLocal.get();
    }
}
