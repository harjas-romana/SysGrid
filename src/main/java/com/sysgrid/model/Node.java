package com.sysgrid.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Node {

    private UUID id;
    private String name;
    private NodeType type;
    private double maxRps;

    @Builder.Default
    private double currentRps = 0.0;

    @Builder.Default
    private NodeStatus status = NodeStatus.HEALTHY;

}
