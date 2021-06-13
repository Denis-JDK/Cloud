package nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelExamples {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'a');
        buffer.put((byte) 'b');
        buffer.put((byte) 'c');
        buffer.flip(); // перевели буфер в режим чтения.      без этого печатается в консю 0!!
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.rewind(); // не смещает лимит буфера, а flip смещает
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.clear(); // девственный буфер

        RandomAccessFile file = new RandomAccessFile("client/2.txt", "rw");
        FileChannel channel = file.getChannel(); // получили канал
        int read = 0;
        while ((read = channel.read(buffer)) > -1) { //записываем в буффер
            buffer.flip();
            while (buffer.hasRemaining()) {           // по факту перешли в режим чтения из буфера
                System.out.print((char) buffer.get());
            }
            buffer.clear();
        }
    }
}
