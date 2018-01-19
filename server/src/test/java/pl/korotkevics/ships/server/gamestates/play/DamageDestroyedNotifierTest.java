package pl.korotkevics.ships.server.gamestates.play;

import org.testng.annotations.Test;
import pl.korotkevics.ships.server.MessageSender;
import pl.korotkevics.ships.server.TurnManager;
import pl.korotkevics.ships.server.WrappedClient;
import pl.korotkevics.ships.shared.infra.communication.api.Message;
import pl.korotkevics.ships.shared.infra.communication.api.message.Author;
import pl.korotkevics.ships.shared.infra.communication.api.message.Header;
import pl.korotkevics.ships.shared.infra.communication.core.message.BaseMessage;

import static org.mockito.Mockito.*;

@Test
public class DamageDestroyedNotifierTest {

  public void shouldNotifyAboutDamage() {
    //given
    MessageSender messageSender = mock(MessageSender.class);
    TurnManager turnManager = mock(TurnManager.class);
    WrappedClient player = mock(WrappedClient.class);
    WrappedClient opponent = mock(WrappedClient.class);
    when(turnManager.getCurrentPlayer()).thenReturn(player);
    when(turnManager.getOtherPlayer()).thenReturn(opponent);
    Message message = BaseMessage.builder()
        .setAuthor(Author.SERVER)
        .setHeader(Header.SHIP_DESTROYED)
        .build();

    //when
    new DamageDestroyedNotifier(messageSender, turnManager).notify(message);

    //then
    verify(messageSender, times(1)).send(player, Header.SHIP_DESTROYED);
    verify(messageSender, times(1)).send(opponent, message);
  }

}