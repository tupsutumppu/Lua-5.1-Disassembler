import Bytecode.Prototype;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String fileName = "bytecode.luac";

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            Disassembler disassembler = new Disassembler(bytes);
            Prototype mainProto = disassembler.disassemble();
            mainProto.print(0);
        }
        catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}