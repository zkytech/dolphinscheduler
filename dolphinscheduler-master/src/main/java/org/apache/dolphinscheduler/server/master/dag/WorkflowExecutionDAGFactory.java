package org.apache.dolphinscheduler.server.master.dag;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkflowExecutionDAGFactory implements IWorkflowExecutionDAGFactory {

    @Autowired
    private ITaskExecutionRunnableRepositoryFactory taskExecutionRunnableRepositoryFactory;

    @Override
    public IWorkflowExecutionDAG createWorkflowExecutionDAG(WorkflowExecutionContext workflowExecutionContext) {
        // todo: create the dagNodeMap of execution dag.
        return WorkflowExecutionDAG.builder()
                .dagNodeMap(null)
                .workflowExecutionContext(workflowExecutionContext)
                .taskExecutionRunnableRepository(
                        taskExecutionRunnableRepositoryFactory.createTaskExecutionRunnableRepository())
                .build();
    }
}
