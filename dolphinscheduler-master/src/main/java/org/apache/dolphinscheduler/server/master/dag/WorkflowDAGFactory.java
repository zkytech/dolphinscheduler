package org.apache.dolphinscheduler.server.master.dag;

import org.apache.dolphinscheduler.dao.entity.ProcessTaskRelationLog;
import org.apache.dolphinscheduler.dao.entity.TaskDefinitionLog;
import org.apache.dolphinscheduler.dao.repository.ProcessTaskRelationLogDao;
import org.apache.dolphinscheduler.dao.repository.TaskDefinitionLogDao;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkflowDAGFactory implements IWorkflowDAGFactory {

    @Autowired
    private TaskDefinitionLogDao taskDefinitionLogDao;

    @Autowired
    private ProcessTaskRelationLogDao processTaskRelationLogDao;

    @Override
    public IWorkflowDAG createWorkflowDAG(WorkflowIdentify workflowIdentify) {
        List<TaskDefinitionLog> taskDefinitions = queryTaskNodes(workflowIdentify);
        List<ProcessTaskRelationLog> taskRelations = queryTaskEdges(workflowIdentify);
        return WorkflowDAGBuilder.newBuilder()
                .addTaskNodes(taskDefinitions)
                .addTaskEdges(taskRelations)
                .build();
    }

    private List<TaskDefinitionLog> queryTaskNodes(WorkflowIdentify workflowIdentify) {
        return taskDefinitionLogDao.queryByWorkflowDefinitionCodeAndVersion(
                workflowIdentify.getWorkflowCode(), workflowIdentify.getWorkflowVersion());
    }

    private List<ProcessTaskRelationLog> queryTaskEdges(WorkflowIdentify workflowIdentify) {
        return processTaskRelationLogDao.queryByWorkflowDefinitionCodeAndVersion(
                workflowIdentify.getWorkflowCode(), workflowIdentify.getWorkflowVersion());
    }
}
