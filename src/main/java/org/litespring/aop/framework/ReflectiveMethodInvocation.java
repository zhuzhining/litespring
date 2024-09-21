package org.litespring.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectiveMethodInvocation implements MethodInvocation {

    private Object targetObj;

    private Method targetMethod;

    private Object[] args;

    private List<MethodInterceptor> interceptors;

    private int currentInterceptorsIndex = -1;

    public ReflectiveMethodInvocation(Object targetObj, Method targetMethod, Object[] args, List<MethodInterceptor> interceptors) {
        this.targetObj = targetObj;
        this.targetMethod = targetMethod;
        this.args = args;
        this.interceptors = interceptors;
    }

    @Override
    public Method getMethod() {
        return targetMethod;
    }

    @Override
    public Object[] getArguments() {
        return args;
    }

    @Override
    public Object proceed() throws Throwable {
        if (this.currentInterceptorsIndex == this.interceptors.size() - 1) {
            return invokeJoinPoint();
        }
        this.currentInterceptorsIndex++;
        MethodInterceptor interceptor = this.interceptors.get(this.currentInterceptorsIndex);
        Object o = interceptor.invoke(this);
        return o;
    }

    private Object invokeJoinPoint() throws Throwable {
        return targetMethod.invoke(targetObj, args);
    }

    @Override
    public Object getThis() {
        return this;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return null;
    }
}
