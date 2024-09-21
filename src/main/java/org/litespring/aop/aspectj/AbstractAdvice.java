package org.litespring.aop.aspectj;

import org.litespring.aop.Advice;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

public abstract class AbstractAdvice implements Advice {

    protected AspectJExpressionPointcut pointcut;

    protected Method adviceMethod;

    protected AspectInstanceFactory aspectInstanceFactory;

    public AbstractAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory aspectInstanceFactory) {
        this.aspectInstanceFactory = aspectInstanceFactory;
        this.pointcut = pointcut;
        this.adviceMethod = adviceMethod;
    }

    @Override
    public AspectJExpressionPointcut getPointcut() {
        return pointcut;
    }

    public Method getAdviceMethod() {
        return adviceMethod;
    }

    public Object getAdviceInstance() {
        return this.aspectInstanceFactory.getObject();
    }

    protected void invokeAdviceMethod() throws Throwable {
        adviceMethod.invoke(this.getAdviceInstance());
    }
}
