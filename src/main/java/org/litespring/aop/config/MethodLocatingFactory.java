package org.litespring.aop.config;

import org.litespring.beans.BeanUtils;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.BeanFactoryAware;
import org.litespring.beans.factory.FactoryBean;

import java.lang.reflect.Method;

public class MethodLocatingFactory implements FactoryBean<Method>, BeanFactoryAware {
    private String targetBeanName;
    private String methodName;
    private Method method;

    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setBeanFactory(BeanFactory factory) {
        Class<?> targetClass = factory.getType(this.targetBeanName);
        this.method = BeanUtils.resolveSignature(this.methodName, targetClass);
    }

    public Method getObject() {
        return this.method;
    }

    @Override
    public Class<?> getObjectType() {
        return this.method.getClass();
    }
}
