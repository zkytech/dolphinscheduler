package org.apache.dolphinscheduler.server.master.dag;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;

/**
 * The Directed Acyclic Graph class.
 * <p>
 * The DAG is a directed graph, which contains the nodes and the edges, the nodeName is the unique identifier of the node.
 * The nodes are the tasks, the edges are the dependencies between the tasks.
 * The DAG is acyclic, which means there is no cycle in the graph.
 * The DAG is a directed graph, which means the edges have direction.
 *
 * @param <E> type of the node content.
 */
public interface DAG<E> {

    /**
     * Get the direct post node of given dagNode, if the dagNode is null, return the nodes which doesn't have inDegrees.
     * e.g. The DAG is:
     * <pre>
     *      {@code
     *          1 -> 2 -> 3
     *          4 -> 5
     *          6
     *      }
     * </pre>
     * <li> The post node of 1 is 2.
     * <li> The post node of 3 is empty.
     * <li> The post node of null is 1,4,6.
     *
     * @param dagNode the node of the DAG, can be null.
     * @return post node list, sort by priority.
     */
    List<DAGNode<E>> getDirectPostNodes(DAGNode<E> dagNode);

    /**
     * Same with {@link #getDirectPostNodes(DAGNode)}.
     * <p>
     * If the dagNodeName is null, return the nodes which doesn't have inDegrees. Otherwise, return the post nodes of
     * the given dagNodeName. If the dagNodeName is not null and cannot find the node in DAG, throw IllegalArgumentException.
     *
     * @param dagNodeName task name, can be null.
     * @return post task name list, sort by priority.
     * @throws IllegalArgumentException if the dagNodeName is not null and cannot find the node in DAG.
     */
    default List<DAGNode<E>> getDirectPostNodes(String dagNodeName) {
        DAGNode<E> dagNode = getDAGNode(dagNodeName);
        if (dagNodeName != null && dagNode == null) {
            throw new IllegalArgumentException("Cannot find the Node: " + dagNodeName + " in DAG");
        }
        return getDirectPostNodes(dagNode);
    }

    /**
     * Same with {@link #getDirectPostNodes(String)}. Return the post node names.
     *
     * @param dagNodeName task name, can be null.
     * @return post task name list, sort by priority.
     * @throws IllegalArgumentException if the dagNodeName is not null and cannot find the node in DAG.
     */
    default List<String> getDirectPostNodeNames(String dagNodeName) {
        DAGNode<E> dagNode = getDAGNode(dagNodeName);
        if (dagNodeName != null && dagNode == null) {
            throw new IllegalArgumentException("Cannot find the Node: " + dagNodeName + " in DAG");
        }
        return getDirectPostNodes(dagNode).stream().map(DAGNode::getNodeName).collect(Collectors.toList());
    }

    /**
     * Get the direct pre node of given dagNode, if the dagNode is null, return the nodes which doesn't have outDegrees.
     * e.g. The DAG is:
     * <pre>
     *      {@code
     *          1 -> 2 -> 3
     *          4 -> 5
     *          6
     *      }
     * </pre>
     * <li> The pre node of 1 is empty.
     * <li> The pre node of 3 is 2.
     * <li> The pre node of null is 3,5,6.
     *
     * @param dagNode the node of the DAG, can be null.
     * @return pre node list, sort by priority.
     */
    List<DAGNode<E>> getDirectPreNodes(DAGNode<E> dagNode);

    /**
     * Same with {@link #getDirectPreNodes(DAGNode)}.
     * <p>
     * If the dagNodeName is null, return the nodes which doesn't have outDegrees. Otherwise, return the pre nodes of
     * the given dagNodeName. If the dagNodeName is not null and cannot find the node in DAG, throw IllegalArgumentException.
     *
     * @param dagNodeName task name, can be null.
     * @return pre task name list, sort by priority.
     * @throws IllegalArgumentException if the dagNodeName is not null and cannot find the node in DAG.
     */
    default List<DAGNode<E>> getDirectPreNodes(String dagNodeName) {
        DAGNode<E> dagNode = getDAGNode(dagNodeName);
        if (dagNodeName != null && dagNode == null) {
            throw new IllegalArgumentException("Cannot find the Node: " + dagNodeName + " in DAG");
        }
        return getDirectPreNodes(dagNode);
    }

    /**
     * Same with {@link #getDirectPreNodes(String)}. Return the pre node names.
     *
     * @param dagNodeName task name, can be null.
     * @return pre task name list, sort by priority.
     * @throws IllegalArgumentException if the dagNodeName is not null and cannot find the node in DAG.
     */
    default List<String> getDirectPreNodeNames(String dagNodeName) {
        DAGNode<E> dagNode = getDAGNode(dagNodeName);
        if (dagNodeName != null && dagNode == null) {
            throw new IllegalArgumentException("Cannot find the Node: " + dagNodeName + " in DAG");
        }
        return getDirectPreNodes(dagNode).stream().map(DAGNode::getNodeName).collect(Collectors.toList());
    }

    /**
     * Get the node of the DAG by the node name.
     *
     * @param nodeName the node name.
     * @return the node of the DAG, return null if cannot find the node.
     */
    DAGNode<E> getDAGNode(String nodeName);

    /**
     * The node of the DAG.
     * <p>
     * The node contains the node name, the content of the node, the inDegrees and the outDegrees.
     * The inDegrees is the edge from other nodes to the current node, the outDegrees is the edge from the current
     * node to other nodes.
     *
     * @param <E> content type of the node.
     */
    @Data
    @Builder
    class DAGNode<E> {

        private String nodeName;
        private E nodeContent;

        private List<DAGEdge> inDegrees;
        private List<DAGEdge> outDegrees;

        public DAGNode(String nodeName, E nodeContent, List<DAGEdge> inDegrees, List<DAGEdge> outDegrees) {
            if (StringUtils.isEmpty(nodeName)) {
                throw new IllegalArgumentException("nodeName cannot be empty");
            }

            if (CollectionUtils.isNotEmpty(inDegrees)) {
                inDegrees.forEach(dagEdge -> {
                    if (!nodeName.equals(dagEdge.getToNodeName())) {
                        throw new IllegalArgumentException(
                                "The toNodeName of inDegree should be the nodeName of the node: "
                                        + nodeName + ", inDegree: " + dagEdge);
                    }
                });
            }

            if (CollectionUtils.isNotEmpty(outDegrees)) {
                outDegrees.forEach(dagEdge -> {
                    if (!nodeName.equals(dagEdge.getFromNodeName())) {
                        throw new IllegalArgumentException(
                                "The fromNodeName of outDegree should be the nodeName of the node: "
                                        + nodeName + ", outDegree: " + dagEdge);
                    }
                });
            }

            this.nodeName = nodeName;
            this.nodeContent = nodeContent;
            this.inDegrees = inDegrees;
            this.outDegrees = outDegrees;
        }

    }

    /**
     * The edge of the DAG.
     * <p>
     * The edge contains the fromNodeName and the toNodeName, the fromNodeName is the node name of the from node, the toNodeName is the node name of the to node.
     * <p>
     * The formNodeName can be null, which means the edge is from the start node of the DAG.
     * The toNodeName can be null, which means the edge is to the end node of the DAG.
     * The fromNodeName and the toNodeName cannot be null at the same time.
     */
    @Data
    @Builder
    class DAGEdge {

        private String fromNodeName;
        private String toNodeName;

        public DAGEdge(String fromNodeName, String toNodeName) {
            if (fromNodeName == null && toNodeName == null) {
                throw new IllegalArgumentException("fromNodeName and toNodeName cannot be null at the same time"
                        + "fromNodeName: " + fromNodeName + ", toNodeName: " + toNodeName);
            }
            if (fromNodeName != null && fromNodeName.equals(toNodeName)) {
                throw new IllegalArgumentException("fromNodeName and toNodeName cannot be the same"
                        + "fromNodeName: " + fromNodeName + ", toNodeName: " + toNodeName);
            }
            this.fromNodeName = fromNodeName;
            this.toNodeName = toNodeName;
        }
    }

}
