package com.example.security.matches;

import com.example.security.team.TeamService;
import com.example.security.teamStats.TeamStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("matchesService")
public class MatchesService {
    // public static List<PredictedResult> stats = new ArrayList<>(); //v1
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamStatsService teamStatsService;
    @Autowired
    private Predictor predictor;
    List<String> homes = new ArrayList<>(
            List.of("Aston Villa", "Everton", "Leeds", "Man United", "Chelsea", "Leicester", "Arsenal", "Southampton")// "Brentford","Crystal Palace",
    );
    List<String> aways = new ArrayList<>(
            List.of("Brighton", "Bournemouth", "Tottenham", "Fulham", "Newcastle", "West Ham", "Wolves",  "Liverpool")// "Man City","Nottingham",
    );
    /*
    public void predictMatch(){

        for(int i=0;i<homes.size();i++){
            System.out.println(homes.get(i));
            System.out.println(aways.get(i));
            stats.add(predictor.predict(teamStatsService, teamService, homes.get(i), aways.get(i)));
        }
    }

     */
    public PredictedResult predictWithTeamName(String homeTeam, String awayTeam){
        return predictor.predict(teamStatsService, teamService, homeTeam, awayTeam);
    }
    public List<WinLoseResult> predictWinLose(){
        List<WinLoseResult> l = new ArrayList<>();
        for(int i=0;i<8;i++){
            l.add(predictor.predictWinLose(teamStatsService, teamService, homes.get(i), aways.get(i)));
        }
        return l;
    }

    public List<BigSmallResult> predictBigSmall() {
        List<BigSmallResult> l = new ArrayList<>();
        for(int i=0;i<8;i++){
            l.add(predictor.predictBigSmall(teamStatsService, homes.get(i), aways.get(i)));
        }
        return l;
    }

    public List<RedYellowResult> predictRedYellow() {
        List<RedYellowResult> l = new ArrayList<>();
        for(int i=0;i<8;i++){
            l.add(predictor.predictRedYellow(teamStatsService, homes.get(i), aways.get(i)));
        }
        return l;
    }

    public List<CornerResult> predictCorner() {
        List<CornerResult> l = new ArrayList<>();
        for(int i=0;i<8;i++){
            l.add(predictor.predictCorner(teamStatsService, homes.get(i), aways.get(i)));
        }
        return l;
    }
}