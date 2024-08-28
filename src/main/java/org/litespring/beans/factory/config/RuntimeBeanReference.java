package org.litespring.beans.factory.config;

public class RuntimeBeanReference {

    private String beanName;

    public RuntimeBeanReference(String beanClassName) {
        this.beanName = beanClassName;
    }

    public String getBeanName() {
        return beanName;
    }
}
