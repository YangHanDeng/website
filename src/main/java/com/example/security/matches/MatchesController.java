package com.example.security.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/teams")
public class MatchesController {
    private final MatchesService matchesService;
    @Autowired
    public MatchesController(MatchesService matchesService) {
        this.matchesService = matchesService;
    }
    /*
    @GetMapping("/GetStat") // @RequestHeader(value="authorization") String auth
    public PredictedResult getMatchStat(){
        return matchesService.stats.get(1);
    }

     */
    @GetMapping("/GetStat")
    public PredictedResult getMatchStat(@RequestBody TeamRequest teamRequest){
        return matchesService.predictWithTeamName(teamRequest.getHome(), teamRequest.getAway());
    }

}