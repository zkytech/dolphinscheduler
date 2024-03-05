package org.apache.dolphinscheduler.server.master.dag;

import org.apache.dolphinscheduler.server.master.events.IEventRepository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DAGEngineFactory implements IDAGEngineFactory {

    @Autowired
    private IWorkflowExecutionDAGFactory workflowExecutionDAGFactory;

    @Autowired
    private IWorkflowDAGFactory workflowDAGFactory;

    @Autowired
    private List<TaskTriggerConditionChecker> taskTriggerConditionCheckers;

    @Autowired
    private ITaskExecutionRunnableFactory taskExecutionRunnableFactory;

    @Override
    public IDAGEngine createDAGEngine(WorkflowExecutionContext workflowExecutionContext,
                                      IEventRepository eventRepository) {
        IWorkflowExecutionDAG workflowExecutionDAG =
                workflowExecutionDAGFactory.createWorkflowExecutionDAG(workflowExecutionContext);
        IWorkflowDAG workflowDAG =
                workflowDAGFactory.createWorkflowDAG(workflowExecutionContext.getWorkflowIdentify());
        return DAGEngine.builder()
                .workflowExecutionContext(workflowExecutionContext)
                .workflowDAG(workflowDAG)
                .workflowExecutionDAG(workflowExecutionDAG)
                .taskExecutionRunnableFactory(taskExecutionRunnableFactory)
                .taskTriggerConditionCheckers(taskTriggerConditionCheckers)
                .eventRepository(eventRepository)
                .build();
    }
}
