import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
public class Reader {
    private final ByteBuffer bytecode;
    private final byte format;
    private final byte endian;
    private final byte intSize;
    private final byte sizeT;
    private final byte instrSize;
    private final byte lNumSize;
    private final byte integralFlag;

    public Reader(byte[] bytes) {
        this.bytecode = ByteBuffer.wrap(bytes);

        if (!readString(4).equals("\u001BLua"))
            throw new IllegalArgumentException("Lua bytecode expected!");

        if (readByte() != 0x51)
            throw new IllegalArgumentException("Expected Lua 5.1 bytecode!");

        this.format = readByte();
        this.endian = readByte();
        this.intSize = readByte();
        this.sizeT = readByte();
        this.instrSize = readByte();
        this.lNumSize = readByte();
        this.integralFlag = readByte();

        bytecode.order(endian == 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
    }

    public byte readByte() {
        return bytecode.get();
    }

    public int readInt() {
        byte[] bytes = new byte[intSize];
        bytecode.get(bytes);

        int result = 0;
        for (int i = 0; i < intSize; i++) {
            result |= (bytes[i] & 0xFF) << (i * 8);
        }
        return result;
    }

    public long readLong() {
        byte[] bytes = new byte[lNumSize];
        bytecode.get(bytes);

        long result = 0;
        for (int i = 0; i < lNumSize; i++) {
            result |= ((long) (bytes[i] & 0xFF) << (i * 8));
        }
        return result;
    }

    public double readDouble() {
        return bytecode.getDouble();
    }

    public String readString(long len) {
        if (len == 0)
            len = sizeT == 4 ? readInt() : readLong();
        if (len > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Length exceeds max array size!");
        if (bytecode.remaining() < len)
            throw new IllegalArgumentException("Not enough data in the buffer to read the string!");

        byte[] bytes = new byte[(int) len];
        bytecode.get(bytes);

        return new String(bytes, StandardCharsets.UTF_8).replace("\0", "");
    }

    public String readString() {
        return readString(0);
    }
}