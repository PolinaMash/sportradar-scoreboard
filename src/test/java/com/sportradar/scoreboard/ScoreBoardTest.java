package com.sportradar.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreBoardTest {
    private static final String HOME_TEAM_NAME = "Team A";
    private static final String AWAY_TEAM_NAME = "Team B";
    private static final String HOME_TEAM_NAME2 = "Team C";
    private static final String AWAY_TEAM_NAME2 = "Team D";

    private ScoreBoard scoreBoard;

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoard();
    }

    @Test
    void testStartMatchValidTeams() throws ScoreBoardException {
        // Act
        ImmutableMatch match = scoreBoard.startMatch(HOME_TEAM_NAME, AWAY_TEAM_NAME);

        // Assert
        assertNotNull(match, "Match should not be null");
        assertEquals(HOME_TEAM_NAME, match.homeTeamName());
        assertEquals(AWAY_TEAM_NAME, match.awayTeamName());
        assertEquals(0, match.awayTeamScore());
        assertEquals(0, match.homeTeamScore());

        List<ImmutableMatch> matches = scoreBoard.getSortedMatchesInProgress();
        assertTrue(matches.contains(match), "Started match should be in the list of matches in progress");
    }

    @Test
    void testStartMatchWithSpecialCharactersInTeamName() throws ScoreBoardException {
        // Arrange
        String homeTeam = HOME_TEAM_NAME + "$";
        String awayTeam = AWAY_TEAM_NAME + "!";

        // Act
        ImmutableMatch match = scoreBoard.startMatch(homeTeam, awayTeam);

        // Assert
        assertNotNull(match, "Match should not be null");
        assertEquals(homeTeam, match.homeTeamName());
        assertEquals(awayTeam, match.awayTeamName());
    }

    @Test
    void testStartMatchSameTeamNames() {
        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.startMatch(HOME_TEAM_NAME, HOME_TEAM_NAME), "Should throw exception for same team names");
        assertTrue(scoreBoard.getSortedMatchesInProgress().isEmpty(), "No match should be added to score board");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void testStartMatchNullBlankEmptyTeamName(String value) {
        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.startMatch(value, AWAY_TEAM_NAME), "Should throw exception for null/empty/blank home team");
        assertThrows(ScoreBoardException.class, () -> scoreBoard.startMatch(HOME_TEAM_NAME, value), "Should throw exception for null/empty/blank away team");
    }

    @Test
    void testStartMatchAlreadyStarted() throws ScoreBoardException {
        // Arrange
        scoreBoard.startMatch(HOME_TEAM_NAME, AWAY_TEAM_NAME);

        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.startMatch(HOME_TEAM_NAME, AWAY_TEAM_NAME), "Should throw exception for already started match");
    }

    @Test
    void testUpdateScoreValidInput() throws ScoreBoardException {
        // Arrange
        scoreBoard.startMatch(HOME_TEAM_NAME, AWAY_TEAM_NAME);

        // Act
        ImmutableMatch updatedMatch = scoreBoard.updateScore(HOME_TEAM_NAME, AWAY_TEAM_NAME, (short) 3, (short) 2);

        // Assert
        assertEquals(3, updatedMatch.homeTeamScore());
        assertEquals(2, updatedMatch.awayTeamScore());
    }

    @Test
    void testUpdateScoreNegativeScores() {
        // Arrange
        scoreBoard.startMatch(HOME_TEAM_NAME, AWAY_TEAM_NAME);

        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.updateScore(HOME_TEAM_NAME, AWAY_TEAM_NAME, (short) -1, (short) 2), "Should throw exception for negative home score");
        assertThrows(ScoreBoardException.class, () -> scoreBoard.updateScore(HOME_TEAM_NAME, AWAY_TEAM_NAME, (short) 2, (short) -1), "Should throw exception for negative away score");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void testUpdateScoreNullEmptyBlankMatch(String value) {
        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.updateScore(HOME_TEAM_NAME, value, (short) 1, (short) 1), "Should throw exception for null match");
        assertThrows(ScoreBoardException.class, () -> scoreBoard.updateScore(value, AWAY_TEAM_NAME, (short) 1, (short) 1), "Should throw exception for null match");
    }

    @Test
    void testUpdateScoreMatchNotFromBoard() {
        // Arrange
        scoreBoard.startMatch(HOME_TEAM_NAME, AWAY_TEAM_NAME);

        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.updateScore(HOME_TEAM_NAME, AWAY_TEAM_NAME2, (short) 1, (short) 1), "Should throw exception for matches out of board");
        assertThrows(ScoreBoardException.class, () -> scoreBoard.updateScore(HOME_TEAM_NAME2, AWAY_TEAM_NAME, (short) 1, (short) 1), "Should throw exception for matches out of board");
        assertThrows(ScoreBoardException.class, () -> scoreBoard.updateScore(HOME_TEAM_NAME2, AWAY_TEAM_NAME2, (short) 1, (short) 1), "Should throw exception for matches out of board");
    }

    @Test
    void testFinishMatchValidInput() throws ScoreBoardException {
        // Arrange
        ImmutableMatch match = scoreBoard.startMatch(HOME_TEAM_NAME, AWAY_TEAM_NAME);

        // Act
        scoreBoard.finishMatch(HOME_TEAM_NAME, AWAY_TEAM_NAME);

        // Assert
        List<ImmutableMatch> matches = scoreBoard.getSortedMatchesInProgress();
        assertFalse(matches.contains(match), "Finished match should not be in the list of matches in progress");
    }

    @Test
    void testFinishMatchAlreadyFinished() throws ScoreBoardException {
        // Arrange
        scoreBoard.startMatch(HOME_TEAM_NAME, AWAY_TEAM_NAME);

        // Act
        scoreBoard.finishMatch(HOME_TEAM_NAME, AWAY_TEAM_NAME);

        // Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.finishMatch(HOME_TEAM_NAME, AWAY_TEAM_NAME), "Should throw exception for finishing already finished match");
    }

    @Test
    void testFinishMatchNotFromBoard() {
        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.finishMatch(HOME_TEAM_NAME, AWAY_TEAM_NAME), "Should throw exception for matches out of board");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void testFinishMatchNullInput(String value) {
        // Act & Assert
        assertThrows(ScoreBoardException.class, () -> scoreBoard.finishMatch(HOME_TEAM_NAME, value), "Should throw exception for null match");
        assertThrows(ScoreBoardException.class, () -> scoreBoard.finishMatch(value, AWAY_TEAM_NAME), "Should throw exception for null match");
    }

    @Test
    void testGetSortedMatchesInProgressEmptyList() {
        // Act
        List<ImmutableMatch> matches = scoreBoard.getSortedMatchesInProgress();

        // Assert
        assertNotNull(matches, "Match list should not be null");
        assertTrue(matches.isEmpty(), "Match list should be empty initially");
    }

    @Test
    void testGetSortedMatchesInProgressSorting() throws ScoreBoardException {
        // Arrange
        ImmutableMatch matchA = scoreBoard.startMatch("Mexico", "Canada");
        matchA = scoreBoard.updateScore("Mexico", "Canada", (short) 0, (short) 5);
        ImmutableMatch matchB = scoreBoard.startMatch("Spain", "Brazil");
        matchB = scoreBoard.updateScore("Spain", "Brazil", (short) 10, (short) 2);
        ImmutableMatch matchC = scoreBoard.startMatch("Germany", "France");
        matchC = scoreBoard.updateScore("Germany", "France", (short) 2, (short) 2);
        ImmutableMatch matchD = scoreBoard.startMatch("Uruguay", "Italy");
        matchD = scoreBoard.updateScore("Uruguay", "Italy", (short) 6, (short) 6);
        ImmutableMatch matchE = scoreBoard.startMatch("Argentina", "Australia");
        matchE = scoreBoard.updateScore("Argentina", "Australia", (short) 3, (short) 1);

        // Act
        List<ImmutableMatch> matches = scoreBoard.getSortedMatchesInProgress();

        // Assert
        assertEquals(5, matches.size(), "Should contain all active matches");
        assertEquals(matchD, matches.get(0), "Matches should be sorted correctly");
        assertEquals(matchB, matches.get(1), "Matches should be sorted correctly");
        assertEquals(matchA, matches.get(2), "Matches should be sorted correctly");
        assertEquals(matchE, matches.get(3), "Matches should be sorted correctly");
        assertEquals(matchC, matches.get(4), "Matches should be sorted correctly");
    }

}
