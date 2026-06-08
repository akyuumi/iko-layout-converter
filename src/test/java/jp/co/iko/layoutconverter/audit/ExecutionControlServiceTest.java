package jp.co.iko.layoutconverter.audit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import jp.co.iko.layoutconverter.config.MigrationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExecutionControlServiceTest {

    private static final String TABLE = "TARGET_SCHEMA.EXECUTION_CONTROL";

    @Mock
    private ExecutionControlMapper mapper;

    private ExecutionControlService service;

    @BeforeEach
    void setUp() {
        MigrationProperties properties = new MigrationProperties();
        properties.setTargetSchema("TARGET_SCHEMA");
        service = new ExecutionControlService(mapper, properties);
    }

    @Test
    void prepareInsertsRunningWhenNoPreviousExecutionExists() {
        when(mapper.findLatest(TABLE, "TASK001")).thenReturn(null);

        ExecutionDecision decision = service.prepare("RUN001", "TASK001");

        assertTrue(decision.executable());
        verify(mapper).findLatest(TABLE, "TASK001");
        verify(mapper).insertRunning(TABLE, "RUN001", "TASK001");
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void prepareSkipsWhenPreviousExecutionSucceeded() {
        ExecutionControlRecord latest = new ExecutionControlRecord(
                "RUN000",
                "TASK001",
                ExecutionStatus.SUCCESS,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(mapper.findLatest(TABLE, "TASK001")).thenReturn(latest);

        ExecutionDecision decision = service.prepare("RUN001", "TASK001");

        assertFalse(decision.executable());
        verify(mapper).findLatest(TABLE, "TASK001");
        verify(mapper).insertRunning(TABLE, "RUN001", "TASK001");
        verify(mapper).updateStatus(TABLE, "RUN001", "TASK001", ExecutionStatus.SKIPPED);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void prepareRetriesWhenPreviousExecutionWasRunning() {
        ExecutionControlRecord latest = new ExecutionControlRecord(
                "RUN000",
                "TASK001",
                ExecutionStatus.RUNNING,
                LocalDateTime.now(),
                null
        );
        when(mapper.findLatest(TABLE, "TASK001")).thenReturn(latest);

        ExecutionDecision decision = service.prepare("RUN001", "TASK001");

        assertTrue(decision.executable());
        verify(mapper).findLatest(TABLE, "TASK001");
        verify(mapper).insertRunning(TABLE, "RUN001", "TASK001");
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void markErrorUpdatesStatus() {
        when(mapper.updateStatus(TABLE, "RUN001", "TASK001", ExecutionStatus.ERROR)).thenReturn(1);

        service.markError("RUN001", "TASK001");

        verify(mapper).updateStatus(TABLE, "RUN001", "TASK001", ExecutionStatus.ERROR);
        verifyNoMoreInteractions(mapper);
    }
}
