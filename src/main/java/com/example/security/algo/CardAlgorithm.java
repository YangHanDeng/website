package com.example.security.algo;

import com.example.security.teamStats.TeamStats;
import com.example.security.teamStats.TeamStatsService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CardAlgorithm {
    public static double[] allCardCal(int total_gamecnt, int home_yellow,
                                   int home_red, int away_yellow, int away_red){
        int total_homepoint = home_yellow + home_red * 2;
        int total_awaypoint = away_yellow + away_red * 2;
        double[] allgame_mean = new double[2];
        allgame_mean[0] = (double) total_homepoint / total_gamecnt;
        allgame_mean[1] = (double) total_awaypoint / total_gamecnt;
        return allgame_mean;
    }

    public static double[] pastTenCardCal(int home_yellow, int home_red, int away_yellow, int away_red){
        int total_homepoint = home_yellow + home_red * 2;
        int total_awaypoint = away_yellow + away_red * 2;
        double home_mean = (double) total_homepoint / 10.0;
        double away_mean = (double) total_awaypoint / 10.0;
        double[] pastten_mean = new double[2];
        pastten_mean[0] = home_mean;
        pastten_mean[1] = away_mean;
        return pastten_mean;
    }

    public static double redYellowPtCal(double[] allgame_mean, double[] pastten_mean){
        double final_home = allgame_mean[0] * 0.35 + pastten_mean[0] * 0.65;
        double final_away = allgame_mean[1] * 0.35 + pastten_mean[1] * 0.65;
        return final_home + final_away;
    }

    //the algorithm betting is used to calculate the betting ratio after the deadline of do betting amount
    public static double[] redYellowBetting(double small_amount, double big_amount){
        double small_rate = 1.0 + (big_amount / small_amount) * 0.9;
        double big_rate = 1.0 + (small_amount / big_amount) * 0.9;
        double[] small_big = new double[2];
        small_big[0] = small_rate;
        small_big[1] = big_rate;
        return small_big;
    }
    public void predict(TeamStatsService teamStatsService, String homeTeam, String awayTeam){
        int total_gamecnt = 40; // should be changed
        int home_yellowcard = teamStatsService.TotalYellowCard(homeTeam);
        int home_redcard = teamStatsService.TotalRedCard(homeTeam);
        int away_yellowcard = teamStatsService.TotalYellowCard(awayTeam);
        int away_redcard = teamStatsService.TotalRedCard(awayTeam);
        List<TeamStats> HomeLast10 = teamStatsService.getLast10Stat(homeTeam);
        List<TeamStats> AwayLast10 = teamStatsService.getLast10Stat(homeTeam);
        int pastten_homey = teamStatsService.GetLast10YellowCard(HomeLast10, homeTeam);
        int pastten_homer = teamStatsService.GetLast10RedCard(HomeLast10, homeTeam);
        int pastten_awayy = teamStatsService.GetLast10YellowCard(AwayLast10, awayTeam);
        int pastten_awayr = teamStatsService.GetLast10RedCard(AwayLast10, awayTeam);
        double redYellowSmallAmount = 100000;
        double redYellowBigAmount = 56546;
        double[] allgame_mean = allCardCal(total_gamecnt, home_yellowcard, home_redcard, away_yellowcard, away_redcard);
        double[] pastten_mean = pastTenCardCal(pastten_homey, pastten_homer, pastten_awayy, pastten_awayr);
        double redYellowPt = redYellowPtCal(allgame_mean, pastten_mean);
        System.out.println(redYellowPt);
        double[] redYellowSmallBig = redYellowBetting(redYellowSmallAmount, redYellowBigAmount);
        System.out.println("the betting ratio for small is: " + redYellowSmallBig[0] +
                ", and the betting ratio for big is: " + redYellowSmallBig[1]);
    }
}
