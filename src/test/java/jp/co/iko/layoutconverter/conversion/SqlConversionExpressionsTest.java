package jp.co.iko.layoutconverter.conversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SqlConversionExpressionsTest {

    @Test
    void createsCommonSqlExpressions() {
        assertEquals("S1.COL_A", SqlConversionExpressions.copy("S1.COL_A"));
        assertEquals("S1.YY || LPAD(S1.MM, 2, '0')", SqlConversionExpressions.concat(
                "S1.YY",
                SqlConversionExpressions.zeroPad("S1.MM", 2)
        ));
        assertEquals("'A''B'", SqlConversionExpressions.fixedString("A'B"));
        assertEquals("TO_NUMBER(S1.AMOUNT)", SqlConversionExpressions.toNumber("S1.AMOUNT"));
        assertEquals("NVL(S1.AMOUNT, 0)", SqlConversionExpressions.nvl("S1.AMOUNT", "0"));
    }

    @Test
    void copyRejectsUnsafeIdentifierPath() {
        assertThrows(IllegalArgumentException.class, () -> SqlConversionExpressions.copy("S1.COL;DROP"));
    }
}

