package com.fx.mvvm.internal.viewloader;


import com.fx.mvvm.Context;
import com.fx.mvvm.Scope;
import com.fx.mvvm.internal.ContextImpl;

import java.util.Collection;

class ViewLoaderScopeUtils {

    static ContextImpl prepareContext(Context parentContext, Collection<Scope> providedScopes) {
        ContextImpl context = null;

        if (parentContext == null || !(parentContext instanceof ContextImpl)) {
            context = new ContextImpl();
        } else {
            context = (ContextImpl) parentContext;
        }

        if (providedScopes != null) {

            for (Scope scope : providedScopes) {
                context.addScopeToContext(scope);
            }
        }

        return context;
    }

}
