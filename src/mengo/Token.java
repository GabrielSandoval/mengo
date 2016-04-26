package mengo;

public class Token {

    private TokenType kind;
    private String lexeme;

    public Token() {
        kind = TokenType.STXERROR;
        lexeme = "";
    }
    @Override
    public String toString(){
        return lexeme.toString();
    }
    public Token(TokenType tokKind, String tokLexeme) {
        kind = tokKind;
        lexeme = tokLexeme;
    }
    public Token(TokenType toKind){
        kind = toKind;
        lexeme = toKind.name();
    }
    public TokenType getKind() {
        return kind;
    }

    // Returns the lexeme of the token
    protected String getLexeme() {
        return lexeme;
    }

    // Returns a string representation of the token
    /*
    public String toString() {
        return kind + ": " + lexeme;
    }
*/
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
    LOGOP,
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
    BE,
    INCREMENT,
    BY,
    NOISE,
    UNARYMINUS,
    UNARYPLUS,
    NOT
}
