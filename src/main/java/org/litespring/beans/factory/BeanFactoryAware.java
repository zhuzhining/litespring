package org.litespring.beans.factory;

import org.litespring.beans.BeansException;

public interface BeanFactoryAware {

    void setBeanFactory(BeanFactory factory) throws BeansException;
}
