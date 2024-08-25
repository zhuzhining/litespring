package org.litespring.test.v1;

import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.*;

public class BeanFactoryTest {

    DefaultBeanFactory factory;
    XmlBeanDefinitionReader reader;
    @Before
    public void setUp() {
        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(factory);
    }

    @Test
    public void testGetBean() {
        reader.loadBeanDefinition("petstore-v1.xml");
        BeanDefinition bd = factory.getBeanDefinition("petStore");

        assertEquals("org.litespring.service.v1.PetStoreService", bd.getBeanClassName());

        PetStoreService petStore = (PetStoreService)factory.getBean("petStore");

        assertNotNull(petStore);

    }

    @Test
    public void testInvalidBean() {
        reader.loadBeanDefinition("petstore-v1.xml");
        try {
            factory.getBean("InvalidBean");
        } catch (BeanCreationException e) {
            return;
        }
        fail("export BeanCreationException");
    }

    @Test
    public void testInvalidXML() {

        try {
            reader.loadBeanDefinition("xxxx.xml");
        } catch (BeanDefinitionStoreException e) {
            return;
        }
        fail("export BeanDefinitionStoreException");
    }
}
