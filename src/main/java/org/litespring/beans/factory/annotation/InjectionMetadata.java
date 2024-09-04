package org.litespring.beans.factory.annotation;

import java.util.List;

public class InjectionMetadata {
    private Class<?> targetClass;
    private List<InjectionElement> injectionElements;

    public InjectionMetadata(Class<?> targetClass, List<InjectionElement> injectionElements) {
        this.targetClass = targetClass;
        this.injectionElements = injectionElements;
    }

    public void inject(Object target) {
        if (injectionElements == null || injectionElements.size() == 0) {
            return;
        }
        for (InjectionElement injectionElement : injectionElements) {
            injectionElement.inject(target);
        }
    }

    public List<InjectionElement> getInjectionElements() {
        return injectionElements;
    }
}
