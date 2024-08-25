package org.litespring.beans.factory.config;

public interface SingletonBeanRegistry {

    void registrySingleton(String beanID, Object bean);

    Object getSingleton(String beanID);
}
