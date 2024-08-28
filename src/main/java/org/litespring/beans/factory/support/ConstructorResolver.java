package org.litespring.beans.factory.support;

import org.litespring.beans.*;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;

import java.lang.reflect.Constructor;
import java.util.List;

public class ConstructorResolver {

    private ConfigurableBeanFactory factory;

    public ConstructorResolver(ConfigurableBeanFactory factory) {
        this.factory = factory;
    }

    public Constructor findConstructor(BeanDefinition bd) {
        return null;
    }

    public Object autowireConstructor(BeanDefinition bd) {
        Class<?> cls;
        try {
            cls = this.factory.getClassLoader().loadClass(bd.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException("加载bean的class异常：" + bd.getBeanClassName());
        }
        ConstructorArgument constructorArgument = bd.getConstructorArgument();
        Constructor<?>[] candidates = cls.getConstructors();
        Object args[] = null;
        Constructor<?> candidateToUse = null;
        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(factory);
        TypeConverter converter = new SimpleTypeConverter();
        for (Constructor<?> candidate : candidates) {
            if (candidate.getParameterCount() != constructorArgument.getArgumentValuesCount()) {
                continue;
            }
            args = new Object[candidate.getParameterCount()];
            boolean isMatch = valuesMatchType(args, resolver, converter, candidate.getParameterTypes(), constructorArgument.getArgumentValues());
            if (isMatch) {
                candidateToUse = candidate;
            }
        }
        if (candidateToUse == null) {
            throw new BeanCreationException("匹配不到构造器");
        }
        try {
            return candidateToUse.newInstance(args);
        } catch (Exception e) {
            throw new BeanCreationException("匹配构造器实例化失败");
        }
    }

    private boolean valuesMatchType(Object[] args, BeanDefinitionValueResolver resolver, TypeConverter converter,
                                    Class<?>[] parameterTypes, List<ConstructorArgument.ValueHolder> argumentValues) {
        for (int i = 0; i < parameterTypes.length; i++) {
            try {
                Object resolveValue = resolver.resolveValueIfNecessary(argumentValues.get(i).getValue());
                Object value = converter.convertIfNecessary(resolveValue, parameterTypes[i]);
                args[i] = value;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
