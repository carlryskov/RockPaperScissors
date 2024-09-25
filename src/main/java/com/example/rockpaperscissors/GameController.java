package com.example.rockpaperscissors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private Map<String, Game> gameStore = new HashMap<>();

    // Create a new game
    @PostMapping
    public ResponseEntity<String> createGame(@RequestBody Map<String, String> request) {
        String playerName = request.get("name");
        String gameId = "game-" + System.currentTimeMillis();
        Game game = new Game();
        game.setId(gameId);
        game.setPlayer1(playerName);
        game.setStatus("WAITING");
        gameStore.put(gameId, game);

        System.out.println("Game created: " + game);

        return ResponseEntity.ok("Game created with ID: " + gameId);
    }

    // Join an existing game
    @PostMapping("/{id}/join")
    public ResponseEntity<String> joinGame(@PathVariable String id, @RequestBody Map<String, String> request) {
        String playerName = request.get("name");
        Game game = gameStore.get(id);
        if (game == null || !game.getStatus().equals("WAITING")) {
            return ResponseEntity.badRequest().body("Game not found or already in progress.");
        }
        game.setPlayer2(playerName);
        game.setStatus("IN_PROGRESS");

        System.out.println("Game after join: " + game);

        return ResponseEntity.ok(playerName + " joined the game with ID: " + id);
    }

    // Make a move in the game
    @PostMapping("/{id}/move")
    public ResponseEntity<String> makeMove(@PathVariable String id, @RequestBody Map<String, String> request) {
        String playerName = request.get("name");
        String moveString = request.get("move");

        Game game = gameStore.get(id);
        if (game == null || !game.getStatus().equals("IN_PROGRESS")) {
            return ResponseEntity.badRequest().body("Game not found or not in progress.");
        }

        // Convert the move String to the Move enum
        Move move;
        try {
            move = Move.valueOf(moveString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid move. Must be Rock, Paper, or Scissors.");
        }

        // Assign the move to the correct player
        if (playerName.equalsIgnoreCase(game.getPlayer1())) {
            game.setPlayer1Move(move);
            System.out.println("Player 1 move set to: " + game.getPlayer1Move());
        } else if (playerName.equalsIgnoreCase(game.getPlayer2())) {
            game.setPlayer2Move(move);
            System.out.println("Player 2 move set to: " + game.getPlayer2Move());
        } else {
            return ResponseEntity.badRequest().body("Invalid player.");
        }

        // Check if both players have made their moves
        if (game.getPlayer1Move() != null && game.getPlayer2Move() != null) {
            // Determine the winner
            String winner = determineWinner(game.getPlayer1Move(), game.getPlayer2Move());
            game.setWinner(winner);
            game.setStatus("FINISHED");
        }

        System.out.println("After move - " + game);

        return ResponseEntity.ok("Move accepted for game " + id);
    }

    // Get the game state
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameState(@PathVariable String id) {
        Game game = gameStore.get(id);
        if (game == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(game);
    }

    // Determine the winner based on the moves
    private String determineWinner(Move move1, Move move2) {
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