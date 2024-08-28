package org.litespring.beans.factory.support;

import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;

public class BeanDefinitionValueResolver {

    private BeanFactory factory;

    public BeanDefinitionValueResolver(BeanFactory factory) {
        this.factory = factory;
    }

    public Object resolveValueIfNecessary(Object value) {

        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference reference = (RuntimeBeanReference) value;
            return this.factory.getBean(reference.getBeanName());
        } else if(value instanceof TypedStringValue) {
            TypedStringValue valueHolder = (TypedStringValue) value;
            return valueHolder.getValue();
        } else {
            //TODO
            throw new RuntimeException("暂时不支持对应类型的value处理：" + value);
        }
    }
}
