package mengo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class Parser {

    public HashMap<String, State> ParsingTable = new HashMap<>();
    public HashMap<String, Token> ReservedWordsTable = new HashMap();
    public HashMap<String, Token> IdentifierTable = new HashMap();
    public HashMap<String, Rule> RuleTable = new HashMap();

    public void Parse(String inFile) throws FileNotFoundException, IOException {
        CreateTable();
        FillNumberOfProductionsPerStates();
        //insert loop here
        FillReservedWordTable();
        //String root = "E:\\Jullian\\4thyr_2nd sem\\CS105 - Compiler Design\\";
        //String inFile = root + "SAMPLE2.mpl";

        LexicalAnalyzer lexAnalyzer = new LexicalAnalyzer(inFile, IdentifierTable, ReservedWordsTable);

        Token CurrentToken = lexAnalyzer.nextToken();
        Stack<TreeNode> tree = new Stack();
        Stack<String> state = new Stack();
        tree.push(new TreeNode(new Token(TokenType.EOF, (char) 65535 + "")));
        state.push("0");
        Boolean ConsumedFlag = false;
        Boolean ErrorFlag = false;
        loop:
        while (true) {
            //print state
            System.out.print(state.toString() + "\t");
            //print stack
            System.out.print(tree.toString() + "\t");
            if (ConsumedFlag == true) {
                CurrentToken = lexAnalyzer.nextToken();
                ConsumedFlag = false;
            }
            switch (CurrentToken.getKind()) {
                case COMMENT:
                case THE:
                    break;
                case STXERROR:
                    System.out.println("\n" + CurrentToken.toString() + "");
                    ConsumedFlag = true;
                    break;
                case STXERRORTERMINATE:
                    System.out.println("[" + CurrentToken.toString() + "]");
                    ErrorFlag = true;
                    break loop;
                default:
                    //System.out.println("[" + tok.getKind() + "]");
                    State CurrentState = ParsingTable.get(state.peek());
                    Action CurrentActionItem = CurrentState.getLRAction(CurrentToken.getKind());
                    switch (CurrentActionItem.Action) {
                        case accept:

                            System.out.println("\tAccept");
                            break loop;
                        //create a node then push to stack and update state stack
                        case shift:
                            System.out.println("Shift " + CurrentActionItem.ActionState);
                            state.push(CurrentActionItem.ActionState);
                            tree.push(new TreeNode(CurrentToken));
                            ConsumedFlag = true;
                            break;
                        //only during reduce a node has given a parent
                        case reduce:
                            //get the properties of the rule
                            System.out.println("Reduce " + CurrentActionItem.ActionState);
                            //the actionstate is equal to the state in the action table to be used as rule number
                            //System.out.print("\t" + "Push " + );
                            Rule tempRule = RuleTable.get(CurrentActionItem.ActionState);
                            //System.out.println("\t" + state.peek());
                            int NumOfPop = tempRule.getNumberOfTokens();
                            Stack<TreeNode> temporaryChildren = new Stack();
                            //get the lhs of the rule
                            String currentVariable = tempRule.getLeftHandSide();
                            //create a tree node with the variable
                            TreeNode tempNodeLHS = new TreeNode(currentVariable);
                            //pop items then make it as the children of a new LHS
                            for (int i = 0; i < NumOfPop; i++) {
                                //save to temporary stack
                                TreeNode tempChild = null;
                                try {
                                    state.pop();
                                    tempChild = tree.pop();

                                } catch (Exception e) {
                                    System.out.println("Error at reduce. Stack is empty.");
                                    ErrorFlag = true;
                                    break loop;
                                }
                                tempChild.setParent(tempNodeLHS);
                                temporaryChildren.push(tempChild);
                            }

                            //put the children to the new node
                            tempNodeLHS.addChildren(temporaryChildren);
                            //check the goto table using the variable then push the new state
                            //System.out.println(""+CurrentState.getGotoVariable(currentVariable));
                            //
                            CurrentState = ParsingTable.get(state.peek());
                            state.push(CurrentState.getGotoVariable(currentVariable));
                            //push the created tree to the stack
                            tree.push(tempNodeLHS);

                            break;
                        default:
                        case no_action:
                            System.out.println("Syntax Error: " + CurrentToken.getLexeme() + " at line " + lexAnalyzer.getCurrentLineNumber());
                            System.out.println("Expecting tokens: " + CurrentState.getExpectedTokens().toString());
                            break loop;
                    }
                    break;
            }

        }

        if (ErrorFlag == false) {
            System.out.println();
            System.out.println("Done parsing file: " + inFile);
            System.out.println("Parse Tree:\n");
            traverse(tree.peek());
            System.out.println("\n\nContents of ID Table: ");
            Iterator it = IdentifierTable.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
        }

    }

    void traverse(TreeNode root) {
        TreeNode n = root;
        ArrayList<TreeNode> currentChildren = n.getChildren();
        if (currentChildren.isEmpty()) {
            System.out.print(n.getValue() + " ");
            return;
        }
        System.out.print(n.getValue() + " [ "); // visit(n);
        for (int i = 0; i < currentChildren.size(); i++) {
            traverse(currentChildren.get(i));
        }
        System.out.print("] ");
    }

    void CreateTable() throws FileNotFoundException, IOException {
        BufferedReader inFile = new BufferedReader(new FileReader("src\\LRTable.csv"));
        String line = inFile.readLine();
        ArrayList<TokenType> TokenTypeList = new ArrayList();
        ArrayList<String> VariableList = new ArrayList();
        while (line != null) {
            String elem[] = line.split(";");
            if (elem[0].equals("state")) {
                //System.out.println("first line");
                int i = 1;
                do {
                    //get TokeyType
                    TokenTypeList.add(getTokenType(elem[i]));
                    //System.out.println(getTokenType(elem[i]).toString());
                    i++;
                } while (!elem[i - 1].equals("EOF"));
                do {
                    VariableList.add(elem[i]);
                    //System.out.println(elem[i]);
                    i++;
                } while (i < elem.length);
            } else {
                //create new state
                ParsingTable.put(elem[0], new State(elem[0]));
                State tempState = ParsingTable.get(elem[0]);
                int ctr = 1;
                int listctr = 0;
                do {

                    //System.out.print(listctr + ":" + elem[ctr] + "\t");
                    LRAction Action;
                    String temp = null;
                    if (elem[ctr].contentEquals("accept")) {
                        Action = LRAction.accept;
                    } else if (elem[ctr].startsWith("s")) {
                        Action = LRAction.shift;
                        temp = elem[ctr].replace("s", "");
                    } else if (elem[ctr].startsWith("r")) {
                        Action = LRAction.reduce;
                        temp = elem[ctr].replace("r", "");
                    } else {
                        Action = LRAction.no_action;
                    }
                    tempState.addActionItem(TokenTypeList.get(listctr), Action, temp);

                    ctr++;
                    listctr++;
                } while (ctr <= TokenTypeList.size());
                listctr = 0;
                do {
                    if (ctr < elem.length && listctr <= VariableList.size()) {
                        //System.out.print(listctr + ":" + elem[ctr] + "\t");
                        tempState.addGotoItem(VariableList.get(listctr), elem[ctr]);
                    }
                    ctr++;
                    listctr++;
                } while (ctr <= TokenTypeList.size() + VariableList.size());
            }
            //System.out.println("");
            line = inFile.readLine();
        }
        inFile.close();
    }

    TokenType getTokenType(String lexeme) throws IOException {
        return TokenType.valueOf(lexeme);
    }

    Action getLRAction(String state, TokenType Tokentype) {
        State temp = ParsingTable.get(state);
        return temp.getLRAction(TokenType.ID);
    }

    String getGotoItem(String state, String variable) {
        State temp = ParsingTable.get(state);
        return temp.getGotoVariable(variable);
    }

    public void FillNumberOfProductionsPerStates() {

        //sample rules for pseudo grammar
        //production number, new rule(production number, left hand side, rhs items)
        //RuleTable.put("1", new Rule("1", "A", 3));
        //RuleTable.put("2", new Rule("2", "A", 1));
        //rules for mengo grammar
        RuleTable.put("1", new Rule("1", "program", 7));
        RuleTable.put("2", new Rule("2", "program", 6));
        RuleTable.put("3", new Rule("3", "program", 6));
        RuleTable.put("4", new Rule("4", "program", 5));
        RuleTable.put("5", new Rule("5", "main", 5));
        RuleTable.put("6", new Rule("6", "main", 4));
        RuleTable.put("7", new Rule("7", "task", 5));
        RuleTable.put("8", new Rule("8", "task", 4));
        RuleTable.put("9", new Rule("9", "taskdeclaration", 2));
        RuleTable.put("10", new Rule("10", "taskdeclaration", 6));
        RuleTable.put("11", new Rule("11", "taskcalldec", 3));
        RuleTable.put("12", new Rule("12", "taskcalldec", 2));
        RuleTable.put("13", new Rule("13", "taskcall", 1));
        RuleTable.put("14", new Rule("14", "taskcall", 5));
        RuleTable.put("15", new Rule("15", "paramlist", 3));
        RuleTable.put("16", new Rule("16", "paramlist", 1));
        RuleTable.put("17", new Rule("17", "declaration", 3));
        RuleTable.put("18", new Rule("18", "declaration", 2));
        RuleTable.put("19", new Rule("19", "datatype", 1));
        RuleTable.put("20", new Rule("20", "datatype", 1));
        RuleTable.put("21", new Rule("21", "datatype", 1));
        RuleTable.put("22", new Rule("22", "initialization", 3));
        RuleTable.put("23", new Rule("23", "initialization", 5));
        RuleTable.put("24", new Rule("24", "statements", 2));
        RuleTable.put("25", new Rule("25", "statements", 4));
        RuleTable.put("26", new Rule("26", "statements", 2));
        RuleTable.put("27", new Rule("27", "statements", 2));
        RuleTable.put("28", new Rule("28", "statements", 2));
        RuleTable.put("29", new Rule("29", "statements", 2));
        RuleTable.put("30", new Rule("30", "statements", 2));
        RuleTable.put("31", new Rule("31", "statements", 2));
        RuleTable.put("32", new Rule("32", "statements", 2));
        RuleTable.put("33", new Rule("33", "statements", 2));
        RuleTable.put("34", new Rule("34", "statements", 1));
        RuleTable.put("35", new Rule("35", "statements", 1));
        RuleTable.put("36", new Rule("36", "statements", 1));
        RuleTable.put("37", new Rule("37", "statements", 1));
        RuleTable.put("38", new Rule("38", "statements", 1));
        RuleTable.put("39", new Rule("39", "statements", 1));
        RuleTable.put("40", new Rule("40", "statements", 1));
        RuleTable.put("41", new Rule("41", "statements", 1));
        RuleTable.put("42", new Rule("42", "whilestatement", 6));
        RuleTable.put("43", new Rule("43", "ifstatement", 8));
        RuleTable.put("44", new Rule("44", "ifstatement", 6));
        RuleTable.put("45", new Rule("45", "assignmentstatement", 4));
        RuleTable.put("46", new Rule("46", "forloopstatement", 13));
        RuleTable.put("47", new Rule("47", "returnstatement", 3));
        RuleTable.put("48", new Rule("48", "iostatement", 3));
        RuleTable.put("49", new Rule("49", "iostatement", 3));
        RuleTable.put("50", new Rule("50", "concatstatement", 5));
        RuleTable.put("51", new Rule("51", "concatprime", 3));
        RuleTable.put("52", new Rule("52", "concatprime", 3));
        RuleTable.put("53", new Rule("53", "concatprime", 1));
        RuleTable.put("54", new Rule("54", "concatprime", 1));
        RuleTable.put("55", new Rule("55", "relationalexpr", 3));
        RuleTable.put("56", new Rule("56", "relationalexpr", 1));
        RuleTable.put("57", new Rule("57", "logicalexpr", 3));
        RuleTable.put("58", new Rule("58", "logicalexpr", 2));
        RuleTable.put("59", new Rule("59", "logicalexpr", 1));
        RuleTable.put("60", new Rule("60", "E", 3));
        RuleTable.put("61", new Rule("61", "E", 3));
        RuleTable.put("62", new Rule("62", "E", 3));
        RuleTable.put("63", new Rule("63", "T", 3));
        RuleTable.put("64", new Rule("64", "T", 3));
        RuleTable.put("65", new Rule("65", "T", 3));
        RuleTable.put("66", new Rule("66", "T", 1));
        RuleTable.put("67", new Rule("67", "F", 3));
        RuleTable.put("68", new Rule("68", "F", 1));
        RuleTable.put("69", new Rule("69", "F", 1));
        RuleTable.put("70", new Rule("70", "value", 1));
        RuleTable.put("71", new Rule("71", "value", 2));
        RuleTable.put("72", new Rule("72", "value", 1));
        RuleTable.put("73", new Rule("73", "value", 1));
        RuleTable.put("74", new Rule("74", "value", 1));
        RuleTable.put("75", new Rule("75", "unaryoperator", 1));
        RuleTable.put("76", new Rule("76", "unaryoperator", 1));

        /*
         ArrayList<TreeNode> tempprod = new ArrayList();
         tempprod.clear();
         tempprod.add(new TreeNode(new Token(TokenType.LPAREN, "(")));
         tempprod.add(new TreeNode("A"));
         tempprod.add(new TreeNode(new Token(TokenType.RPAREN, ")")));
         RuleTable.put("1", new Rule("1", "A", tempprod, 3));
         tempprod.clear();
         tempprod.add(new TreeNode(new Token(TokenType.ID, "")));
         RuleTable.put("2", new Rule("2", "A", tempprod, 1));
         */
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
