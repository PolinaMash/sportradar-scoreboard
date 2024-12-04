package com.sportradar.scoreboard;

public class Match {
    private String homeTeamName;
    private short homeTeamScore;
    private String awayTeamName;
    private short awayTeamScore;

    public Match(String homeTeamName, String awayTeamName) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public short getHomeTeamScore() {
        return homeTeamScore;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public short getAwayTeamScore() {
        return awayTeamScore;
    }
}
