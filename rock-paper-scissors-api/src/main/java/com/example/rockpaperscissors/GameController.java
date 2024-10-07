package com.example.rockpaperscissors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private Map<String, Game> gameStore = new HashMap<>();
    private GameRule gameRule;  // The strategy for determining the winner

    public GameController() {
        this.gameRule = new StandardGameRule();  // Default to the standard rule
    }

    // Create a new game
    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody Map<String, String> request) {
        String playerName = request.get("name");
        if (playerName == null || playerName.isEmpty()) {
            System.out.println("Error: Player name is missing.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Player name is required", HttpStatus.BAD_REQUEST.value()));
        }

        String gameId = "game-" + System.currentTimeMillis();
        Game game = new Game();
        game.setPlayer1(playerName);
        game.setStatus("WAITING");
        gameStore.put(gameId, game);

        System.out.println("Game created: " + game);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body("Game created with ID: " + gameId);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinGame(@PathVariable String id, @RequestBody Map<String, String> request) {
        String playerName = request.get("name");
        if (playerName == null || playerName.isEmpty()) {
            System.out.println("Error: Player name is missing.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Player name is required", HttpStatus.BAD_REQUEST.value()));
        }

        Game game = gameStore.get(id);
        if (game == null) {
            System.out.println("Error: Game with ID " + id + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Game not found", HttpStatus.NOT_FOUND.value()));
        }

        if (!game.getStatus().equals("WAITING")) {
            System.out.println("Error: Game with ID " + id + " is already in progress or finished.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Game already in progress or finished", HttpStatus.BAD_REQUEST.value()));
        }

        game.setPlayer2(playerName);
        game.setStatus("IN_PROGRESS");

        System.out.println("Game after join: " + game);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(playerName + " joined the game with ID: " + id);
    }

    // Make a move in the game
    @PostMapping("/{id}/move")
    public ResponseEntity<?> makeMove(@PathVariable String id, @RequestBody Map<String, String> request) {
        String playerName = request.get("name");
        String moveString = request.get("move");

        if (playerName == null || playerName.isEmpty()) {
            System.out.println("Error: Player name is missing.");
            return new ResponseEntity<>(new ErrorResponse("Player name is required", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        if (moveString == null || moveString.isEmpty()) {
            System.out.println("Error: Move is missing.");
            return new ResponseEntity<>(new ErrorResponse("Move is required", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

        Game game = gameStore.get(id);
        if (game == null) {
            System.out.println("Error: Game with ID " + id + " not found.");
            return new ResponseEntity<>(new ErrorResponse("Game not found", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        if (!game.getStatus().equals("IN_PROGRESS")) {
            System.out.println("Error: Game with ID " + id + " is not in progress.");
            return new ResponseEntity<>(new ErrorResponse("Game not in progress", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

        // Convert the move string to the Move enum
        Move move;
        try {
            move = Move.valueOf(moveString.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid move provided.");
            return new ResponseEntity<>(new ErrorResponse("Invalid move. Must be Rock, Paper, or Scissors.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

        // Assign the move to the correct player
        if (playerName.equalsIgnoreCase(game.getPlayer1())) {
            game.setPlayer1Move(move);
            System.out.println("Player 1 move set to: " + game.getPlayer1Move());
        } else if (playerName.equalsIgnoreCase(game.getPlayer2())) {
            game.setPlayer2Move(move);
            System.out.println("Player 2 move set to: " + game.getPlayer2Move());
        } else {
            System.out.println("Error: Invalid player trying to make a move.");
            return new ResponseEntity<>(new ErrorResponse("Invalid player", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

        // Check if both players have made their moves
        if (game.getPlayer1Move() != null && game.getPlayer2Move() != null) {
            // Use the strategy to determine the winner
            String winner = gameRule.determineWinner(game.getPlayer1Move(), game.getPlayer2Move());

            // Set the winnerName based on the result
            if ("Player1".equals(winner)) {
                game.setWinnerName(game.getPlayer1());
            } else if ("Player2".equals(winner)) {
                game.setWinnerName(game.getPlayer2());
            } else {
                game.setWinnerName("TIE");
            }

            game.setStatus("FINISHED");
        }

        System.out.println("After move - " + game);

        return ResponseEntity.ok("Move accepted for game " + id);
    }

    // Get the game state
    @GetMapping("/{id}")
    public ResponseEntity<?> getGameState(@PathVariable String id) {
        Game game = gameStore.get(id);
        if (game == null) {
            System.out.println("Error: Game with ID " + id + " not found.");
            return new ResponseEntity<>(new ErrorResponse("Game not found", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(game);
    }
}