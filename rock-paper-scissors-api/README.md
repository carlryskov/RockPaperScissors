# Rock Paper Scissors REST API

## Introduction

This project provides a REST API that lets developers resolve their differences by playing a game of Rock, Paper, Scissors. The rules follow the traditional game: Rock beats Scissors, Scissors beats Paper, and Paper beats Rock. The game consists of a single match that determines the winner, or ends in a tie if both players choose the same move.

This API is designed as a proof of concept with production-ready best practices, including error handling, clean code, and the application of the Strategy Design Pattern. The solution is easily maintainable and extendable, allowing for future enhancements.

## Features
- **Create a Game**: Start a new game with one player.
- **Join a Game**: A second player can join an existing game by providing the game ID.
- **Make a Move**: Both players submit their moves (Rock, Paper, or Scissors).
- **Determine the Winner**: The API resolves the winner once both players have made their moves, or declares a tie.
- **Game State**: Retrieve the current state of the game, including player moves, game status, and winner.

## API Endpoints

### 1. **Create a New Game**
- **Endpoint**: `POST /api/games`
- **Description**: Creates a new game with a player and returns the game ID.
- **Request Body**:
  ```json
  {
    "name": "Player1"
  }
  ```
- **Response Example**:
  ```json
  "Game created with ID: game-<timestamp>"
  ```

### 2. **Join an Existing Game**
- **Endpoint**: `POST /api/games/{id}/join`
- **Description**: A second player joins an existing game by game ID.
- **Request Body**:
  ```json
  {
    "name": "Player2"
  }
  ```
- **Response Example**:
  ```json
  "Player2 joined the game with ID: game-<id>"
  ```

### 3. **Make a Move**
- **Endpoint**: `POST /api/games/{id}/move`
- **Description**: Submit a move (Rock, Paper, or Scissors) for a player.
- **Request Body**:
  ```json
  {
    "name": "Player1",
    "move": "ROCK"
  }
  ```
- **Response Example**:
  ```json
  "Move accepted for game game-<id>"
  ```

### 4. **Get Game State**
- **Endpoint**: `GET /api/games/{id}`
- **Description**: Retrieves the current state of the game, including moves, status, and winner (if applicable).
- **Response Example**:
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

Errors are handled gracefully and return structured JSON responses with a message and the corresponding HTTP status code.

**Example Error Response** (e.g., missing player name):
```json
{
  "message": "Player name is required",
  "status": 400
}
```

## Technology Stack
- **Java**: Primary language used for implementation.
- **Spring Boot**: Framework for building the REST API.
- **JUnit**: Testing framework for unit tests.
- **MockMvc**: Used to test API endpoints.
- **Strategy Design Pattern**: Used for determining the game outcome based on player moves.
- **Jackson**: JSON library for serializing and deserializing data.

## Installation & Setup

### Prerequisites
- **Java**: Ensure you have JDK 8 or higher installed.
- **Maven**: Build tool used for managing dependencies and running the project.
- **Tip for easy running!**: Use IntelliJ IDEA and press the play button in the top right corner, which will run all necessary commands for you.

### Steps to Install and Run
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
4. Start the application:
   ```bash
   mvn spring-boot:run
   ```

The API will be running at `http://localhost:8080`.

### Running Tests
To run the unit tests, execute the following Maven command:
```bash
mvn test
```

## Design Choices

### Error Handling
Errors are handled using a centralized approach, returning JSON responses with appropriate status codes and descriptive messages. Invalid inputs such as missing player names or invalid moves are captured and handled gracefully.

### Strategy Design Pattern
The **Strategy Pattern** is used to determine the winner based on the moves made by the players. This pattern allows for flexibility and future enhancements, such as extending the game to include additional moves (e.g., "Rock, Paper, Scissors, Lizard, Spock") with minimal changes to the core logic.

### Unit Testing
Unit tests cover the core functionalities, ensuring that the API behaves as expected. This includes creating and joining games, making moves, and handling edge cases like invalid inputs.

## Future Improvements
- **Persisting Game State**: Currently, the game data is stored in memory and lost when the server is restarted. A future enhancement could involve integrating a database (e.g., PostgreSQL or MongoDB) to persist game state across sessions and enable features like history tracking.
- **Extended Game Logic**: The current implementation supports the traditional Rock, Paper, Scissors rules. The logic could be extended to include variations like “Rock, Paper, Scissors, Lizard, Spock” or even custom rules, allowing for more flexibility and fun.
- **Enhanced Security Features**: For production deployment, security measures such as user authentication (e.g., OAuth2) and rate limiting should be implemented to prevent abuse or malicious use of the API. Additional features like API key validation or IP blocking could also be considered.
- **Increased Test Coverage**: UAlthough the core functionality is covered by unit tests, expanding test coverage to include edge cases, stress testing, and integration tests could improve the overall robustness of the application.
- **Concurrency and Scalability**: To handle multiple games simultaneously, multi-threading or an event-driven architecture could be implemented. This would allow for more efficient management of concurrent games and enable many users to play at the same time without performance bottlenecks. 