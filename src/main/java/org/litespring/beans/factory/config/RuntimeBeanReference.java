package org.litespring.beans.factory.config;

public class RuntimeBeanReference {

    private String beanClassName;

    public RuntimeBeanReference(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public String getBeanClassName() {
        return beanClassName;
    }
}
