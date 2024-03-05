package org.apache.dolphinscheduler.server.master.dag;

import org.apache.dolphinscheduler.server.master.events.MemoryEventRepository;

import org.mockito.Mockito;

class TestWorkflowExecutionRunnableFactory {

    public static WorkflowExecutionRunnable createEmptyWorkflowExecuteRunnable() {
        WorkflowExecutionContext workflowExecutionContext =
                TestWorkflowExecutionContextFactory.createEmptyWorkflowExecutionContext();
        return WorkflowExecutionRunnable.builder()
                .workflowExecutionContext(workflowExecutionContext)
                .dagEngine(Mockito.mock(IDAGEngine.class))
                .eventRepository(new MemoryEventRepository())
                .build();
    }
}
