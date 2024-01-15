package com.example.mad_assignment1_final;

public class PlayerData {

    private String playerName;
    private int wins;
    private int losses;
    private int draws;
    private int totalGames; // Total games played
    private double winPercentage; // Win percentage




    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }
    public int getTotalGames() {
        return totalGames;
    }

    public double getWinPercentage() {
        return winPercentage;
    }
//    public void setWins(int wins) {
//        this.wins = wins;
//        this.totalGames = wins + losses + draws;
//        this.winPercentage = calculateWinPercentage();
//    }

//    public void setLosses(int losses) {
//        this.losses = losses;
//        this.totalGames = wins + losses + draws;
//        this.winPercentage = calculateWinPercentage();
//    }
//
//    public void setDraws(int draws) {
//        this.draws = draws;
//        this.totalGames = wins + losses + draws;
//        this.winPercentage = calculateWinPercentage();
//    }


    public PlayerData(String playerName, int wins, int losses, int draws, int totalGames, int winPercentage) {
        this.playerName = playerName;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.totalGames = totalGames;
        this.winPercentage = winPercentage;
    }





}
