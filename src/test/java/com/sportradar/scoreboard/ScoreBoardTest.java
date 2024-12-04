package com.sportradar.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreBoardTest {

    private ScoreBoard scoreBoard;

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoard();
    }

    @Test
    void testStartMatchValidTeams() throws ScoreBoardException {
        // Arrange
        String homeTeam = "Team A";
        String awayTeam = "Team B";

        // Act
        Match match = scoreBoard.startMatch(homeTeam, awayTeam);

        // Assert
        assertNotNull(match, "Match should not be null");
        assertEquals(homeTeam, match.getHomeTeamName());
        assertEquals(awayTeam, match.getAwayTeamName());
        assertEquals(0, match.getAwayTeamScore());
        assertEquals(0, match.getHomeTeamScore());
    }

    @Test
    void testStartMatchWithSpecialCharactersInTeamName() throws ScoreBoardException {
        // Arrange
        String homeTeam = "Team A$";
        String awayTeam = "Team B!";

        // Act
        Match match = scoreBoard.startMatch(homeTeam, awayTeam);

        // Assert
        assertNotNull(match, "Match should not be null");
        assertEquals(homeTeam, match.getHomeTeamName());
        assertEquals(awayTeam, match.getAwayTeamName());
    }

    @Test
    void testStartMatchSameTeamNames() {
        // Arrange
        String team = "Team A";

        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.startMatch(team, team), "Should throw exception for same team names");
    }

    @Test
    void testStartMatchNullTeamName() {
        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.startMatch(null, "Team B"), "Should throw exception for null home team");
        assertThrows(ScoreBoardException.class, () -> scoreBoard.startMatch("Team A", null), "Should throw exception for null away team");
    }

    @Test
    void testStartMatchEmptyTeamName() {
        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.startMatch("", "Team B"), "Should throw exception for empty home team");
        assertThrows(ScoreBoardException.class, () -> scoreBoard.startMatch("Team A", ""), "Should throw exception for empty away team");
    }

    @Test
    void testStartMatchBlankTeamName() {
        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.startMatch("  ", "Team B"), "Should throw exception for blank home team");
        assertThrows(ScoreBoardException.class, () -> scoreBoard.startMatch("Team A", "  "), "Should throw exception for blank away team");
    }

    @Test
    void testUpdateScoreValidInput() throws ScoreBoardException {
        // Arrange
        Match match = scoreBoard.startMatch("Team A", "Team B");

        // Act
        Match updatedMatch = scoreBoard.updateScore(match, (short) 3, (short) 2);

        // Assert
        assertEquals(3, updatedMatch.getHomeTeamScore());
        assertEquals(2, updatedMatch.getAwayTeamScore());
    }

    @Test
    void testUpdateScoreNegativeScores() {
        // Arrange
        Match match = scoreBoard.startMatch("Team A", "Team B");

        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.updateScore(match, (short) -1, (short) 2), "Should throw exception for negative home score");
        assertThrows(ScoreBoardException.class, () -> scoreBoard.updateScore(match, (short) 2, (short) -1), "Should throw exception for negative away score");
    }

    @Test
    void testUpdateScoreNullMatch() {
        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.updateScore(null, (short) 1, (short) 1), "Should throw exception for null match");
    }

    @Test
    void testUpdateScoreMatchNotFromBoard() {
        // Arrange
        Match match = new Match("Team A", "Team B", System.currentTimeMillis());

        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.updateScore(match, (short) 1, (short) 1), "Should throw exception for matches out of board");
    }

    @Test
    void testFinishMatchValidInput() throws ScoreBoardException {
        // Arrange
        Match match = scoreBoard.startMatch("Team A", "Team B");

        // Act
        scoreBoard.finishMatch(match);

        // Assert
        List<Match> matches = scoreBoard.getSortedMatchesInProgress();
        assertFalse(matches.contains(match), "Finished match should not be in the list of matches in progress");
    }

    @Test
    void testFinishMatchAlreadyFinished() throws ScoreBoardException {
        // Arrange
        Match match = scoreBoard.startMatch("Team A", "Team B");

        // Act
        scoreBoard.finishMatch(match);

        // Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.finishMatch(match), "Should throw exception for finishing already finished match");
    }

    @Test
    void testFinishMatchNotFromBoard() {
        // Arrange
        Match match = new Match("Team A", "Team B", System.currentTimeMillis());

        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.finishMatch(match), "Should throw exception for matches out of board");
    }

    @Test
    void testFinishMatchNullInput() {
        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.finishMatch(null), "Should throw exception for null match");
    }

    @Test
    void testGetSortedMatchesInProgressEmptyList() {
        // Act
        List<Match> matches = scoreBoard.getSortedMatchesInProgress();

        // Assert
        assertNotNull(matches, "Match list should not be null");
        assertTrue(matches.isEmpty(), "Match list should be empty initially");
    }

    @Test
    void testGetSortedMatchesInProgressSorting() throws ScoreBoardException {
        // Arrange
        Match matchA = scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.updateScore(matchA, (short) 0, (short) 5);
        Match matchB = scoreBoard.startMatch("Spain", "Brazil");
        scoreBoard.updateScore(matchB, (short) 10, (short) 2);
        Match matchC = scoreBoard.startMatch("Germany", "France");
        scoreBoard.updateScore(matchC, (short) 2, (short) 2);
        Match matchD = scoreBoard.startMatch("Uruguay", "Italy");
        scoreBoard.updateScore(matchD, (short) 6, (short) 6);
        Match matchE = scoreBoard.startMatch("Argentina", "Australia");
        scoreBoard.updateScore(matchE, (short) 3, (short) 1);

        // Act
        List<Match> matches = scoreBoard.getSortedMatchesInProgress();

        // Assert
        assertEquals(5, matches.size(), "Should contain all active matches");
        assertEquals(matchD, matches.get(0), "Matches should be sorted correctly");
        assertEquals(matchB, matches.get(1), "Matches should be sorted correctly");
        assertEquals(matchA, matches.get(2), "Matches should be sorted correctly");
        assertEquals(matchE, matches.get(3), "Matches should be sorted correctly");
        assertEquals(matchC, matches.get(4), "Matches should be sorted correctly");
    }

}
