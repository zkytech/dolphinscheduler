package org.apache.dolphinscheduler.server.master.dag;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class WorkflowExecutionRunnableRepositoryAssertions {

    private final WorkflowExecuteRunnableRepository workflowExecuteRunnableRepository;

    private WorkflowExecutionRunnableRepositoryAssertions(WorkflowExecuteRunnableRepository workflowExecuteRunnableRepository) {
        this.workflowExecuteRunnableRepository = workflowExecuteRunnableRepository;
    }

    public static WorkflowExecutionRunnableRepositoryAssertions workflowExecutionRunnableRepository(WorkflowExecuteRunnableRepository workflowExecuteRunnableRepository) {
        return new WorkflowExecutionRunnableRepositoryAssertions(workflowExecuteRunnableRepository);
    }

    public void existWorkflowExecutionRunnable(Integer workflowInstanceId) {
        assertNotNull(workflowExecuteRunnableRepository.getWorkflowExecutionRunnableById(workflowInstanceId));
    }

    public void notExistWorkflowExecutionRunnable(Integer workflowInstanceId) {
        assertNull(workflowExecuteRunnableRepository.getWorkflowExecutionRunnableById(workflowInstanceId));
    }

}
