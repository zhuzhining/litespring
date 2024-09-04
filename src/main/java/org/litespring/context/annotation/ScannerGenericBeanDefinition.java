package org.litespring.context.annotation;

import org.litespring.beans.factory.annotation.AnnotatedBeanDefinition;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.core.type.AnnotationMetadata;

public class ScannerGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition {

    private AnnotationMetadata annotationMetadata;

    public ScannerGenericBeanDefinition(AnnotationMetadata annotationMetadata) {
        super();
        this.annotationMetadata = annotationMetadata;
        this.setBeanClassName(this.annotationMetadata.getClassName());
    }

    @Override
    public AnnotationMetadata getMetadata() {
        return this.annotationMetadata;
    }
}
