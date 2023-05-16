package com.example.security;

import com.example.security.matches.MatchesService;
import com.example.security.teamStats.TeamStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Optional;
@SpringBootApplication
public class SecurityApplication {
	@Autowired
	TeamStatsService teamStatsService;
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SecurityApplication.class, args);
		/*TeamStatsService teamStatsService = context.getBean(TeamStatsService.class);
		//teamStatsService.test();
		TeamService teamService = context.getBean(TeamService.class);
		//teamService.test();
		String homeTeam = "Chelsea";
		String awayTeam = "Man United";
		BettingAlgorithm bettingAlgorithm = context.getBean(BettingAlgorithm.class);
		bettingAlgorithm.predict(teamStatsService, teamService, homeTeam, awayTeam);
		*/
		//MatchesService matchesService = context.getBean(MatchesService.class);
		//matchesService.predictMatch();
	}
}
// https://www.javainuse.com/spring/bootjdbc
