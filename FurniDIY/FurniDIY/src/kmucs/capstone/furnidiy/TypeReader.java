package kmucs.capstone.furnidiy;

import java.io.IOException;

// yhchung
// parsing

public interface TypeReader {
    short getShort() throws IOException;
    
    // yhchung 140524
    byte getByteData() throws IOException;
    
    byte getByte() throws IOException;
    
    int getInt() throws IOException;

    float getFloat() throws IOException;

    void skip(int i) throws IOException;

    String readString() throws IOException;

    int position();
}
