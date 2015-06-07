package com.mediatek.FMRadio.tests;

import junit.framework.TestSuite;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;
import com.mediatek.android.performance.util.ServiceBindHelper;
public class FMRadioPerformanceRunner extends InstrumentationTestRunner {
	@Override
	public TestSuite getAllTests() {
		ServiceBindHelper.setModuleName("FMRadio");
		InstrumentationTestSuite suite = new InstrumentationTestSuite(this);
		suite.addTestSuite(FMRadioTestCase1.class);
		return suite;
	}
	
	@Override
	public ClassLoader getLoader() {
		return FMRadioPerformanceRunner.class.getClassLoader();
	}
}
