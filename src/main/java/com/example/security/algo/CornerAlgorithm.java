package com.example.security.algo;

import com.example.security.teamStats.TeamStatsService;
import org.springframework.stereotype.Service;

@Service
public class CornerAlgorithm {

    public static double[][] cornerProbCal(int[] home_cornercnt, int[] away_cornercnt, double[] homeprob_corner, double[] awayprob_corner){
        int sum_home = 0;
        int sum_away = 0;

        //calculate the total matches amount for the hometeam
        for(int i : home_cornercnt) sum_home += i;

        //calculate the total matches amount for the awayteam
        for(int j : away_cornercnt){
            sum_away += j;
        }

        //calculate the probability of different amount of corners for home team
        for(int i = 0; i < home_cornercnt.length; i++){
            double temp = home_cornercnt[i];
            homeprob_corner[i] = temp / sum_home;
        }

        //calculate the probability of different amount of corners for away team
        for(int j = 0; j < away_cornercnt.length; j++){
            double temp = away_cornercnt[j];
            awayprob_corner[j] = temp / sum_away;
        }
        double[][] allscore_prob = new double[homeprob_corner.length][awayprob_corner.length];
        //a 2d array for corner ratio between home and away team
        for(int i = 0; i < homeprob_corner.length; i++){
            for(int j = 0; j < awayprob_corner.length; j++){
                allscore_prob[i][j] = homeprob_corner[i] * awayprob_corner[j];
            }
        }
        return allscore_prob;
    }

    public static double[] cornerRateCal(double[][] allscore_prob){
        int home_length = allscore_prob.length;
        int away_length = allscore_prob[0].length;

        double[] prob_game_poisson = new double[3]; //0 stands for bigger home corner,
        // 1 stands for draw of corner, 2 stands for bigger away corner

        for(int i = 0; i < home_length; i++){
            for(int j = 0; j < away_length; j++){
                if(i > j){
                    prob_game_poisson[0] += allscore_prob[i][j];
                }
                else if(i == j){
                    prob_game_poisson[1] += allscore_prob[i][j];
                }
                else{
                    prob_game_poisson[2] += allscore_prob[i][j];
                }
            }
        }
        return prob_game_poisson;
    }

    public static double[] corner_betting_algo(double[] prob_game_poisson){
        double[] odds = new double[3];
        double home_rate = prob_game_poisson[0];
        double draw_rate = prob_game_poisson[1];
        double away_rate = prob_game_poisson[2];
        double bet_win = (1 / home_rate) * 0.9;
        double bet_draw = (1 / draw_rate) * 0.9;
        double bet_loss = (1 / away_rate) * 0.9;
        odds[0] = bet_win; //0 stands for the odd for home win,
        odds[1] = bet_draw;//1 stands for the draw
        odds[2] = bet_loss;// 2 stands for the away win
        return odds;
    }
    public void predict(TeamStatsService teamStatsService, String homeTeam, String awayTeam){
        int[] homeCornerCnt = teamStatsService.CountCorner(homeTeam);
        int[] awayCornerCnt = teamStatsService.CountCorner(awayTeam);
        double[] homeProbCorner = new double[homeCornerCnt.length];
        double[] awayProbCorner = new double[awayCornerCnt.length];
        double[][] cornerAllScoreProb = cornerProbCal(homeCornerCnt, awayCornerCnt, homeProbCorner, awayProbCorner);
        double[] cornerProbGamePoisson = cornerRateCal(cornerAllScoreProb);
        double[] cornerOdds = corner_betting_algo(cornerProbGamePoisson);
        /*
        System.out.println("probability of home team gets more corners is: " + prob_game_poisson[0]
                + ", and the probability of the draw of the corners is: " + prob_game_poisson[1]
                + ", and the probability of away team gets more corners is: " + prob_game_poisson[2]);
         */
        System.out.println("betting odd for home corner is: " + cornerOdds[0]
                + ", betting odd for draw of corner is: " + cornerOdds[1]
                + ", betting odd for away corner is: " + cornerOdds[2]);

    }
}
