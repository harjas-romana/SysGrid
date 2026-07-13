package com.sysgrid.service;

import com.sysgrid.exceptions.NodeNotFoundException;
import com.sysgrid.model.Edge;
import com.sysgrid.model.Graph;
import com.sysgrid.model.Node;
import com.sysgrid.model.NodeStatus;
import com.sysgrid.model.NodeType;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class GraphService {
    private final Graph graph;

    public GraphService() {
        this.graph = new Graph();
    }

    public Node findNodeByUUID(UUID id) {
        Node node = graph.getNodes().get(id);
        if (node == null) {
            throw new NodeNotFoundException("Node with ID " + id + " not found");
        }
        return node;
    }

    public Edge findEdgeByUUID(UUID id) {
        Edge edge = graph.getEdges().get(id);
        if (edge == null) {
            throw new NodeNotFoundException("Edge with ID " + id + " not found");
        }
        return edge;
    }

    public Graph getGraph() {
        return graph;
    }

    public Node createNode(String name, NodeType type, double maxRps) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Node name cannot be empty");
        }
        if (maxRps <= 0) {
            throw new IllegalArgumentException("maxRps must be greater than 0");
        }

        Node node = Node.builder()
                .id(UUID.randomUUID())
                .name(name)
                .type(type)
                .maxRps(maxRps)
                .build();
        graph.addNode(node);
        return node;
    }

    public void removeNode(UUID id) {
        Node nodeToRemove = graph.getNodes().get(id);
        if (nodeToRemove != null) {
            // Remove all edges connected to this node
            Iterator<Edge> iterator = graph.getEdges().values().iterator();
            while (iterator.hasNext()) {
                Edge edge = iterator.next();
                if (edge.getSourceId().equals(id) || edge.getTargetId().equals(id)) {
                    iterator.remove();
                }
            }
            graph.removeNode(id);
        } else {
            throw new IllegalArgumentException("Node with ID " + id + " not found");
        }
    }

    public Edge createEdge(UUID sourceId, UUID targetId) {
        Node source = graph.getNodes().get(sourceId);
        Node target = graph.getNodes().get(targetId);

        if (source == null || target == null) {
            throw new IllegalArgumentException("Source or target node does not exist");
        }
        if (sourceId.equals(targetId)) {
            throw new IllegalArgumentException("Cannot create edge with same source and target");
        }

        // Check for duplicate edges
        for (Edge edge : graph.getEdges().values()) {
            if (edge.getSourceId().equals(sourceId) && edge.getTargetId().equals(targetId)) {
                throw new IllegalArgumentException("Edge already exists between these nodes");
            }
        }

        Edge edge = Edge.builder()
                .id(UUID.randomUUID())
                .sourceId(sourceId)
                .targetId(targetId)
                .build();
        graph.addEdge(edge);
        return edge;
    }

    public void removeEdge(UUID id) {
        Edge edgeToRemove = graph.getEdges().get(id);
        if (edgeToRemove != null) {
            graph.removeEdge(id);
        }
    }

    public boolean validateGraph() {
        for (Edge edge : graph.getEdges().values()) {
            if (!graph.getNodes().containsKey(edge.getSourceId()) ||
                !graph.getNodes().containsKey(edge.getTargetId())) {
                return false;
            }
        }
        return true;
    }

    public Map<String, Object> getGraphStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("nodeCount", graph.getNodes().size());
        stats.put("edgeCount", graph.getEdges().size());

        // Node type distribution
        Map<NodeType, Long> typeCounts = graph.getNodes().values().stream()
                .collect(Collectors.groupingBy(Node::getType, Collectors.counting()));
        stats.put("nodeTypeDistribution", typeCounts);

        // Node status distribution
        Map<NodeStatus, Long> statusCounts = graph.getNodes().values().stream()
                .collect(Collectors.groupingBy(Node::getStatus, Collectors.counting()));
        stats.put("nodeStatusDistribution", statusCounts);

        // Average degree
        double avgDegree = graph.getNodes().size() > 0 ?
                (double) graph.getEdges().size() * 2 / graph.getNodes().size() : 0;
        stats.put("averageDegree", avgDegree);

        return stats;
    }
    
    public org.jgrapht.Graph<Node, DefaultEdge> toJGraph() {
        org.jgrapht.Graph<Node, DefaultEdge> jgrapht = new SimpleDirectedGraph<>(DefaultEdge.class);

        for(Node n : graph.getNodes().values()) {
            jgrapht.addVertex(n);
        }

        for(Edge e : graph.getEdges().values()) {
            Node sourceNodeEdge = graph.getNodes().get(e.getSourceId());
            Node targetNodeEdge = graph.getNodes().get(e.getTargetId());

            jgrapht.addEdge(sourceNodeEdge,targetNodeEdge);
        }
        return jgrapht;
    }

    public List<Node> shortestPath(UUID sourceId, UUID targetId) {

        org.jgrapht.Graph<Node, DefaultEdge> tempGraph = toJGraph();
        Node sourceNodeuuidNode = graph.getNodes().get(sourceId);
        Node targetNodeuuidNode = graph.getNodes().get(targetId);

        if(sourceNodeuuidNode == null || targetNodeuuidNode == null) {
            throw new IllegalArgumentException("Invalid Source/Target Node");
        }
        DijkstraShortestPath<Node, DefaultEdge> sp = new DijkstraShortestPath<>(tempGraph);
        return sp.getPath(sourceNodeuuidNode,targetNodeuuidNode).getVertexList();
    }
}