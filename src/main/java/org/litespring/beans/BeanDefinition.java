package org.litespring.beans;

import java.util.List;

public interface BeanDefinition {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";
    String SCOPE_DEFAULT = "";

    String getBeanClassName();

    boolean isSingleton();

    boolean isPrototype();

    String getScope();

    void setScope(String scope);

    List<PropertyValue> getPropertyValues();

    ConstructorArgument getConstructorArgument();

    String getId();

    boolean hasBeanClass();

    Class getBeanClass();

    void resolveBeanClass(ClassLoader loader) throws ClassNotFoundException;
}
