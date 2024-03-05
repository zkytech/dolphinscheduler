package org.apache.dolphinscheduler.server.master.events;

import org.apache.dolphinscheduler.server.master.dag.TaskExecutionRunnable;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskOperationEventOperator implements ITaskEventOperator<TaskOperationEvent> {

    @Override
    public void handleEvent(TaskOperationEvent event) {
        TaskExecutionRunnable taskExecutionRunnable = event.getTaskExecutionRunnable();
        switch (event.getTaskOperationType()) {
            case DISPATCH:
                taskExecutionRunnable.dispatch();
                break;
            case KILL:
                taskExecutionRunnable.kill();
                break;
            case PAUSE:
                taskExecutionRunnable.pause();
                break;
            default:
                log.error("Unknown TaskOperationType for event: {}", event);
        }
    }
}
