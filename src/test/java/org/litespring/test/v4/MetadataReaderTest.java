package org.litespring.test.v4;

import org.junit.Test;
import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.ClassMetadata;
import org.litespring.core.type.classreading.MetadataReader;
import org.litespring.core.type.classreading.SimpleMetadataReader;
import org.litespring.stereotype.Component;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class MetadataReaderTest {
    @Test
    public void testGetMetadata() {
        ClassPathResource resource = new ClassPathResource("org/litespring/service/v4/PetStoreService.class");
        MetadataReader reader = new SimpleMetadataReader(resource);

        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
        String annotation = Component.class.getName();
        assertTrue(annotationMetadata.hasAnnotation(annotation));

        AnnotationAttributes attributes = annotationMetadata.getAnnotationAttributes(annotation);
        assertEquals("petStore", attributes.get("value"));

        ClassMetadata classMetadata = reader.getClassMetadata();

        assertFalse(classMetadata.isAbstract());
        assertFalse(classMetadata.isFinal());
        assertEquals("org.litespring.service.v4.PetStoreService", classMetadata.getClassName());
    }
}
