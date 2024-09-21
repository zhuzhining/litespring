package org.litespring.aop.aspectj;

import org.litespring.aop.Advice;
import org.litespring.aop.MethodMatcher;
import org.litespring.aop.Pointcut;
import org.litespring.aop.framework.AopConfig;
import org.litespring.aop.framework.AopConfigSupport;
import org.litespring.aop.framework.AopProxyFactory;
import org.litespring.aop.framework.CglibProxyFactory;
import org.litespring.beans.BeansException;
import org.litespring.beans.factory.config.BeanPostProcessor;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AspectJAutoProxyCreator implements BeanPostProcessor {
    ConfigurableBeanFactory beanFactory;
    @Override
    public Object beforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object afterInitialization(Object bean, String beanName) throws BeansException {
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }
        List<Advice> advices = getCandidateAdvices(bean);
        if (advices.isEmpty()) {
            return bean;
        }
        return createProxy(advices, bean);
    }

    private Object createProxy(List<Advice> advices, Object bean) {
        AopConfigSupport config = new AopConfigSupport();
        for (Advice advice : advices) {
            config.addAdvice(advice);
        }
        Set<Class> targetInterfaces = ClassUtils.getAllInterfacesForClassAsSet(bean.getClass());
        for (Class targetInterface : targetInterfaces) {
            config.addInterface(targetInterface);
        }
        config.setTargetObject(bean);

        AopProxyFactory factory = null;

        if (config.getProxiedInterfaces().length == 0) {
            factory = new CglibProxyFactory(config);

        } else {
            //TODO 使用JDK动态代理
        }

        return factory.getProxy();
    }

    private List<Advice> getCandidateAdvices(Object bean) {
        List<Advice> result = new ArrayList<>();
        List<Object> list = this.beanFactory.getBeansByType(Advice.class);
        for (Object o : list) {
            Pointcut pointcut = ((Advice) o).getPointcut();
            if(canApply(pointcut, bean.getClass())) {
                result.add((Advice) o);
            }
        }
        return result;
    }

    private boolean canApply(Pointcut pointcut, Class<?> targetClass) {
        MethodMatcher methodMatcher = pointcut.getMethodMatcher();
        Set<Class> classes = ClassUtils.getAllInterfacesForClassAsSet(targetClass);
        classes.add(targetClass);
        for (Class cls : classes) {
            for (Method method : cls.getMethods()) {
                if (methodMatcher.matches(method)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isInfrastructureClass(Class<?> cls) {
        return Advice.class.isAssignableFrom(cls);
    }

    public void setBeanFactory(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
