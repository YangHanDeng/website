package com.example.security.teamStats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service("teamStatsService")
public class TeamStatsService {
    private final int cornerSize = 11;
    private final int GoalLossSize = 6;
    private final TeamStatsRepository teamStatsRepository;
    @Autowired
    public TeamStatsService(TeamStatsRepository teamStatsRepository){
        this.teamStatsRepository = teamStatsRepository;
    }
    public Integer CountTotalGameByName(String name){
        return teamStatsRepository.TotalGameCount(name).orElseThrow();
    }
    public Integer CountWinByName(String name){
        return teamStatsRepository.WinGameCount(name).orElseThrow();
    }
    public Integer CountDrawByName(String name){
        return teamStatsRepository.DrawGameCount(name).orElseThrow();
    }
    /*
    public Integer CountPast10WinByName(String name){
        return teamStatsRepository.Past10WinCount(name).orElseThrow();
    }
    public Integer CountPast10DrawByName(String name){
        return teamStatsRepository.Past10DrawCount(name).orElseThrow();
    }
    */
    public Integer CountLast10WinByName(List<TeamStats> last10, String name){
        int sum = 0;
        for(TeamStats ts:last10){
            sum += (ts.getHome_team().equals(name)) ? (ts.getFTR()=='H' ?1:0):(ts.getFTR()=='A' ?1:0);
        }
        return sum;
    }
    public Integer CountLast10Draw(List<TeamStats> last10){
        int sum=0;
        for(TeamStats ts : last10){
            sum += (ts.getFTR()=='D') ?1:0;
        }
        return sum;
    }
    public Integer CountHomeGameByName(String name){
        return teamStatsRepository.HomeGamePlayed(name).orElseThrow();
    }
    public Integer CountHomeGameWonByName(String name){
        return teamStatsRepository.HomeGameWon(name).orElseThrow();
    }
    public Integer CountHomeGameDrewByName(String name){
        return teamStatsRepository.HomeGameDrew(name).orElseThrow();
    }
    public Integer CountAwayGameByName(String name){
        return teamStatsRepository.AwayGamePlayed(name).orElseThrow();
    }
    public Integer CountAwayGameWonByName(String name){
        return teamStatsRepository.AwayGameWon(name).orElseThrow();
    }
    public Integer CountAwayGameDrewByName(String name){
        return teamStatsRepository.AwayGameDrew(name).orElseThrow();
    }
    public int[] ListOfAwayGoal(String name){return teamStatsRepository.AllGoalAway(name).orElseThrow();}
    public int[] ListOfAwayLoss(String name){return teamStatsRepository.AllLossAway(name).orElseThrow();}
    public int[] ListOfHomeGoal(String name){return teamStatsRepository.AllGoalHome(name).orElseThrow();}
    public int[] ListOfHomeLoss(String name){return teamStatsRepository.AllLossHome(name).orElseThrow();}
    public Float MeanGoalAllHome(){
        return teamStatsRepository.MeanGoalAllHome().orElseThrow();
    }
    public Float MeanGoalAllAway(){
        return teamStatsRepository.MeanGoalAllAway().orElseThrow();
    }
    public int[] CountDiffGoal(String name){
        List<Integer> hc = teamStatsRepository.CountDiffGoalHome(name).orElseThrow();
        List<Integer> ac = teamStatsRepository.CountDiffGoalAway(name).orElseThrow();
        int[] result = new int[GoalLossSize];
        for(int i=0;i<GoalLossSize && i<hc.size();i++){
            result[i] += hc.get(i);
        }
        for(int i=0;i<GoalLossSize&&i<ac.size();i++){
            result[i] += ac.get(i);
        }
        return result;
    }
    //red yellow
    public Integer TotalYellowCard(String name){
        int hy = teamStatsRepository.CountHomeYellowCard(name).orElseThrow();
        int ay = teamStatsRepository.CountAwayYellowCard(name).orElseThrow();
        return hy+ay;
    }
    public Integer TotalRedCard(String name){
        int hr = teamStatsRepository.CountHomeRedCard(name).orElseThrow();
        int ar = teamStatsRepository.CountAwayRedCard(name).orElseThrow();
        return hr+ar;
    }
/*
    public Integer Last10YellowCard(String name){
        int hy = teamStatsRepository.Last10HomeSumYellowCard(name).orElseThrow();
        int ay = teamStatsRepository.Last10AwaySumYellowCard(name).orElseThrow();
        return hy + ay;
    }
*/
    public List<TeamStats> getLast10Stat(String name){
        return teamStatsRepository.GetLast10(name).orElseThrow();
    }
    public Integer GetLast10YellowCard(List<TeamStats> last10, String name){
        int sum=0;
        for(TeamStats ts : last10){
            sum += (ts.getHome_team().equals(name)) ? ts.getHY() : ts.getAY();
        }
        return sum;
    }
    public Integer GetLast10RedCard(List<TeamStats> last10, String name){
        int sum = 0;
        for(TeamStats ts:last10){
            sum += (ts.getHome_team().equals(name)) ? ts.getHR() : ts.getAR();
        }
        return sum;
    }
    //corner
    public int[] CountCorner(String name){
        List<Integer> hc = teamStatsRepository.CountHomeCorner(name).orElseThrow();
        List<Integer> ac = teamStatsRepository.CountAwayCorner(name).orElseThrow();
        int[] result = new int[cornerSize];
        for(int i = 0; (i < cornerSize) && (i < hc.size()); i++){
            result[i] += hc.get(i);
        }
        for(int i = 0; (i < cornerSize) && (i < ac.size()); i++){
            result[i] += ac.get(i);
        }
        return result;
    }

    public void test(){
        /*
        String name="Chelsea";
        System.out.println(CountTotalGameByName("Chelsea"));
        System.out.println(CountWinByName("Chelsea"));
        System.out.println(CountDrawByName("Chelsea"));
        System.out.printf("%d, %d, %d, %d, %d, %d", CountHomeGameByName(name), CountHomeGameWonByName(name), CountHomeGameDrewByName(name),
                CountAwayGameByName(name), CountAwayGameWonByName(name), CountAwayGameDrewByName(name));
        System.out.printf("%f, %f\n", MeanGoalAllAway(), MeanGoalAllHome());

        System.out.println(Arrays.toString(ListOfAwayGoal(name)));
        System.out.println(Arrays.toString(ListOfAwayLoss(name)));
        System.out.println(Arrays.toString(ListOfHomeGoal(name)));
        System.out.println(Arrays.toString(ListOfHomeLoss(name)));

        int corner[] = CountCorner(name);
        System.out.println(Arrays.toString(corner));
        */
        //List<TeamStats> Last10 = getLast10Stat(name);
        //System.out.println(Arrays.toString(CountDiffGoal(name)));
        //System.out.println(Arrays.toString(CountDiffLoss(name)));
        //System.out.printf("%d, %d\n", GetLast10RedCard(Last10, name), GetLast10YellowCard(Last10, name));
        //System.out.printf("%d,%d\n", CountLast10WinByName(Last10, name), CountLast10DrawByName(Last10, name));


    }
}
