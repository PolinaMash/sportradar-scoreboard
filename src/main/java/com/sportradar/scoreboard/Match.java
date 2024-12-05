package com.sportradar.scoreboard;

public class Match implements Comparable<Match> {
    private String homeTeamName;
    private short homeTeamScore;
    private String awayTeamName;
    private short awayTeamScore;
    private long startTime;

    public Match(String homeTeamName, String awayTeamName, long startTime) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.startTime = startTime;
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

    int getTotalScore() {
        return homeTeamScore + awayTeamScore;
    }

    public void setScore(short homeTeamScore, short awayTeamScore) {
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
    }

    @Override
    public int compareTo(Match other) {
        int thisTotalScore = this.getTotalScore();
        int otherTotalScore = other.getTotalScore();

        if (thisTotalScore != otherTotalScore) {
            return Integer.compare(otherTotalScore, thisTotalScore); // Descending
        }

        return Long.compare(other.startTime, this.startTime); // Descending
    }

}
