package com.sysgrid.model;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Graph {
    private final Map<UUID, Node> nodes;
    private final Map<UUID, Edge> edges;

    public Graph() {
        this.nodes = new ConcurrentHashMap<>();
        this.edges = new ConcurrentHashMap<>();
    }

    public Map<UUID, Node> getNodes() {
        return Collections.unmodifiableMap(nodes);
    }

    public Map<UUID, Edge> getEdges() {
        return Collections.unmodifiableMap(edges);
    }

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }

    public void removeNode(UUID id) {
        nodes.remove(id);
    }

    public void addEdge(Edge edge) {
        edges.put(edge.getId(), edge);
    }

    public void removeEdge(UUID id) {
        edges.remove(id);
    }
}