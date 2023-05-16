package com.example.security.teamStats;

import com.example.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
@Repository
public interface TeamStatsRepository extends JpaRepository<TeamStats, Integer> {
    Optional<TeamStats> findById(Integer id);
    // Win Loss
    @Query("select count(*) from TeamStats where home_team = ?1 or away_team = ?1")
    Optional<Integer>TotalGameCount(String name);
    @Query("select count(*) from TeamStats where (home_team=?1 and FTR = 'H') or (away_team=?1 and FTR='A')")
    Optional<Integer>WinGameCount(String name);
    @Query("select count(*) from TeamStats where (home_team=?1 or away_team=?1) and FTR='D'")
    Optional<Integer>DrawGameCount(String name);
    /*
    @Query("select count(*) from TeamStats ts where ts.id in \n" +
            "(select ts2.id from TeamStats ts2 where (ts2.home_team = ?1 or ts2.away_team = ?1) order by ts2.date_time desc limit 10) \n" +
            "and ((ts.home_team=?1 and ts.FTR='H') or (ts.away_team=?1 and ts.FTR='A'))")
    Optional<Integer>Past10WinCount(String name);
    @Query("select count(*) from TeamStats ts where ts.id in \n" +
            "(select ts2.id from TeamStats ts2 where (ts2.home_team = ?1 or ts2.away_team = ?1) order by ts2.date_time desc limit 10) \n" +
            "and ts.FTR='D'")
    Optional<Integer>Past10DrawCount(String name);

    */
    @Query("select count(*) from TeamStats where home_team=?1")
    Optional<Integer>HomeGamePlayed(String name);
    @Query("select count(*) from TeamStats where home_team=?1 and FTR='H'")
    Optional<Integer>HomeGameWon(String name);
    @Query("select count(*) from TeamStats where home_team=?1 and FTR='D'")
    Optional<Integer>HomeGameDrew(String name);
    @Query("select count(*) from TeamStats where away_team=?1")
    Optional<Integer>AwayGamePlayed(String name);
    @Query("select count(*) from TeamStats where away_team=?1 and FTR='A'")
    Optional<Integer>AwayGameWon(String name);
    @Query("select count(*) from TeamStats where away_team=?1 and FTR='D'")
    Optional<Integer>AwayGameDrew(String name);
    @Query("select FTAG from TeamStats where away_team=?1")
    Optional<int[]>AllGoalAway(String name);
    @Query("select FTHG from TeamStats where away_team=?1")
    Optional<int[]>AllLossAway(String name);
    @Query("select FTHG from TeamStats where home_team=?1")
    Optional<int[]>AllGoalHome(String name);
    @Query("select FTAG from TeamStats where home_team=?1")
    Optional<int[]>AllLossHome(String name);
    @Query("select AVG(FTHG) from TeamStats")
    Optional<Float>MeanGoalAllHome();
    @Query("select AVG(FTAG) from TeamStats")
    Optional<Float>MeanGoalAllAway();
    @Query("select count(id) from TeamStats where home_team=?1 group by FTHG order by FTHG")
    Optional<List<Integer>>CountDiffGoalHome(String name);
    @Query("select count(id) from TeamStats where away_team=?1 group by FTAG order by FTAG")
    Optional<List<Integer>>CountDiffGoalAway(String name);
    /*
    @Query("select count(id) from TeamStats where away_team=?1 group by FTHG order by FTHG")
    Optional<List<Integer>>CountDiffLossAway(String name);
    @Query("select count(id) from TeamStats where home_team=?1 group by FTAG order by FTAG")
    Optional<List<Integer>>CountDiffLossHome(String name);
    */
    // Red/ Yellow
    @Query("select sum(HY) from TeamStats where home_team=?1")
    Optional<Integer>CountHomeYellowCard(String name);
    @Query("select sum(AY) from TeamStats where away_team=?1")
    Optional<Integer>CountAwayYellowCard(String name);
    @Query("select sum(HR) from TeamStats where home_team=?1")
    Optional<Integer>CountHomeRedCard(String name);
    @Query("select sum(AR) from TeamStats where home_team=?1")
    Optional<Integer>CountAwayRedCard(String name);
    @Query("select ts from TeamStats ts where (ts.home_team = ?1 or ts.away_team = ?1) order by ts.date_time desc limit 10")
    Optional<List<TeamStats> > GetLast10(String name);
    // corner
    @Query("select count(id) from TeamStats where home_team=?1 group by HC order by HC")
    Optional<List<Integer>> CountHomeCorner(String name);
    @Query("select count(id) from TeamStats where away_team=?1 group by AC order by AC")
    Optional<List<Integer>> CountAwayCorner(String name);
    /*
    @Query("select sum(ts.HY) from TeamStats ts where ts.id in \n" +
            "(select ts2.id from TeamStats ts2 where (ts2.home_team = ?1 or ts2.away_team = ?1) order by ts2.date_time desc limit 10)\n" +
            "and ts.home_team = ?1")
    Optional<Integer>Last10HomeSumYellowCard(String name);

    @Query("select sum(ts.AY) from TeamStats ts where ts.id in \n" +
            "(select ts2.id from TeamStats ts2 where (ts2.home_team = ?1 or ts2.away_team = ?1) order by ts2.date_time desc limit 10)\n" +
            "and ts.away_team = ?1")
    Optional<Integer>Last10AwaySumYellowCard(String name);
     */
}
