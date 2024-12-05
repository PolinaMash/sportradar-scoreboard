package com.sportradar.scoreboard;

public record ImmutableMatch(String homeTeamName, short homeTeamScore, String awayTeamName, short awayTeamScore) {

    public ImmutableMatch(Match match) {
        this(match.getHomeTeamName(), match.getHomeTeamScore(), match.getAwayTeamName(), match.getAwayTeamScore());
    }

    @Override
    public String toString() {
        return homeTeamName + " " + homeTeamScore + " - " + awayTeamName + " " + awayTeamScore;
    }
}
