package com.epam.ships.server;

import com.epam.ships.infra.communication.api.Message;
import com.epam.ships.infra.logging.api.Target;
import com.epam.ships.infra.logging.core.SharedLogger;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * It starts a server, gets its client sockets
 * and adds them wrapped into own list.
 *
 * @author Piotr Czyż, Magdalena Aarsman, Sandor Korotkevics
 * @since 2017-12-09
 */
public class CommunicationBus {

  private static final int SERVER_PORT = 8189;
  private final Target logger = new SharedLogger(CommunicationBus.class);
  private final AppServer appServer;

  private List<WrappedClient> clients;

  public CommunicationBus() throws IOException {
    appServer = new AppServer(SERVER_PORT);
    clients = new ArrayList<>();
  }

  /**
   * It ask AppServer for clients and then wrap them into wrapped client.
   */
  public void start() {
    appServer.connectClients();
    for (Socket socketClient : appServer.getClientSockets()) {
      wrapClient(socketClient);
    }
    logger.info("Communication bus started..");
  }

  /**
   * Close clients sockets and clear list of wrapped clients.
   */
  public void stop() {
    for (WrappedClient c : clients) {
      c.close();
    }
    clients.clear();
  }

  private void wrapClient(final Socket socketClient) {
    WrappedClient wrappedClient = new WrappedClient(socketClient);
    clients.add(wrappedClient);
  }

  /**
   * It receive message from player.
   *
   * @param sender player who sent a message
   * @return message
   */
  public Message receive(final WrappedClient sender) {
    return sender.receive();
  }

  void send(final WrappedClient recipient, final Message response) {
    recipient.send(response);
  }

  void sendToAll(final Message message) {
    clients.forEach(wrappedClient -> send(wrappedClient, message));
  }

  /**
   * Gets player which was connected earlier.
   *
   * @return WrappedClient
   */
  public WrappedClient getFirstClient() {
    return this.clients.get(0);
  }

  /**
   * Gets player which was connected as second one.
   *
   * @return WrappedClient
   */
  public WrappedClient getSecondClient() {
    return this.clients.get(1);
  }
}
