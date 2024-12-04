package com.sportradar.scoreboard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {

    @Test
    public void testConstructorAndGetters() {
        long startTime = System.currentTimeMillis();
        Match match = new Match("Team A", "Team B", startTime);

        assertEquals("Team A", match.getHomeTeamName(), "Home team name mismatch");
        assertEquals("Team B", match.getAwayTeamName(), "Away team name mismatch");
        assertEquals(0, match.getHomeTeamScore(), "Initial home team score should be 0");
        assertEquals(0, match.getAwayTeamScore(), "Initial away team score should be 0");
    }

    @Test
    public void testSetScores() {
        Match match = new Match("Team A", "Team B", System.currentTimeMillis());
        match.setScore((short) 3, (short) 2);

        assertEquals(3, match.getHomeTeamScore(), "Home team score mismatch");
        assertEquals(2, match.getAwayTeamScore(), "Away team score mismatch");
    }

    @Test
    public void testGetTotalScore() {
        Match match = new Match("Team A", "Team B", System.currentTimeMillis());
        match.setScore((short) 3, (short) 2);

        assertEquals(5, match.getTotalScore(), "Total score mismatch");
    }

    @Test
    public void testEqualsAndHashCode() {
        long startTime = System.currentTimeMillis();
        Match match1 = new Match("Team A", "Team B", startTime);
        match1.setScore((short) 3, (short) 2);

        Match match2 = new Match("Team A", "Team B", startTime);
        match2.setScore((short) 3, (short) 2);

        Match match3 = new Match("Team C", "Team D", startTime);
        match3.setScore((short) 3, (short) 2);

        assertEquals(match1, match2, "Matches with the same fields should be equal");
        assertEquals(match1.hashCode(), match2.hashCode(), "HashCodes of equal objects should be the same");
        assertNotEquals(match1, match3, "Matches with different fields should not be equal");
    }

    @Test
    public void testCompareToDifferentScores() {
        Match match1 = new Match("Team A", "Team B", System.currentTimeMillis());
        match1.setScore((short) 5, (short) 3);

        Match match2 = new Match("Team C", "Team D", System.currentTimeMillis());
        match2.setScore((short) 4, (short) 2);

        assertTrue(match1.compareTo(match2) < 0, "Match1 should come before Match2 due to higher total score");
    }

    @Test
    public void testCompareToSameScoresDifferentStartTime() {
        long earlierTime = System.currentTimeMillis() - 1000;
        long laterTime = System.currentTimeMillis();

        Match match1 = new Match("Team A", "Team B", earlierTime);
        match1.setScore((short) 3, (short) 2);

        Match match2 = new Match("Team C", "Team D", laterTime);
        match2.setScore((short) 3, (short) 2);

        assertTrue(match2.compareTo(match1) < 0, "Match2 should come before Match1 due to later start time");
    }

    @Test
    public void testCompareToSameScoresAndStartTime() {
        long startTime = System.currentTimeMillis();

        Match match1 = new Match("Team A", "Team B", startTime);
        match1.setScore((short) 3, (short) 2);

        Match match2 = new Match("Team C", "Team D", startTime);
        match2.setScore((short) 3, (short) 2);
        assertEquals(0, match1.compareTo(match2), "Matches with the same score and start time should be equal in comparison");
    }
}
