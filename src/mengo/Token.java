package mengo;

public class Token {

    private String kind;
    private String lexeme;

    public Token() {
        kind = "";
        lexeme = "";
    }

    public Token(String tokKind, String tokLexeme) {
        kind = tokKind;
        lexeme = tokLexeme;
    }

    public String getKind() {
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
