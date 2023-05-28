package com.example.security.matches;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedYellowResult {
    private String homeTeam;
    private Double ratio;
}
