package org.litespring.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.litespring.test.v1.V1AllTests;
import org.litespring.test.v2.V2AllTests;
import org.litespring.test.v3.V3AllTest;
import org.litespring.test.v4.V4AllTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        V1AllTests.class,
        V2AllTests.class,
        V3AllTest.class,
        V4AllTest.class
})
public class AllTests {
}
