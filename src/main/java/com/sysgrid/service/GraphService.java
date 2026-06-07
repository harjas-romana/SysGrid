package com.sysgrid.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sysgrid.model.Edge;
import com.sysgrid.model.Graph;
import com.sysgrid.model.Node;
import com.sysgrid.model.NodeType;

@Service
public class GraphService {

    private final Graph graph;

    public GraphService() {
        this.graph = new Graph();
    }

    public Node findNodeByUUID(UUID id) {

        for (Node node : graph.getNodes()) {
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }

    public Edge findEdgeByUUID(UUID id) {

        for (Edge edge : graph.getEdges()) {
            if (edge.getId().equals(id)) {
                return edge;
            }
        }
        return null;
    }

    public Graph getGraph() {
        return graph;
    }

    public Node createNode(String name, NodeType type, double maxRps) {

        Node node = Node.builder().id(UUID.randomUUID()).name(name).type(type).maxRps(maxRps).build();
        graph.getNodes().add(node);

        return node;
    }

    public void removeNode(UUID id) {

        Node nodeToRemove = findNodeByUUID(id);

        if (nodeToRemove != null) {
            graph.getEdges().removeIf(edge -> edge.getSourceId().equals(id) || edge.getTargetId().equals(id));
            graph.getNodes().remove(nodeToRemove);
        }

    }

    public Edge createEdge(UUID sourceId, UUID targetId) {

        Node source = findNodeByUUID(sourceId);
        Node target = findNodeByUUID(targetId);

        if (source == null || target == null) {
            return null;
        }

        if (sourceId.equals(targetId)) {
            return null;
        }

        Edge edge = Edge.builder().id(UUID.randomUUID()).sourceId(sourceId).targetId(targetId).build();

        if (source != null && target != null) {
            graph.getEdges().add(edge);
        }

        return edge;
    }

    public void removeEdge(UUID id) {

        Edge edgeToRemove = findEdgeByUUID(id);

        if (edgeToRemove != null) {
            graph.getEdges().remove(edgeToRemove);
        }
    }
}
