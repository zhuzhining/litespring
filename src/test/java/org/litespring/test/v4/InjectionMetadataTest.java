package org.litespring.test.v4;

import org.junit.Test;
import org.litespring.beans.factory.annotation.AutowireFieldElement;
import org.litespring.beans.factory.annotation.InjectionElement;
import org.litespring.beans.factory.annotation.InjectionMetadata;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.dao.v4.AccountDao;
import org.litespring.dao.v4.ItemDao;
import org.litespring.service.v4.PetStoreService;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InjectionMetadataTest {
    @Test
    public void testInjection() throws Exception {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinition(new ClassPathResource("petStore-v4.xml"));

        Class<PetStoreService> targetClass = PetStoreService.class;
        List<InjectionElement> injectionElements = new LinkedList<>();
        {
            Field field = targetClass.getDeclaredField("accountDao");
            AutowireFieldElement autowireFieldElement = new AutowireFieldElement(field, true, factory);
            injectionElements.add(autowireFieldElement);
        }
        {
            Field field = targetClass.getDeclaredField("itemDao");
            AutowireFieldElement autowireFieldElement = new AutowireFieldElement(field, true, factory);
            injectionElements.add(autowireFieldElement);
        }

        InjectionMetadata injectionMetadata = new InjectionMetadata(targetClass, injectionElements);
        PetStoreService petStoreService = new PetStoreService();
        injectionMetadata.inject(petStoreService);

        assertNotNull(petStoreService.getAccountDao());
        assertNotNull(petStoreService.getItemDao());
        assertTrue(petStoreService.getAccountDao() instanceof AccountDao);
        assertTrue(petStoreService.getItemDao() instanceof ItemDao);
    }
}
