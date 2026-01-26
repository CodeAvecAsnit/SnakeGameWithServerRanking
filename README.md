# Autonomous Snake Game

This repository contains the implementation of the classic Snake game. It features an autonomous agent powered by Q-Learning and Flood Fill algorithm. The project is built with a Java Swing client and a Spring Boot backend for leaderboard management and score tracking.

For the complete breakdown of the mathematical foundation and the building process, check out the full article on Medium:  
**[Building an Autonomous Snake Game with Q-Learning](https://medium.com/@asnitbakhati/building-an-autonomous-snake-game-with-q-learning-5a7e5840e1f2)**

---
## Project Structure

The repository is divided into two main components:

1. **SnakeGameSwing**: The client side application. This includes the game engine, the Java Swing GUI, the Q-Learning agent logic, and the local file handling for Q-Table persistence.
2. **SnakeGameServer**: The backend service. A Spring Boot application that manages high scores and provides a REST API for global rankings.(Not secure)

---

## Core Features

- **Autonomous Agent**: Uses Reinforcement Learning (Q-Learning) to learn optimal paths (not the shortest path) through trial and error.
- **Safety Heuristics**: Implements a Flood Fill algorithm to prevent the snake from entering dead end traps where the reachable area is smaller than its current length.
- **Robust UI**: Custom rendered Java Swing interface utilizing an event driven architecture.
- **Secure Rankings**: Scores are submitted to the server using HMAC-SHA256 digital signatures to prevent tampering and ensure the integrity of the leaderboard.(Some level of safety.)

---

## Installation and Execution

To run the game, you must generate a JAR file locally. This process ensures that the application is built within your environment, which helps bypass certain operating system security warnings associated with untrusted third-party binaries.

### Building the JAR File

1. **Visit the Website**:
   You can view the project live and download the jar directly from the official landing page: 
    **[quilversnakebot.vercel.app](https://quilversnakebot.vercel.app/)**

Note: Please note that your Operating System (Windows/macOS) may flag the downloaded `.jar` file as untrusted and prevent it from running. To resolve this and ensure a secure execution, you can build the file manually on your own device after checking the code for security issues.

2. **Clone the Repository**:
   If you have Git installed, run:
   ```bash
   git clone [https://github.com/CodeAvecAsnit/AutonomousSnakeGame.git](https://github.com/CodeAvecAsnit/AutonomousSnakeGame.git)

Note: If you cannot clone the repo or download from the website, manually download the source files as a ZIP from the repository.

3. **Navigate to the Client Directory**:
   ```bash
   cd AutonomousSnakeGame/SnakeGameSwing


4. **Compile and Package: Use Maven to build the project and generate the executable JAR**:
    ```bash
    mvn clean package


5. **Run the Game: The JAR file will be generated in the target folder. Execute it using**:
    ```bash
    java -jar target/SnakeGameSwing-1.0-SNAPSHOT.jar

---

### Implementation Details

* **Language:** Java
* **Frameworks:** Spring Boot (Backend), Java Swing (Frontend)
* **Build Tool:** Maven
* **Algorithm Reference:** The Q-Learning logic and pathfinding constraints are inspired by research into Safe Reinforcement Learning.

---

