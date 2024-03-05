package org.apache.dolphinscheduler.server.master.events;

import org.apache.dolphinscheduler.server.master.dag.IWorkflowExecutionRunnable;
import org.apache.dolphinscheduler.server.master.dag.WorkflowExecuteRunnableRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkflowTriggerNextTaskEventOperator
        implements
            IWorkflowEventOperator<WorkflowTriggerNextTaskEvent> {

    @Autowired
    private WorkflowExecuteRunnableRepository workflowExecuteRunnableRepository;

    @Override
    public void handleEvent(WorkflowTriggerNextTaskEvent event) {
        IWorkflowExecutionRunnable workflowExecutionRunnable =
                workflowExecuteRunnableRepository.getWorkflowExecutionRunnableById(event.getWorkflowInstanceId());
        workflowExecutionRunnable.getDagEngine().triggerNextTasks(event.getParentTaskNodeName());
    }

}
