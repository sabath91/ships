package com.epam.ships.server;

import com.epam.ships.server.gamestates.GameState;

public class Game {
  private GameState currentState;

  public Game(GameState initialState) {
    currentState = initialState;
  }

  public void loop() {
    while (true) {
      currentState = currentState.process();
    }
  }

}
