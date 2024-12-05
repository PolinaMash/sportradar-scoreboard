package com.sportradar.scoreboard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {
    private static final String HOME_TEAM_NAME = "Team A";
    private static final String AWAY_TEAM_NAME = "Team B";
    private static final String HOME_TEAM_NAME2 = "Team C";
    private static final String AWAY_TEAM_NAME2 = "Team D";

    @Test
    public void testConstructorAndGetters() {
        long startTime = System.currentTimeMillis();
        Match match = new Match(HOME_TEAM_NAME, AWAY_TEAM_NAME, startTime);

        assertEquals(HOME_TEAM_NAME, match.getHomeTeamName(), "Home team name mismatch");
        assertEquals(AWAY_TEAM_NAME, match.getAwayTeamName(), "Away team name mismatch");
        assertEquals(0, match.getHomeTeamScore(), "Initial home team score should be 0");
        assertEquals(0, match.getAwayTeamScore(), "Initial away team score should be 0");
    }

    @Test
    public void testSetScores() {
        Match match = new Match(HOME_TEAM_NAME, AWAY_TEAM_NAME, System.currentTimeMillis());
        match.setScore((short) 3, (short) 2);

        assertEquals(3, match.getHomeTeamScore(), "Home team score mismatch");
        assertEquals(2, match.getAwayTeamScore(), "Away team score mismatch");
    }

    @Test
    public void testGetTotalScore() {
        Match match = new Match(HOME_TEAM_NAME, AWAY_TEAM_NAME, System.currentTimeMillis());
        match.setScore((short) 3, (short) 2);

        assertEquals(5, match.getTotalScore(), "Total score mismatch");
    }

    @Test
    public void testCompareToDifferentScores() {
        Match match1 = new Match(HOME_TEAM_NAME, AWAY_TEAM_NAME, System.currentTimeMillis());
        match1.setScore((short) 5, (short) 3);

        Match match2 = new Match(HOME_TEAM_NAME2, AWAY_TEAM_NAME2, System.currentTimeMillis());
        match2.setScore((short) 4, (short) 2);

        assertTrue(match1.compareTo(match2) < 0, "Match1 should come before Match2 due to higher total score");
    }

    @Test
    public void testCompareToSameScoresDifferentStartTime() {
        long earlierTime = System.currentTimeMillis() - 1000;
        long laterTime = System.currentTimeMillis();

        Match match1 = new Match(HOME_TEAM_NAME, AWAY_TEAM_NAME, earlierTime);
        match1.setScore((short) 3, (short) 2);

        Match match2 = new Match(HOME_TEAM_NAME2, AWAY_TEAM_NAME2, laterTime);
        match2.setScore((short) 3, (short) 2);

        assertTrue(match2.compareTo(match1) < 0, "Match2 should come before Match1 due to later start time");
    }

    @Test
    public void testCompareToSameScoresAndStartTime() {
        long startTime = System.currentTimeMillis();

        Match match1 = new Match(HOME_TEAM_NAME, AWAY_TEAM_NAME, startTime);
        match1.setScore((short) 3, (short) 2);

        Match match2 = new Match(HOME_TEAM_NAME2, AWAY_TEAM_NAME2, startTime);
        match2.setScore((short) 3, (short) 2);
        assertEquals(0, match1.compareTo(match2), "Matches with the same score and start time should be equal in comparison");
    }
}
