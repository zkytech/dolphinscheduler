package org.apache.dolphinscheduler.server.master.dag;

import org.apache.dolphinscheduler.server.master.events.IEvent;

import org.opentest4j.AssertionFailedError;

class WorkflowExecutionRunnableAssertions {

    private final IWorkflowExecutionRunnable workflowExecutionRunnable;

    private WorkflowExecutionRunnableAssertions(IWorkflowExecutionRunnable workflowExecutionRunnable) {
        this.workflowExecutionRunnable = workflowExecutionRunnable;
    }

    public static WorkflowExecutionRunnableAssertions workflowExecutionRunnable(IWorkflowExecutionRunnable workflowExecutionRunnable) {
        return new WorkflowExecutionRunnableAssertions(workflowExecutionRunnable);
    }

    public void existEvent(IEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        boolean exist = workflowExecutionRunnable.getEventRepository().getAllEvent()
                .stream()
                .anyMatch(event1 -> event1.equals(event1));
        if (!exist) {
            throw new AssertionFailedError("The workflowExecuteRunnable doesn't exist event: " + event);
        }
    }

}
