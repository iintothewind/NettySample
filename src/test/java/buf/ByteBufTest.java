package buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.charset.Charset;

public class ByteBufTest {

  @Test
  public void testRead() {
    ByteBuf buf = Unpooled.copiedBuffer("Netty In Action rocks!", Charset.defaultCharset());
    System.out.println("before readerIndex == " + buf.readerIndex());
    System.out.println("before writerIndex == " + buf.writerIndex());
    System.out.println((char) buf.readByte());
    int readerIndex = buf.readerIndex();
    int writerIndex = buf.writerIndex();
    buf.writeByte((byte) '?');
    System.out.println("after readerIndex == " + buf.readerIndex());
    System.out.println("after writerIndex == " + buf.writerIndex());

    assert readerIndex == buf.readerIndex();
    assert writerIndex != buf.writerIndex();
  }


}
