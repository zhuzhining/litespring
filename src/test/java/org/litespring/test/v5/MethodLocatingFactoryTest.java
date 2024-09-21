package org.litespring.test.v5;

import org.junit.Test;
import org.litespring.aop.config.MethodLocatingFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.tx.TransactionManager;

import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

public class MethodLocatingFactoryTest extends AbstractV5Test {
    @Test
    public void testGetMethod() throws Exception {
        DefaultBeanFactory factory = (DefaultBeanFactory) this.getBeanFactory("petstore-v5.xml");

        MethodLocatingFactory locatingFactory = new MethodLocatingFactory();
        locatingFactory.setTargetBeanName("tx");
        locatingFactory.setMethodName("start");
        locatingFactory.setBeanFactory(factory);

        Method method = locatingFactory.getObject();

        assertTrue(TransactionManager.class.equals(method.getDeclaringClass()));
        assertTrue(method.equals(TransactionManager.class.getMethod("start")));
    }
}
