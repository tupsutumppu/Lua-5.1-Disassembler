package Bytecode;

public class Instruction {
    public Op opcode;
    public int A;
    public int B;
    public int C;

    public Instruction(int data) {
        this.opcode = Op.fromNum(data & 0x3F);
        this.A = (data >> 6) & 0xFF;

        switch (opcode.type) {
            case ABC, AC, AB -> {
                this.B = (data >> 23) & 0x1FF;
                this.C = (data >> 14) & 0x1FF;
            }
            case ABx -> this.B = (data >> 14) & 0x3FFFF;
            case AsBx, sBx -> this.B = ((data >> 14) & 0x3FFFF) - 131071;
            default -> throw new IllegalArgumentException("Unknown instruction type!");
        }
    }
}