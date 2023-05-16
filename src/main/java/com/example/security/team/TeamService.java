package com.example.security.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teamService")
public class TeamService {
    private final TeamRepository teamRepository;
    @Autowired
    public TeamService(TeamRepository teamRepository){this.teamRepository = teamRepository;}
    public Integer GetRankByTeamName(String name){
        Integer rk = teamRepository.getRankByName(name).orElseThrow();
        return rk;
    }
    public void test(){
        System.out.println(GetRankByTeamName("Man United"));
    }
}
