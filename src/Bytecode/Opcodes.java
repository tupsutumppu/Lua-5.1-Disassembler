package Bytecode;

public enum Opcodes {
    MOVE(0, "MOVE", InstructionType.ABC),
    LOADK(1, "LOADK", InstructionType.ABx),
    LOADBOOL(2, "LOADBOOL", InstructionType.ABC),
    LOADNIL(3, "LOADNIL", InstructionType.ABC),
    GETUPVAL(4, "GETUPVAL", InstructionType.ABC),
    GETGLOBAL(5, "GETGLOBAL", InstructionType.ABx),
    GETTABLE(6, "GETTABLE", InstructionType.ABC),
    SETGLOBAL(7, "SETGLOBAL", InstructionType.ABx),
    SETUPVAL(8, "SETUPVAL", InstructionType.ABC),
    SETTABLE(9, "SETTABLE", InstructionType.ABC),
    NEWTABLE(10, "NEWTABLE", InstructionType.ABC),
    SELF(11, "SELF", InstructionType.ABC),
    ADD(12, "ADD", InstructionType.ABC),
    SUB(13, "SUB", InstructionType.ABC),
    MUL(14, "MUL", InstructionType.ABC),
    DIV(15, "DIV", InstructionType.ABC),
    MOD(16, "MOD", InstructionType.ABC),
    POW(17, "POW", InstructionType.ABC),
    UNM(18, "UNM", InstructionType.ABC),
    NOT(19, "NOT", InstructionType.ABC),
    LEN(20, "LEN", InstructionType.ABC),
    CONCAT(21, "CONCAT", InstructionType.ABC),
    JMP(22, "JMP", InstructionType.AsBx),
    EQ(23, "EQ", InstructionType.ABC),
    LT(24, "LT", InstructionType.ABC),
    LE(25, "LE", InstructionType.ABC),
    TEST(26, "TEST", InstructionType.ABC),
    TESTSET(27, "TESTSET", InstructionType.ABC),
    CALL(28, "CALL", InstructionType.ABC),
    TAILCALL(29, "TAILCALL", InstructionType.ABC),
    RETURN(30, "RETURN", InstructionType.ABC),
    FORLOOP(31, "FORLOOP", InstructionType.AsBx),
    FORPREP(32, "FORPREP", InstructionType.AsBx),
    TFORLOOP(33, "TFORLOOP", InstructionType.ABC),
    SETLIST(34, "SETLIST", InstructionType.ABC),
    CLOSE(35, "CLOSE", InstructionType.ABC),
    CLOSURE(36, "CLOSURE", InstructionType.ABx),
    VARARG(37, "VARARG", InstructionType.ABC);

    public final int opcodeNum;
    public final String name;
    public final InstructionType type;

    Opcodes(int opcodeNum, String name, InstructionType type) {
        this.opcodeNum = opcodeNum;
        this.name = name;
        this.type = type;
    }

    public static Opcodes fromNum(int opcode) {
        for (Opcodes op : values()) {
            if (op.opcodeNum == opcode) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unknown opcode: " + opcode);
    }
}
