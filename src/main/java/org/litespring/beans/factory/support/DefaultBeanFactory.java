package org.litespring.beans.factory.support;

import org.apache.commons.beanutils.BeanUtils;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanFactoryAware;
import org.litespring.beans.factory.NoSuchBeanDefinitionException;
import org.litespring.beans.factory.config.BeanPostProcessor;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends AbstractBeanFactory implements BeanDefinitionRegistry {

    private ClassLoader classLoader;

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private List<BeanPostProcessor> postProcessors = new ArrayList<>();

    @Override
    public List<Object> getBeansByType(Class<?> type) {
        List<Object> result = new ArrayList<>();
        for (String beanId : beanDefinitionMap.keySet()) {
            if (type.isAssignableFrom(this.getType(beanId))) {
                result.add(getBean(beanId));
            }
        }
        return result;
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanID) {
        return this.beanDefinitionMap.get(beanID);
    }

    @Override
    public void registerBeanDefinition(String beanID, BeanDefinition bd) {
        this.beanDefinitionMap.put(beanID, bd);
    }

    @Override
    public Class<?> getType(String beanName) throws NoSuchBeanDefinitionException {
        BeanDefinition bd = this.getBeanDefinition(beanName);
        if (bd == null) {
            throw new NoSuchBeanDefinitionException("获取不到bean定义");
        }
        try {
            bd.resolveBeanClass(this.getBeanClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return bd.getBeanClass();
    }

    @Override
    public Object getBean(String beanID) {
        BeanDefinition bd = this.beanDefinitionMap.get(beanID);
        if (bd == null) {
            throw new BeanCreationException("bean定义不存在");
        }
        if (bd.isSingleton()) {
            Object bean = this.getSingleton(beanID);
            if (bean == null) {
                bean = createBean(bd);
                this.registrySingleton(beanID, bean);
            }
            return bean;
        }
        return createBean(bd);
    }

    protected Object createBean(BeanDefinition bd) {
        //创建实例
        Object bean = instantiateBean(bd);
        //设置属性
        populateBeanUseCommonBeanUtils(bd, bean);
//        populateBean(bd, bean);
        bean = initializeBean(bd, bean);

        return bean;
    }

    private Object instantiateBean(BeanDefinition bd) {
        if (!bd.getConstructorArgument().isEmpty()) {
            ConstructorResolver resolver = new ConstructorResolver(this);
            return resolver.autowireConstructor(bd);
        } else {
            ClassLoader cl = this.getBeanClassLoader();
            try {
                return cl.loadClass(bd.getBeanClassName()).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new BeanCreationException("实例化bean失败", e);
            }
        }

    }

    protected Object initializeBean(BeanDefinition bd, Object bean) {
        invokeAwareMethods(bean);
        //创建代理
        if (!bd.isSynthetic()) {
            return applyBeanPostProcessorsAfterInitialization(bean,bd.getId());
        }
        return bean;
    }

    private Object applyBeanPostProcessorsAfterInitialization(Object existBean, String beanID) {
        Object result = existBean;
        for (BeanPostProcessor beanPostProcessor : this.getBeanPostProcessors()) {
            result = beanPostProcessor.afterInitialization(existBean, beanID);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    private void invokeAwareMethods(final Object bean) {
        if(bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
    }

    protected void populateBean(BeanDefinition bd, Object bean){

        for(BeanPostProcessor processor : this.getBeanPostProcessors()){
            if(processor instanceof InstantiationAwareBeanPostProcessor){
                ((InstantiationAwareBeanPostProcessor)processor).postProcessPropertyValues(bean, bd.getId());
            }
        }

        List<PropertyValue> pvs = bd.getPropertyValues();

        if (pvs == null || pvs.isEmpty()) {
            return;
        }

        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
        SimpleTypeConverter converter = new SimpleTypeConverter();
        try{
            for (PropertyValue pv : pvs){
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);

                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    if(pd.getName().equals(propertyName)){
                        Object convertedValue = converter.convertIfNecessary(resolvedValue, pd.getPropertyType());
                        pd.getWriteMethod().invoke(bean, convertedValue);
                        break;
                    }
                }


            }
        }catch(Exception ex){
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + bd.getBeanClassName() + "]", ex);
        }
    }

    private void populateBeanUseCommonBeanUtils(BeanDefinition bd, Object bean) {

        for (BeanPostProcessor beanPostProcessor : this.getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                ((InstantiationAwareBeanPostProcessor)beanPostProcessor).postProcessPropertyValues(bean, bd.getId());
            }
        }

        List<PropertyValue> propertyValues = bd.getPropertyValues();
        if (propertyValues == null || propertyValues.size() == 0) {
            return;
        }
        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this);
        try {
            for (PropertyValue pv : propertyValues) {
                String name = pv.getName();
                Object originalValue = pv.getValue();
                Object resolveValue = resolver.resolveValueIfNecessary(originalValue);
                BeanUtils.setProperty(bean, name, resolveValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanCreationException("bean填充属性失败", e);
        }
    }

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader();
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor postProcessor) {
        this.postProcessors.add(postProcessor);
    }

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.postProcessors;
    }

    @Override
    public Object resolveDependency(DependencyDescriptor descriptor) {
        Class fieldType = descriptor.getDependencyType();
        for (BeanDefinition bd : this.beanDefinitionMap.values()) {
            try {
                bd.resolveBeanClass(this.getBeanClassLoader());
                if (bd.getBeanClass().isAssignableFrom(fieldType)) {
                    return this.getBean(bd.getId());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
