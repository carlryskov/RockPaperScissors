package com.example.rockpaperscissors;

public class Game {
    private String id;
    private String player1;
    private String player2;
    private Move player1Move;
    private Move player2Move;
    private String status;  // "WAITING", "IN_PROGRESS", "FINISHED"
    private String winner;  // "Player1", "Player2", or "TIE"

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public void setPlayer1Move(Move player1Move) {
        this.player1Move = player1Move;
    }

    public void setPlayer2Move(Move player2Move) {
        this.player2Move = player2Move;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public Move getPlayer1Move() {
        return player1Move;
    }

    public Move getPlayer2Move() {
        return player2Move;
    }

    public String getStatus() {
        return status;
    }

    public String getWinner() {
        return winner;
    }

    // toString() method for easier debugging
    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                ", player1Move=" + player1Move +
                ", player2Move=" + player2Move +
                ", status='" + status + '\'' +
                ", winner='" + winner + '\'' +
                '}';
    }
}