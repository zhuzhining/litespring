package org.litespring.test.v4;

import org.junit.Test;
import org.litespring.core.io.Resource;
import org.litespring.core.io.support.PackageResourceLoader;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class PackageResourceLoaderTest {
    @Test
    public void testGetResource() throws IOException {
        PackageResourceLoader loader = new PackageResourceLoader();
        Resource[] resources = loader.getResources("org.litespring.dao.v4");

        assertTrue(resources.length == 2);
    }
}
