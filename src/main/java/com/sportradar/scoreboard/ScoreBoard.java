package com.sportradar.scoreboard;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class ScoreBoard {
    List<Match> matches = new ArrayList<>();

    public Match startMatch(String homeTeamName, String awayTeamName) throws ScoreBoardException {
        if (StringUtils.isBlank(homeTeamName) || StringUtils.isBlank(awayTeamName)) {
            throw new ScoreBoardException("Both home team name and away team name should be not empty values");
        }
        if (homeTeamName.equals(awayTeamName)) {
            throw new ScoreBoardException("Teams within one match cannot be equal");
        }

        Match match = new Match(homeTeamName, awayTeamName, System.nanoTime());
        matches.add(match);
        return match;
    }

    public Match updateScore(Match match, short homeTeamScore, short awayTeamScore) throws ScoreBoardException {
        if (match == null) {
            throw new ScoreBoardException("Match cannot be null");
        } else if (!matches.contains(match)) {
            throw new ScoreBoardException("Match to update should be from score board");
        } else if (homeTeamScore < 0 || awayTeamScore < 0) {
            throw new ScoreBoardException("Score both for home team and away team cannot be negative");
        }
        match.setScore(homeTeamScore, awayTeamScore);
        return match;
    }

    public void finishMatch(Match match) throws ScoreBoardException {
        if (match == null) {
            throw new ScoreBoardException("Match cannot be null");
        } else if (!matches.contains(match)) {
            throw new ScoreBoardException("Match to update should be from score board");
        }
        matches.remove(match);
    }

    public List<Match> getSortedMatchesInProgress() {
        Collections.sort(matches);
        return Collections.unmodifiableList(matches);
    }

}
