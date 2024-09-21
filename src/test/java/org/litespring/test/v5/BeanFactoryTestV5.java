package org.litespring.test.v5;

import org.junit.Test;
import org.litespring.aop.Advice;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJAfterThrowingAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.tx.TransactionManager;

import java.util.List;
import static org.junit.Assert.assertEquals;

public class BeanFactoryTestV5 extends AbstractV5Test {
    @Test
    public void testGetBeanByType() throws Exception {
        String expression = "execution(* org.litespring.service.v5.*.placeOrder(..))";
        BeanFactory factory = this.getBeanFactory("petstore-v5.xml");
        List<Object> advices = factory.getBeansByType(Advice.class);

        assertEquals(3, advices.size());
        {
            AspectJBeforeAdvice advice = (AspectJBeforeAdvice) this.getAdvice(AspectJBeforeAdvice.class, advices);

            assertEquals(advice.getAdviceMethod(), TransactionManager.class.getMethod("start"));

            assertEquals(advice.getPointcut().getExpression(), expression);

            assertEquals(advice.getAdviceInstance().getClass(), TransactionManager.class);
        }

        {
            AspectJAfterReturningAdvice advice = (AspectJAfterReturningAdvice) this.getAdvice(AspectJAfterReturningAdvice.class, advices);

            assertEquals(advice.getAdviceMethod(), TransactionManager.class.getMethod("commit"));

            assertEquals(advice.getPointcut().getExpression(), expression);

            assertEquals(advice.getAdviceInstance().getClass(), TransactionManager.class);
        }

        {
            AspectJAfterThrowingAdvice advice = (AspectJAfterThrowingAdvice) this.getAdvice(AspectJAfterThrowingAdvice.class, advices);

            assertEquals(advice.getAdviceMethod(), TransactionManager.class.getMethod("rollback"));

            assertEquals(advice.getPointcut().getExpression(), expression);

            assertEquals(advice.getAdviceInstance().getClass(), TransactionManager.class);
        }
    }

    private Object getAdvice(Class<?> cls, List<Object> advices) {
        for (Object advice : advices) {
            if (advice.getClass().equals(cls)) {
                return advice;
            }
        }
        return null;
    }
}
