package org.litespring.core.type.classreading;

import org.litespring.core.io.Resource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.ClassMetadata;
import org.springframework.asm.ClassReader;

import java.io.IOException;

public class SimpleMetadataReader implements MetadataReader {

    private ClassMetadata classMetadata;

    private AnnotationMetadata annotationMetadata;

    private Resource resource;

    public SimpleMetadataReader(Resource resource) {
        this.resource = resource;
        try {
            ClassReader reader = new ClassReader(resource.getInputStream());
            AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
            reader.accept(visitor, ClassReader.SKIP_DEBUG);
            this.annotationMetadata = visitor;
            this.classMetadata = visitor;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }

    @Override
    public Resource getResource() {
        return this.resource;
    }

    @Override
    public ClassMetadata getClassMetadata() {
        return this.classMetadata;
    }
}
