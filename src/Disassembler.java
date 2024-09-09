import Bytecode.Instruction;
import Bytecode.Local;
import Bytecode.Prototype;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
public class Disassembler {
    private final ByteBuffer bytecode;
    private final byte version;
    private final byte format;
    private final byte endian;
    private final byte intSize;
    private final byte sizeT;
    private final byte instrSize;
    private final byte lNumSize;
    private final byte integralFlag;

    public Disassembler(byte[] bytes) {
        this.bytecode = ByteBuffer.wrap(bytes);

        if (!readString(4).equals("\u001BLua"))
                throw new IllegalArgumentException("Lua bytecode expected!");

        this.version = readByte();
        this.format = readByte();
        this.endian = readByte();
        this.intSize = readByte();
        this.sizeT = readByte();
        this.instrSize = readByte();
        this.lNumSize = readByte();
        this.integralFlag = readByte();

        System.out.printf("Integer size: %d bytes%n", intSize);
        System.out.printf("Long size: %d bytes%n", lNumSize);
        System.out.printf("Endian: %s%n%n", endian == 0 ? "BIG_ENDIAN" : "LITTLE_ENDIAN");

        if (version != 0x51)
            throw new IllegalArgumentException("Expected Lua 5.1 bytecode!");

        bytecode.order(endian == 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
    }

    public Prototype disassemble() {
        Prototype proto = new Prototype();

        proto.name = readString();
        proto.firstLine = readInt();
        proto.lastLine = readInt();
        proto.numUpvals = readByte();
        proto.numParams = readByte();
        proto.varArgFlag = readByte();
        proto.maxStackSize = readByte();

        proto.instructions = readInstructions();
        proto.constants = readConstants();
        proto.prototypes = readPrototypes();
        proto.lines = readDebugLines();
        proto.locals = readDebugLocals();
        proto.upvalues = readDebugUpvalues();

        return proto;
    }

    private Instruction[] readInstructions() {
        int instructionCount = readInt();
        Instruction[] instructions = new Instruction[instructionCount];

        for (int i = 0; i < instructionCount; i++) {
            instructions[i] = new Instruction(readInt());
        }
        return instructions;
    }

    private String[] readConstants() {
        int constantCount = readInt();
        String[] constants = new String[constantCount];

        for (int i = 0; i < constantCount; i++) {
            byte type = readByte();

            switch (type) {
                case 0 -> constants[i] = "nil";
                case 1 -> constants[i] = readByte() != 0 ? "true" : "false";
                case 3 -> constants[i] = Double.toString(readDouble());
                case 4 -> constants[i] = readString();
                default -> throw new IllegalArgumentException("Unknown constant type!");
            }
        }
        return constants;
    }

    private Prototype[] readPrototypes() {
        int protoCount = readInt();
        Prototype[] prototypes = new Prototype[protoCount];

        for (int i = 0; i < protoCount; i++) {
            prototypes[i] = disassemble();
        }
        return prototypes;
    }

    private int[] readDebugLines() {
        int lineCount = readInt();
        int[] lines = new int[lineCount];

        for (int i = 0; i < lineCount; i++) {
            lines[i] = readInt();
        }
        return lines;
    }

    private Local[] readDebugLocals() {
        int localCount = readInt();
        Local[] locals = new Local[localCount];

        for (int i = 0; i < localCount; i++) {
            locals[i] = new Local(readString(), readInt(), readInt());
        }
        return locals;
    }

    private String[] readDebugUpvalues() {
        int upvalCount = readInt();
        String[] upvalues = new String[upvalCount];

        for (int i = 0; i < upvalCount; i++) {
            upvalues[i] = readString();
        }
        return upvalues;
    }

    private byte readByte() {
        return bytecode.get();
    }

    private int readInt() {
        byte[] bytes = new byte[intSize];
        bytecode.get(bytes);

        int result = 0;
        for (int i = 0; i < intSize; i++) {
            result |= (bytes[i] & 0xFF) << (i * 8);
        }
        return result;
    }

    private long readLong() {
        byte[] bytes = new byte[lNumSize];
        bytecode.get(bytes);

        long result = 0;
        for (int i = 0; i < lNumSize; i++) {
            result |= ((long) (bytes[i] & 0xFF) << (i * 8));
        }
        return result;
    }

    private double readDouble() {
        return bytecode.getDouble();
    }

    private String readString(long len) {
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

    private String readString() {
        return readString(0);
    }
}
