/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 5:03:22 PM
 * Time to generate: 00:20.462 seconds
 *
 */

package com.huawei.unibi.molap.engine.mondrian.extensions;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import mondrian.spi.Dialect;

public class MolapDummyDialectAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return MolapDummyDialect.class;
    }
    
    public void testConstructor() throws Throwable {
        MolapDummyDialect molapDummyDialect = new MolapDummyDialect();
        assertEquals("molapDummyDialect.getDatabaseProduct()", Dialect.DatabaseProduct.UNKNOWN, molapDummyDialect.getDatabaseProduct());
    }
    
    public void testAllowsAs() throws Throwable {
        boolean result = new MolapDummyDialect().allowsAs();
        assertFalse("result", result);
    }
    
    public void testAllowsCompoundCountDistinct() throws Throwable {
        boolean result = new MolapDummyDialect().allowsCompoundCountDistinct();
        assertFalse("result", result);
    }
    
    public void testAllowsCountDistinct() throws Throwable {
        boolean result = new MolapDummyDialect().allowsCountDistinct();
        assertFalse("result", result);
    }
    
    public void testAllowsDdl() throws Throwable {
        boolean result = new MolapDummyDialect().allowsDdl();
        assertFalse("result", result);
    }
    
    public void testAllowsDialectSharing() throws Throwable {
        boolean result = new MolapDummyDialect().allowsDialectSharing();
        assertFalse("result", result);
    }
    
    public void testAllowsFromQuery() throws Throwable {
        boolean result = new MolapDummyDialect().allowsFromQuery();
        assertFalse("result", result);
    }
    
    public void testAllowsJoinOn() throws Throwable {
        boolean result = new MolapDummyDialect().allowsJoinOn();
        assertFalse("result", result);
    }
    
    public void testAllowsMultipleCountDistinct() throws Throwable {
        boolean result = new MolapDummyDialect().allowsMultipleCountDistinct();
        assertFalse("result", result);
    }
    
    public void testAllowsMultipleDistinctSqlMeasures() throws Throwable {
        boolean result = new MolapDummyDialect().allowsMultipleDistinctSqlMeasures();
        assertTrue("result", result);
    }
    
    public void testAllowsOrderByAlias() throws Throwable {
        boolean result = new MolapDummyDialect().allowsOrderByAlias();
        assertFalse("result", result);
    }
    
    public void testAllowsRegularExpressionInWhereClause() throws Throwable {
        boolean result = new MolapDummyDialect().allowsRegularExpressionInWhereClause();
        assertFalse("result", result);
    }
    
    public void testAllowsSelectNotInGroupBy() throws Throwable {
        boolean result = new MolapDummyDialect().allowsSelectNotInGroupBy();
        assertFalse("result", result);
    }
    
    public void testAppendHintsAfterFromClause() throws Throwable {
        MolapDummyDialect molapDummyDialect = new MolapDummyDialect();
        Map hints = new HashMap(100, 100.0F);
        molapDummyDialect.appendHintsAfterFromClause(new StringBuilder(100), hints);
        assertEquals("molapDummyDialect.getDatabaseProduct()", Dialect.DatabaseProduct.UNKNOWN, molapDummyDialect.getDatabaseProduct());
    }
    
    public void testCaseWhenElse() throws Throwable {
        String result = new MolapDummyDialect().caseWhenElse("testMolapDummyDialectCond", "testMolapDummyDialectThenExpr", "testMolapDummyDialectElseExpr");
        assertNull("result", result);
    }
    
    public void testGenerateCountExpression() throws Throwable {
        String result = new MolapDummyDialect().generateCountExpression("testMolapDummyDialectExp");
        assertNull("result", result);
    }
    
    public void testGenerateInline() throws Throwable {
        String result = new MolapDummyDialect().generateInline(new ArrayList(100), new ArrayList(1000), new ArrayList(0));
        assertNull("result", result);
    }
    
    public void testGenerateOrderItem() throws Throwable {
        String result = new MolapDummyDialect().generateOrderItem("testMolapDummyDialectExpr", true, false, true);
        assertNull("result", result);
    }
    
    public void testGenerateRegularExpression() throws Throwable {
        String result = new MolapDummyDialect().generateRegularExpression("testMolapDummyDialectSource", "testMolapDummyDialectJavaRegExp");
        assertNull("result", result);
    }
    
    public void testGetDatabaseProduct() throws Throwable {
        Dialect.DatabaseProduct result = new MolapDummyDialect().getDatabaseProduct();
        assertEquals("result", Dialect.DatabaseProduct.UNKNOWN, result);
    }
    
    public void testGetMaxColumnNameLength() throws Throwable {
        int result = new MolapDummyDialect().getMaxColumnNameLength();
        assertEquals("result", 0, result);
    }
    
    public void testGetQuoteIdentifierString() throws Throwable {
        String result = new MolapDummyDialect().getQuoteIdentifierString();
        assertNull("result", result);
    }
    
    public void testNeedsExponent() throws Throwable {
        boolean result = new MolapDummyDialect().needsExponent("", "testMolapDummyDialectValueString");
        assertFalse("result", result);
    }
    
    public void testQuote() throws Throwable {
        MolapDummyDialect molapDummyDialect = new MolapDummyDialect();
        StringBuilder buf = new StringBuilder("testMolapDummyDialectParam1");
        Dialect.Datatype datatype = (Dialect.Datatype) Mockingbird.getProxyObject(Dialect.Datatype.class);
        Mockingbird.enterTestMode(MolapDummyDialect.class);
        molapDummyDialect.quote(buf, "testString", datatype);
        assertEquals("molapDummyDialect.getDatabaseProduct()", Dialect.DatabaseProduct.UNKNOWN, molapDummyDialect.getDatabaseProduct());
    }
    
    public void testQuoteBooleanLiteral() throws Throwable {
        MolapDummyDialect molapDummyDialect = new MolapDummyDialect();
        molapDummyDialect.quoteBooleanLiteral(new StringBuilder(100), "testMolapDummyDialectValue");
        assertEquals("molapDummyDialect.getDatabaseProduct()", Dialect.DatabaseProduct.UNKNOWN, molapDummyDialect.getDatabaseProduct());
    }
    
    public void testQuoteDateLiteral() throws Throwable {
        MolapDummyDialect molapDummyDialect = new MolapDummyDialect();
        molapDummyDialect.quoteDateLiteral(new StringBuilder(), "testMolapDummyDialectValue");
        assertEquals("molapDummyDialect.getDatabaseProduct()", Dialect.DatabaseProduct.UNKNOWN, molapDummyDialect.getDatabaseProduct());
    }
    
    public void testQuoteIdentifier() throws Throwable {
        String result = new MolapDummyDialect().quoteIdentifier("testMolapDummyDialectVal");
        assertNull("result", result);
    }
    
    public void testQuoteIdentifier1() throws Throwable {
        String result = new MolapDummyDialect().quoteIdentifier("testMolapDummyDialectQual", "testMolapDummyDialectName");
        assertNull("result", result);
    }
    
    public void testQuoteIdentifier2() throws Throwable {
        MolapDummyDialect molapDummyDialect = new MolapDummyDialect();
        String[] names = new String[0];
        molapDummyDialect.quoteIdentifier(new StringBuilder(), names);
        assertEquals("molapDummyDialect.getDatabaseProduct()", Dialect.DatabaseProduct.UNKNOWN, molapDummyDialect.getDatabaseProduct());
    }
    
    public void testQuoteIdentifier3() throws Throwable {
        MolapDummyDialect molapDummyDialect = new MolapDummyDialect();
        molapDummyDialect.quoteIdentifier("testMolapDummyDialectVal", new StringBuilder());
        assertEquals("molapDummyDialect.getDatabaseProduct()", Dialect.DatabaseProduct.UNKNOWN, molapDummyDialect.getDatabaseProduct());
    }
    
    public void testQuoteNumericLiteral() throws Throwable {
        MolapDummyDialect molapDummyDialect = new MolapDummyDialect();
        molapDummyDialect.quoteNumericLiteral(new StringBuilder(), "testMolapDummyDialectValue");
        assertEquals("molapDummyDialect.getDatabaseProduct()", Dialect.DatabaseProduct.UNKNOWN, molapDummyDialect.getDatabaseProduct());
    }
    
    public void testQuoteStringLiteral() throws Throwable {
        MolapDummyDialect molapDummyDialect = new MolapDummyDialect();
        molapDummyDialect.quoteStringLiteral(new StringBuilder(100), "testMolapDummyDialects");
        assertEquals("molapDummyDialect.getDatabaseProduct()", Dialect.DatabaseProduct.UNKNOWN, molapDummyDialect.getDatabaseProduct());
    }
    
    public void testQuoteTimeLiteral() throws Throwable {
        MolapDummyDialect molapDummyDialect = new MolapDummyDialect();
        molapDummyDialect.quoteTimeLiteral(new StringBuilder(100), "testMolapDummyDialectValue");
        assertEquals("molapDummyDialect.getDatabaseProduct()", Dialect.DatabaseProduct.UNKNOWN, molapDummyDialect.getDatabaseProduct());
    }
    
    public void testQuoteTimestampLiteral() throws Throwable {
        MolapDummyDialect molapDummyDialect = new MolapDummyDialect();
        molapDummyDialect.quoteTimestampLiteral(new StringBuilder(), "testMolapDummyDialectValue");
        assertEquals("molapDummyDialect.getDatabaseProduct()", Dialect.DatabaseProduct.UNKNOWN, molapDummyDialect.getDatabaseProduct());
    }
    
    public void testRequiresAliasForFromQuery() throws Throwable {
        boolean result = new MolapDummyDialect().requiresAliasForFromQuery();
        assertFalse("result", result);
    }
    
    public void testRequiresGroupByAlias() throws Throwable {
        boolean result = new MolapDummyDialect().requiresGroupByAlias();
        assertFalse("result", result);
    }
    
    public void testRequiresHavingAlias() throws Throwable {
        boolean result = new MolapDummyDialect().requiresHavingAlias();
        assertFalse("result", result);
    }
    
    public void testRequiresOrderByAlias() throws Throwable {
        boolean result = new MolapDummyDialect().requiresOrderByAlias();
        assertFalse("result", result);
    }
    
    public void testRequiresUnionOrderByExprToBeInSelectClause() throws Throwable {
        boolean result = new MolapDummyDialect().requiresUnionOrderByExprToBeInSelectClause();
        assertFalse("result", result);
    }
    
    public void testRequiresUnionOrderByOrdinal() throws Throwable {
        boolean result = new MolapDummyDialect().requiresUnionOrderByOrdinal();
        assertFalse("result", result);
    }
    
    public void testSupportsGroupByExpressions() throws Throwable {
        boolean result = new MolapDummyDialect().supportsGroupByExpressions();
        assertFalse("result", result);
    }
    
    public void testSupportsGroupingSets() throws Throwable {
        boolean result = new MolapDummyDialect().supportsGroupingSets();
        assertFalse("result", result);
    }
    
    public void testSupportsMultiValueInExpr() throws Throwable {
        boolean result = new MolapDummyDialect().supportsMultiValueInExpr();
        assertFalse("result", result);
    }
    
    public void testSupportsResultSetConcurrency() throws Throwable {
        boolean result = new MolapDummyDialect().supportsResultSetConcurrency(100, 1000);
        assertFalse("result", result);
    }
    
    public void testSupportsUnlimitedValueList() throws Throwable {
        boolean result = new MolapDummyDialect().supportsUnlimitedValueList();
        assertFalse("result", result);
    }
    
    public void testToUpper() throws Throwable {
        String result = new MolapDummyDialect().toUpper("testMolapDummyDialectExpr");
        assertNull("result", result);
    }
}

