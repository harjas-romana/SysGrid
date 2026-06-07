package com.sysgrid.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Edge {

    private UUID id;
    private UUID sourceId;
    private UUID targetId;

    @Builder.Default
    private double currentLoad = 0.0;
}
