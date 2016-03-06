package mengo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class LexicalAnalyzer {

    private static PushbackInputStream buffRead;
    private char c;
    private static int EOF = 65535;
    private static int lookahead;
    private int currentCol = 0;
    private int curretLineNum = 1;
    public Token LastToken;

    public LexicalAnalyzer(String inFile) {
        LastToken = null;
        lookahead = 10;
        try {
            buffRead = new PushbackInputStream(new FileInputStream(inFile), lookahead);
        } catch (Exception e) {
            System.out.println(e);
        }
        c = read();
    }

    private char read() {
        try {
            currentCol++;
            return (char) (buffRead.read());
        } catch (IOException e) {
            e.printStackTrace();
            return (char) -2;
        }
    }

    private void unread(char c) {
        try {
            currentCol--;
            buffRead.unread(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Token nextToken() {

        int state = 1; // Initial state
        String lexemeBuffer = "";
        while (true) {
            switch (state) {
                case 1:                                                      // SPECIAL SYMBOLS
                    switch (c) {
                        case ' ':
                            c = read();
                            continue;
                        case '\r':
                            c = read();
                            continue;
                        case '\b':
                            c = read();
                            continue;
                        case '\f':
                            c = read();
                            continue;
                        case '\t':
                            c = read();
                            continue;
                        case '\n':
                            currentCol = 0;
                            curretLineNum++;
                            c = read();
                            continue;
                        case 255:                                           //non braking space
                            c = read();
                            continue;
                        case '+':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.ADD, lexemeBuffer);
                            return LastToken;
                        case '-':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.SUB, lexemeBuffer);
                            return LastToken;
                        case '*':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.MUL, lexemeBuffer);
                            return LastToken;
                        case '/':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.DIV, lexemeBuffer);
                            return LastToken;
                        case '%':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.MOD, lexemeBuffer);
                            return LastToken;
                        case '<':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 2;
                            continue;
                        case '>':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 3;
                            continue;
                        case ',':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.COMMA, lexemeBuffer);
                            return LastToken;
                        case '(':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.LPAREN, lexemeBuffer);
                            return LastToken;
                        case ')':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.RPAREN, lexemeBuffer);
                            return LastToken;
                        case '&':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.AND, lexemeBuffer);
                            return LastToken;
                        case '|':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.OR, lexemeBuffer);
                            return LastToken;
                        case '~':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.NOT, lexemeBuffer);
                            return LastToken;
                        default:
                            state = 4;
                            continue;
                    }
                case 2:                                                         // < or <=

                    switch (c) {
                        case '=':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.LTE, lexemeBuffer);
                            return LastToken;
                        default:
                            LastToken = new Token(TokenType.LT, "<");
                            return LastToken;
                    }

                case 3:                                                         // > or >=
                    switch (c) {
                        case '=':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.GTE, lexemeBuffer);
                            return LastToken;
                        default:
                            LastToken = new Token(TokenType.GT, ">");
                            return LastToken;
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
                            unread(c);
                            c = read();
                            LastToken = new Token(TokenType.PERIOD, ".");
                            return LastToken;
                    }
                case 7:
                    switch (c) {
                        case '.':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 8;
                            continue;
                        default:
                            LastToken = new Token(TokenType.STXERROR, "Syntax Error! Expecting another \".\" in creating a comment. At line " + this.curretLineNum + " at column " + (this.currentCol - 1));
                            return LastToken;
                    }
                case 8:
                    switch (c) {
                        case '<':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 9;
                            continue;
                        default:
                            LastToken = new Token(TokenType.STXERROR, "Syntax Error! Expecting \"<\" in creating a comment. At line " + this.curretLineNum + " at column " + (this.currentCol - 1));
                            return LastToken;
                    }
                case 9:
                    switch (c) {
                        case '>':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 10;
                            continue;
                        default:
                            if (c == '\n') {
                                this.curretLineNum++;
                            }
                            if (c == EOF) {
                                LastToken = new Token(TokenType.STXERRORTERMINATE, "EOF found, comment unfinished at line " + this.curretLineNum  + " at " + (this.currentCol-1));
                                return LastToken;
                            }
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
                            if (c == EOF) {
                                LastToken = new Token(TokenType.STXERRORTERMINATE, "EOF found, comment unfinished at line " + this.curretLineNum  + " at " + (this.currentCol-1));
                                return LastToken;
                            }
                            state = 9;
                            continue;
                    }
                case 11:
                    switch (c) {
                        case '.':
                            lexemeBuffer += c + "";
                            c = read();
                            state = 12;
                            continue;
                        default:
                            if (c == EOF) {
                                LastToken = new Token(TokenType.STXERRORTERMINATE, "EOF found, comment unfinished at line " + this.curretLineNum  + " at " + (this.currentCol-1));
                                return LastToken;
                            }
                            state = 9;
                    }
                case 12:                                                        // END OF COMMENT
                    switch (c) {
                        case '.':
                            lexemeBuffer += c + "";
                            c = read();
                            LastToken = new Token(TokenType.COMMENT, lexemeBuffer);
                            return LastToken;
                        default:
                            if (c == EOF) {
                                LastToken = new Token(TokenType.STXERRORTERMINATE, "EOF found, comment unfinished at line " + this.curretLineNum  + " at " + (this.currentCol-1));
                                return LastToken;
                            }
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
                        continue;
                    } else {
                        LastToken = new Token(TokenType.NUMCONST, lexemeBuffer);
                        return LastToken;
                    }
                case 15:
                    if (isNumber(c)) {
                        lexemeBuffer += ".";
                        while (true) {
                            if (isNumber(c)) {
                                lexemeBuffer += c + "";
                                c = read();
                            } else {
                                LastToken = new Token(TokenType.NUMCONST, lexemeBuffer);
                                return LastToken;
                            }
                        }
                    } else {
                        unread(c);
                        unread('.');
                        c = read();
                        LastToken = new Token(TokenType.NUMCONST, lexemeBuffer);
                        return LastToken;
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
                            LastToken = new Token(TokenType.LITSTRING, lexemeBuffer);
                            return LastToken;
                        default:
                            if (c == '\n') {
                                this.curretLineNum++;
                            }
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
                        unread(c);
                        c = read();
                        Token temp = Parser.ReservedWordsTable.get(lexemeBuffer);
                        if (temp == null) {
                            LastToken = new Token(TokenType.ID, lexemeBuffer);
                            return LastToken;
                        }
                        return temp;
                    }
                case 20:
                    if (c == EOF) {
                        LastToken = new Token(TokenType.EOF, "End of File");
                        return LastToken;
                    } else {
                        String temp = c + "";
                        c = read();
                        LastToken = new Token(TokenType.STXERROR, "Syntax Error! The character " + temp + " is unknown. Remove character at line " + this.curretLineNum + " at column " + (this.currentCol - 1));
                        return LastToken;
                    }
                default:
                    String temp = c + "";
                    c = read();
                    LastToken = new Token(TokenType.STXERROR, "Syntax Error! The character " + temp + " is unknown. Remove character at line " + this.curretLineNum + " at column " + (this.currentCol - 1));
                    return LastToken;
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
