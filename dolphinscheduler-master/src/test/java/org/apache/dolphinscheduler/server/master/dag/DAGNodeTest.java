package org.apache.dolphinscheduler.server.master.dag;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

class DAGNodeTest {

    @Test
    void testBuildDAGNode_EmptyNodeName() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> DAG.DAGNode.<String>builder()
                        .inDegrees(new ArrayList<>())
                        .outDegrees(new ArrayList<>())
                        .build());
        assertEquals("nodeName cannot be empty", illegalArgumentException.getMessage());
    }

    @Test
    void testBuildDAGNode_BadInDegree() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> DAG.DAGNode.<String>builder()
                        .nodeName("A")
                        .inDegrees(Lists.newArrayList(DAG.DAGEdge.builder()
                                .fromNodeName(null)
                                .toNodeName("B")
                                .build()))
                        .outDegrees(new ArrayList<>())
                        .build());
        assertEquals(
                "The toNodeName of inDegree should be the nodeName of the node: A, inDegree: DAG.DAGEdge(fromNodeName=null, toNodeName=B)",
                illegalArgumentException.getMessage());
    }

    @Test
    void testBuildDAGNode_NiceInDegree() {
        assertDoesNotThrow(() -> DAG.DAGNode.<String>builder()
                .nodeName("A")
                .inDegrees(Lists.newArrayList(DAG.DAGEdge.builder()
                        .fromNodeName(null)
                        .toNodeName("A")
                        .build()))
                .outDegrees(new ArrayList<>())
                .build());
    }

    @Test
    void testBuildDAGNode_BadOutDegree() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> DAG.DAGNode.<String>builder()
                        .nodeName("A")
                        .inDegrees(new ArrayList<>())
                        .outDegrees(Lists.newArrayList(DAG.DAGEdge.builder()
                                .fromNodeName("B")
                                .toNodeName(null)
                                .build()))
                        .build());
        assertEquals(
                "The fromNodeName of outDegree should be the nodeName of the node: A, outDegree: DAG.DAGEdge(fromNodeName=B, toNodeName=null)",
                illegalArgumentException.getMessage());
    }

    @Test
    void testBuildDAGNode_NiceOutDegree() {
        assertDoesNotThrow(() -> DAG.DAGNode.<String>builder()
                .nodeName("A")
                .inDegrees(new ArrayList<>())
                .outDegrees(Lists.newArrayList(DAG.DAGEdge.builder()
                        .fromNodeName("A")
                        .toNodeName(null)
                        .build()))
                .build());
    }

}
