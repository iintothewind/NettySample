package netty.trans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class PlainOioServer {
  private final static Logger log = LoggerFactory.getLogger(PlainOioServer.class);

  public void serve(int port) throws IOException {
    final ServerSocket socket = new ServerSocket(port);
    for (; ; ) {
      try (final Socket clientSocket = socket.accept()) {
        log.info("Accepted connection from {}", clientSocket);
        new Thread(() -> {
          try (OutputStream out = clientSocket.getOutputStream()) {
            out.write("Hi\r\n".getBytes(Charset.defaultCharset()));
            out.flush();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }).start();
      }
    }
  }
}
