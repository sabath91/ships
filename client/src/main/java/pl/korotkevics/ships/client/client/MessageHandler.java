package pl.korotkevics.ships.client.client;

import javafx.scene.control.Button;
import lombok.Getter;
import pl.korotkevics.ships.client.reporting.Reporter;
import pl.korotkevics.ships.shared.infra.communication.api.Message;
import pl.korotkevics.ships.shared.infra.communication.api.message.Header;
import pl.korotkevics.ships.shared.infra.communication.api.message.Status;
import pl.korotkevics.ships.shared.infra.logging.api.Target;
import pl.korotkevics.ships.shared.infra.logging.core.SharedLogger;

import java.util.Map;

/**
 * Handling message, enable to react in proper way to server messages.
 *
 * @author Magdalena Aarsman
 * @since 2017-12-16
 */
class MessageHandler {

  private static final Target logger = new SharedLogger(Client.class);
  
  private final Map<Header, EventTrigger> triggers;
  private Reporter reporter;
  @Getter
  private final DispatcherAdapter dispatcherAdapter;
  @Getter
  private boolean endConnectionTriggered;

  /**
   * Creates an instance of MessageHandler and configures all possible triggers types.
   */
  MessageHandler(final Map<Header, EventTrigger> triggerMap, DispatcherAdapter dispatcherAdapter,
                 final Reporter reporter) {
    this.triggers = triggerMap;
    this.dispatcherAdapter = dispatcherAdapter;
    this.reporter = reporter;
  }

  void setCurrentEventButton(Button eventButton) {
    this.dispatcherAdapter.setEventButton(eventButton);
  }

  void handle(Message message) {
    if (dispatcherAdapter.getEventButton() == null) {
      throw new IllegalStateException("there is no object on which there can be fire event on");
    }

    Header header = message.getHeader();

    if (!triggers.containsKey(header)) {
      logger.error("message header: " + header + " is unknown");
      return;
    }
    checkIfEndWillBeTriggered(message);
    triggers.get(header).fire(dispatcherAdapter, message);
    this.startReporter(header);
  }
  
  private void startReporter(final Header header) {
    Thread reporterThread = new Thread(()-> {
    if (this.reporter!=null) {
        this.reporter.report(triggers.get(header).provideDescription());
      }
    });
    reporterThread.setName("Reporter");
    reporterThread.start();
  }

  private void checkIfEndWillBeTriggered(final Message message) {
    if (Status.END.equals(message.getStatus())
        || Header.WIN.equals(message.getHeader())
        || Header.LOSE.equals(message.getHeader())) {
      endConnectionTriggered = true;
    }
  }
}
