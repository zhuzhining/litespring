package org.litespring.context.support;

import org.litespring.aop.aspectj.AspectJAutoProxyCreator;
import org.litespring.beans.factory.NoSuchBeanDefinitionException;
import org.litespring.beans.factory.annotation.AutowiredAnnotationProcessor;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.core.io.Resource;
import org.litespring.util.ClassUtils;

import java.util.List;

public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory factory;

    public AbstractApplicationContext(String configFile) {
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = this.getResourceByPath(configFile);
        reader.loadBeanDefinition(resource);
        registryBeanPostProcessor(factory);
    }

    @Override
    public List<Object> getBeansByType(Class<?> type) {
        return this.factory.getBeansByType(type);
    }

    @Override
    public Object getBean(String beanID) {
        return factory.getBean(beanID);
    }

    protected abstract Resource getResourceByPath(String configFile);

    protected void registryBeanPostProcessor(ConfigurableBeanFactory factory) {
        {
            AutowiredAnnotationProcessor processor = new AutowiredAnnotationProcessor();
            processor.setBeanFactory(factory);
            factory.addBeanPostProcessor(processor);
        }
        {
            AspectJAutoProxyCreator processor = new AspectJAutoProxyCreator();
            processor.setBeanFactory(factory);
            factory.addBeanPostProcessor(processor);
        }
    }

    @Override
    public Class<?> getType(String beanName) throws NoSuchBeanDefinitionException {
        return this.factory.getType(beanName);
    }
}
