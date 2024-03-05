package org.apache.dolphinscheduler.server.master.dag;

/**
 * The Factory used to create {@link IWorkflowDAG}
 */
public interface IWorkflowDAGFactory {

    /**
     * Create the WorkflowDAG
     *
     * @param workflowIdentify workflow identify.
     * @return workflow DAG.
     */
    IWorkflowDAG createWorkflowDAG(WorkflowIdentify workflowIdentify);

}
