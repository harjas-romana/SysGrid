package com.sysgrid.controller;

import com.sysgrid.dto.CreateEdgeRequest;
import com.sysgrid.dto.CreateNodeRequest;
import com.sysgrid.dto.GraphResponse;
import com.sysgrid.model.Edge;
import com.sysgrid.model.Node;
import com.sysgrid.service.GraphService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class GraphController {
    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/graph")
    public ResponseEntity<GraphResponse> getGraph() {
        return ResponseEntity.ok(
                new GraphResponse(
                        graphService.getGraph().getNodes().values().stream().collect(Collectors.toList()),
                        graphService.getGraph().getEdges().values().stream().collect(Collectors.toList())
                )
        );
    }

    @GetMapping("/nodes")
    public ResponseEntity<List<Node>> getAllNodes() {
        return ResponseEntity.ok(
                graphService.getGraph().getNodes().values().stream().collect(Collectors.toList())
        );
    }

    @PostMapping("/nodes")
    public ResponseEntity<Node> createNode(@Valid @RequestBody CreateNodeRequest request) {
        Node node = graphService.createNode(request.getName(), request.getType(), request.getMaxRps());
        return ResponseEntity.status(HttpStatus.CREATED).body(node);
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<Node> getNodeById(@PathVariable UUID id) {
        Node node = graphService.findNodeByUUID(id);
        return ResponseEntity.ok(node);
    }

    @DeleteMapping("/nodes/{id}")
    public ResponseEntity<Void> deleteNode(@PathVariable UUID id) {
        graphService.removeNode(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/edges")
    public ResponseEntity<List<Edge>> getAllEdges() {
        return ResponseEntity.ok(
                graphService.getGraph().getEdges().values().stream().collect(Collectors.toList())
        );
    }

    @PostMapping("/edges")
    public ResponseEntity<Edge> createEdge(@Valid @RequestBody CreateEdgeRequest request) {
        Edge edge = graphService.createEdge(request.getSourceId(), request.getTargetId());
        return ResponseEntity.status(HttpStatus.CREATED).body(edge);
    }

    @DeleteMapping("/edges/{id}")
    public ResponseEntity<Void> deleteEdge(@PathVariable UUID id) {
        graphService.removeEdge(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/graph/stats")
    public ResponseEntity<Map<String, Object>> getGraphStats() {
        return ResponseEntity.ok(graphService.getGraphStats());
    }

    @GetMapping("/graph/shortest-path")
    public ResponseEntity<List<Node>> getShortestPath(@RequestParam UUID source, @RequestParam UUID target) {
        try {
            List<Node> sp = graphService.shortestPath(source,target);
            return ResponseEntity.ok(sp);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonList(new Node()));
        }
    }
}