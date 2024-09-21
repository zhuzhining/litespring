package org.litespring.test.v5;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Before;
import org.junit.Test;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJAfterThrowingAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.config.AspectInstanceFactory;
import org.litespring.aop.framework.ReflectiveMethodInvocation;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.litespring.util.MessageTracker;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReflectiveMethodInvocationTest extends AbstractV5Test {
    AspectJBeforeAdvice beforeAdvice;
    AspectJAfterReturningAdvice afterAdvice;
    AspectJAfterThrowingAdvice afterThrowingAdvice;
    DefaultBeanFactory factory;
    @Before
    public void setUp() throws Exception {
        factory = (DefaultBeanFactory) this.getBeanFactory("petstore-v5.xml");
        AspectInstanceFactory aspectInstanceFactory = this.getAspectInstanceFactory("tx");
        aspectInstanceFactory.setBeanFactory(factory);
        beforeAdvice = new AspectJBeforeAdvice(TransactionManager.class.getMethod("start"), null, aspectInstanceFactory);
        afterAdvice = new AspectJAfterReturningAdvice(TransactionManager.class.getMethod("commit"), null, aspectInstanceFactory);
        afterThrowingAdvice = new AspectJAfterThrowingAdvice(TransactionManager.class.getMethod("rollback"), null, aspectInstanceFactory);
    }

    @Test
    public void testMethodInvocation() throws Throwable {
        List<MethodInterceptor> interceptors = new ArrayList<>();
        interceptors.add(beforeAdvice);
        interceptors.add(afterAdvice);

        ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(new PetStoreService(), PetStoreService.class.getMethod("placeOrder"), new Object[0], interceptors);
        methodInvocation.proceed();
        List<String> msgs = MessageTracker.get();
        assertTrue(msgs.size() == 3);
        assertEquals(msgs.get(0), "tx start");
        assertEquals(msgs.get(1), "place order");
        assertEquals(msgs.get(2), "tx commit");
    }

    @Test
    public void testMethodInvocation2() throws Throwable {
        List<MethodInterceptor> interceptors = new ArrayList<>();
        interceptors.add(afterAdvice);
        interceptors.add(beforeAdvice);

        ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(new PetStoreService(), PetStoreService.class.getMethod("placeOrder"), new Object[0], interceptors);
        methodInvocation.proceed();
        List<String> msgs = MessageTracker.get();
        assertTrue(msgs.size() == 3);
        assertEquals(msgs.get(0), "tx start");
        assertEquals(msgs.get(1), "place order");
        assertEquals(msgs.get(2), "tx commit");
    }

    @Test
    public void testMethodInvocationAfterThrowing() throws Throwable {
        List<MethodInterceptor> interceptors = new ArrayList<>();
        interceptors.add(afterAdvice);
        interceptors.add(beforeAdvice);
        interceptors.add(afterThrowingAdvice);

        ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(new PetStoreService(), PetStoreService.class.getMethod("placeOrderThrowing"), new Object[0], interceptors);
        try {
            methodInvocation.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        List<String> msgs = MessageTracker.get();
        assertTrue(msgs.size() == 2);
        assertEquals(msgs.get(0), "tx start");
        assertEquals(msgs.get(1), "tx rollback");
    }
}
