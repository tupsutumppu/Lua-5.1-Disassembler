package Bytecode;

public enum Op {
    MOVE(0, InstructionType.AB),
    LOADK(1, InstructionType.ABx),
    LOADBOOL(2, InstructionType.ABC),
    LOADNIL(3, InstructionType.AB),
    GETUPVAL(4, InstructionType.AB),
    GETGLOBAL(5, InstructionType.ABx),
    GETTABLE(6, InstructionType.ABC),
    SETGLOBAL(7, InstructionType.ABx),
    SETUPVAL(8, InstructionType.AB),
    SETTABLE(9, InstructionType.ABC),
    NEWTABLE(10, InstructionType.ABC),
    SELF(11, InstructionType.ABC),
    ADD(12, InstructionType.ABC),
    SUB(13, InstructionType.ABC),
    MUL(14, InstructionType.ABC),
    DIV(15, InstructionType.ABC),
    MOD(16, InstructionType.ABC),
    POW(17, InstructionType.ABC),
    UNM(18, InstructionType.AB),
    NOT(19, InstructionType.AB),
    LEN(20, InstructionType.AB),
    CONCAT(21, InstructionType.ABC),
    JMP(22, InstructionType.sBx),
    EQ(23, InstructionType.ABC),
    LT(24, InstructionType.ABC),
    LE(25, InstructionType.ABC),
    TEST(26, InstructionType.ABC),
    TESTSET(27, InstructionType.ABC),
    CALL(28, InstructionType.ABC),
    TAILCALL(29, InstructionType.ABC),
    RETURN(30, InstructionType.AB),
    FORLOOP(31, InstructionType.AsBx),
    FORPREP(32, InstructionType.AsBx),
    TFORLOOP(33, InstructionType.AC),
    SETLIST(34, InstructionType.ABC),
    CLOSE(35, InstructionType.A),
    CLOSURE(36, InstructionType.ABx),
    VARARG(37, InstructionType.AB);

    public final InstructionType type;
    private final int opNum;

    Op(int opNum, InstructionType type) {
        this.type = type;
        this.opNum = opNum;
    }

    public static Op fromNum(int opcode) {
        for (Op op : values()) {
            if (op.opNum == opcode) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unknown opcode: " + opcode);
    }
}