package org.litespring.test.v3;

import org.junit.Test;
import org.litespring.beans.factory.support.ConstructorResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.service.v3.PetStoreService;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConstructorResolverTest {
    @Test
    public void testConstructorResolve() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinition(new ClassPathResource("petstore-v3.xml"));

        ConstructorResolver resolver = new ConstructorResolver(factory);
        PetStoreService petStore = (PetStoreService) resolver.autowireConstructor(factory.getBeanDefinition("petStore"));

        assertTrue(petStore.getVersion() == 1);
        assertNotNull(petStore.getAccountDao());
        assertNotNull(petStore.getItemDao());
    }
}
