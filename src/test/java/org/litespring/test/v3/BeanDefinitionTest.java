package org.litespring.test.v3;

import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BeanDefinitionTest {

    @Test
    public void testConstructorValue() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        reader.loadBeanDefinition(new ClassPathResource("petstore-v3.xml"));

        BeanDefinition bd = factory.getBeanDefinition("petStore");

        ConstructorArgument constructorArgument = bd.getConstructorArgument();
        assertNotNull(constructorArgument);

        List<ConstructorArgument.ValueHolder> valueHolders = constructorArgument.getArgumentValues();

        assertTrue(valueHolders.size() == 3);

        assertTrue(valueHolders.get(0).getValue() instanceof RuntimeBeanReference);
        assertTrue("accountDao".equals(((RuntimeBeanReference) valueHolders.get(0).getValue()).getBeanName()));

        assertTrue(valueHolders.get(1).getValue() instanceof RuntimeBeanReference);
        assertTrue("itemDao".equals(((RuntimeBeanReference) valueHolders.get(1).getValue()).getBeanName()));

        assertTrue(valueHolders.get(2).getValue() instanceof TypedStringValue);
        assertTrue("1".equals(((TypedStringValue) valueHolders.get(2).getValue()).getValue()));

    }
}
