package org.litespring.test.v2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
     ApplicationContextTestV2.class,
     BeanDefinitionTest.class,
     BeanDefinitionValueResolverTest.class,
     CustomBooleanEditorTest.class,
     CustomNumberEditorTest.class,
     SimpleTypeConverterTest.class
})
public class V2AllTests {
}
