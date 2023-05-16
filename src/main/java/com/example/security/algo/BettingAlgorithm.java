package com.example.security.algo;

import com.example.security.team.TeamService;
import com.example.security.teamStats.TeamStats;
import com.example.security.teamStats.TeamStatsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BettingAlgorithm {

    //There are two parts for goal prediction, one is the most possible goal ratio. The other one is the betting rate for different scores

    public static double expectedgoal_cal(double game_amount, int[] goal_amount){
        //this would be used for HAS and HDS calculation
        double total_goal = 0.0;
        for(double goal : goal_amount){
            total_goal += goal;
        }
        return total_goal / game_amount; //expected_goal
    }

    public static double expectedloss_cal(double game_amount, int[] loss_amount){
        //this would be used for HDS and ADS calculation
        double total_loss = 0.0;
        for(double loss : loss_amount){
            total_loss += loss;
        }
        return total_loss / game_amount; // expected_loss
    }

    public static double HAS(double expected_goal, double allteam_goal){ //the score for home team for goal ability
        return expected_goal / allteam_goal; // HAS_score
    }

    public static double HDS(double expected_loss, double allteam_loss){
        return expected_loss / allteam_loss; // HDS_score
    }

    public static double AAS(double expected_goal, double allteam_goal){
        return expected_goal / allteam_goal; //AAS_score
    }

    public static double ADS(double expected_loss, double allteam_loss){
        return expected_loss / allteam_loss; // ADS_score
    }

    public static double[] expected_goal(double HAS, double HDS, double AAS,
                                     double ADS, double expected_homegoal, double expected_awaygoal){
        double home_expected = HAS * ADS * expected_homegoal;
        double away_expected = HDS * AAS * expected_awaygoal;
        double[] team_goal = new double[2];
        team_goal[0] = home_expected;
        team_goal[1] = away_expected;
        return team_goal;
    }

    public static double[] big_or_small(double homegoal, double awaygoal, double small_betting_amount, double big_betting_amount){
        //the initialization of the small and big are both 1 at first.
        //we make this betting algorithm by the amount of the pool of different results
        //This is a betting between different participants, so the betting rate won't be known before the close of the betting
        //under this algorithm, we will use the final ratio of the betting amount to calculate the betting ratio
        double game_ratio = homegoal / awaygoal;
        double small_rate = 1.0 + (big_betting_amount / small_betting_amount) * 0.9;
        double big_rate = 1.0 + (small_betting_amount / big_betting_amount) * 0.9;
        double[] big_small = new double[3];
        big_small[0] = game_ratio; // we record this to let participants know the goal ratio we predict to help them make their choice
        big_small[1] = small_rate;
        big_small[2] = big_rate;
        return big_small;
    }

    public static double[][] allScoreProbCal(int[] home_scorecnt, int[] away_scorecnt){
        int sum_home = 0;
        int sum_away = 0;
        for (int k : home_scorecnt) {
            sum_home += k;
        }
        for (int i : away_scorecnt) {
            sum_away += i;
        }
        double[] homeprob_goal = new double[home_scorecnt.length];
        double[] awayprob_goal = new double[away_scorecnt.length];
        for(int i = 0; i < home_scorecnt.length; i++){
            double temp = home_scorecnt[i];
            homeprob_goal[i] = temp / sum_home;
        }
        for (int j = 0; j < away_scorecnt.length; j++){
            double temp = away_scorecnt[j];
            awayprob_goal[j] = temp / sum_away;
        }
        double[][] allscore_prob = new double[homeprob_goal.length][awayprob_goal.length];
        //above is calculating the probability for different goal amount
        for(int i = 0; i < homeprob_goal.length; i++){
            for(int j = 0; j < awayprob_goal.length; j++){
                allscore_prob[i][j] = homeprob_goal[i] * awayprob_goal[j];
            }
        }
        return allscore_prob;
    }

    public static double[] rateCalWL(double[][] allscore_prob){
        int home_length = allscore_prob.length;
        int away_length = allscore_prob[0].length;
        double[] prob_game_poisson = new double[3]; //0 stands for home win, 1 stands for draw, 2 stands for home loss
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

    public static int totalGameWinLosePoint(int win_game, int draw, int total_game){
        int loss_game = total_game - win_game - draw;
        return win_game * 2 + draw - loss_game; // points
    }
    public static double[] gamescore_ratio(double home, double away){
        double home_p = home / (home + away);
        double away_p = away / (home + away);
        double home_final = home_p * 20;
        double away_final = away_p * 20;
        double[] score_ratio = new double[2];
        score_ratio[0] = home_final;
        score_ratio[1] = away_final;
        return score_ratio;
    }
    public static double[] game_rank_ratio(double home_rank, double away_rank){
        double total = home_rank + away_rank;
        double home_ratio = 1.00 / (home_rank / total);
        double away_ratio = 1.00 / (away_rank / total);
        double[] rank_ratio = new double[2];
        //can be estimated as 15% of calculation
        rank_ratio[0] = home_ratio; // homerank_final
        rank_ratio[1] = away_ratio; // awayrank_final
        return rank_ratio;
    }

    public static int pastTenWinLosePt(int win_game, int draw, int total_game){
        int loss_game = total_game - win_game - draw;
        return win_game * 2 + draw - loss_game; // points
    }

    public static double[] pastTenWinLoseRatioCmp(double home, double away){
        double home_p = home / (home + away);
        double away_p = away / (home + away);
        double home_final = home_p * 30;
        double away_final = away_p * 30;
        double[] pastten_ratio = new double[2];
        pastten_ratio[0] = home_final;
        pastten_ratio[1] = away_final;
        return pastten_ratio;
    }

    public static double away_winpoint(double win_game, double draw, double total_game){
        double loss_game = total_game - win_game - draw;
        double points = win_game * 2 + draw - loss_game;
        return points * 1.5; // add_on
    }

    public static double home_winpoint(double win_game, double draw, double total_game){
        double loss_game = total_game - win_game - draw;
        return win_game * 2 + draw - loss_game; // add_on /points
    }

    public static double draw_rate(double home_draw, double away_draw, double past_homedraw, double past_awaydraw, double total_game1, double total_game2){
        double total_draw = home_draw + away_draw;
        double all_drawrate = total_draw / (total_game1 + total_game2);
        double past_draw = past_homedraw + past_awaydraw;
        double past_drawrate = past_draw / (10 * 2);
        return (all_drawrate + past_drawrate * 1.5) / 2.5; // final_rate
    }

    public static double[] win_rate(double h_total, double a_total, double h_rank, double a_rank,
                                double h_pastten, double a_pastten, double h_point, double a_point,
                                double draw_rate, double home_goal, double away_goal){
        //return 0.0;
        double home = h_total + h_rank + h_pastten + h_point + home_goal * 5.0;
        double away = a_total + a_rank + a_pastten + a_point + away_goal * 5.0;
        double rate = 1.0 - draw_rate;
        double home_init = home / (home + away);
        double away_init = away / (home + away);
        double home_final = rate * home_init;
        double away_final = rate * away_init;
        double[] final_ratio = new double[2];
        final_ratio[0] = home_final;
        final_ratio[1] = away_final;
        return final_ratio;
    }

    public static double[] betWinLoseRate(double win_rate, double draw_rate, double loss_rate, double[] prob_game_poisson){
        //return 0.0;
        double final_winrate = win_rate * 0.3 + prob_game_poisson[0] * 0.7;
        double final_drawrate = draw_rate * 0.3 + prob_game_poisson[1] * 0.7;
        double final_lossrate = loss_rate * 0.3 + prob_game_poisson[2] * 0.7;
        double bet_win = (1 / final_winrate) * 0.9;
        double bet_draw = (1 / final_drawrate) * 0.9;
        double bet_loss = (1 / final_lossrate) * 0.9;
        double[] bet_amount = new double[3];
        bet_amount[0] = bet_win;
        bet_amount[1] = bet_draw;
        bet_amount[2] = bet_loss;
        return bet_amount;
    }

    public void predict(TeamStatsService teamStatsService, TeamService teamService, String homeTeam, String awayTeam){
        int homeTeamWin = teamStatsService.CountWinByName(homeTeam);
        int homeTeamDraw = teamStatsService.CountDrawByName(homeTeam);
        int homeTeamTotalGame = teamStatsService.CountTotalGameByName(homeTeam);
        int awayTeamWin = teamStatsService.CountWinByName(awayTeam);
        int awayTeamDraw = teamStatsService.CountDrawByName(awayTeam);
        int awayTeamTotalGame = teamStatsService.CountTotalGameByName(awayTeam);
        int rank_home = teamService.GetRankByTeamName(homeTeam);
        int rank_away = teamService.GetRankByTeamName(awayTeam);
        List<TeamStats> HomeLast10 = teamStatsService.getLast10Stat(homeTeam);
        int homePastTenWin = teamStatsService.CountLast10WinByName(HomeLast10, homeTeam);
        int homePastTenDraw = teamStatsService.CountLast10Draw(HomeLast10);
        List<TeamStats> AwayLast10 = teamStatsService.getLast10Stat(homeTeam);
        int awayPastTenWin = teamStatsService.CountLast10WinByName(AwayLast10, awayTeam);
        int awayPastTenDraw = teamStatsService.CountLast10Draw(AwayLast10);

        double awayTeamAwayWinCnt = teamStatsService.CountAwayGameDrewByName(awayTeam);
        double awayTeamAwayDrawCnt = teamStatsService.CountAwayGameWonByName(awayTeam);
        double awayTeamAwayGameCnt = teamStatsService.CountAwayGameByName(awayTeam);
        double homeTeamHomeWinCnt = teamStatsService.CountHomeGameWonByName(homeTeam);
        double homeTeamHomeDrawCnt = teamStatsService.CountHomeGameDrewByName(homeTeam);
        double homeTeamHomeGameCnt = teamStatsService.CountHomeGameByName(homeTeam);

        //we need to have an algorithm for predict expected goal for different teams

        int[] home_pastgoal = teamStatsService.ListOfHomeGoal(homeTeam);
        int[] away_pastgoal = teamStatsService.ListOfAwayGoal(awayTeam);
        int[] home_pastloss = teamStatsService.ListOfHomeLoss(homeTeam);
        int[] away_pastloss = teamStatsService.ListOfAwayLoss(awayTeam);
        //double HAS_expectedgoal = 3.5;
        //double HDS_expectedloss = 0.9;
        //double AAS_expectedgoal = 2.1;
        //double ADS_expectedloss = 1.8;
        double HAS_meangoal = teamStatsService.MeanGoalAllHome();
        double HDS_meanloss = teamStatsService.MeanGoalAllAway();
        double AAS_meangoal = teamStatsService.MeanGoalAllAway();
        double ADS_meanloss = teamStatsService.MeanGoalAllHome();

        double betting_small = 1000000;
        double betting_big = 508945;

        int[] home_scorecnt = teamStatsService.CountDiffGoal(homeTeam);
        // the above means that home team had 0 goal for 8 matches, 1 goal for 8 matches, 2 goal for 6 matches....
        int[] away_scorecnt = teamStatsService.CountDiffGoal(awayTeam);
        // for away part, it is the same as home_socrecnt

        double HAS_expectedgoal = expectedgoal_cal(homeTeamHomeGameCnt, home_pastgoal);
        double HDS_expectedloss = expectedloss_cal(homeTeamHomeGameCnt, home_pastloss);
        double AAS_expectedgoal = expectedgoal_cal(awayTeamAwayGameCnt, away_pastgoal);
        double ADS_expectedloss = expectedloss_cal(awayTeamAwayGameCnt, away_pastloss);
        double HAS_point = HAS(HAS_expectedgoal, HAS_meangoal);
        double HDS_point = HDS(HDS_expectedloss, HDS_meanloss);
        double AAS_point = AAS(AAS_expectedgoal, AAS_meangoal);
        double ADS_point = ADS(ADS_expectedloss, ADS_meanloss);
        double[] team_goal = expected_goal(HAS_point, HDS_point, AAS_point, ADS_point, HAS_expectedgoal, AAS_expectedgoal);
        double away_point = away_winpoint(awayTeamAwayWinCnt, awayTeamAwayDrawCnt, awayTeamAwayGameCnt);
        double home_point = home_winpoint(homeTeamHomeWinCnt, homeTeamHomeDrawCnt, homeTeamHomeGameCnt);
        int homeWLPoint = totalGameWinLosePoint(homeTeamWin,homeTeamDraw,homeTeamTotalGame);
        int awayWLPoint = totalGameWinLosePoint(awayTeamWin,awayTeamDraw,awayTeamTotalGame);
        double[] score_ratio = gamescore_ratio(homeWLPoint, awayWLPoint);
        double[] rank_ratio = game_rank_ratio(rank_home, rank_away);
        int pastTenWinLoseHomePt = pastTenWinLosePt(homePastTenWin, homePastTenDraw, 10);
        int pastTenWinLoseAwayPt = pastTenWinLosePt(awayPastTenWin, awayPastTenDraw, 10);
        double[] pastTenWLRatio = pastTenWinLoseRatioCmp(pastTenWinLoseHomePt, pastTenWinLoseAwayPt);
        double draw_rate = draw_rate(homeTeamDraw,homeTeamTotalGame,homePastTenDraw,awayPastTenDraw,awayTeamDraw,awayTeamTotalGame);
        double[] finalWLRatio = win_rate(score_ratio[0], score_ratio[1], rank_ratio[0], rank_ratio[1],
                pastTenWLRatio[0], pastTenWLRatio[1], home_point, away_point, draw_rate, team_goal[0], team_goal[1]);
        double[][] allscore_prob = allScoreProbCal(home_scorecnt, away_scorecnt);
        double[] winLosePoisson = rateCalWL(allscore_prob);
        double[] WDLBetAmount = betWinLoseRate(finalWLRatio[0], draw_rate, finalWLRatio[1], winLosePoisson);

        double win_bet = WDLBetAmount[0];
        double draw_bet = WDLBetAmount[1];
        double loss_bet = WDLBetAmount[2];
        double[] big_small = big_or_small(team_goal[0], team_goal[1], betting_small, betting_big);
        /*
        System.out.println(score_ratio[0]);
        System.out.println(score_ratio[1]);
        System.out.println(rank_ratio[0]);
        System.out.println(rank_ratio[1]);
        System.out.println(pastten_ratio[0]);
        System.out.println(pastten_ratio[1]);

        System.out.println("win rate:" + final_ratio[0]);
        System.out.println("loss rate:" + final_ratio[1]);
        System.out.println(away_point);
        System.out.println(home_point);
        System.out.println("draw rate:" + draw_rate);
        System.out.println("the prob of the score 0:0 is: " + allscore_prob[3][4]);
        System.out.println("the prob for home win: " + prob_game_poisson[0] + ", the prob for draw: " + prob_game_poisson[1] +
                ", the prob for home loss: " + prob_game_poisson[2]);

         */

        //System.out.println("expected goal for home team: " + team_goal[0] + ", expected goal for away team: " + team_goal[1]);

        System.out.println("bet rate for win is :" + win_bet +
                ", bet rate for draw is :" + draw_bet + ", bet rate for loss is :" + loss_bet);
        System.out.println("for the predicted game point" + big_small[0] + ", the betting ratio for small is:" + big_small[1]
                + ", the betting ratio for big is:" + big_small[2]);
    }
}
