package kmucs.capstone.furnidiy;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

// yhchung
// parsing

public class MapReader implements TypeReader {

    private MappedByteBuffer buffer;

    public MapReader(FileChannel channel) throws IOException {
        buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }
    
    // yhchung 140524 Ãß°¡
    public byte getByteData() throws IOException {
    	return buffer.get();
    }
    
    public byte getByte() throws IOException {
    	return buffer.get();
    }

    public short getShort() throws IOException {
        return buffer.getShort();
    }

    public int getInt() throws IOException {
        return buffer.getInt();
    }

    public float getFloat() throws IOException {
        return buffer.getFloat();
    }

    public void skip(int i) throws IOException {
        buffer.position(buffer.position() + i);
    }

    public String readString() throws IOException {
        StringBuilder sb = new StringBuilder(256);
        byte ch = buffer.get();
        while (ch != 0) {
            sb.append((char)ch);
            ch = buffer.get();
        }
        return sb.toString();
    }

    public int position() {
        return buffer.position();
    }

}
