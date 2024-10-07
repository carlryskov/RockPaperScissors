package com.example.rockpaperscissors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;  // For converting objects to JSON strings

    @Test
    public void shouldCreateGameSuccessfully() throws Exception {
        // Define request payload
        String playerName = "{\"name\": \"Player1\"}";

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerName))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Game created with ID")));
    }

    @Test
    public void shouldReturnBadRequestWhenPlayerNameIsMissing() throws Exception {
        String invalidRequest = "{}";

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)  // Ensure the request expects JSON response
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Validate JSON response
                .andExpect(jsonPath("$.message").value("Player name is required"));
    }

    @Test
    public void shouldJoinGameSuccessfully() throws Exception {
        // First, create a game
        String player1 = "{\"name\": \"Player1\"}";
        String gameId = mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(player1))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .split(":")[1]
                .trim()
                .replace("\"", ""); // Extract the actual ID and remove quotes

        // Now, join the game with Player 2
        String player2 = "{\"name\": \"Player2\"}";
        mockMvc.perform(post("/api/games/" + gameId + "/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(player2))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Player2 joined the game")));
    }

    @Test
    public void shouldReturnGameNotFoundWhenJoiningNonExistentGame() throws Exception {
        String player2 = "{\"name\": \"Player2\"}";

        mockMvc.perform(post("/api/games/non-existent-game-id/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)  // Ensure the request expects JSON response
                        .content(player2))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Validate JSON response
                .andExpect(jsonPath("$.message").value("Game not found"));
    }

    @Test
    public void shouldMakeMoveSuccessfully() throws Exception {
        // Create a game and join with both players first
        String player1 = "{\"name\": \"Player1\"}";
        String player2 = "{\"name\": \"Player2\"}";

        String gameId = mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(player1))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .split(":")[1]
                .trim()
                .replace("\"", "");  // Extract the game ID and remove quotes

        // Player 2 joins
        mockMvc.perform(post("/api/games/" + gameId + "/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(player2))
                .andExpect(status().isOk());

        // Now, Player1 makes a move
        String movePlayer1 = "{\"name\": \"Player1\", \"move\": \"ROCK\"}";
        mockMvc.perform(post("/api/games/" + gameId + "/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(movePlayer1))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Move accepted for game")));
    }

    @Test
    public void shouldReturnBadRequestWhenInvalidMoveIsMade() throws Exception {
        // Create a game
        String player1 = "{\"name\": \"Player1\"}";

        String gameId = mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)  // Ensure Accept header is JSON
                        .content(player1))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .split(":")[1]
                .trim()
                .replace("\"", "");  // Extract the game ID

        // Join the game as Player 2 to start the game
        String player2 = "{\"name\": \"Player2\"}";
        mockMvc.perform(post("/api/games/" + gameId + "/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)  // Ensure Accept header is JSON
                        .content(player2))
                .andExpect(status().isOk());

        // Attempt to make an invalid move
        String invalidMove = "{\"name\": \"Player1\", \"move\": \"INVALID_MOVE\"}";
        mockMvc.perform(post("/api/games/" + gameId + "/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)  // Ensure Accept header is JSON
                        .content(invalidMove))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid move. Must be Rock, Paper, or Scissors."));
    }
}