package org.litespring.test.v2;

import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;

import static org.junit.Assert.assertTrue;

public class BeanDefinitionTest {

    @Test
    public void testPropertyValue() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        reader.loadBeanDefinition(new ClassPathResource("petstore-v2.xml"));

        BeanDefinition bd = factory.getBeanDefinition("petStore");

        assertTrue(bd.getPropertyValues().size() == 4);

        {
            PropertyValue pv = this.getPropertyValueByName(bd, "accountDao");
            assertTrue(pv.getValue() instanceof RuntimeBeanReference);
        }
        {
            PropertyValue pv = this.getPropertyValueByName(bd, "itemDao");
            assertTrue(pv.getValue() instanceof RuntimeBeanReference);
        }
    }

    private PropertyValue getPropertyValueByName(BeanDefinition bd, String name) {
        for (PropertyValue propertyValue : bd.getPropertyValues()) {
            if (propertyValue.getName().equals(name)) {
                return propertyValue;
            }
        }
        return null;
    }
}
