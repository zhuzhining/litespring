package org.litespring.context.annotation;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.BeanNameGenerator;
import org.litespring.core.io.Resource;
import org.litespring.core.io.support.PackageResourceLoader;
import org.litespring.core.type.classreading.MetadataReader;
import org.litespring.core.type.classreading.SimpleMetadataReader;
import org.litespring.stereotype.Component;
import org.litespring.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ClassPathBeanDefinitionScanner {

    private BeanDefinitionRegistry registry;

    private PackageResourceLoader loader = new PackageResourceLoader();

    private BeanNameGenerator generator = new AnnotationBeanNameGenerator();

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String packageToScan) {
        String[] basePackages = StringUtils.tokenizeToStringArray(packageToScan, ",");
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                this.registry.registerBeanDefinition(candidate.getId(), candidate);
            }
        }
    }

    private Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> result = new HashSet<>();
        try {
            Resource[] resources = loader.getResources(basePackage);
            for (Resource resource : resources) {
                MetadataReader reader = new SimpleMetadataReader(resource);
                if (reader.getAnnotationMetadata().hasAnnotation(Component.class.getName())) {
                    ScannerGenericBeanDefinition bd = new ScannerGenericBeanDefinition(reader.getAnnotationMetadata());
                    String beanName = generator.generateBeanName(bd, this.registry);
                    bd.setId(beanName);
                    result.add(bd);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
