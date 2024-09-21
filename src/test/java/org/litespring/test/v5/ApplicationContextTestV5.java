package org.litespring.test.v5;

import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.service.v5.PetStoreService;
import org.litespring.util.MessageTracker;

import static org.junit.Assert.*;

public class ApplicationContextTestV5 {

    @Test
    public void testPlaceOrder() {
        ApplicationContext context = new ClassPathXmlApplicationContext("petstore-v5.xml");
        PetStoreService petStore = (PetStoreService) context.getBean("petStore");

        assertNotNull(petStore.getItemDao());
        assertNotNull(petStore.getAccountDao());

        petStore.placeOrder();

        assertEquals(MessageTracker.get().size(), 3);
    }
}
