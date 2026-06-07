package com.sysgrid.dto;

import java.util.List;

import com.sysgrid.model.Edge;
import com.sysgrid.model.Node;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GraphResponse {

    private List<Node> nodes;
    private List<Edge> edges;
}
