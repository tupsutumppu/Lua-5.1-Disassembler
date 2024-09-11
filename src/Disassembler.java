import Bytecode.*;

@SuppressWarnings("unused")
public class Disassembler {
    private final Reader reader;

    public Disassembler(byte[] bytes) {
        this.reader = new Reader(bytes);

    }

    public Prototype disassemble() {
        Prototype proto = new Prototype();

        proto.name = reader.readString();
        proto.firstLine = reader.readInt();
        proto.lastLine = reader.readInt();
        proto.numUpvals = reader.readByte();
        proto.numParams = reader.readByte();
        proto.varArgFlag = reader.readByte();
        proto.maxStackSize = reader.readByte();

        proto.instructions = getInstructions();
        proto.constants = getConstants();
        proto.prototypes = getPrototypes();
        proto.lines = getDebugLines();
        proto.locals = getDebugLocals();
        proto.upvalues = getDebugUpvalues();

        return proto;
    }

    private Instruction[] getInstructions() {
        int instructionCount = reader.readInt();
        Instruction[] instructions = new Instruction[instructionCount];

        for (int i = 0; i < instructionCount; i++) {
            instructions[i] = new Instruction(reader.readInt());
        }
        return instructions;
    }

    private String[] getConstants() {
        int constantCount = reader.readInt();
        String[] constants = new String[constantCount];

        for (int i = 0; i < constantCount; i++) {
            byte type = reader.readByte();

            switch (type) {
                case 0 -> constants[i] = "nil";
                case 1 -> constants[i] = reader.readByte() != 0 ? "true" : "false";
                case 3 -> constants[i] = Double.toString(reader.readDouble());
                case 4 -> constants[i] = reader.readString();
                default -> throw new IllegalArgumentException("Unknown constant type!");
            }
        }
        return constants;
    }

    private Prototype[] getPrototypes() {
        int protoCount = reader.readInt();
        Prototype[] prototypes = new Prototype[protoCount];

        for (int i = 0; i < protoCount; i++) {
            prototypes[i] = disassemble();
        }
        return prototypes;
    }

    private int[] getDebugLines() {
        int lineCount = reader.readInt();
        int[] lines = new int[lineCount];

        for (int i = 0; i < lineCount; i++) {
            lines[i] = reader.readInt();
        }
        return lines;
    }

    private Local[] getDebugLocals() {
        int localCount = reader.readInt();
        Local[] locals = new Local[localCount];

        for (int i = 0; i < localCount; i++) {
            locals[i] = new Local(reader.readString(), reader.readInt(), reader.readInt()); // name, start, end
        }
        return locals;
    }

    private String[] getDebugUpvalues() {
        int upvalCount = reader.readInt();
        String[] upvalues = new String[upvalCount];

        for (int i = 0; i < upvalCount; i++) {
            upvalues[i] = reader.readString();
        }
        return upvalues;
    }
}
