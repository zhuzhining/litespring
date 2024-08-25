package org.litespring.beans.factory.support;

import org.litespring.beans.factory.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {


    private final Map<String, Object> singletonMap = new ConcurrentHashMap<>();

    @Override
    public void registrySingleton(String beanID, Object bean) {
        if (this.singletonMap.get(beanID) != null) {
            throw new RuntimeException("单例beanID: " + beanID + "已经注册过了");
        }
        this.singletonMap.put(beanID, bean);
    }

    @Override
    public Object getSingleton(String beanID) {
        return this.singletonMap.get(beanID);
    }
}
