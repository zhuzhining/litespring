package org.litespring.test.v2;

import org.junit.Test;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.TypeMismatchException;

import static org.junit.Assert.*;

public class SimpleTypeConverterTest {

    @Test
    public void testConvertIfNecessary() {
        SimpleTypeConverter converter = new SimpleTypeConverter();
        Integer value = converter.convertIfNecessary("3", Integer.class);
        assertNotNull(value);
        assertTrue(value == 3);

        try {
            converter.convertIfNecessary("3.1", Integer.class);
        } catch (TypeMismatchException e) {
            return;
        }
        fail();
    }
}
