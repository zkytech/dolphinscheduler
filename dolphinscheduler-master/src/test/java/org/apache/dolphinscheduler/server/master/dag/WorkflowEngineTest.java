package org.apache.dolphinscheduler.server.master.dag;

import static org.apache.dolphinscheduler.server.master.dag.WorkflowExecutionRunnableAssertions.workflowExecutionRunnable;
import static org.apache.dolphinscheduler.server.master.dag.WorkflowExecutionRunnableRepositoryAssertions.workflowExecutionRunnableRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.dolphinscheduler.server.master.events.WorkflowOperationEvent;
import org.apache.dolphinscheduler.server.master.events.WorkflowOperationType;
import org.apache.dolphinscheduler.server.master.exception.WorkflowExecuteRunnableNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkflowEngineTest {

    private WorkflowEngine workflowEngine;

    private WorkflowExecuteRunnableRepository workflowExecuteRunnableRepository;

    @BeforeEach
    public void before() {
        workflowExecuteRunnableRepository = new WorkflowExecuteRunnableRepository();
        workflowEngine = new WorkflowEngine(workflowExecuteRunnableRepository);
    }

    @Test
    void triggerWorkflow() {
        IWorkflowExecutionRunnable emptyWorkflowExecuteRunnable =
                TestWorkflowExecutionRunnableFactory.createEmptyWorkflowExecuteRunnable();
        Integer workflowInstanceId =
                emptyWorkflowExecuteRunnable.getWorkflowExecutionContext().getWorkflowInstance().getId();

        workflowEngine.triggerWorkflow(emptyWorkflowExecuteRunnable);
        workflowExecutionRunnable(emptyWorkflowExecuteRunnable)
                .existEvent(WorkflowOperationEvent.of(workflowInstanceId, WorkflowOperationType.TRIGGER));
    }

    @Test
    void pauseWorkflow_WorkflowNotExist() {
        WorkflowExecuteRunnableNotFoundException exception =
                assertThrows(WorkflowExecuteRunnableNotFoundException.class, () -> workflowEngine.pauseWorkflow(1));
        assertEquals("WorkflowExecuteRunnable not found: [id=1]", exception.getMessage());
    }

    @Test
    void pauseWorkflow_WorkflowExist() {
        IWorkflowExecutionRunnable emptyWorkflowExecuteRunnable =
                TestWorkflowExecutionRunnableFactory.createEmptyWorkflowExecuteRunnable();
        Integer workflowInstanceId =
                emptyWorkflowExecuteRunnable.getWorkflowExecutionContext().getWorkflowInstance().getId();
        workflowExecuteRunnableRepository.storeWorkflowExecutionRunnable(emptyWorkflowExecuteRunnable);

        workflowEngine.pauseWorkflow(workflowInstanceId);
        workflowExecutionRunnable(emptyWorkflowExecuteRunnable)
                .existEvent(WorkflowOperationEvent.of(workflowInstanceId, WorkflowOperationType.PAUSE));
    }

    @Test
    void killWorkflow_WorkflowNotExist() {
        WorkflowExecuteRunnableNotFoundException exception =
                assertThrows(WorkflowExecuteRunnableNotFoundException.class,
                        () -> workflowEngine.killWorkflow(1));
        assertEquals("WorkflowExecuteRunnable not found: [id=1]", exception.getMessage());
    }

    @Test
    void killWorkflow_WorkflowExist() {
        IWorkflowExecutionRunnable emptyWorkflowExecuteRunnable =
                TestWorkflowExecutionRunnableFactory.createEmptyWorkflowExecuteRunnable();
        Integer workflowInstanceId =
                emptyWorkflowExecuteRunnable.getWorkflowExecutionContext().getWorkflowInstance().getId();
        workflowExecuteRunnableRepository.storeWorkflowExecutionRunnable(emptyWorkflowExecuteRunnable);

        workflowEngine.killWorkflow(workflowInstanceId);
        workflowExecutionRunnable(emptyWorkflowExecuteRunnable)
                .existEvent(WorkflowOperationEvent.of(workflowInstanceId, WorkflowOperationType.KILL));
    }

    @Test
    void finalizeWorkflow_WorkflowNotExist() {
        workflowEngine.finalizeWorkflow(1);
    }

    @Test
    void finalizeWorkflow_WorkflowExist() {
        IWorkflowExecutionRunnable emptyWorkflowExecuteRunnable =
                TestWorkflowExecutionRunnableFactory.createEmptyWorkflowExecuteRunnable();
        Integer workflowInstanceId =
                emptyWorkflowExecuteRunnable.getWorkflowExecutionContext().getWorkflowInstance().getId();
        workflowExecuteRunnableRepository.storeWorkflowExecutionRunnable(emptyWorkflowExecuteRunnable);
        workflowExecutionRunnableRepository(workflowExecuteRunnableRepository)
                .existWorkflowExecutionRunnable(workflowInstanceId);

        workflowEngine.finalizeWorkflow(workflowInstanceId);
        workflowExecutionRunnableRepository(workflowExecuteRunnableRepository)
                .notExistWorkflowExecutionRunnable(workflowInstanceId);
    }

}
