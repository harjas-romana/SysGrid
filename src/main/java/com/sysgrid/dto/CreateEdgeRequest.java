package com.sysgrid.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateEdgeRequest {
    @NotNull(message = "Source ID cannot be null")
    private UUID sourceId;

    @NotNull(message = "Target ID cannot be null")
    private UUID targetId;
}