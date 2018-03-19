package netty.trans;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class PlainNioServer {
  private final static Logger log = LoggerFactory.getLogger(PlainOioServer.class);

  public void serve(int port) throws IOException {
    ServerSocketChannel channel = ServerSocketChannel.open();
    channel.configureBlocking(false);
    ServerSocket socket = channel.socket();
    InetSocketAddress address = new InetSocketAddress(port);
    socket.bind(address);
    Selector selector = Selector.open();
    channel.register(selector, SelectionKey.OP_ACCEPT);
    final ByteBuffer msg = ByteBuffer.wrap("Hi\r\n".getBytes());
    for (; ; ) {
      try {
        selector.select();
      } catch (IOException e) {
        e.printStackTrace();
        break;
      }
      Set<SelectionKey> readyKeys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = readyKeys.iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        iterator.remove();
        try (SelectableChannel selectableChannel = key.channel()) {
          if (key.isAcceptable() && selectableChannel instanceof ServerSocketChannel) {
            try (SocketChannel client = ((ServerSocketChannel) selectableChannel).accept()) {
              client.configureBlocking(false);
              client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
              log.info("Accepted connection from {}", client);
            }
          }
          if (key.isWritable() && selectableChannel instanceof SocketChannel) {
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            while (buffer.hasRemaining()) {
              if (((SocketChannel) selectableChannel).write(buffer) == 0) {
                break;
              }
            }
          }
        } catch (IOException e) {
          key.cancel();
        }
      }

    }
  }

}
