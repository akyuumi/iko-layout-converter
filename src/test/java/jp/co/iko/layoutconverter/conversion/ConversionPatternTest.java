package jp.co.iko.layoutconverter.conversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ConversionPatternTest {

    @Test
    void fromIdReturnsPattern() {
        assertEquals(ConversionPattern.CV001, ConversionPattern.fromId("CV001"));
        assertEquals(ConversionResponsibility.SQL, ConversionPattern.fromId("CV009").responsibility());
    }

    @Test
    void fromIdRejectsUnknownPattern() {
        assertThrows(IllegalArgumentException.class, () -> ConversionPattern.fromId("CV999"));
    }
}

