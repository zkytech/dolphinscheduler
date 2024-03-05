package org.apache.dolphinscheduler.server.master.dag;

import org.apache.dolphinscheduler.dao.entity.TaskInstance;

import java.util.List;

/**
 * The WorkflowExecutionDAG represent the running workflow DAG.
 */
public interface IWorkflowExecutionDAG extends DAG<TaskInstance> {

    /**
     * Get TaskExecutionRunnable by given TaskInstanceId.
     *
     * @param taskInstanceId taskInstanceId.
     * @return TaskExecutionRunnable
     */
    TaskExecutionRunnable getTaskExecutionRunnableById(Integer taskInstanceId);

    /**
     * Get TaskExecutionRunnable which is not finished.
     *
     * @return TaskExecutionRunnable
     */
    List<TaskExecutionRunnable> getActiveTaskExecutionRunnable();

    /**
     * Create the TaskExecutionRunnable of the given taskName.
     *
     * @param taskName taskName
     * @return TaskExecutionRunnable
     */
    TaskExecutionRunnable createTaskExecutionRunnable(String taskName);
}
