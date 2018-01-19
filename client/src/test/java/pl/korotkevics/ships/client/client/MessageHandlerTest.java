package pl.korotkevics.ships.client.client;

import javafx.scene.control.Button;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pl.korotkevics.ships.client.JavaFxInitializer;
import pl.korotkevics.ships.shared.infra.communication.api.Message;
import pl.korotkevics.ships.shared.infra.communication.api.message.Header;
import pl.korotkevics.ships.shared.infra.communication.api.message.Status;
import pl.korotkevics.ships.shared.infra.communication.core.message.BaseMessage;

import java.util.EnumMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertTrue;

public class MessageHandlerTest {

  @BeforeClass
  public void setUp() throws InterruptedException {
    if(!JavaFxInitializer.isLaunched()) {
      JavaFxInitializer.initialize();
    }
  }

  @BeforeMethod
  public void skipTestIfThereIsNoGraphicsSupport() {
    if (!JavaFxInitializer.isEnable()){
      throw new SkipException("skipping test because of lack of support for graphics");
    }
  }

  @Test
  public void shouldCallConnectionEndTrigger() {
    //given
    ConnectionEndTrigger connectionEndTrigger = mock(ConnectionEndTrigger.class);
    MessageHandler messageHandler = produceMessageHandler(connectionEndTrigger);
    Message message = connectionEndMessage();

    //when
    messageHandler.handle(message);

    //then
    verify(connectionEndTrigger, times(1)).fire(messageHandler.getCurrentEventButton(), message);
  }

  @Test
  public void shouldReturnTrueAfterTriggerOpponentWithdrawEven() {
    //given
    ConnectionEndTrigger connectionEndTrigger = mock(ConnectionEndTrigger.class);
    MessageHandler messageHandler = produceMessageHandler(connectionEndTrigger);
    Message message = connectionEndMessage();

    //when
    messageHandler.handle(message);

    assertTrue(messageHandler.isEndConnectionTriggered());
  }

  private Message connectionEndMessage() {
    return BaseMessage.builder()
        .setHeader(Header.CONNECTION)
        .setStatus(Status.END)
        .setStatement("End of a message")
        .build();
  }

  private MessageHandler produceMessageHandler(EventTrigger eventTrigger) {
    Map<Header, EventTrigger> triggers = new EnumMap<>(Header.class);
    triggers.put(Header.CONNECTION, eventTrigger);
    MessageHandler messageHandler = new MessageHandler(triggers);
    messageHandler.setCurrentEventButton(new Button());
    return messageHandler;
  }
}