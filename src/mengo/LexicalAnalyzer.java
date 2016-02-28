/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mengo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author lenovo G40-70
 */
public class LexicalAnalyzer {

    private static BufferedReader buffRead;
    private char c;
    private static char SYNTAXERROR = (char) -1;
    private static String EOF = "EOF";

    public LexicalAnalyzer(String inFile) {
        try {
            buffRead = new BufferedReader(new FileReader(inFile));
        } catch (Exception e) {
            System.out.println(e);
        }
        c = read();
    }

    private char read() {
        try {
            return (char) (buffRead.read());
        } catch (IOException e) {
            e.printStackTrace();
            return (char) -2;
        }
    }

    public Token nextToken() {

        int state = 1; // Initial state
        String lexemeBuffer = "";
        while (true) {
//            System.out.println(state);
            switch (state) {
                case 1:                                                      // SPECIAL SYMBOLS
                    switch (c) {
                        case ' ':
                        case '\r':
                        case '\b':
                        case '\f':
                        case '\t':
                        case '\n':
                            c = read();
                            continue;
                        case '+':
                            c = read();
                            return new Token("ADD", "" + c);
                        case '-':
                            c = read();
                            return new Token("SUB", "" + c);
                        case '*':
                            c = read();
                            return new Token("MUL", "" + c);
                        case '/':
                            c = read();
                            return new Token("DIV", "" + c);
                        case '%':
                            c = read();
                            return new Token("MOD", "" + c);
                        case '<':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 2;
                            continue;
                        case '>':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 3;
                        case ',':
                            c = read();
                            return new Token("COMMA", "" + c);
                        case '(':
                            c = read();
                            return new Token("LPAREN", "" + c);
                        case ')':
                            c = read();
                            return new Token("RPAREN", "" + c);
                        case '&':
                            c = read();
                            return new Token("AND", "" + c);
                        case '|':
                            c = read();
                            return new Token("OR", "" + c);
                        case '!':
                            c = read();
                            return new Token("NOT", "" + c);
                        default:
                            state = 4;
                            continue;
                    }

                case 2:                                                         // < or <=

                    switch (c) {
                        case '=':
                            c = read();
                            return new Token("LTE", lexemeBuffer);
                        default:
                            c = read();
                            return new Token("LT", "<");
                    }

                case 3:                                                         // > or >=
                    switch (c) {
                        case '=':
                            c = read();
                            return new Token("GTE", lexemeBuffer);
                        default:
                            c = read();
                            return new Token("GT", ">");
                    }

                case 4:                                                         // START OF COMMENTS or PERIOD DELIMITER
                    switch (c) {
                        case '.':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 5;
                            continue;
                        default:
                            state = 13;
                            continue;
                    }
                case 5:                                                         // CAN BE COMMENT OR PERIOD DELIMITER
                    switch (c) {
                        case '.':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 7;
                            continue;
                        default:
                            c = read();
                            state = 6;
                            continue;

                    }
                case 6:
                    switch (c) {
                        case '\n':
                            c = read();
                            return new Token("PERIOD", ".");
                        default:
                            return new Token(SYNTAXERROR + "", "Syntax Error");
                    }
                case 7:
                    switch (c) {
                        case '.':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 8;
                            continue;
                        default:
                            return new Token(SYNTAXERROR + "", "Syntax Error");
                    }
                case 8:
                    switch (c) {
                        case '<':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 9;
                            continue;
                        default:
                            return new Token(SYNTAXERROR + "", "Syntax Error");
                    }
                case 9:
                    switch (c) {
                        case '>':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 10;
                            break;
                        default:
                            lexemeBuffer += c + "";
                            c = read();
                            continue;
                    }
                case 10:
                    switch (c) {
                        case '.':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 11;
                            continue;
                        default:
                            state = 9;
                    }
                case 11:
                    switch (c) {
                        case '.':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 12;
                            continue;
                        default:
                            state = 9;
                    }
                case 12:                                                        // END OF COMMENT
                    switch (c) {
                        case '.':
                            lexemeBuffer += c + "";
                            c = read();
                            return new Token("COMMENT", lexemeBuffer);
                        default:
                            state = 9;
                    }
                case 13:                                                        // NUMERIC CONSTANT
                    if (isNumber(c)) {
                        lexemeBuffer += c + "";
                        c = read();
                        state = 14;
                        continue;
                    } else {
                        state = 16;
                        continue;
                    }

                case 14:                                                        // NUMERIC CONSTANT

                    if (c == '.') {
                        c = read();
                        state = 15;
                        continue;
                    } else if (isNumber(c)) {
                        lexemeBuffer += c + "";
                        c = read();
                    } else {
                        c = read();
                        return new Token("NUMCONST", lexemeBuffer);
                    }

                case 15:
                    if (c == '\n') {
                        state = 6;
                        continue;
                    }
                    while (true) {
                        if (isNumber(c)) {
                            lexemeBuffer += c + "";
                            c = read();
                        } else {
                            lexemeBuffer += c + "";
                            c = read();
                            return new Token("NUMCONST", lexemeBuffer);
                        }
                    }

                case 16:                                                        // LITERAL STRING
                    switch (c) {
                        case '\"':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 17;
                            continue;
                        default:
                            state = 18;
                            continue;
                    }
                case 17:
                    switch (c) {
                        case '\"':
                            lexemeBuffer += c + "";
                            c = read();
                            return new Token("LITSTRING", lexemeBuffer);
                        default:
                            lexemeBuffer += c + "";
                            c = read();
                            continue;
                    }

                case 18:                                                        // KEYWORDS
                    if (isLetter(c)) {
                        lexemeBuffer += c + "";
                        c = read();
                        state = 19;
                        continue;
                    } else {
                        state = 20;
                        continue;
                    }
                case 19:
                    if (isNumber(c) || isLetter(c)) {
                        lexemeBuffer += c + "";
                        c = read();
                        continue;
                    } else {
                        switch (lexemeBuffer) {
                            case "HELLO":
                                return new Token("HELLO", lexemeBuffer);
                            case "GOODBYE":
                                return new Token("GOODBYE", lexemeBuffer);
                            case "STARTHERE":
                                return new Token("STARTHERE", lexemeBuffer);
                            case "ENDHERE":
                                return new Token("ENDHERE", lexemeBuffer);
                            case "TASK":
                                return new Token("TASK", lexemeBuffer);
                            case "ENDTASK":
                                return new Token("ENDTASK", lexemeBuffer);
                            case "INVOLVES":
                                return new Token("INVOLVES", lexemeBuffer);
                            case "PERMANENT":
                                return new Token("PERMANENT", lexemeBuffer);
                            case "NUMBER":
                                return new Token("NUMBER", lexemeBuffer);
                            case "BOOLEAN":
                                return new Token("BOOLEAN", lexemeBuffer);
                            case "STRING":
                                return new Token("STRING", lexemeBuffer);
                            case "ACCEPT":
                                return new Token("ACCEPT", lexemeBuffer);
                            case "RETURN":
                                return new Token("RETURN", lexemeBuffer);
                            case "WHILE":
                                return new Token("WHILE", lexemeBuffer);
                            case "ENDWHILE":
                                return new Token("ENDWHILE", lexemeBuffer);
                            case "UNTIL":
                                return new Token("UNTIL", lexemeBuffer);
                            case "INCREMENT":
                                return new Token("INCREMENT", lexemeBuffer);
                            case "BY":
                                return new Token("BY", lexemeBuffer);
                            case "SHOW":
                                return new Token("SHOW", lexemeBuffer);
                            case "THE":
                                return new Token("THE", lexemeBuffer);
                            case "NOW":
                                return new Token("NOW", lexemeBuffer);
                            case "IS":
                                return new Token("ASSIGNMENT", lexemeBuffer);
                            default:
                                return new Token("ID", lexemeBuffer);
                        }
                    }
                case 20:
                    c = read();
                    return new Token(EOF + "", "End of File");
                default:
                    return new Token(SYNTAXERROR + "", "Syntax Error");
            }
        }
    }

// HELPERS
    public static boolean isLetter(char c) {
        String symbol = "" + c + "";
        if ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains(symbol)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNumber(char c) {
        String symbol = "" + c + "";
        if ("0123456789".contains(symbol)) {
            return true;
        } else {
            return false;
        }
    }

}
