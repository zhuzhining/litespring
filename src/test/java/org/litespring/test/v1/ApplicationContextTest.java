package org.litespring.test.v1;

import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.context.support.FileSystemXmlApplicationContext;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.assertNotNull;

public class ApplicationContextTest {

    @Test
    public void  testGetBean() {
        ApplicationContext context = new ClassPathXmlApplicationContext("petstore-v1.xml");
        PetStoreService petStore = (PetStoreService)context.getBean("petStore");

        assertNotNull(petStore);
    }

    @Test
    public void  testGetBeanFormFileSystemApplicationContext() {
        ApplicationContext context = new FileSystemXmlApplicationContext("src\\test\\resources\\petstore-v1.xml");
        PetStoreService petStore = (PetStoreService)context.getBean("petStore");

        assertNotNull(petStore);
    }
}
