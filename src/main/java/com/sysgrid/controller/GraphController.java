package com.sysgrid.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sysgrid.model.Graph;
import com.sysgrid.service.GraphService;

@RestController
@RequestMapping("/api")
public class GraphController {

    private GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/graph")
    public Graph getGraph() {
        return graphService.getGraph();
    }
}
