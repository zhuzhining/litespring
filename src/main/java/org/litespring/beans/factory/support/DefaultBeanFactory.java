package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.util.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanFactory, BeanDefinitionRegistry {


    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String beanID) {
        return this.beanDefinitionMap.get(beanID);
    }

    @Override
    public void registryBeanDefinition(String beanID, BeanDefinition bd) {
        this.beanDefinitionMap.put(beanID, bd);
    }

    @Override
    public Object getBean(String beanID) {
        BeanDefinition bd = this.beanDefinitionMap.get(beanID);
        if (bd == null) {
            throw new BeanCreationException("bean定义不存在");
        }

        ClassLoader cl = ClassUtils.getDefaultClassLoader();
        Class<?> cls = null;
        try {
            cls = cl.loadClass(bd.getBeanClassName());
            return cls.newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("实例化bean失败");
        }
    }
}
