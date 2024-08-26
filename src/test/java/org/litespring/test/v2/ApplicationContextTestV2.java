package org.litespring.test.v2;

import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.dao.v2.AccountDao;
import org.litespring.dao.v2.ItemDao;
import org.litespring.service.v2.PetStoreService;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ApplicationContextTestV2 {

    @Test
    public void testGetBean() {
        ApplicationContext context = new ClassPathXmlApplicationContext("petstore-v2.xml");
        PetStoreService petstore = (PetStoreService) context.getBean("petStore");

        assertNotNull(petstore.getAccountDao());
        assertNotNull(petstore.getItemDao());

        assertTrue(petstore.getAccountDao() instanceof AccountDao);
        assertTrue(petstore.getItemDao() instanceof ItemDao);
        assertTrue("zzn".equals(petstore.getName()));
    }
}
