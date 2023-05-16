package com.example.security.teamStats;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class TeamStats {
    @Id
    private Integer id;
    private String season;
    private String date_time;
    private String home_team;
    private String away_team;
    private Integer FTHG;
    private Integer FTAG;
    private Character FTR;
    private Float HTHG;
    private Float HTAG;
    private Character HTR;
    private String Referee;
    private Float HS;
    private Float _AS;
    private Float HST;
    private Float AST;
    private Float HC;
    private Float AC;
    private Float HF;
    private Float AF;
    private Float HY;
    private Float AY;
    private Float HR;
    private Float AR;

}
