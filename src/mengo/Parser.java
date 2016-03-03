/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mengo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author lenovo G40-70
 */
public class Parser {

    public static HashMap<String, Token> ReservedWordsTable = new HashMap();
    public static HashMap<String, Token> IdentifierTable = new HashMap();

    public static void main(String[] args) {
        String root = "C:\\Users\\Jullian\\Desktop\\mengo\\";
        String inFile = root + "SAMPLE PROGRAM.mpl";
        String outFile = root + "sample2out.mpl";

        LexicalAnalyzer lexAnalyzer = new LexicalAnalyzer(inFile);
        FillReservedWordTable();
        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

            Token tok = new Token();

            while ((tok = lexAnalyzer.nextToken()) != null) {
                if (tok.getKind().equals("STXERROR")) {
                    System.out.println("[" + tok.toString() + "]");
                } else if (tok.getKind().equals("EOF")) {
                    System.out.println("[" + tok.toString() + "]");
                    break;
                }else if(tok.getKind().equals("ID") || tok.getKind().equals("LITSTRING") || tok.getKind().equals("NUMCONST")|| tok.getKind().equals("BOOLEANCONST")){
                    if(tok.getKind().equals("ID")){
                        IdentifierTable.put(tok.getLexeme(), tok);                     
                    }
                    writer.write(tok.toString() + "\n");
                    System.out.print("[" + tok.toString() + "]");
                }
                else if(tok.getKind().equals("COMMENT")){
                    
                }
                else {
                    writer.write(tok.toString() + "\n");
                    System.out.print("[" + tok.getKind() + "]");
                }
            }

            writer.close();
            System.out.println();
            System.out.println("Done tokenizing file: " + inFile);
            System.out.println("Output written in file: " + outFile);
//            System.out.println("\nIdentifiers:");
//            Iterator i = IdentifierTable.entrySet().iterator();
//            while(i.hasNext()){
//                Map.Entry pair = (Map.Entry)i.next();
//                System.out.println(pair.getKey() + " : " + pair.getValue());
//                i.remove();   
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void FillReservedWordTable(){
//        ReservedWordsTable.put("ADD",new Token("ADD", "+"));
//        ReservedWordsTable.put("SUB",new Token("SUB", "-"));
//        ReservedWordsTable.put("MUL",new Token("MUL", "*"));
//        ReservedWordsTable.put("DIV",new Token("DIV", "/"));
//        ReservedWordsTable.put("MOD",new Token("MOD", "%"));
//        ReservedWordsTable.put("COMMA",new Token("COMMA", ","));
//        ReservedWordsTable.put("LPAREN",new Token("LPAREN", "("));
//        ReservedWordsTable.put("RPAREN",new Token("RPAREN", ")"));
//        ReservedWordsTable.put("AND",new Token("AND", "&"));
//        ReservedWordsTable.put("OR",new Token("OR", "|"));
//        ReservedWordsTable.put("NOT",new Token("NOT", "~"));
//        ReservedWordsTable.put("LTE",new Token("LTE", "<="));
//        ReservedWordsTable.put("LT",new Token("LT", "<"));
//        ReservedWordsTable.put("GTE",new Token("GTE", ">="));
//        ReservedWordsTable.put("GT",new Token("GT", ">"));
//        ReservedWordsTable.put("PERIOD",new Token("PERIOD", "."));
        ReservedWordsTable.put("HELLO",new Token("HELLO", "HELLO"));
        ReservedWordsTable.put("GOODBYE",new Token("GOODBYE", "GOODBYE"));
        ReservedWordsTable.put("STARTHERE",new Token("STARTHERE", "STARTHERE"));
        ReservedWordsTable.put("ENDHERE",new Token("ENDHERE", "ENDHERE"));
        ReservedWordsTable.put("TASK",new Token("TASK", "TASK"));
        ReservedWordsTable.put("ENDTASK",new Token("ENDTASK", "ENDTASK"));
        ReservedWordsTable.put("INVOLVES",new Token("INVOLVES", "INVOLVES"));
        ReservedWordsTable.put("NUMBER",new Token("NUMBER", "NUMBER"));
        ReservedWordsTable.put("BOOLEAN",new Token("BOOLEAN", "BOOLEAN"));
        ReservedWordsTable.put("STRING",new Token("STRING", "STRING"));
        ReservedWordsTable.put("ACCEPT",new Token("ACCEPT", "ACCEPT"));
        ReservedWordsTable.put("RETURN",new Token("RETURN", "RETURN"));
        ReservedWordsTable.put("WHILE",new Token("WHILE", "WHILE"));
        ReservedWordsTable.put("ENDWHILE",new Token("ENDWHILE", "ENDWHILE"));
        ReservedWordsTable.put("UNTIL",new Token("UNTIL", "UNTIL"));
        ReservedWordsTable.put("INCREMENT",new Token("INCREMENT", "INCREMENT"));
        ReservedWordsTable.put("BY",new Token("BY", "BY"));
        ReservedWordsTable.put("SHOW",new Token("SHOW", "SHOW"));
        ReservedWordsTable.put("THE",new Token("THE", "THE"));
        ReservedWordsTable.put("NOW",new Token("NOW", "NOW"));
        ReservedWordsTable.put("IS",new Token("IS", "IS"));
        ReservedWordsTable.put("FROM",new Token("FROM", "FROM"));
        ReservedWordsTable.put("WHEN",new Token("WHEN", "WHEN"));
        ReservedWordsTable.put("ENDWHEN",new Token("ENDWHEN", "ENDWHEN"));
        ReservedWordsTable.put("OTHERWISE",new Token("OTHERWISE", "OTHERWISE"));
        ReservedWordsTable.put("BE",new Token("BE", "BE"));
    }
}
