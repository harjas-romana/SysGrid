package com.sysgrid.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateEdgeRequest {

    private UUID sourceId;
    private UUID targetId;

}
