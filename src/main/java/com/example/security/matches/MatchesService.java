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
    /*
    public void predictMatch(){
        List<String> homes = new ArrayList<>(
                List.of("Arsenal", "Man United", "Leicester", "Leeds", "Everton", "Chelsea", "Aston Villa", "Southampton")// "Brentford","Crystal Palace",
        );
        List<String> aways = new ArrayList<>(
                List.of("Wolves", "Fulham", "West Ham", "Tottenham", "Bournemouth", "Newcastle", "Brighton", "Liverpool")// "Man City","Nottingham",
        );
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
}