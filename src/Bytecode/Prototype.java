package Bytecode;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Prototype {
    public Instruction[] instructions;
    public Prototype[] prototypes;
    public String[] constants;
    public int[] lines;
    public Local[] locals;
    public String[] upvalues;

    public String name;
    public int firstLine;
    public int lastLine;
    public byte numUpvals;
    public byte numParams;
    public byte varArgFlag;
    public byte maxStackSize;
    
    // https://github.com/CPunch/LuaPytecode/blob/63756b3bcd02dd53ac60f4f73d301a103ef3c5e1/luac.py#L73
    private String formatRK(int rk) {
        return (whichRK(rk) ? "K[" + readRKasK(rk) + "]" : "R[" + rk + "]");
    }

    private boolean whichRK(int rk) {
        return (rk & (1 << 8)) > 0;
    }

    private int readRKasK(int rk) {
        return (rk & ~(1 << 8));
    }

    private String getArgsString() {
        if (varArgFlag != 0) {
            return "...";
        }
        else if (numParams > 0) {
            StringBuilder args = new StringBuilder();

            for (int i = 0; i < numParams; i++) {
                if (i > 0) {
                    args.append(", ");
                }
                args.append(String.format("a%d", i + 1));
            }
            return args.toString();
        }
        else {
            return "";
        }
    }

    // Todo: use local names
    public void print(int level) {
        String indentation = " ".repeat(level * 4);

        for (int i = 0; i < instructions.length; i++) {
            Instruction instruction = instructions[i];
            InstructionType type = instruction.opcode.type;

            String pc = String.format("[%3d]", i + 1);
            String opcodeName = String.format("%-" + 12 + "s", instruction.opcode.name());
            String A = String.format("R[%d]", instruction.A);
            String B = String.format("R[%d]", instruction.B);
            String C = String.format("R[%d]", instruction.C);

            StringBuilder constantString = new StringBuilder();

            switch (type) {
                case ABC -> {
                    if (InstructionFormats.RKBC.contains(instruction.opcode)) {
                        B = formatRK(instruction.B);
                        C = formatRK(instruction.C);

                        if (whichRK(instruction.C)) {
                            constantString.append(" ; ").append(constants[readRKasK(instruction.C)]);
                        }
                        if (whichRK(instruction.B)) {
                            constantString.append(" ; ").append(constants[readRKasK(instruction.B)]);
                        }
                    }
                    else if (InstructionFormats.RKC.contains(instruction.opcode)) {
                        C = formatRK(instruction.C);

                        if (whichRK(instruction.C)) {
                            constantString.append(" ; ").append(constants[readRKasK(instruction.C)]);
                        }
                    }

                    A = String.format("%-" + 12 + "s", A);
                    B = String.format("%-" + 12 + "s", B);
                    C = String.format("%-" + 12 + "s", C);
                }
                case ABx, AsBx -> {
                    if (InstructionFormats.KBX.contains(instruction.opcode)) {
                        B = String.format("K[%d]", instruction.B);
                        constantString.append(" ; ").append(constants[instruction.B]);
                    }
                    else {
                        B = String.format("R[%d]", instruction.B);
                    }

                    A = String.format("%-" + 12 + "s", A);
                    B = String.format("%-" + 12 + "s", B);
                    C = "";
                }
                default -> throw new IllegalArgumentException("Unknown instruction type!");
            }

            System.out.printf("%s%-" + 5 + "s\t%-" + 12 + "s%-" + 12 + "s%-" + 12 + "s%-" + 12 + "s%s%n",
                    indentation, pc, opcodeName, A, B, C, constantString);

            if (instruction.opcode == Opcodes.CLOSURE) {
                Prototype proto = prototypes[instruction.B];

                String protoName = String.format("UNNAMED_PROTO_%d", instruction.B);

                System.out.printf("%sfunction %s(%s)%n", indentation, protoName, proto.getArgsString());
                prototypes[instruction.B].print(level + 1);
                System.out.printf("%send%n", indentation);
            }
        }
    }
}
