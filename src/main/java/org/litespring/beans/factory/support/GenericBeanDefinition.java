package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;

import java.util.ArrayList;
import java.util.List;

public class GenericBeanDefinition implements BeanDefinition {

    private String id;

    private String className;

    private List<PropertyValue> properties = new ArrayList<>();

    private ConstructorArgument constructorArgument = new ConstructorArgument();

    private boolean singleton = true;

    private boolean protoType = false;

    private String scope = SCOPE_DEFAULT;

    public GenericBeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
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
        return this.className;
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
}
