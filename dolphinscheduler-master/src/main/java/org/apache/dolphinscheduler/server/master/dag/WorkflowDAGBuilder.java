package org.apache.dolphinscheduler.server.master.dag;

import org.apache.dolphinscheduler.dao.entity.ProcessTaskRelationLog;
import org.apache.dolphinscheduler.dao.entity.TaskDefinition;
import org.apache.dolphinscheduler.dao.entity.TaskDefinitionLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to build WorkflowDAG, you need to add TaskNode first, then add TaskEdge.
 * After adding all the TaskNodes and TaskEdges, you can call the build method to get the WorkflowDAG.
 * <p>
 *     Example:
 * <pre>
 *     {@code
 *          WorkflowDAG workflowDAG = WorkflowDAGBuilder.newBuilder()
 *                 .addTaskNode(taskNodeA)
 *                 .addTaskNode(taskNodeB)
 *                 .addTaskNode(taskNodeC)
 *                 .addTaskEdge(edgeAB)
 *                 .addTaskEdge(edgeBC)
 *                 .build();
 *     }
 * </pre>
 */
public class WorkflowDAGBuilder {

    private final Map<String, DAG.DAGNode<TaskDefinition>> taskNameMap;

    private final Map<Long, DAG.DAGNode<TaskDefinition>> taskCodeMap;

    private WorkflowDAGBuilder() {
        this.taskCodeMap = new HashMap<>();
        this.taskNameMap = new HashMap<>();
    }

    public static WorkflowDAGBuilder newBuilder() {
        return new WorkflowDAGBuilder();
    }

    public WorkflowDAGBuilder addTaskNodes(List<TaskDefinitionLog> taskDefinitionList) {
        taskDefinitionList.forEach(this::addTaskNode);
        return this;
    }

    public WorkflowDAGBuilder addTaskNode(TaskDefinitionLog taskDefinition) {
        String taskName = taskDefinition.getName();
        long taskCode = taskDefinition.getCode();
        if (taskCodeMap.containsKey(taskCode)) {
            throw new IllegalArgumentException("TaskNode with code " + taskCode + " already exists");
        }
        if (taskNameMap.containsKey(taskName)) {
            throw new IllegalArgumentException("TaskNode with name " + taskName + " already exists");
        }

        DAG.DAGNode<TaskDefinition> taskNode = DAG.DAGNode.<TaskDefinition>builder()
                .nodeName(taskName)
                .nodeContent(taskDefinition)
                .inDegrees(new ArrayList<>())
                .outDegrees(new ArrayList<>())
                .build();
        taskNameMap.put(taskName, taskNode);
        taskCodeMap.put(taskCode, taskNode);
        return this;
    }

    public WorkflowDAGBuilder addTaskEdges(List<ProcessTaskRelationLog> processTaskRelations) {
        processTaskRelations.forEach(this::addTaskEdge);
        return this;
    }

    public WorkflowDAGBuilder addTaskEdge(ProcessTaskRelationLog processTaskRelation) {
        long preTaskCode = processTaskRelation.getPreTaskCode();
        long postTaskCode = processTaskRelation.getPostTaskCode();

        if (taskCodeMap.containsKey(preTaskCode)) {
            DAG.DAGNode<TaskDefinition> fromTask = taskCodeMap.get(preTaskCode);
            if (taskCodeMap.containsKey(postTaskCode)) {
                DAG.DAGNode<TaskDefinition> toTask = taskCodeMap.get(postTaskCode);
                DAG.DAGEdge edge = DAG.DAGEdge.builder()
                        .fromNodeName(fromTask.getNodeName())
                        .toNodeName(toTask.getNodeName())
                        .build();
                if (fromTask.getOutDegrees().contains(edge)) {
                    throw new IllegalArgumentException(
                            "Edge from " + fromTask.getNodeName() + " to " + toTask.getNodeName() + " already exists");
                }
                fromTask.getOutDegrees().add(edge);
                if (toTask.getInDegrees().contains(edge)) {
                    throw new IllegalArgumentException(
                            "Edge from " + fromTask.getNodeName() + " to " + toTask.getNodeName() + " already exists");
                }
                toTask.getInDegrees().add(edge);
            }
        }
        return this;
    }

    public WorkflowDAG build() {
        return WorkflowDAG.builder()
                .dagNodeMap(taskNameMap)
                .build();
    }

}
