package org.litespring.aop.config;

import org.litespring.beans.BeansException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.BeanFactoryAware;

public class AspectInstanceFactory implements BeanFactoryAware {

    private BeanFactory factory;

    private String targetBeanName;

    public AspectInstanceFactory() {
    }

    @Override
    public void setBeanFactory(BeanFactory factory) throws BeansException {
        this.factory = factory;
    }

    public void setAspectBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    public Object getObject() {
        return this.factory.getBean(this.targetBeanName);
    }
}
