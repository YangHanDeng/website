package com.example.security.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotBlank
    private String homeTeam;
    @NotBlank
    private String awayTeam;
    @NotBlank
    private String type;
    @NotBlank
    private String subType;
    @NotBlank
    private Integer amount;
}
