package org.litespring.test.v5;

import org.junit.Test;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.aspectj.AspectJExpressionPointcut;
import org.litespring.aop.config.AspectInstanceFactory;
import org.litespring.aop.config.MethodLocatingFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.tx.TransactionManager;

import java.util.List;

import static org.junit.Assert.*;

public class BeanDefinitionTest extends AbstractV5Test{
    @Test
    public void testBeanDefinition() {
        DefaultBeanFactory factory = (DefaultBeanFactory) this.getBeanFactory("petstore-v5.xml");
        {
            BeanDefinition bd = factory.getBeanDefinition("tx");
            assertTrue(TransactionManager.class.getName().equals(bd.getBeanClassName()));
        }

        {
            BeanDefinition bd = factory.getBeanDefinition("placeOrder");
            assertEquals(bd.getBeanClass(), AspectJExpressionPointcut.class);
            PropertyValue pv = bd.getPropertyValues().get(0);
            assertEquals(pv.getName(), "expression");
            assertEquals(pv.getValue(), "execution(* org.litespring.service.v5.*.placeOrder(..))");
        }
        {
            String beanId = AspectJBeforeAdvice.class.getName() + "#0";
            BeanDefinition bd = factory.getBeanDefinition(beanId);
            assertNotNull(bd);
            assertEquals(bd.getBeanClass(), AspectJBeforeAdvice.class);

            assertTrue(bd.isSynthetic());

            PropertyValue propertyValue = bd.getPropertyValues().get(0);
            assertEquals(propertyValue.getName(), "aspectName");
            assertEquals(propertyValue.getValue(), "tx");

            List<ConstructorArgument.ValueHolder> args = bd.getConstructorArgument().getArgumentValues();
            assertEquals(args.size(), 3);

            {
                BeanDefinition innerBeanDef = (BeanDefinition) args.get(0).getValue();
                assertTrue(innerBeanDef.isSynthetic());
                assertEquals(innerBeanDef.getBeanClass(), MethodLocatingFactory.class);

                List<PropertyValue> pvs = innerBeanDef.getPropertyValues();
                assertEquals(pvs.get(0).getName(), "targetBeanName");
                assertEquals(pvs.get(0).getValue(), "tx");
                assertEquals(pvs.get(1).getName(), "methodName");
                assertEquals(pvs.get(1).getValue(), "start");
            }
            {
                Object value = args.get(1).getValue();
                assertTrue(value instanceof RuntimeBeanReference);
                RuntimeBeanReference rb = (RuntimeBeanReference) value;
                assertEquals(rb.getBeanName(), "placeOrder");
            }
            {
                BeanDefinition innerBeanDef = (BeanDefinition) args.get(2).getValue();
                assertTrue(innerBeanDef.isSynthetic());
                assertEquals(innerBeanDef.getBeanClass(), AspectInstanceFactory.class);

                PropertyValue pv = innerBeanDef.getPropertyValues().get(0);
                assertEquals(pv.getName(), "aspectBeanName");
                assertEquals(pv.getValue(), "tx");
            }
        }
    }
}
