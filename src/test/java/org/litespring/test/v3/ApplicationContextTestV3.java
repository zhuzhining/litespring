package org.litespring.test.v3;

import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.service.v3.PetStoreService;

import static org.junit.Assert.assertNotNull;

public class ApplicationContextTestV3 {

    @Test
    public void testGetBeanProperty() {
        ApplicationContext context = new ClassPathXmlApplicationContext("petstore-v3.xml");
        PetStoreService petStore = (PetStoreService) context.getBean("petStore");

        assertNotNull(petStore.getItemDao());
        assertNotNull(petStore.getAccountDao());
        assertNotNull(petStore.getVersion());
    }
}
