package org.litespring.test.v4;

import org.junit.Test;
import org.litespring.beans.factory.annotation.AutowireFieldElement;
import org.litespring.beans.factory.annotation.AutowiredAnnotationProcessor;
import org.litespring.beans.factory.annotation.InjectionElement;
import org.litespring.beans.factory.annotation.InjectionMetadata;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.service.v4.PetStoreService;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class AutowiredAnnotationProcessorTest {

    @Test
    public void testGetInjectionMetadata() {
        AutowiredAnnotationProcessor processor = new AutowiredAnnotationProcessor();
        DefaultBeanFactory factory = new DefaultBeanFactory();
        processor.setBeanFactory(factory);

        InjectionMetadata injectionMetadata = processor.buildAutowiringMetadata(PetStoreService.class);
        List<InjectionElement> injectionElements = injectionMetadata.getInjectionElements();
        assertTrue(injectionElements.size() == 2);

        assertFieldExists(injectionElements, "accountDao");
        assertFieldExists(injectionElements, "itemDao");
    }

    private void assertFieldExists(List<InjectionElement> injectionElements, String fieldName) {
        for (InjectionElement ele : injectionElements) {
            AutowireFieldElement element = (AutowireFieldElement) ele;
            if (fieldName.equals(element.getField().getName())) {
                return;
            }
        }
        fail("找不到属性");
    }
}
