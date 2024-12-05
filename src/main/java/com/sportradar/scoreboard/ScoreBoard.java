package com.sportradar.scoreboard;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreBoard {
    List<Match> matches = new ArrayList<>();

    public ImmutableMatch startMatch(String homeTeamName, String awayTeamName) throws ScoreBoardException {
        validateTeamNames(homeTeamName, awayTeamName);
        validateMatchIsNotLive(homeTeamName, awayTeamName);

        Match match = new Match(homeTeamName, awayTeamName, System.nanoTime());
        matches.add(match);
        return new ImmutableMatch(match);
    }

    public ImmutableMatch updateScore(String homeTeamName, String awayTeamName, short homeTeamScore, short awayTeamScore) throws ScoreBoardException {
        validateTeamScore(homeTeamScore, awayTeamScore);
        Match match = findAndValidateExistingMatch(homeTeamName, awayTeamName);

        match.setScore(homeTeamScore, awayTeamScore);
        return new ImmutableMatch(match);
    }

    public void finishMatch(String homeTeamName, String awayTeamName) throws ScoreBoardException {
        Match match = findAndValidateExistingMatch(homeTeamName, awayTeamName);
        matches.remove(match);
    }

    public List<ImmutableMatch> getSortedMatchesInProgress() {
        Collections.sort(matches);
        return Collections.unmodifiableList(matches.stream().map(ImmutableMatch::new).collect(Collectors.toList()));
    }

    private Match findMatch(String homeTeamName, String awayTeamName) {
        return matches.stream()
                .filter(match -> match.getHomeTeamName().equals(homeTeamName) && match.getAwayTeamName().equals(awayTeamName))
                .findFirst()
                .orElse(null);
    }

    private void validateTeamScore(short homeTeamScore, short awayTeamScore) {
        if (homeTeamScore < 0 || awayTeamScore < 0) {
            throw new ScoreBoardException("Score both for home team and away team cannot be negative");
        }
    }

    private void validateTeamNames(String homeTeamName, String awayTeamName) {
        if (StringUtils.isBlank(homeTeamName) || StringUtils.isBlank(awayTeamName)) {
            throw new ScoreBoardException("Both home team name and away team name should be not empty string");
        }
        if (homeTeamName.equals(awayTeamName)) {
            throw new ScoreBoardException("Teams within one match cannot be equal");
        }
    }

    private Match findAndValidateExistingMatch(String homeTeamName, String awayTeamName) {
        validateTeamNames(homeTeamName, awayTeamName);
        Match match = findMatch(homeTeamName, awayTeamName);
        if (match == null) {
            throw new ScoreBoardException("Match to update should be from score board");
        }
        return match;
    }

    private void validateMatchIsNotLive(String homeTeamName, String awayTeamName) {
        if (findMatch(homeTeamName, awayTeamName) != null) {
            throw new ScoreBoardException("Match is already live");
        }
    }

}
