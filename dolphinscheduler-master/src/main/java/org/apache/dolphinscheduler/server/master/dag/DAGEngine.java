package org.apache.dolphinscheduler.server.master.dag;

import org.apache.dolphinscheduler.server.master.events.IEventRepository;
import org.apache.dolphinscheduler.server.master.events.TaskOperationEvent;
import org.apache.dolphinscheduler.server.master.events.TaskOperationType;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@AllArgsConstructor
public class DAGEngine implements IDAGEngine {

    private final WorkflowExecutionContext workflowExecutionContext;

    @Getter
    private final IWorkflowDAG workflowDAG;

    @Getter
    private final IWorkflowExecutionDAG workflowExecutionDAG;

    private final List<TaskTriggerConditionChecker> taskTriggerConditionCheckers;

    private final ITaskExecutionRunnableFactory taskExecutionRunnableFactory;

    private final IEventRepository eventRepository;

    @Override
    public void triggerNextTasks(String parentTaskNodeName) {
        workflowDAG.getDirectPostNodeNames(parentTaskNodeName).forEach(this::triggerTask);
    }

    @Override
    @SneakyThrows
    public void triggerTask(String taskName) {
        boolean triggerConditionUnSuccess = taskTriggerConditionCheckers.stream()
                .anyMatch(taskTriggerConditionChecker -> !taskTriggerConditionChecker.taskCanTrigger(taskName));
        if (triggerConditionUnSuccess) {
            return;
        }
        TaskExecutionRunnable taskExecuteRunnable = workflowExecutionDAG.createTaskExecutionRunnable(taskName);
        TaskOperationEvent taskOperationEvent = TaskOperationEvent.builder()
                .taskExecutionRunnable(taskExecuteRunnable)
                .taskOperationType(TaskOperationType.DISPATCH)
                .build();
        eventRepository.storeEventToTail(taskOperationEvent);
    }

    @Override
    public void pauseTask(Integer taskInstanceId) {
        TaskExecutionRunnable taskExecutionRunnable = workflowExecutionDAG.getTaskExecutionRunnableById(taskInstanceId);
        if (taskExecutionRunnable == null) {
            log.error("Cannot find the ITaskExecutionRunnable for taskInstance: {}", taskInstanceId);
            return;
        }
        TaskOperationEvent taskOperationEvent = TaskOperationEvent.builder()
                .taskExecutionRunnable(taskExecutionRunnable)
                .taskOperationType(TaskOperationType.PAUSE)
                .build();
        eventRepository.storeEventToTail(taskOperationEvent);
    }

    @Override
    public void killTask(Integer taskInstanceId) {
        TaskExecutionRunnable taskExecutionRunnable = workflowExecutionDAG.getTaskExecutionRunnableById(taskInstanceId);
        if (taskExecutionRunnable == null) {
            log.error("Cannot find the ITaskExecutionRunnable for taskInstance: {}", taskInstanceId);
            return;
        }

        TaskOperationEvent taskOperationEvent = TaskOperationEvent.builder()
                .taskExecutionRunnable(taskExecutionRunnable)
                .taskOperationType(TaskOperationType.KILL)
                .build();
        eventRepository.storeEventToTail(taskOperationEvent);
    }

}
