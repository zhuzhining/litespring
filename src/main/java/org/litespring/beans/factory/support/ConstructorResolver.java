package org.litespring.beans.factory.support;

import org.litespring.beans.*;
import org.litespring.beans.factory.BeanCreationException;

import java.lang.reflect.Constructor;
import java.util.List;

public class ConstructorResolver {

    private AbstractBeanFactory beanFactory;

    public ConstructorResolver(AbstractBeanFactory factory) {
        this.beanFactory = factory;
    }

    public Constructor findConstructor(BeanDefinition bd) {
        return null;
    }

    public Object autowireConstructor(final BeanDefinition bd) {

        Constructor<?> constructorToUse = null;
        Object[] argsToUse = null;

        Class<?> beanClass = null;
        try {
            beanClass = this.beanFactory.getBeanClassLoader().loadClass(bd.getBeanClassName());

        } catch (ClassNotFoundException e) {
            throw new BeanCreationException( bd.getId(), "Instantiation of bean failed, can't resolve class", e);
        }


        Constructor<?>[] candidates = beanClass.getConstructors();


        BeanDefinitionValueResolver valueResolver =
                new BeanDefinitionValueResolver(this.beanFactory);

        ConstructorArgument cargs = bd.getConstructorArgument();
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();

        for(int i=0; i<candidates.length;i++){

            Class<?> [] parameterTypes = candidates[i].getParameterTypes();
            if(parameterTypes.length != cargs.getArgumentCount()){
                continue;
            }
            argsToUse = new Object[parameterTypes.length];

            boolean result = this.valuesMatchTypes(parameterTypes,
                    cargs.getArgumentValues(),
                    argsToUse,
                    valueResolver,
                    typeConverter);

            if(result){
                constructorToUse = candidates[i];
                break;
            }

        }


        //找不到一个合适的构造函数
        if(constructorToUse == null){
            throw new BeanCreationException( bd.getId(), "can't find a apporiate constructor");
        }


        try {
            return constructorToUse.newInstance(argsToUse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanCreationException( bd.getId(), "can't find a create instance using "+constructorToUse);
        }


    }

    private boolean valuesMatchTypes(Class<?>[] parameterTypes, List<ConstructorArgument.ValueHolder> argumentValues,
                                     Object[] args, BeanDefinitionValueResolver resolver, TypeConverter converter
                                    ) {
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
