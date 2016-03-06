package mengo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Parser {

    public static HashMap<String, Token> ReservedWordsTable = new HashMap();
    public static HashMap<String, Token> IdentifierTable = new HashMap();

    public static void main(String[] args) {
        String root = "C:\\Users\\lenovo G40-70\\Desktop\\MENGO_Code\\";
        String inFile = root + "sample1.mpl";
        String outFile = root + "sample1out.mpl";

        LexicalAnalyzer lexAnalyzer = new LexicalAnalyzer(inFile);
        FillReservedWordTable();
        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

            Token tok = new Token();

            while ((tok = lexAnalyzer.nextToken()) != null) {
                if (tok.getKind().equals("STXERROR")) {
                    writer.write(tok.toString() + "\n");
                    System.out.println("[" + tok.toString() + "]");
                } else if (tok.getKind().equals("EOF")) {
                    System.out.println("[" + tok.toString() + "]");
                    break;
                } else if (tok.getKind().equals("ID") || tok.getKind().equals("LITSTRING") || tok.getKind().equals("NUMCONST") || tok.getKind().equals("BOOLEANCONST")) {
                    if (tok.getKind().equals("ID")) {
                        IdentifierTable.put(tok.getLexeme(), tok);
                    }
                    writer.write(tok.toString() + "\n");
                    System.out.println("[" + tok.toString() + "]");
                } else if (tok.getKind().equals("COMMENT")) {

                } else {
                    writer.write(tok.toString() + "\n");
                    System.out.println("[" + tok.getKind() + "]");
                }
            }

            writer.close();
            System.out.println();
            System.out.println("Done tokenizing file: " + inFile);
            System.out.println("Output written in file: " + outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FillReservedWordTable() {
        ReservedWordsTable.put("HELLO", new Token("HELLO", "HELLO"));
        ReservedWordsTable.put("GOODBYE", new Token("GOODBYE", "GOODBYE"));
        ReservedWordsTable.put("STARTHERE", new Token("STARTHERE", "STARTHERE"));
        ReservedWordsTable.put("ENDHERE", new Token("ENDHERE", "ENDHERE"));
        ReservedWordsTable.put("TASK", new Token("TASK", "TASK"));
        ReservedWordsTable.put("ENDTASK", new Token("ENDTASK", "ENDTASK"));
        ReservedWordsTable.put("INVOLVES", new Token("INVOLVES", "INVOLVES"));
        ReservedWordsTable.put("NUMBER", new Token("NUMBER", "NUMBER"));
        ReservedWordsTable.put("BOOLEAN", new Token("BOOLEAN", "BOOLEAN"));
        ReservedWordsTable.put("STRING", new Token("STRING", "STRING"));
        ReservedWordsTable.put("ACCEPT", new Token("ACCEPT", "ACCEPT"));
        ReservedWordsTable.put("RETURN", new Token("RETURN", "RETURN"));
        ReservedWordsTable.put("WHILE", new Token("WHILE", "WHILE"));
        ReservedWordsTable.put("ENDWHILE", new Token("ENDWHILE", "ENDWHILE"));
        ReservedWordsTable.put("UNTIL", new Token("UNTIL", "UNTIL"));
        ReservedWordsTable.put("INCREMENT", new Token("INCREMENT", "INCREMENT"));
        ReservedWordsTable.put("BY", new Token("BY", "BY"));
        ReservedWordsTable.put("SHOW", new Token("SHOW", "SHOW"));
        ReservedWordsTable.put("THE", new Token("THE", "THE"));
        ReservedWordsTable.put("NOW", new Token("NOW", "NOW"));
        ReservedWordsTable.put("IS", new Token("IS", "IS"));
        ReservedWordsTable.put("FROM", new Token("FROM", "FROM"));
        ReservedWordsTable.put("WHEN", new Token("WHEN", "WHEN"));
        ReservedWordsTable.put("ENDWHEN", new Token("ENDWHEN", "ENDWHEN"));
        ReservedWordsTable.put("OTHERWISE", new Token("OTHERWISE", "OTHERWISE"));
        ReservedWordsTable.put("BE", new Token("BE", "BE"));
        ReservedWordsTable.put("TRUE", new Token("BOOLEANCONST", "TRUE"));
        ReservedWordsTable.put("FALSE", new Token("BOOLEANCONST", "FALSE"));
        ReservedWordsTable.put("PERMANENT", new Token("PERMANENT", "PERMANENT"));
    }
}
