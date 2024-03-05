package org.apache.dolphinscheduler.server.master.events;

import org.apache.dolphinscheduler.server.master.dag.IWorkflowExecutionRunnable;
import org.apache.dolphinscheduler.server.master.dag.WorkflowExecuteRunnableRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskSuccessEventOperator implements ITaskEventOperator<TaskSuccessEvent> {

    @Autowired
    private WorkflowExecuteRunnableRepository workflowExecuteRunnableRepository;

    @Override
    public void handleEvent(TaskSuccessEvent event) {
        Integer workflowInstanceId = event.getWorkflowInstanceId();
        Integer taskInstanceId = event.getTaskInstanceId();
        IWorkflowExecutionRunnable workflowExecutionRunnable =
                workflowExecuteRunnableRepository.getWorkflowExecutionRunnableById(workflowInstanceId);
        if (workflowExecutionRunnable == null) {
            log.error("Cannot find the WorkflowExecutionRunnable, the event: {} will be dropped", event);
            return;
        }

    }
}
