package org.litespring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.core.io.Resource;
import org.litespring.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {

    public static final String ID_ATTRIBUTE = "id";

    public static final String CLASS_ATTRIBUTE = "class";

    public static final String SCOPE_ATTRIBUTE = "scope";

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
                String id = element.attributeValue(ID_ATTRIBUTE);
                String className = element.attributeValue(CLASS_ATTRIBUTE);
                BeanDefinition bd = new GenericBeanDefinition(id, className);
                String scope = element.attributeValue(SCOPE_ATTRIBUTE);
                if (scope != null) {
                    bd.setScope(scope);
                }
                this.registry.registryBeanDefinition(id, bd);
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
}
