package pl.korotkevics.ships.server.communication;

import org.testng.annotations.Test;
import pl.korotkevics.ships.shared.infra.communication.api.Message;
import pl.korotkevics.ships.shared.infra.communication.api.message.Author;
import pl.korotkevics.ships.shared.infra.communication.api.message.Header;
import pl.korotkevics.ships.shared.infra.communication.core.message.MessageBuilder;

import static org.testng.Assert.assertEquals;

@Test
public class MessageRepositoryTest {


  public void shouldGetMessageByHeader() {
    //given
    Header header = Header.SHIP_DESTROYED;

    Message expectedMessage = new MessageBuilder()
        .withAuthor(Author.SERVER)
        .withHeader(header)
        .build();

    //when
    Message result = new MessageRepository().getMessage(header);

    //then
    assertEquals(result, expectedMessage);
  }
}