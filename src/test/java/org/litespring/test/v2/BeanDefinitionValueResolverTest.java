package org.litespring.test.v2;

import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinitionValueResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.dao.v2.AccountDao;
import org.litespring.dao.v2.ItemDao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BeanDefinitionValueResolverTest {
    DefaultBeanFactory factory;
    XmlBeanDefinitionReader reader;
    BeanDefinitionValueResolver resolver;

    @Before
    public void setUp() {
        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinition(new ClassPathResource("petStore-v2.xml"));
        resolver = new BeanDefinitionValueResolver(factory);
    }

    @Test
    public void testResolveRuntimeBeanReference() {
        RuntimeBeanReference reference = new RuntimeBeanReference("accountDao");
        Object value = resolver.resolveValueIfNecessary(reference);
        assertNotNull(value);
        assertTrue(value instanceof AccountDao);

    }

    @Test
    public void testResolveTypedStringValue() {

        TypedStringValue typedStringValue = new TypedStringValue("3");
        Object value = resolver.resolveValueIfNecessary(typedStringValue);
        assertNotNull(value);
        assertTrue("3".equals(value));

    }
}
