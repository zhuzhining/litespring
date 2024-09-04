package org.litespring.test.v4;

import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.context.annotation.ClassPathBeanDefinitionScanner;
import org.litespring.context.annotation.ScannerGenericBeanDefinition;
import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.stereotype.Component;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClassPathBeanDefinitionScannerTest {

    @Test
    public void testGetBeanDefinition() {
        DefaultBeanFactory factory = new DefaultBeanFactory();

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(factory);

        scanner.doScan("org.litespring.dao.v4,org.litespring.service.v4");

        {
            BeanDefinition bd = factory.getBeanDefinition("petStore");
            assertNotNull(bd);
            assertTrue(bd.getBeanClassName().equals("org.litespring.service.v4.PetStoreService"));
            assertTrue(bd instanceof ScannerGenericBeanDefinition);
            ScannerGenericBeanDefinition beanDefinition = (ScannerGenericBeanDefinition) bd;
            assertTrue(beanDefinition.getMetadata().hasAnnotation(Component.class.getName()));
            AnnotationAttributes annotationAttributes = beanDefinition.getMetadata().getAnnotationAttributes(Component.class.getName());
            assertTrue("petStore".equals(annotationAttributes.get("value")));
        }
        {
            BeanDefinition bd = factory.getBeanDefinition("accountDao");
            assertNotNull(bd);
            assertTrue(bd.getBeanClassName().equals("org.litespring.dao.v4.AccountDao"));
        }
        {
            BeanDefinition bd = factory.getBeanDefinition("itemDao");
            assertNotNull(bd);
            assertTrue(bd.getBeanClassName().equals("org.litespring.dao.v4.ItemDao"));
        }
    }
}
