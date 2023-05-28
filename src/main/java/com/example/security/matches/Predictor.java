package com.example.security.matches;

import com.example.security.team.TeamService;
import com.example.security.teamStats.TeamStats;
import com.example.security.teamStats.TeamStatsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Predictor {
    private static double expectedgoal_cal(double game_amount, int[] goal_amount){
        //this would be used for HAS and HDS calculation
        double total_goal = 0.0;
        for(double goal : goal_amount){
            total_goal += goal;
        }
        return total_goal / game_amount; //expected_goal
    }

    private static double expectedloss_cal(double game_amount, int[] loss_amount){
        //this would be used for HDS and ADS calculation
        double total_loss = 0.0;
        for(double loss : loss_amount){
            total_loss += loss;
        }
        return total_loss / game_amount; // expected_loss
    }

    private static double HAS(double expected_goal, double allteam_goal){ //the score for home team for goal ability
        return expected_goal / allteam_goal; // HAS_score
    }

    private static double HDS(double expected_loss, double allteam_loss){
        return expected_loss / allteam_loss; // HDS_score
    }

    private static double AAS(double expected_goal, double allteam_goal){
        return expected_goal / allteam_goal; //AAS_score
    }

    private static double ADS(double expected_loss, double allteam_loss){
        return expected_loss / allteam_loss; // ADS_score
    }

    private static double[] expected_goal(double HAS, double HDS, double AAS,
                                          double ADS, double expected_homegoal, double expected_awaygoal){
        double home_expected = HAS * ADS * expected_homegoal;
        double away_expected = HDS * AAS * expected_awaygoal;
        double[] team_goal = new double[2];
        team_goal[0] = home_expected;
        team_goal[1] = away_expected;
        return team_goal;
    }

    private static double[] big_or_small(double homegoal, double awaygoal, double small_betting_amount, double big_betting_amount){
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

    private static double[][] allScoreProbCal(int[] home_scorecnt, int[] away_scorecnt){
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

    private static double[] rateCalWL(double[][] allscore_prob){
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

    private static int totalGameWinLosePoint(int win_game, int draw, int total_game){
        int loss_game = total_game - win_game - draw;
        return win_game * 2 + draw - loss_game; // points
    }
    private static double[] gamescore_ratio(double home, double away){
        double home_p = home / (home + away);
        double away_p = away / (home + away);
        double home_final = home_p * 20;
        double away_final = away_p * 20;
        double[] score_ratio = new double[2];
        score_ratio[0] = home_final;
        score_ratio[1] = away_final;
        return score_ratio;
    }
    private static double[] game_rank_ratio(double home_rank, double away_rank){
        double total = home_rank + away_rank;
        double home_ratio = 1.00 / (home_rank / total);
        double away_ratio = 1.00 / (away_rank / total);
        double[] rank_ratio = new double[2];
        //can be estimated as 15% of calculation
        rank_ratio[0] = home_ratio; // homerank_final
        rank_ratio[1] = away_ratio; // awayrank_final
        return rank_ratio;
    }

    private static int pastTenWinLosePt(int win_game, int draw){
        int loss_game = 10 - win_game - draw;
        return win_game * 2 + draw - loss_game; // points
    }

    private static double[] pastTenWinLoseRatioCmp(double home, double away){
        double home_p = home / (home + away);
        double away_p = away / (home + away);
        double home_final = home_p * 30;
        double away_final = away_p * 30;
        double[] pastten_ratio = new double[2];
        pastten_ratio[0] = home_final;
        pastten_ratio[1] = away_final;
        return pastten_ratio;
    }

    private static double away_winpoint(double win_game, double draw, double total_game){
        double loss_game = total_game - win_game - draw;
        double points = win_game * 2 + draw - loss_game;
        return points * 1.5; // add_on
    }

    private static double home_winpoint(double win_game, double draw, double total_game){
        double loss_game = total_game - win_game - draw;
        return win_game * 2 + draw - loss_game; // add_on /points
    }

    private static double draw_rate(double home_draw, double away_draw, double past_homedraw, double past_awaydraw, double total_game1, double total_game2){
        double total_draw = home_draw + away_draw;
        double all_drawrate = total_draw / (total_game1 + total_game2);
        double past_draw = past_homedraw + past_awaydraw;
        double past_drawrate = past_draw / (10 * 2);
        return (all_drawrate + past_drawrate * 1.5) / 2.5; // final_rate
    }

    private static double[] win_rate(double h_total, double a_total, double h_rank, double a_rank,
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

    private static double[] betWinLoseRate(double win_rate, double draw_rate, double loss_rate, double[] prob_game_poisson){
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
    private static double[] allCardCal(int homeTotalGameCnt, int awayTotalGameCnt, int home_yellow,
                                       int home_red, int away_yellow, int away_red){
        int total_homepoint = home_yellow + home_red * 2;
        int total_awaypoint = away_yellow + away_red * 2;
        double[] allgame_mean = new double[2];
        allgame_mean[0] = (double) total_homepoint / homeTotalGameCnt;
        allgame_mean[1] = (double) total_awaypoint / awayTotalGameCnt;
        return allgame_mean;
    }

    private static double[] pastTenCardCal(int home_yellow, int home_red, int away_yellow, int away_red){
        int total_homepoint = home_yellow + home_red * 2;
        int total_awaypoint = away_yellow + away_red * 2;
        double home_mean = (double) total_homepoint / 10.0;
        double away_mean = (double) total_awaypoint / 10.0;
        double[] pastten_mean = new double[2];
        pastten_mean[0] = home_mean;
        pastten_mean[1] = away_mean;
        return pastten_mean;
    }

    private static double redYellowPtCal(double[] allgame_mean, double[] pastten_mean){
        double final_home = allgame_mean[0] * 0.35 + pastten_mean[0] * 0.65;
        double final_away = allgame_mean[1] * 0.35 + pastten_mean[1] * 0.65;
        return final_home + final_away;
    }

    //the algorithm betting is used to calculate the betting ratio after the deadline of do betting amount
    private static double[] redYellowBetting(double small_amount, double big_amount){
        double small_rate = 1.0 + (big_amount / small_amount) * 0.9;
        double big_rate = 1.0 + (small_amount / big_amount) * 0.9;
        double[] small_big = new double[2];
        small_big[0] = small_rate;
        small_big[1] = big_rate;
        return small_big;
    }
    private static double[][] cornerProbCal(int[] home_cornercnt, int[] away_cornercnt, double[] homeprob_corner, double[] awayprob_corner){
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

    private static double[] cornerRateCal(double[][] allscore_prob){
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

    private static double[] corner_betting_algo(double[] prob_game_poisson){
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
    public PredictedResult predict(TeamStatsService teamStatsService, TeamService teamService, String homeTeam, String awayTeam){
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

        double HAS_meangoal = teamStatsService.MeanGoalAllHome();
        double HDS_meanloss = teamStatsService.MeanGoalAllAway();
        double AAS_meangoal = teamStatsService.MeanGoalAllAway();
        double ADS_meanloss = teamStatsService.MeanGoalAllHome();

        double betting_small = 1000000; //select sum() from order where order.type = 'bigsmall' and order.subtype = 'big' and order.hometeam=hometeam and order.awayteam=awayteam
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
        int pastTenWinLoseHomePt = pastTenWinLosePt(homePastTenWin, homePastTenDraw);
        int pastTenWinLoseAwayPt = pastTenWinLosePt(awayPastTenWin, awayPastTenDraw);
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
        int home_yellowcard = teamStatsService.TotalYellowCard(homeTeam);
        int home_redcard = teamStatsService.TotalRedCard(homeTeam);
        int away_yellowcard = teamStatsService.TotalYellowCard(awayTeam);
        int away_redcard = teamStatsService.TotalRedCard(awayTeam);

        int pastten_homey = teamStatsService.GetLast10YellowCard(HomeLast10, homeTeam);
        int pastten_homer = teamStatsService.GetLast10RedCard(HomeLast10, homeTeam);
        int pastten_awayy = teamStatsService.GetLast10YellowCard(AwayLast10, awayTeam);
        int pastten_awayr = teamStatsService.GetLast10RedCard(AwayLast10, awayTeam);
        double redYellowSmallAmount = 100000;
        double redYellowBigAmount = 56546;
        double[] allgame_mean = allCardCal(homeTeamTotalGame, awayTeamTotalGame, home_yellowcard, home_redcard, away_yellowcard, away_redcard);
        double[] pastten_mean = pastTenCardCal(pastten_homey, pastten_homer, pastten_awayy, pastten_awayr);
        double redYellowPt = redYellowPtCal(allgame_mean, pastten_mean);
        //System.out.println(redYellowPt);
        double[] redYellowSmallBig = redYellowBetting(redYellowSmallAmount, redYellowBigAmount);
        int[] homeCornerCnt = teamStatsService.CountCorner(homeTeam);
        int[] awayCornerCnt = teamStatsService.CountCorner(awayTeam);
        double[] homeProbCorner = new double[homeCornerCnt.length];
        double[] awayProbCorner = new double[awayCornerCnt.length];
        double[][] cornerAllScoreProb = cornerProbCal(homeCornerCnt, awayCornerCnt, homeProbCorner, awayProbCorner);
        double[] cornerProbGamePoisson = cornerRateCal(cornerAllScoreProb);
        double[] cornerOdds = corner_betting_algo(cornerProbGamePoisson);
        return new PredictedResult(homeTeam, awayTeam, win_bet, draw_bet, loss_bet, big_small[0], big_small[1], big_small[2], redYellowPt,
                redYellowSmallBig[0], redYellowSmallBig[1], cornerOdds[0], cornerOdds[1], cornerOdds[2]);
    }

    public WinLoseResult predictWinLose(TeamStatsService teamStatsService, TeamService teamService, String homeTeam, String awayTeam) {
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

        double HAS_meangoal = teamStatsService.MeanGoalAllHome();
        double HDS_meanloss = teamStatsService.MeanGoalAllAway();
        double AAS_meangoal = teamStatsService.MeanGoalAllAway();
        double ADS_meanloss = teamStatsService.MeanGoalAllHome();

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
        int pastTenWinLoseHomePt = pastTenWinLosePt(homePastTenWin, homePastTenDraw);
        int pastTenWinLoseAwayPt = pastTenWinLosePt(awayPastTenWin, awayPastTenDraw);
        double[] pastTenWLRatio = pastTenWinLoseRatioCmp(pastTenWinLoseHomePt, pastTenWinLoseAwayPt);
        double draw_rate = draw_rate(homeTeamDraw,homeTeamTotalGame,homePastTenDraw,awayPastTenDraw,awayTeamDraw,awayTeamTotalGame);
        double[] finalWLRatio = win_rate(score_ratio[0], score_ratio[1], rank_ratio[0], rank_ratio[1],
                pastTenWLRatio[0], pastTenWLRatio[1], home_point, away_point, draw_rate, team_goal[0], team_goal[1]);
        double[][] allscore_prob = allScoreProbCal(home_scorecnt, away_scorecnt);
        double[] winLosePoisson = rateCalWL(allscore_prob);
        double[] WDLBetAmount = betWinLoseRate(finalWLRatio[0], draw_rate, finalWLRatio[1], winLosePoisson);
        return new WinLoseResult(homeTeam, WDLBetAmount[0], WDLBetAmount[1], WDLBetAmount[2]);
    }

    public BigSmallResult predictBigSmall(TeamStatsService teamStatsService, String homeTeam, String awayTeam) {

        int[] home_pastgoal = teamStatsService.ListOfHomeGoal(homeTeam);
        int[] away_pastgoal = teamStatsService.ListOfAwayGoal(awayTeam);
        int[] home_pastloss = teamStatsService.ListOfHomeLoss(homeTeam);
        int[] away_pastloss = teamStatsService.ListOfAwayLoss(awayTeam);

        double HAS_meangoal = teamStatsService.MeanGoalAllHome();
        double HDS_meanloss = teamStatsService.MeanGoalAllAway();
        double AAS_meangoal = teamStatsService.MeanGoalAllAway();
        double ADS_meanloss = teamStatsService.MeanGoalAllHome();

        double awayTeamAwayGameCnt = teamStatsService.CountAwayGameByName(awayTeam);
        double homeTeamHomeGameCnt = teamStatsService.CountHomeGameByName(homeTeam);

        double HAS_expectedgoal = expectedgoal_cal(homeTeamHomeGameCnt, home_pastgoal);
        double HDS_expectedloss = expectedloss_cal(homeTeamHomeGameCnt, home_pastloss);
        double AAS_expectedgoal = expectedgoal_cal(awayTeamAwayGameCnt, away_pastgoal);
        double ADS_expectedloss = expectedloss_cal(awayTeamAwayGameCnt, away_pastloss);
        double HAS_point = HAS(HAS_expectedgoal, HAS_meangoal);
        double HDS_point = HDS(HDS_expectedloss, HDS_meanloss);
        double AAS_point = AAS(AAS_expectedgoal, AAS_meangoal);
        double ADS_point = ADS(ADS_expectedloss, ADS_meanloss);
        double[] team_goal = expected_goal(HAS_point, HDS_point, AAS_point, ADS_point, HAS_expectedgoal, AAS_expectedgoal);
        //double betting_small = 1000000; //select sum() from order where order.type = 'bigsmall' and order.subtype = 'big' and order.hometeam=hometeam and order.awayteam=awayteam
        //double betting_big = 508945;
        //double[] big_small = big_or_small(team_goal[0], team_goal[1], betting_small, betting_big);
        return new BigSmallResult(homeTeam, team_goal[0]);
    }

    public RedYellowResult predictRedYellow(TeamStatsService teamStatsService, String homeTeam, String awayTeam) {
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


        int homeTeamTotalGame = teamStatsService.CountTotalGameByName(homeTeam);
        int awayTeamTotalGame = teamStatsService.CountTotalGameByName(awayTeam);

        double[] allgame_mean = allCardCal(homeTeamTotalGame, awayTeamTotalGame, home_yellowcard, home_redcard, away_yellowcard, away_redcard);
        double[] pastten_mean = pastTenCardCal(pastten_homey, pastten_homer, pastten_awayy, pastten_awayr);
        double redYellowPt = redYellowPtCal(allgame_mean, pastten_mean);
        return new RedYellowResult(homeTeam, redYellowPt);
    }

    public CornerResult predictCorner(TeamStatsService teamStatsService, String homeTeam, String awayTeam) {
        int[] homeCornerCnt = teamStatsService.CountCorner(homeTeam);
        int[] awayCornerCnt = teamStatsService.CountCorner(awayTeam);
        double[] homeProbCorner = new double[homeCornerCnt.length];
        double[] awayProbCorner = new double[awayCornerCnt.length];
        double[][] cornerAllScoreProb = cornerProbCal(homeCornerCnt, awayCornerCnt, homeProbCorner, awayProbCorner);
        double[] cornerProbGamePoisson = cornerRateCal(cornerAllScoreProb);
        double[] cornerOdds = corner_betting_algo(cornerProbGamePoisson);
        return new CornerResult(homeTeam,cornerOdds[0],cornerOdds[1],cornerOdds[2]);
    }
}