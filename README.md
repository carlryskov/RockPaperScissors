# Rock Paper Scissors REST API

## Introduction

This project is a REST API that allows developers to resolve their differences by playing the game of Rock, Paper, Scissors. The game follows the traditional rules: Rock beats Scissors, Scissors beats Paper, and Paper beats Rock. A single match determines the winner. The API is designed for simplicity, but is built to follow good software engineering practices, with a focus on maintainability and extendability.

This API was developed as part of a home assignment, and the solution has been prepared for production-like use, following best practices such as proper error handling, clean code principles, and the use of design patterns like the Strategy Pattern.

## Features
- **Create a Game**: Start a new game by specifying the first player.
- **Join a Game**: A second player can join the game by using the game ID.
- **Make a Move**: Both players can submit their moves (Rock, Paper, or Scissors).
- **Determine Winner**: Once both moves have been made, the API determines the winner or declares a tie.
- **Game State**: Query the current state of the game, including player moves and winner.

## API Endpoints

### 1. **Create a New Game**
- **Endpoint**: `POST /api/games`
- **Description**: Creates a new game and returns a game ID.
- **Request Body**:
  ```json
  {
    "name": "Player1"
  }
  ```
- **Response**:
  ```json
  "Game created with ID: game-<timestamp>"
  ```

### 2. **Join an Existing Game**
- **Endpoint**: `POST /api/games/{id}/join`
- **Description**: Allows a second player to join an existing game by its ID.
- **Request Body**:
  ```json
  {
    "name": "Player2"
  }
  ```
- **Response**:
  ```json
  "Player2 joined the game with ID: game-<id>"
  ```

### 3. **Make a Move**
- **Endpoint**: `POST /api/games/{id}/move`
- **Description**: Submits a move (Rock, Paper, or Scissors) for a player.
- **Request Body**:
  ```json
  {
    "name": "Player1",
    "move": "ROCK"
  }
  ```
- **Response**:
  ```json
  "Move accepted for game game-<id>"
  ```

### 4. **Get Game State**
- **Endpoint**: `GET /api/games/{id}`
- **Description**: Retrieves the current state of the game.
- **Response**:
  ```json
  {
    "player1": "Player1",
    "player2": "Player2",
    "player1Move": "ROCK",
    "player2Move": "PAPER",
    "status": "FINISHED",
    "winner": "Player2"
  }
  ```

## Error Handling

All errors return a structured JSON response with a message and HTTP status code.

Example error response for missing player name:
```json
{
  "message": "Player name is required",
  "status": 400
}
```

## Technology Stack
- **Java**: Primary language used for the implementation.
- **Spring Boot**: Framework used for creating the REST API.
- **JUnit**: Framework used for unit testing.
- **MockMvc**: Used for testing the API endpoints.
- **Strategy Design Pattern**: Used for determining the winner based on the moves.
- **Jackson**: Library used for serializing and deserializing JSON responses.

## Installation & Setup

### Prerequisites
- **Java**: Make sure you have JDK 8 or higher installed.
- **Maven**: Used for building the project.

### Steps
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd RockPaperScissors
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The API will now be running on `http://localhost:8080`.

### Testing

To run the unit tests, execute the following Maven command:
```bash
mvn test
```

## Design Choices

### Error Handling
Error handling has been implemented in a centralized and consistent way, with custom error messages returned in a JSON format. The API ensures that invalid inputs, such as missing player names or invalid moves, are handled gracefully, providing meaningful error responses.

### Strategy Design Pattern
The **Strategy Pattern** was used to determine the winner based on the players' moves. This design choice ensures flexibility and allows the game logic to be extended easily in the future. For example, new game rules (like "Rock, Paper, Scissors, Lizard, Spock") could be introduced without major changes to the core logic.

### Unit Testing
All the main functionalities of the API are covered with unit tests, ensuring robustness. Tests include validation for creating games, joining games, making moves, and edge cases like invalid inputs or non-existent games.

## Future Improvements
- **Persisting Games**: Currently, the game state is stored in memory. A future improvement would be to add database persistence for games.
- **Extendable Game Logic**: The API could be extended to support variations of Rock, Paper, Scissors (e.g., including Lizard and Spock).
- **Rate Limiting**: For production use, rate limiting could be introduced to prevent misuse of the API.
