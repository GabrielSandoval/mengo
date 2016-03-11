package mengo;

public class Token {

    private TokenType kind;
    private String lexeme;

    public Token() {
        kind = TokenType.STXERROR;
        lexeme = "";
    }

    public Token(TokenType tokKind, String tokLexeme) {
        kind = tokKind;
        lexeme = tokLexeme;
    }

    public TokenType getKind() {
        return kind;
    }

    // Returns the lexeme of the token
    public String getLexeme() {
        return lexeme;
    }

    // Returns a string representation of the token
    public String toString() {
        return kind + ": " + lexeme;
    }
}

enum TokenType {

    STXERROR,
    STXERRORTERMINATE,
    HELLO,
    GOODBYE,
    STARTHERE,
    ENDHERE,
    TASK,
    ENDTASK,
    INVOLVES,
    NUMBER,
    BOOLEAN,
    STRING,
    ACCEPT,
    RETURN,
    WHILE,
    ENDWHILE,
    UNTIL,
    SHOW,
    THE,
    FROM,
    ENDFROM,
    WHEN,
    ENDWHEN,
    OTHERWISE,
    ADD,
    SUB,
    MUL,
    DIV,
    MOD,
    COMMA,
    LPAREN,
    RPAREN,
    LOGICOP,
    RELOP,
    PERIOD,
    COMMENT,
    NUMCONST,
    LITSTRING,
    BOOLEANCONST,
    ID,
    PERMANENT,
    CONCATENATE,
    EOF,
    ASSIGN,
    INCREMENT,
    BY,
    NOISE
}
