package com.mediatek.FMRadio.tests;

import junit.framework.TestSuite;

import com.mediatek.FMRadio.tests.FMRadioFunctionTest;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

public class FMRadioFunctionRunner extends InstrumentationTestRunner {
    @Override
    public TestSuite getAllTests() {
        InstrumentationTestSuite suite =new InstrumentationTestSuite(this);
        suite.addTestSuite(FMRadioFunctionTest.class);
        return suite;
    }

    @Override
    public ClassLoader getLoader() {
        return FMRadioFunctionRunner.class.getClassLoader();
    }

}
