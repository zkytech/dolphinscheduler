package org.apache.dolphinscheduler.server.master.dag;

import org.apache.dolphinscheduler.dao.entity.TaskDefinition;

/**
 * The IWorkflowDAG represent the DAG of a workflow.
 */
public interface IWorkflowDAG extends DAG<TaskDefinition> {

}
