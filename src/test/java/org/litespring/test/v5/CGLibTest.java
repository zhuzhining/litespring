package org.litespring.test.v5;

import org.junit.Test;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

public class CGLibTest {
//    @Test
    public void testCallBack() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PetStoreService.class);
        enhancer.setCallback(new TransactionInterceptor());

        PetStoreService petStore = (PetStoreService) enhancer.create();
        petStore.placeOrder();
        petStore.toString();
    }

    public static class TransactionInterceptor implements MethodInterceptor {
        TransactionManager manager = new TransactionManager();
        @Override
        public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            manager.start();
            Object o = methodProxy.invokeSuper(obj, objects);
            manager.commit();
            return o;
        }
    }

    @Test
    public void testFilter() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PetStoreService.class);
        enhancer.setInterceptDuringConstruction(true);

        Callback[] callbacks = new Callback[]{new TransactionInterceptor(), NoOp.INSTANCE};

        Class<?>[] types = new Class<?>[callbacks.length];
        for (int i = 0; i < callbacks.length; i++) {
            types[i] = callbacks[i].getClass();
        }

        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackTypes(types);
        enhancer.setCallbackFilter(new ProxyCallbackFilter());

        PetStoreService petStore = (PetStoreService) enhancer.create();
        petStore.placeOrder();
        petStore.toString();
    }

    public static class ProxyCallbackFilter implements CallbackFilter {
        @Override
        public int accept(Method method) {
            if (method.getName().startsWith("place")) {
                return 0;
            }
            return 1;
        }
    }

}
