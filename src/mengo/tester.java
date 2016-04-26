package mengo;

import java.io.IOException;

public class tester {

    public static void main(String[] args) throws IOException {
        String loc;
        if (args.length == 0) {
            loc = "E:\\Jullian\\4thyr_2nd sem\\CS105 - Compiler Design\\SampleJ.txt";
        } else {
            loc = args[0];
        }
        TreeNode root;
        System.out.println("\n\nScanning\n\n");
        Scanner dp = new Scanner();
        dp.Parse(loc);
        System.out.println("\n\nParsing\n\n");
        Parser p = new Parser();
        root = p.Parse(loc);
        System.out.println("\n\nTree Traversal\n\n");
        Traverse.traverse(root);
        System.out.println("\n\nInterpreting\n\n");
        Interpreter i = new Interpreter();
        i.interpret(root);
    }


}
