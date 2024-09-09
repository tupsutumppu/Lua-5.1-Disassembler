package Bytecode;

public class Local {
    public String name;
    public int start;
    public int end;

    public Local(String name, int start, int end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }
}