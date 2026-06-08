package jp.co.iko.layoutconverter.task.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jp.co.iko.layoutconverter.task.MigrationTaskContext;
import jp.co.iko.layoutconverter.task.MigrationTaskResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SampleCustomerMigrationTaskTest {

    @Mock
    private SampleCustomerMapper mapper;

    @InjectMocks
    private SampleCustomerMigrationTask task;

    @Test
    void executeCountsSourceAndMergesTarget() {
        MigrationTaskContext context = new MigrationTaskContext(
                "RUN001",
                "SOURCE_SCHEMA",
                "TARGET_SCHEMA",
                10000
        );
        when(mapper.countSource("SOURCE_SCHEMA.SAMPLE_CUSTOMER_001")).thenReturn(10L);
        when(mapper.mergeCustomers(
                "SOURCE_SCHEMA.SAMPLE_CUSTOMER_001",
                "SOURCE_SCHEMA.SAMPLE_CUSTOMER_002",
                "TARGET_SCHEMA.SAMPLE_CUSTOMER"
        )).thenReturn(8);

        MigrationTaskResult result = task.execute(context);

        assertEquals("SAMPLE_CUSTOMER", task.taskId());
        assertEquals(10L, result.readCount());
        assertEquals(8L, result.convertedCount());
        assertEquals(0L, result.errorCount());
        verify(mapper).countSource("SOURCE_SCHEMA.SAMPLE_CUSTOMER_001");
        verify(mapper).mergeCustomers(
                "SOURCE_SCHEMA.SAMPLE_CUSTOMER_001",
                "SOURCE_SCHEMA.SAMPLE_CUSTOMER_002",
                "TARGET_SCHEMA.SAMPLE_CUSTOMER"
        );
    }
}
