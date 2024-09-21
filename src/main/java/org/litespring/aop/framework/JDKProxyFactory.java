package org.litespring.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.aop.Advice;
import org.litespring.aop.framework.AopConfig;
import org.litespring.aop.framework.AopProxyFactory;
import org.litespring.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class JDKProxyFactory implements AopProxyFactory, InvocationHandler {

    protected static final Log logger = LogFactory.getLog(CglibProxyFactory.class);

    private final AopConfig config;

    public JDKProxyFactory(AopConfig config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        return this.getProxy(this.getClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating CGLIB proxy: target source is " + this.config.getTargetClass());
        }
        Set<Class> targetInterfaces = ClassUtils.getAllInterfacesForClassAsSet(this.config.getTargetClass());


        return Proxy.newProxyInstance(classLoader, targetInterfaces.toArray(new Class[targetInterfaces.size()]), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Advice> chain = this.config.getAdvices(method/*, targetClass*/);
        Object target = this.config.getTargetObject();
        Object retVal;
        if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
            // We can skip creating a MethodInvocation: just invoke the target directly.
            // Note that the final invoker must be an InvokerInterceptor, so we know
            // it does nothing but a reflective operation on the target, and no hot
            // swapping or fancy proxying.
            retVal = method.invoke(target, args);
        }
        else {
            List<MethodInterceptor> interceptors = new ArrayList<>();

            interceptors.addAll(chain);


            // We need to create a method invocation...
            retVal = new ReflectiveMethodInvocation(target, method, args, interceptors).proceed();
        }
        //retVal = processReturnType(proxy, target, method, retVal);
        return retVal;
    }
}
