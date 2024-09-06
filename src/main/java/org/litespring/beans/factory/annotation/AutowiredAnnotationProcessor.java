package org.litespring.beans.factory.annotation;

import org.litespring.beans.BeansException;
import org.litespring.beans.factory.config.AutowireCapableBeanFactory;
import org.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.litespring.core.annotation.AnnotationUtils;
import org.litespring.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AutowiredAnnotationProcessor implements InstantiationAwareBeanPostProcessor {
    private AutowireCapableBeanFactory factory;

    private Set<Class<? extends Annotation>> autowiredAnnotationTypes = new HashSet<>();
    private String requiredParameterName = "required";
    private boolean requiredParameterValue = true;

    public AutowiredAnnotationProcessor() {
        autowiredAnnotationTypes.add(Autowired.class);
    }

    public InjectionMetadata buildAutowiringMetadata(Class<?> targetClass) {
        List<InjectionElement> injectionElements = new LinkedList<>();
        Class<?> cls = targetClass;
        do {
            List<InjectionElement> currElements = new LinkedList<>();
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                Annotation annotation = findAutowiredAnnotation(field);
                if (annotation != null) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    boolean required = determineRequiredStatus(annotation);
                    currElements.add(new AutowireFieldElement(field, required, factory));
                }
            }
            injectionElements.addAll(currElements);
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);

        return new InjectionMetadata(cls, injectionElements);
    }

    protected boolean determineRequiredStatus(Annotation ann) {
        try {
            Method method = ReflectionUtils.findMethod(ann.annotationType(), this.requiredParameterName);
            if (method == null) {
                // Annotations like @Inject and @Value don't have a method (attribute) named "required"
                // -> default to required status
                return true;
            }
            return (this.requiredParameterValue == (Boolean) ReflectionUtils.invokeMethod(method, ann));
        }
        catch (Exception ex) {
            // An exception was thrown during reflective invocation of the required attribute
            // -> default to required status
            return true;
        }
    }

    private Annotation findAutowiredAnnotation(Field field) {
        for (Class<? extends Annotation> annotationType : autowiredAnnotationTypes) {
            Annotation annotation = AnnotationUtils.getAnnotation(field, annotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    public void setBeanFactory(AutowireCapableBeanFactory factory) {
        this.factory = factory;
    }

    @Override
    public Object beforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object afterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object beforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean afterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public Object postProcessPropertyValues(Object bean, String beanName) throws BeansException {
        InjectionMetadata metadata = buildAutowiringMetadata(bean.getClass());
        try {
            metadata.inject(bean);
        } catch (Exception e) {
            throw new BeansException("bean属性注入异常", e);
        }
        return null;
    }
}
