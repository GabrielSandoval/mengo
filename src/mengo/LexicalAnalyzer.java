package mengo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.HashMap;

public class LexicalAnalyzer {

    private static PushbackInputStream buffRead;
    private char c;
    private static int EOF = 65535;
    private static int lookahead;
    private int currentCol = 0;
    private int currentLineNum = 1;
    public Token LastToken;
    HashMap<String, Token> IdentifierTable;
    HashMap<String, Token> ReservedWordsTable;
    int getCurrentLineNumber(){
        return currentLineNum;
    }
    public LexicalAnalyzer(String inFile, HashMap<String, Token> IdTable, HashMap<String, Token> RWsTable) {
        ReservedWordsTable = RWsTable;
        IdentifierTable = IdTable;
        LastToken = null;
        lookahead = 10;
        try {
            buffRead = new PushbackInputStream(new FileInputStream(inFile), lookahead);
        } catch (Exception e) {
            System.out.println(e);
        }
        c = read();
    }
    public LexicalAnalyzer(File inFile) {
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
            char c = (char)buffRead.read();
            if (c == '\n') {
                this.currentLineNum++;
                this.currentCol = 0;
            }
            return (char) (c);
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
                            c = read();
                            continue;
                        case 255:                                           //non braking space
                            c = read();
                            continue;
                        case '+':
                            lexemeBuffer += c + "";
                            c = read();
                            while (c == ' ' || c == '\n' || c == '\t') {
                                c = read();
                            }
                            if (LastToken!= null && (LastToken.getKind() == TokenType.NUMCONST || LastToken.getKind() == TokenType.ID)) {
                                setLastToken(new Token(TokenType.ADD, lexemeBuffer));
                                return LastToken;
                            } else {
                                if (isNumber(c)) {
                                    state = 13;
                                    continue;
                                } else {
                                    setLastToken(new Token(TokenType.UNARYPLUS, lexemeBuffer));
                                    return LastToken;
                                }
                            }
                        case '-':
                            lexemeBuffer += c + "";
                            c = read();
                            while (c == ' ' || c == '\n' || c == '\t') {
                                c = read();
                            }
                            if (LastToken!= null && (LastToken.getKind() == TokenType.NUMCONST || LastToken.getKind() == TokenType.ID)) {
                                setLastToken(new Token(TokenType.SUB, lexemeBuffer));
                                return LastToken;
                            } else {
                                if (isNumber(c)) {
                                    state = 13;
                                    continue;
                                } else {
                                    setLastToken(new Token(TokenType.UNARYMINUS, lexemeBuffer));
                                    return LastToken;
                                }
                            }
                        case '*':
                            lexemeBuffer += c + "";
                            c = read();
                            setLastToken(new Token(TokenType.MUL, lexemeBuffer));
                            return LastToken;
                        case '/':
                            lexemeBuffer += c + "";
                            c = read();
                            setLastToken(new Token(TokenType.DIV, lexemeBuffer));
                            return LastToken;
                        case '%':
                            lexemeBuffer += c + "";
                            c = read();
                            setLastToken(new Token(TokenType.MOD, lexemeBuffer));
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
                            setLastToken(new Token(TokenType.COMMA, lexemeBuffer));
                            return LastToken;
                        case '(':
                            lexemeBuffer += c + "";
                            c = read();
                            setLastToken(new Token(TokenType.LPAREN, lexemeBuffer));
                            return LastToken;
                        case ')':
                            lexemeBuffer += c + "";
                            c = read();
                            setLastToken(new Token(TokenType.RPAREN, lexemeBuffer));
                            return LastToken;
                        case '&':
                            lexemeBuffer += c + "";
                            c = read();
                            setLastToken(new Token(TokenType.LOGICOP, lexemeBuffer));
                            return LastToken;
                        case '|':
                            lexemeBuffer += c + "";
                            c = read();
                            setLastToken(new Token(TokenType.LOGICOP, lexemeBuffer));
                            return LastToken;
                        case '~':
                            lexemeBuffer += c + "";
                            c = read();
                            setLastToken(new Token(TokenType.NOT, lexemeBuffer));
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
                            setLastToken(new Token(TokenType.RELOP, lexemeBuffer));
                            return LastToken;
                        default:
                            setLastToken(new Token(TokenType.RELOP, "<"));
                            return LastToken;
                    }

                case 3:                                                         // > or >=
                    switch (c) {
                        case '=':
                            lexemeBuffer += c + "";
                            c = read();
                            setLastToken(new Token(TokenType.RELOP, lexemeBuffer));
                            return LastToken;
                        default:
                            setLastToken(new Token(TokenType.RELOP, ">"));
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
                            setLastToken(new Token(TokenType.PERIOD, "."));
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
                            setLastToken(new Token(TokenType.STXERROR, "Syntax Error! Expecting another \".\" in creating a comment. At line " + this.currentLineNum + " at column " + (this.currentCol - 1)));
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
                            setLastToken(new Token(TokenType.STXERROR, "Syntax Error! Expecting \"<\" in creating a comment. At line " + this.currentLineNum + " at column " + (this.currentCol - 1)));
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
                            if (c == EOF) {
                                setLastToken(new Token(TokenType.STXERRORTERMINATE, "EOF found, comment unfinished at line " + this.currentLineNum + " at " + (this.currentCol - 1)));
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
                                setLastToken(new Token(TokenType.STXERRORTERMINATE, "EOF found, comment unfinished at line " + this.currentLineNum + " at " + (this.currentCol - 1)));
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
                                setLastToken(new Token(TokenType.STXERRORTERMINATE, "EOF found, comment unfinished at line " + this.currentLineNum + " at " + (this.currentCol - 1)));
                                return LastToken;
                            }
                            state = 9;
                            continue;
                    }
                case 12:                                                        // END OF COMMENT
                    switch (c) {
                        case '.':
                            lexemeBuffer += c + "";
                            c = read();
                            setLastToken(new Token(TokenType.COMMENT, lexemeBuffer));
                            return LastToken;
                        default:
                            if (c == EOF) {
                                setLastToken(new Token(TokenType.STXERRORTERMINATE, "EOF found, comment unfinished at line " + this.currentLineNum + " at " + (this.currentCol - 1)));
                                return LastToken;
                            }
                            state = 9;
                            continue;
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
                        setLastToken(new Token(TokenType.NUMCONST, lexemeBuffer));
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
                                setLastToken(new Token(TokenType.NUMCONST, lexemeBuffer));
                                return LastToken;
                            }
                        }
                    } else {
                        unread(c);
                        unread('.');
                        c = read();
                        setLastToken(new Token(TokenType.NUMCONST, lexemeBuffer));
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
                            setLastToken(new Token(TokenType.LITSTRING, lexemeBuffer));
                            return LastToken;
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
                        unread(c);
                        c = read();
                        Token temp = ReservedWordsTable.get(lexemeBuffer);
                        if (temp == null) {
                            setLastToken(new Token(TokenType.ID, lexemeBuffer));
                            return LastToken;
                        }
                        setLastToken(temp);
                        return LastToken;
                    }
                case 20:
                    if (c == EOF) {
                        setLastToken(new Token(TokenType.EOF, "End of File"));
                        return LastToken;
                    } else {
                        String temp = c + "";
                        c = read();
                        setLastToken(new Token(TokenType.STXERROR, "Syntax Error! The character " + temp + " is unknown. Remove character at line " + this.currentLineNum + " at column " + (this.currentCol - 1)));
                        return LastToken;
                    }
                default:
                    String temp = c + "";
                    c = read();
                    setLastToken(new Token(TokenType.STXERROR, "Syntax Error! The character " + temp + " is unknown. Remove character at line " + this.currentLineNum + " at column " + (this.currentCol - 1)));
                    return LastToken;
            }
        }
    }

    public void setLastToken(Token t) {
        if (LastToken != null) {
            if(t.getKind() == TokenType.ID){
                IdentifierTable.put(t.getLexeme(), t);
            }
            if (t.getKind() != TokenType.ID && LastToken.getKind() == TokenType.THE) {
                LastToken = new Token(TokenType.STXERROR, "Syntax Error! the keyword \"THE\" does not appear before a literal string. At line number: " + this.currentLineNum);
            } else {
                LastToken = t;
            }
        } else {
            LastToken = t;
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
