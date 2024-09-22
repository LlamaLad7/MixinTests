package com.llamalad7.mixintests.harness;

import com.google.auto.service.AutoService;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

@AutoService(Extension.class)
public class IsolatingExtension implements InvocationInterceptor, TestInstancePreDestroyCallback {
    private final Map<Object, Sandbox> sandboxes = Collections.synchronizedMap(new IdentityHashMap<>());

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Exception {
        intercept(invocation, invocationContext);
    }

    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Exception {
        intercept(invocation, invocationContext);
    }

    private void intercept(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext) throws Exception {
        invocation.skip();
        invokeTestMethod(invocationContext);
    }

    private void invokeTestMethod(ReflectiveInvocationContext<Method> invocationContext) throws Exception {
        invokeSandboxedMethod(
                invocationContext.getTarget().orElseThrow(),
                invocationContext.getExecutable().getName(),
                invocationContext.getExecutable().getParameterTypes(),
                invocationContext.getArguments().toArray()
        );
    }

    private void invokeSandboxedMethod(Object testInstance, String methodName, Class<?>[] parameterTypes, Object[] args) throws Exception {
        Sandbox sandbox = sandboxes.computeIfAbsent(testInstance, k -> {
            String testClassName = k.getClass().getName();
            return new Sandbox(testClassName);
        });
        Object sandboxedInstance = sandbox.newInstance(testInstance.getClass().getName());
        Class<?> testClass = sandboxedInstance.getClass();

        final Optional<Method> method = ReflectionUtils.findMethod(testClass, methodName, parameterTypes);
        sandbox.withContextClassLoader(
                () -> ReflectionUtils.invokeMethod(
                        method.orElseThrow(() -> new IllegalStateException("No test method named " + methodName)),
                        sandboxedInstance,
                        args
                )
        );
    }

    @Override
    public void preDestroyTestInstance(ExtensionContext context) throws Exception {
        Sandbox sandbox = sandboxes.remove(context.getRequiredTestInstance());
        sandbox.close();
    }
}
