package org.apache.dolphinscheduler.server.master.dag;

import java.util.Collection;

public interface ITaskExecutionRunnableRepository {

    TaskExecutionRunnable getTaskExecutionRunnableById(Integer taskInstanceId);

    Collection<TaskExecutionRunnable> getActiveTaskExecutionRunnable();

    void removeTaskExecutionRunnable(Integer taskInstanceId);

}
