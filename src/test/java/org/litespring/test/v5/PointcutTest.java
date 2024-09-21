package org.litespring.test.v5;

import org.junit.Test;
import org.litespring.aop.MethodMatcher;
import org.litespring.aop.aspectj.AspectJExpressionPointcut;
import org.litespring.service.v5.PetStoreService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PointcutTest {
    @Test
    public void testPointcut() throws Exception {
        String expression = "execution(* org.litespring.service.v5.*.placeOrder(..))";

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        MethodMatcher methodMatcher = pointcut.getMethodMatcher();

        {
            Class<PetStoreService> cls = PetStoreService.class;
            assertTrue(methodMatcher.matches(cls.getMethod("placeOrder")));
            assertFalse(methodMatcher.matches(cls.getMethod("getAccountDao")));
        }

        {
            Class<org.litespring.service.v4.PetStoreService> cls =  org.litespring.service.v4.PetStoreService.class;
            assertFalse(methodMatcher.matches(cls.getMethod("getAccountDao")));
        }
    }
}
