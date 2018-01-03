package com.epam.ships.client.client;

import com.epam.ships.client.gui.events.LooseEvent;
import javafx.application.Platform;
import javafx.scene.control.Button;

/**
 * Enable to fire event reacting to lose the game.
 *
 * @author Magdalena Aarsman
 * @since 2017-12-18
 */
class LoseTrigger implements EventTrigger {

  @Override
  public void fire(Button button, String messageStatement) {
    Platform.runLater(() -> button.fireEvent(new LooseEvent()));
  }
}