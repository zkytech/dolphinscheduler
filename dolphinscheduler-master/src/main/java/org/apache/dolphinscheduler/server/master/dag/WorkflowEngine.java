package org.apache.dolphinscheduler.server.master.dag;

import org.apache.dolphinscheduler.dao.entity.ProcessInstance;
import org.apache.dolphinscheduler.server.master.events.WorkflowOperationEvent;
import org.apache.dolphinscheduler.server.master.events.WorkflowOperationType;
import org.apache.dolphinscheduler.server.master.exception.WorkflowExecuteRunnableNotFoundException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkflowEngine implements IWorkflowEngine {

    private final WorkflowExecuteRunnableRepository workflowExecuteRunnableRepository;

    public WorkflowEngine(WorkflowExecuteRunnableRepository workflowExecuteRunnableRepository) {
        this.workflowExecuteRunnableRepository = workflowExecuteRunnableRepository;
    }

    @Override
    public void triggerWorkflow(IWorkflowExecutionRunnable workflowExecuteRunnable) {
        ProcessInstance workflowInstance = workflowExecuteRunnable.getWorkflowExecutionContext().getWorkflowInstance();
        Integer workflowInstanceId = workflowInstance.getId();
        log.info("Triggering WorkflowExecutionRunnable: {}", workflowInstance.getName());
        workflowExecuteRunnableRepository.storeWorkflowExecutionRunnable(workflowExecuteRunnable);
        workflowExecuteRunnable
                .storeEventToTail(WorkflowOperationEvent.of(workflowInstanceId, WorkflowOperationType.TRIGGER));
    }

    @Override
    public void pauseWorkflow(Integer workflowInstanceId) {
        IWorkflowExecutionRunnable workflowExecuteRunnable =
                workflowExecuteRunnableRepository.getWorkflowExecutionRunnableById(workflowInstanceId);
        if (workflowExecuteRunnable == null) {
            throw new WorkflowExecuteRunnableNotFoundException(workflowInstanceId);
        }
        log.info("Pausing WorkflowExecutionRunnable: {}",
                workflowExecuteRunnable.getWorkflowExecutionContext().getWorkflowInstance().getName());
        workflowExecuteRunnable
                .storeEventToTail(WorkflowOperationEvent.of(workflowInstanceId, WorkflowOperationType.PAUSE));
    }

    @Override
    public void killWorkflow(Integer workflowInstanceId) {
        IWorkflowExecutionRunnable workflowExecuteRunnable =
                workflowExecuteRunnableRepository.getWorkflowExecutionRunnableById(workflowInstanceId);
        if (workflowExecuteRunnable == null) {
            throw new WorkflowExecuteRunnableNotFoundException(workflowInstanceId);
        }
        log.info("Killing WorkflowExecutionRunnable: {}",
                workflowExecuteRunnable.getWorkflowExecutionContext().getWorkflowInstance().getName());
        workflowExecuteRunnable
                .storeEventToTail(WorkflowOperationEvent.of(workflowInstanceId, WorkflowOperationType.KILL));
    }

    @Override
    public void finalizeWorkflow(Integer workflowInstanceId) {
        IWorkflowExecutionRunnable workflowExecutionRunnable =
                workflowExecuteRunnableRepository.getWorkflowExecutionRunnableById(workflowInstanceId);
        if (workflowExecutionRunnable == null) {
            return;
        }
        log.info("Finalizing WorkflowExecutionRunnable: {}",
                workflowExecutionRunnable.getWorkflowExecutionContext().getWorkflowInstance().getName());
        workflowExecuteRunnableRepository.removeWorkflowExecutionRunnable(workflowInstanceId);
    }

}
