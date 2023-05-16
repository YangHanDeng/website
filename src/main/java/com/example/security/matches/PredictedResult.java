package com.example.security.matches;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictedResult {
    private String homeTeam;
    private String awayTeam;
    private double winBet;
    private double drawBet;
    private double loseBet;
    private double bigSmallGamePt;
    private double smallBettingRatio;
    private double bigBettingRatio;
    private double redYellowPt;
    private double redYellowSmallRatio;
    private double redYellowBigRatio;
    private double homeCornerOdd;
    private double drawCornerOdd;
    private double awayCornerOdd;
}