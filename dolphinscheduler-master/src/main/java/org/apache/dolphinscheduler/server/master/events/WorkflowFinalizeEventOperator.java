package org.apache.dolphinscheduler.server.master.events;

import org.apache.dolphinscheduler.server.master.dag.WorkflowExecuteRunnableRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkflowFinalizeEventOperator
        implements
            IWorkflowEventOperator<WorkflowFinalizeEvent> {

    @Autowired
    private WorkflowExecuteRunnableRepository workflowExecuteRunnableRepository;

    @Override
    public void handleEvent(WorkflowFinalizeEvent event) {
        Integer workflowInstanceId = event.getWorkflowInstanceId();
        workflowExecuteRunnableRepository.removeWorkflowExecutionRunnable(workflowInstanceId);
    }

}
