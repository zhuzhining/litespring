package org.litespring.test.v3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationContextTestV3.class,
        BeanDefinitionTest.class,
        ConstructorResolverTest.class
})
public class V3AllTest {

}
