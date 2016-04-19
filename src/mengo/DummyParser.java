package mengo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DummyParser {

    public HashMap<String, Token> ReservedWordsTable = new HashMap();
    public HashMap<String, Token> IdentifierTable = new HashMap();

    public void Parse(String inFile) {
        FillReservedWordTable();
        LexicalAnalyzer lexAnalyzer = new LexicalAnalyzer(inFile, IdentifierTable, ReservedWordsTable);
        
        Token tok;
        loop:
        while ((tok = lexAnalyzer.nextToken()) != null) {
            //writer.write(tok.toString() + "\n");
            switch (tok.getKind()) {
                case STXERROR:
                    System.out.println("[" + tok.toString() + "]");
                    break;
                case EOF:
                case STXERRORTERMINATE:
                    System.out.println("[" + tok.toString() + "]");
                    break loop;
                case ID:
                    System.out.println("[ID: " + tok.toString() + "]");
                    break;
                case LITSTRING:
                case NUMCONST:
                case BOOLEANCONST:
                case RELOP:
                case LOGOP:
                    System.out.println("[" + tok.toString() + "]");
                    break;
                case COMMENT:
                case THE:
                    break;
                default:
                    System.out.println("[" + tok.getKind() + "]");
                    break;
            }
        }
        System.out.println();
        System.out.println("Done scanning file: " + inFile);
        System.out.println("\nContents of ID Table: ");
        Iterator it = IdentifierTable.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public void FillReservedWordTable() {
        ReservedWordsTable.put("HELLO", new Token(TokenType.HELLO, "HELLO"));
        ReservedWordsTable.put("GOODBYE", new Token(TokenType.GOODBYE, "GOODBYE"));
        ReservedWordsTable.put("STARTHERE", new Token(TokenType.STARTHERE, "STARTHERE"));
        ReservedWordsTable.put("ENDHERE", new Token(TokenType.ENDHERE, "ENDHERE"));
        ReservedWordsTable.put("TASK", new Token(TokenType.TASK, "TASK"));
        ReservedWordsTable.put("ENDTASK", new Token(TokenType.ENDTASK, "ENDTASK"));
        ReservedWordsTable.put("INVOLVES", new Token(TokenType.INVOLVES, "INVOLVES"));
        ReservedWordsTable.put("NUMBER", new Token(TokenType.NUMBER, "NUMBER"));
        ReservedWordsTable.put("BOOLEAN", new Token(TokenType.BOOLEAN, "BOOLEAN"));
        ReservedWordsTable.put("TRUE", new Token(TokenType.BOOLEANCONST, "TRUE"));
        ReservedWordsTable.put("FALSE", new Token(TokenType.BOOLEANCONST, "FALSE"));
        ReservedWordsTable.put("STRING", new Token(TokenType.STRING, "STRING"));
        ReservedWordsTable.put("ACCEPT", new Token(TokenType.ACCEPT, "ACCEPT"));
        ReservedWordsTable.put("RETURN", new Token(TokenType.RETURN, "RETURN"));
        ReservedWordsTable.put("WHILE", new Token(TokenType.WHILE, "WHILE"));
        ReservedWordsTable.put("ENDWHILE", new Token(TokenType.ENDWHILE, "ENDWHILE"));
        ReservedWordsTable.put("UNTIL", new Token(TokenType.UNTIL, "UNTIL"));
        ReservedWordsTable.put("INCREMENT", new Token(TokenType.INCREMENT, "INCREMENT"));
        ReservedWordsTable.put("BY", new Token(TokenType.BY, "BY"));
        ReservedWordsTable.put("SHOW", new Token(TokenType.SHOW, "SHOW"));
        ReservedWordsTable.put("THE", new Token(TokenType.THE, "THE"));
        ReservedWordsTable.put("IS", new Token(TokenType.RELOP, "IS"));
        ReservedWordsTable.put("ISNT", new Token(TokenType.RELOP, "ISNT"));
        ReservedWordsTable.put("FROM", new Token(TokenType.FROM, "FROM"));
        ReservedWordsTable.put("ENDFROM", new Token(TokenType.ENDFROM, "ENDFROM"));
        ReservedWordsTable.put("WHEN", new Token(TokenType.WHEN, "WHEN"));
        ReservedWordsTable.put("ENDWHEN", new Token(TokenType.ENDWHEN, "ENDWHEN"));
        ReservedWordsTable.put("OTHERWISE", new Token(TokenType.OTHERWISE, "OTHERWISE"));
        ReservedWordsTable.put("BE", new Token(TokenType.ASSIGN, "BE"));
        ReservedWordsTable.put("PERMANENT", new Token(TokenType.PERMANENT, "PERMANENT"));
        ReservedWordsTable.put("CONCATENATE", new Token(TokenType.CONCATENATE, "CONCATENATE"));
    }
}
