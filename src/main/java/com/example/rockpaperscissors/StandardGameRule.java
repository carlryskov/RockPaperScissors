package com.example.rockpaperscissors;

public class StandardGameRule implements GameRule {

    @Override
    public String determineWinner(Move move1, Move move2) {
        if (move1 == move2) {
            return "TIE";
        }

        switch (move1) {
            case ROCK:
                return (move2 == Move.SCISSORS) ? "Player1" : "Player2";
            case PAPER:
                return (move2 == Move.ROCK) ? "Player1" : "Player2";
            case SCISSORS:
                return (move2 == Move.PAPER) ? "Player1" : "Player2";
            default:
                throw new IllegalArgumentException("Unknown move: " + move1);
        }
    }
}