package org.litespring.beans.factory.annotation;

import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.AutowireCapableBeanFactory;
import org.litespring.beans.factory.config.DependencyDescriptor;

import java.lang.reflect.Field;

public class AutowireFieldElement extends InjectionElement{

    private boolean required;

    public AutowireFieldElement(Field field, boolean required, AutowireCapableBeanFactory factory) {
        super(field, factory);
        this.required = required;
    }

    public Field getField() {
        return (Field) this.member;
    }

    @Override
    public void inject(Object target) {
        Field field = this.getField();
        DependencyDescriptor descriptor = new DependencyDescriptor(field, this.required);
        Object value = factory.resolveDependency(descriptor);
        if (value != null) {
            //简单处理
            field.setAccessible(true);
            try {
                field.set(target, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new BeanCreationException("属性注入异常", e);
            }
        }
    }
}
