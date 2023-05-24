package com.example.security.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String homeTeam;
    private String awayTeam;
    private String Type;
    private String subType;
    private Integer amount;
}
