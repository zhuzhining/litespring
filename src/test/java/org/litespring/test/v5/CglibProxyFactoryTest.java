package org.litespring.test.v5;

import org.junit.Before;
import org.junit.Test;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.aspectj.AspectJExpressionPointcut;
import org.litespring.aop.config.AspectInstanceFactory;
import org.litespring.aop.framework.AopConfig;
import org.litespring.aop.framework.AopConfigSupport;
import org.litespring.aop.framework.AopProxyFactory;
import org.litespring.aop.framework.CglibProxyFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.litespring.util.MessageTracker;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CglibProxyFactoryTest extends AbstractV5Test {

    AspectJBeforeAdvice beforeAdvice;
    AspectJAfterReturningAdvice afterAdvice;
    AspectJExpressionPointcut pc;
    DefaultBeanFactory factory;
    @Before
    public void setUp() throws Exception {
        pc = new AspectJExpressionPointcut();
        pc.setExpression("execution(* org.litespring.service.v5.*.placeOrder(..))");
        TransactionManager tx = new TransactionManager();
        factory = (DefaultBeanFactory) this.getBeanFactory("petstore-v5.xml");
        AspectInstanceFactory aspectInstanceFactory = this.getAspectInstanceFactory("tx");
        aspectInstanceFactory.setBeanFactory(factory);
        beforeAdvice = new AspectJBeforeAdvice(TransactionManager.class.getMethod("start"), pc, aspectInstanceFactory);
        afterAdvice = new AspectJAfterReturningAdvice(TransactionManager.class.getMethod("commit"), pc, aspectInstanceFactory);
    }

    @Test
    public void testProxy() {
        AopConfig config = new AopConfigSupport();
        config.setTargetObject(new PetStoreService());

        config.addAdvice(beforeAdvice);
        config.addAdvice(afterAdvice);

        AopProxyFactory factory = new CglibProxyFactory(config);
        PetStoreService proxy = (PetStoreService) factory.getProxy();

        proxy.placeOrder();

        List<String> msgs = MessageTracker.get();
        assertTrue(msgs.size() == 3);
        assertEquals(msgs.get(0), "tx start");
        assertEquals(msgs.get(1), "place order");
        assertEquals(msgs.get(2), "tx commit");

        proxy.toString();
    }
}
