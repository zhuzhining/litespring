package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;

import java.util.ArrayList;
import java.util.List;

public class GenericBeanDefinition implements BeanDefinition {

    private String id;

    private String beaClassName;

    private Class beanClass;

    private List<PropertyValue> properties = new ArrayList<>();

    private ConstructorArgument constructorArgument = new ConstructorArgument();

    private boolean singleton = true;

    private boolean protoType = false;

    private boolean isSynthetic = false;

    private String scope = SCOPE_DEFAULT;

    public GenericBeanDefinition(String id, String className) {
        this.id = id;
        this.beaClassName = className;
    }

    public GenericBeanDefinition() {
    }

    public GenericBeanDefinition(Class<?> cls) {
        this.beanClass = cls;
        this.beaClassName = cls.getName();
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        return this.properties;
    }

    @Override
    public ConstructorArgument getConstructorArgument() {
        return this.constructorArgument;
    }

    @Override
    public String getBeanClassName() {
        return this.beaClassName;
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    @Override
    public boolean isPrototype() {
        return this.protoType;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
        this.protoType = SCOPE_PROTOTYPE.equals(scope);
    }

    public void setId(String beanID) {
        this.id = beanID;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean hasBeanClass() {
        return this.beanClass != null;
    }

    @Override
    public Class getBeanClass() {
        if (!hasBeanClass()) {
            throw new RuntimeException("请先调用【resolveBeanClass】方法解析beanClass");
        }
        return this.beanClass;
    }

    @Override
    public boolean isSynthetic() {
        return this.isSynthetic;
    }

    public void setSynthetic(boolean synthetic) {
        isSynthetic = synthetic;
    }

    @Override
    public void resolveBeanClass(ClassLoader loader) throws ClassNotFoundException {
        if (!this.hasBeanClass()) {
            this.beanClass = loader.loadClass(this.beaClassName);
        }
    }

    protected void setBeanClassName(String className) {
        this.beaClassName = className;
    }
}
