# Araignée - A Simple Java Game

**Araignée** is a two-player board game implemented in Java using Swing for the GUI. The game has two main phases: **positioning** and **moving**. The objective is to align three of your "Araignée" (spider) pieces either horizontally, vertically, or diagonally before your opponent does.

## Table of Contents

- [Features](#features)
- [Game Rules](#game-rules)
  - [Phase 1: Positioning](#phase-1-positioning)
  - [Phase 2: Moving](#phase-2-moving)
  - [Winning](#winning)
- [Setup](#setup)
- [How to Play](#how-to-play)
- [Future Improvements](#future-improvements)

## Features

- **Two-player game**: Players take turns on the same computer.
- **Graphical User Interface**: Simple and intuitive interface using Java Swing.
- **Dynamic game phases**: The game transitions smoothly between positioning and moving phases.
- **Reset functionality**: Easily restart the game with the "New Game" button.

## Game Rules

### Phase 1: Positioning

- Players take turns placing their pieces on any empty cell in a 3x3 grid.
- Player 1's pieces are represented by a blue spider, and Player 2's pieces are represented by a red spider.
- The positioning phase continues until each player has placed three pieces.

### Phase 2: Moving

- Once all pieces are placed, players take turns moving their pieces.
- A player can only move one piece at a time to an adjacent empty cell.
- The goal is to align three of your pieces in a row (horizontally, vertically, or diagonally).

### Winning

- The first player to align three of their pieces wins the game.
- The game ends immediately once a player wins.

## Setup

1. Clone the repository.
    ```bash
    git clone <repository-url>
    cd araignee-game
    ```
2. Compile the Java program.
    ```bash
    javac Araignee.java
    ```
3. Run the game.
    ```bash
    java Araignee
    ```

## How to Play

- **Starting the Game**: Run the game and follow the on-screen instructions.
- **New Game**: Click "New Game" to reset the board and start over.
- **Making a Move**: Click on an empty cell during the positioning phase. In the moving phase, first click the piece you want to move, then click the destination cell.

## Future Improvements

- **AI Opponent**: Add a single-player mode with an AI opponent.
- **Improved Graphics**: Enhance the visual appeal with better graphics and animations.
- **Multiplayer Over Network**: Allow players to play over a network instead of just locally.

---
