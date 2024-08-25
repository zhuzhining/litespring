package org.litespring.beans.factory.config;

import org.litespring.beans.factory.BeanFactory;

public interface ConfigurableBeanFactory extends BeanFactory {

    void setClassLoader(ClassLoader classLoader);

    ClassLoader getClassLoader();
}
