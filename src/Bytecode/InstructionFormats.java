package Bytecode;

import java.util.EnumSet;

public class InstructionFormats {
    public static final EnumSet<Opcodes> RKBC = EnumSet.of(
            Opcodes.SETTABLE,
            Opcodes.ADD,
            Opcodes.SUB,
            Opcodes.MUL,
            Opcodes.DIV,
            Opcodes.MOD,
            Opcodes.POW,
            Opcodes.EQ,
            Opcodes.LT
    );
    public static final EnumSet<Opcodes> RKC = EnumSet.of(
            Opcodes.GETTABLE,
            Opcodes.SELF
    );
    public static final EnumSet<Opcodes> KBX = EnumSet.of(
            Opcodes.LOADK,
            Opcodes.GETGLOBAL,
            Opcodes.SETGLOBAL
    );
}
