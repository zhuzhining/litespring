package org.litespring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.context.annotation.ClassPathBeanDefinitionScanner;
import org.litespring.core.io.Resource;
import org.litespring.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {

    public static final String ID_ATTRIBUTE = "id";

    public static final String CLASS_ATTRIBUTE = "class";

    public static final String SCOPE_ATTRIBUTE = "scope";

    public static final String PROPERTY_ELEMENT = "property";

    public static final String CONSTRUCTOR_ELEMENT = "Constructor-arg";

    public static final String NAME_ATTRIBUTE = "name";

    public static final String TYPE_ATTRIBUTE = "type";

    public static final String REF_ATTRIBUTE = "ref";

    public static final String VALUE_ATTRIBUTE = "value";

    public static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
    public static final String DEFAULT_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
    public static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context";

    private BeanDefinitionRegistry registry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void loadBeanDefinition(Resource resource) {
        InputStream is = null;
        try {
            is = resource.getInputStream();
            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);
            Element root = doc.getRootElement();
            Iterator iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element element = (Element) iterator.next();
                String namespaceURI = element.getNamespaceURI();
                if(isDefaultNamespaceUri(namespaceURI)) {
                    parseDefaultElement(element);
                } else if(isContextNamespaceUri(namespaceURI)) {
                    parseComponentElement(element);
                }

            }
        } catch (DocumentException | IOException e) {
            throw new BeanDefinitionStoreException("获取对应xml文件失败", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void parseComponentElement(Element element) {
        String basePackages = element.attributeValue(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this.registry);
        scanner.doScan(basePackages);
    }

    private void parseDefaultElement(Element element) {
        String id = element.attributeValue(ID_ATTRIBUTE);
        String className = element.attributeValue(CLASS_ATTRIBUTE);
        BeanDefinition bd = new GenericBeanDefinition(id, className);
        String scope = element.attributeValue(SCOPE_ATTRIBUTE);
        if (scope != null) {
            bd.setScope(scope);
        }
        parserPropertyValues(element, bd);
        parserConstructorArgElements(element, bd);
        this.registry.registryBeanDefinition(id, bd);
    }

    private boolean isDefaultNamespaceUri(String namespaceURI) {
        return DEFAULT_NAMESPACE_URI.equals(namespaceURI);
    }


    private boolean isContextNamespaceUri(String namespaceURI) {
        return CONTEXT_NAMESPACE_URI.equals(namespaceURI);
    }

    private void parserConstructorArgElements(Element parent, BeanDefinition bd) {
        Iterator iterator = parent.elementIterator(CONSTRUCTOR_ELEMENT);
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            parserConstructorArgElement(bd, element);
        }
    }

    private void parserConstructorArgElement(BeanDefinition bd, Element element) {
        String name = element.attributeValue(NAME_ATTRIBUTE);
        String type = element.attributeValue(TYPE_ATTRIBUTE);
        Object value = this.parserPropertyValue(element);
        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value);
        if (StringUtils.hasText(name)) {
            valueHolder.setName(name);
        }
        if (StringUtils.hasText(type)) {
            valueHolder.setName(type);
        }
        bd.getConstructorArgument().addArgumentValue(valueHolder);
    }

    private void parserPropertyValues(Element parent, BeanDefinition bd) {
        Iterator iterator = parent.elementIterator(PROPERTY_ELEMENT);
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            String name = element.attributeValue(NAME_ATTRIBUTE);
            if (name == null) {
                return;
            }
            Object val = this.parserPropertyValue(element);
            PropertyValue pv = new PropertyValue(name, val);
            bd.getPropertyValues().add(pv);
        }

    }

    private Object parserPropertyValue(Element element) {
        String ref = element.attributeValue(REF_ATTRIBUTE);
        String value = element.attributeValue(VALUE_ATTRIBUTE);
        if (ref != null) {
            return new RuntimeBeanReference(ref);
        } else if (value != null) {
            return new TypedStringValue(value);
        } else {
            throw new RuntimeException("获取不到Property标签的ref或者value属性");
        }
    }
}
