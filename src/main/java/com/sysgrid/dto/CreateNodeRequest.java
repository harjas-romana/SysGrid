package com.sysgrid.dto;

import com.sysgrid.model.NodeType;

import lombok.Data;

@Data
public class CreateNodeRequest {

    private String name;

    private NodeType type;

    private double maxRps;

}
