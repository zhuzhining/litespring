package org.litespring.beans.factory.config;

public interface ConfigurableBeanFactory extends AutowireCapableBeanFactory {

    void setClassLoader(ClassLoader classLoader);

    ClassLoader getClassLoader();
}
