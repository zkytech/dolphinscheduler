package org.apache.dolphinscheduler.server.master.dag;

import org.apache.dolphinscheduler.dao.entity.TaskInstance;

import java.util.ArrayList;
import java.util.List;

import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * The WorkflowExecutionDAG represent a running workflow instance DAG.
 */
@Slf4j
@SuperBuilder
public class WorkflowExecutionDAG extends BasicDAG<TaskInstance> implements IWorkflowExecutionDAG {

    private final WorkflowExecutionContext workflowExecutionContext;

    private final TaskExecutionRunnableRepository taskExecutionRunnableRepository;

    @Override
    public TaskExecutionRunnable getTaskExecutionRunnableById(Integer taskInstanceId) {
        return taskExecutionRunnableRepository.getTaskExecutionRunnableById(taskInstanceId);
    }

    @Override
    public List<TaskExecutionRunnable> getActiveTaskExecutionRunnable() {
        return new ArrayList<>(taskExecutionRunnableRepository.getActiveTaskExecutionRunnable());
    }

    @Override
    public TaskExecutionRunnable createTaskExecutionRunnable(String taskName) {
        return null;
    }
}
