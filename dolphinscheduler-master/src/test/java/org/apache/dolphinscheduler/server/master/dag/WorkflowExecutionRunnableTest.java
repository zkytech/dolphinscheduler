package org.apache.dolphinscheduler.server.master.dag;

import static org.apache.dolphinscheduler.server.master.dag.WorkflowExecutionRunnableAssertions.workflowExecutionRunnable;

import org.apache.dolphinscheduler.server.master.events.WorkflowTriggeredEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkflowExecutionRunnableTest {

    private WorkflowExecutionRunnable workflowExecutionRunnable;

    @BeforeEach
    public void before() {
        workflowExecutionRunnable = TestWorkflowExecutionRunnableFactory.createEmptyWorkflowExecuteRunnable();
    }

    @Test
    void start() {
        workflowExecutionRunnable.start();
        workflowExecutionRunnable(workflowExecutionRunnable)
                .existEvent(new WorkflowTriggeredEvent(
                        workflowExecutionRunnable.getWorkflowExecutionContext().getWorkflowInstance().getId()));
    }

    @Test
    void pause() {
    }

    @Test
    void kill() {
    }
}
