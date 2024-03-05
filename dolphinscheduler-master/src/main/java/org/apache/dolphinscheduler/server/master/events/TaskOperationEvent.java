package org.apache.dolphinscheduler.server.master.events;

import org.apache.dolphinscheduler.server.master.dag.TaskExecutionRunnable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskOperationEvent implements ITaskEvent, ISyncEvent {

    private TaskExecutionRunnable taskExecutionRunnable;

    private TaskOperationType taskOperationType;

    @Override
    public Integer getWorkflowInstanceId() {
        return taskExecutionRunnable.getTaskExecutionContext().getTaskInstance().getProcessInstanceId();
    }

    @Override
    public Integer getTaskInstanceId() {
        return taskExecutionRunnable.getTaskExecutionContext().getTaskInstance().getId();
    }
}
