package com.huawei.datasight.testsuite.sortexpr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite from where all the TestCases for sort expression query support will be triggered
 * @author N00902756
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	AllDataTypesTestCase.class, 
	IntegerDataTypeTestCase.class, 
	StringDataTypeTestCase.class, 
	TimestampDataTypeTestCase.class, 
	NumericDataTypeTestCase.class
})
public class SortExprTestSuite {

}
