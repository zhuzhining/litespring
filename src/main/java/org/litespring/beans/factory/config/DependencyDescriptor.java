package org.litespring.beans.factory.config;

import java.lang.reflect.Field;

public class DependencyDescriptor {

    private Field field;

    private boolean require;

    public DependencyDescriptor(Field field, boolean require) {
        this.field = field;
        this.require = require;
    }

    public Class getDependencyType() {
        if (this.field != null) {
            return this.field.getType();
        }
        throw new RuntimeException("暂时不支持其他依赖类型");
    }
}
