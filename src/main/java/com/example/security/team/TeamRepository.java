package com.example.security.team;

import com.example.security.teamStats.TeamStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Optional<Team> findById(Integer id);
    @Query("SELECT rank FROM Team where name=?1")
    Optional<Integer> getRankByName(String name);

}
