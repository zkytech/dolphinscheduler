package org.apache.dolphinscheduler.server.master.dag;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Use to store the TaskExecutionRunnable of a DAG.
 */
public class TaskExecutionRunnableRepository implements ITaskExecutionRunnableRepository {

    private final Map<Integer, TaskExecutionRunnable> taskExecuteRunnableMap = new ConcurrentHashMap<>();

    private final Map<Integer, TaskExecutionRunnable> activeTaskExecutionRunnable = new ConcurrentHashMap<>();

    public TaskExecutionRunnable getTaskExecutionRunnableById(Integer taskInstanceId) {
        return taskExecuteRunnableMap.get(taskInstanceId);
    }

    public Collection<TaskExecutionRunnable> getActiveTaskExecutionRunnable() {
        return activeTaskExecutionRunnable.values();
    }

    public void removeTaskExecutionRunnable(Integer taskInstanceId) {
        taskExecuteRunnableMap.remove(taskInstanceId);
    }

}
