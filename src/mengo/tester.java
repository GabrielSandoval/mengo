package mengo;

import java.io.IOException;

public class tester {
    public static void main(String[] args) throws IOException {
            DummyParser dp = new DummyParser();
            dp.Parse("E:\\Jullian\\4thyr_2nd sem\\CS105 - Compiler Design\\SAMPLE2.mpl");        
            Parser p = new Parser();
            p.Parse("E:\\Jullian\\4thyr_2nd sem\\CS105 - Compiler Design\\SAMPLE2.mpl");
    }
}
