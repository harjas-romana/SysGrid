package com.sysgrid.dto;

import com.sysgrid.model.NodeType;
import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class CreateNodeRequest {
    @NotBlank(message = "Node name cannot be blank")
    private String name;

    @NotNull(message = "Node type cannot be null")
    private NodeType type;

    @Positive(message = "maxRps must be positive")
    private double maxRps;
}