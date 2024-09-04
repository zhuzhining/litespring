package org.litespring.test.v4;

import org.junit.Test;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.dao.v4.AccountDao;
import org.litespring.service.v4.PetStoreService;

import java.lang.reflect.Field;

import static org.junit.Assert.assertTrue;

public class DependencyDescriptorTest {
    @Test
    public void testResolveDependency() throws Exception {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinition(new ClassPathResource("petstore-v4.xml"));

        Field field = PetStoreService.class.getDeclaredField("accountDao");
        Object obj = factory.resolveDependency(new DependencyDescriptor(field, true));

        assertTrue(obj instanceof AccountDao);
    }
}
