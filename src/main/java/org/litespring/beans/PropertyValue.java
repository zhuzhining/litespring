package org.litespring.beans;

public class PropertyValue {

    private String name;

    private Object value;

    private Object convertedValue;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public Object getConvertedValue() {
        return this.convertedValue;
    }
}
