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
    Parser(){
        
    }
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
                    System.out.println("[" + CurrentToken.toString() + "]");
                    break;
                case STXERRORTERMINATE:
                    System.out.println("[" + CurrentToken.toString() + "]");
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
                                TreeNode tempChild = tree.pop();
                                tempChild.setParent(tempNodeLHS);
                                temporaryChildren.push(tempChild);
                                state.pop();
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
                            System.out.println("Syntax Error " + CurrentToken.getLexeme() + " at line " + lexAnalyzer.getCurrentLineNumber());
                            System.out.println("State: " + CurrentActionItem.ActionState);
                            break loop;
                    }
                    break;
            }

        }
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
         RuleTable.put("1", new Rule("1", "A", 3)); 
         RuleTable.put("2", new Rule("2", "A", 1));  
         
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
