package com.example.rockpaperscissors;

public interface GameRule {
    String determineWinner(Move move1, Move move2);
}