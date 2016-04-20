package mengo;

import java.io.IOException;

public class tester {
    public static void main(String[] args) throws IOException {
        String loc;
        if(args.length == 0){
            loc = "E:\\Jullian\\4thyr_2nd sem\\CS105 - Compiler Design\\SampleB.txt";
        }else{
            loc = args[0];
        }
            
            DummyParser dp = new DummyParser();
            dp.Parse(loc);        
            Parser p = new Parser();
            p.Parse(loc);
    }
}
