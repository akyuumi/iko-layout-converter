package jp.co.iko.layoutconverter.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SchemaNamesTest {

    @Test
    void qualifyValidatesAndBuildsQualifiedName() {
        assertEquals("TARGET_SCHEMA.SAMPLE_CUSTOMER", SchemaNames.qualify("TARGET_SCHEMA", "SAMPLE_CUSTOMER"));
        assertEquals("SAMPLE_CUSTOMER", SchemaNames.qualify(null, "SAMPLE_CUSTOMER"));
    }

    @Test
    void qualifyRejectsUnsafeIdentifier() {
        assertThrows(IllegalArgumentException.class, () -> SchemaNames.qualify("TARGET_SCHEMA", "SAMPLE;DROP"));
    }
}
