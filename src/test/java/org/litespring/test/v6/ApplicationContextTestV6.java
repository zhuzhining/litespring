package org.litespring.test.v6;

import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.service.v6.IPetstoreService;
import org.litespring.util.MessageTracker;

import static org.junit.Assert.assertEquals;

public class ApplicationContextTestV6 {

    @Test
    public void testPlaceOrder() {
        ApplicationContext context = new ClassPathXmlApplicationContext("petstore-v6.xml");

        IPetstoreService petStore = (IPetstoreService) context.getBean("petStore");

        petStore.placeOrder();

        assertEquals(MessageTracker.get().size(), 3);
        MessageTracker.clear();
    }
}
