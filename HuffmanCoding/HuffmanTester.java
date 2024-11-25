import java.util.*;
import java.io.*;

public class HuffmanTester{
   public static void main(String[] args) throws IOException{
      Scanner codeInput = new Scanner(new File("tiny.code"));
      HuffmanCode huffman = new HuffmanCode(codeInput);
      System.out.println();
      huffman.save(System.out);
   }
}