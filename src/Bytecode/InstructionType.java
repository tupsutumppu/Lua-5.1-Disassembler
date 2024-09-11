package Bytecode;
// Actually there is only 3 types of instructions: ABC, ABx and AsBx but not all opcodes use all registers.
public enum InstructionType {
    AB, ABx, ABC, AsBx, AC, A, sBx
}