package org.litespring.beans;

import org.litespring.beans.factory.support.GenericBeanDefinition;

import java.util.ArrayList;
import java.util.List;

public class ConstructorArgument {

    private List<ValueHolder> argumentValues = new ArrayList<>();

    public List<ValueHolder> getArgumentValues() {
        return this.argumentValues;
    }

    public void addArgumentValue(Object value) {
        this.argumentValues.add(new ValueHolder(value));
    }

    public void addArgumentValue(ValueHolder valueHolder) {
        this.argumentValues.add(valueHolder);
    }

    public boolean isEmpty() {
        return this.argumentValues.size() == 0;
    }

    public int getArgumentCount() {
        return this.argumentValues.size();
    }


    public static class ValueHolder {

        private Object value;

        private String name;

        private String type;

        public ValueHolder(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
