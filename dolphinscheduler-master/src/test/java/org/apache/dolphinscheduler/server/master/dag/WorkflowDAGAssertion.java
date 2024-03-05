package org.apache.dolphinscheduler.server.master.dag;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class WorkflowDAGAssertion {

    private final IWorkflowDAG workflowDAG;

    private final List<NodeAssertion> nodeAssertions;

    private WorkflowDAGAssertion(IWorkflowDAG workflowDAG) {
        this.workflowDAG = workflowDAG;
        this.nodeAssertions = new ArrayList<>();
    }

    public static WorkflowDAGAssertion workflowDag(IWorkflowDAG workflowDAG) {
        return new WorkflowDAGAssertion(workflowDAG);
    }

    public WorkflowDAGAssertion nodeAssertion(NodeAssertion nodeAssertion) {
        nodeAssertions.add(nodeAssertion);
        return this;
    }

    public void doAssertion() {
        nodeAssertions.forEach(nodeAssertion -> nodeAssertion.doAssertion(workflowDAG));
    }

    public static class NodeAssertion {

        /**
         * node name of the assertion
         */
        private final String nodeName;

        /**
         * whether the node exist
         */
        private boolean exist;

        /**
         * whether the node has out degree
         */
        private List<String> postNodes;

        /**
         * whether the node has in degree
         */
        private List<String> preNodes;

        private NodeAssertion(String nodeName) {
            this.nodeName = nodeName;
            this.postNodes = new ArrayList<>();
            this.preNodes = new ArrayList<>();
        }

        public static NodeAssertion node(String nodeName) {
            return new NodeAssertion(nodeName);
        }

        public NodeAssertion exist() {
            this.exist = true;
            return this;
        }

        public NodeAssertion noEdge() {
            this.postNodes = new ArrayList<>();
            this.preNodes = new ArrayList<>();
            return this;
        }

        public NodeAssertion noInDegree() {
            this.preNodes = new ArrayList<>();
            return this;
        }

        public NodeAssertion inDegrees(List<String> preNodes) {
            this.preNodes.addAll(preNodes);
            return this;
        }

        public NodeAssertion inDegrees(String preNode) {
            this.preNodes.add(preNode);
            return this;
        }

        public NodeAssertion noOutDegree() {
            this.postNodes = new ArrayList<>();
            return this;
        }

        public NodeAssertion outDegrees(List<String> postNodes) {
            this.postNodes.addAll(postNodes);
            return this;
        }

        public NodeAssertion outDegrees(String postNode) {
            this.postNodes.add(postNode);
            return this;
        }

        private void doAssertion(IWorkflowDAG workflowDAG) {
            if (exist) {
                // node exist
                assertNotNull(workflowDAG.getDAGNode(nodeName), "node " + nodeName + " does not exist");
            } else {

                assertNull(workflowDAG.getDAGNode(nodeName), "node " + nodeName + " exist");
            }

            if (CollectionUtils.isEmpty(postNodes)) {
                assertTrue(workflowDAG.getDirectPostNodeNames(nodeName).isEmpty(),
                        "node " + nodeName + " has outDegree " + workflowDAG.getDirectPostNodes(nodeName));
            } else {
                postNodes
                        .forEach(postNode -> assertTrue(workflowDAG.getDirectPostNodeNames(nodeName).contains(postNode),
                                "node " + nodeName + " does has outDegree " + postNode));
            }

            if (CollectionUtils.isEmpty(preNodes)) {
                assertTrue(workflowDAG.getDirectPreNodeNames(nodeName).isEmpty(),
                        "node " + nodeName + " has inDegree " + workflowDAG.getDirectPreNodeNames(nodeName));
            } else {
                preNodes.forEach(preNode -> assertTrue(workflowDAG.getDirectPreNodeNames(nodeName).contains(preNode),
                        "node " + nodeName + " does has inDegree " + preNode));
            }
        }
    }

}
