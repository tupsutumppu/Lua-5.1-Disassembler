package Bytecode;

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


    private String formatRK(int rk) {
        return (whichRK(rk) ? "K[" + readRKasK(rk) + "]" : "R[" + rk + "]");
    }

    // Checks if value is a register or constant index
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
            String B = "";
            String C = "";

            StringBuilder constantString = new StringBuilder();

            switch (type) {
                case AB -> {
                    B = formatRK(instruction.B);
                    C = "";

                    if (whichRK(instruction.B))
                        constantString.append(" ; ").append(constants[readRKasK(instruction.B)]);
                }
                case ABC -> {
                    B = formatRK(instruction.B);
                    C = formatRK(instruction.C);

                    if (whichRK(instruction.B))
                        constantString.append(" ; ").append(constants[readRKasK(instruction.B)]);

                    if (whichRK(instruction.C))
                        constantString.append(" ; ").append(constants[readRKasK(instruction.C)]);
                }
                case ABx -> {
                    A = String.format("R[%d]", instruction.A);
                    C = "";

                    if (instruction.opcode == Op.CLOSURE)
                        B = String.format("R[%d]", readRKasK(instruction.B));
                    else {
                        B = String.format("K[%d]", readRKasK(instruction.B));
                        constantString.append(" ; ").append(constants[readRKasK(instruction.B)]);
                    }
                }
                case AsBx -> {
                    A = String.format("R[%d]", instruction.A);
                    B = String.format("%d", instruction.B);
                    C = "";
                }
                case AC -> {
                    C = formatRK(instruction.C);
                    B = "";

                    if (whichRK(instruction.C))
                        constantString.append(" ; ").append(constants[readRKasK(instruction.C)]);
                }
                case sBx -> {
                    A = "";
                    B = String.format("%d", instruction.B);
                    C = "";

                    if (instruction.opcode == Op.JMP)
                        constantString.append(" ; ").append(String.format("to pc %d", instruction.B + (i + 2)));
                }
            }

            System.out.printf("%s%-5s\t%-12s%-12s%-12s%-12s%s%n",
                    indentation, pc, opcodeName, A, B, C, constantString);

            if (instruction.opcode == Op.CLOSURE) {
                Prototype proto = prototypes[instruction.B];
                String protoName = String.format("UNNAMED_PROTO_%d", instruction.B);

                System.out.printf("%sfunction %s(%s)%n", indentation, protoName, proto.getArgsString());
                proto.print(level + 1);
                System.out.printf("%send%n", indentation);
            }
        }
    }
}