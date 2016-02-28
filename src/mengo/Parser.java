/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mengo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author lenovo G40-70
 */
public class Parser {

    public static void main(String[] args) {
        String root = "C:\\Users\\lenovo G40-70\\Desktop\\MENGO_Code\\";
        String inFile = root+"sample2.mpl";
        String outFile = root+"sample2out.mpl";

        LexicalAnalyzer lexAnalyzer = new LexicalAnalyzer(inFile);

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

            Token tok = new Token();

            while ((tok = lexAnalyzer.nextToken()) != null) {
                
                if (tok.getKind() == "-1") {

                } else if (tok.getKind().equals("EOF")) {
                    break;
                } else {
                    writer.write(tok.toString());
                    System.out.println(tok.toString());
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
}
