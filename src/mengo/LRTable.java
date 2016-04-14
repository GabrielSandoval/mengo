package mengo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LRTable {

    public static HashMap<String, State> States = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException, IOException {
        CreateTable();
        //insert loop here
        //test
        System.out.println(getAction("0", TokenType.ADD));
        System.out.println(getAction("2", TokenType.$));
        System.out.println(getAction("0", "E"));
        System.out.println(getAction("2", TokenType.SUB));
        System.out.println(getAction("4", "F"));
    }
    static void CreateTable() throws FileNotFoundException, IOException{
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
                } while (!elem[i - 1].equals("$"));
                do {
                    VariableList.add(elem[i]);
                    //System.out.println(elem[i]);
                    i++;
                } while (i < elem.length);
            } else {
                //create new state
                States.put(elem[0], new State(elem[0]));
                State tempState = States.get(elem[0]);
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
    static TokenType getTokenType(String lexeme) throws IOException {
        return TokenType.valueOf(lexeme);
    }
    static String getAction(String state, String variable){
        State temp = States.get(state);
        return temp.getAction(variable);
    }
    static String getAction(String state, TokenType Tokentype){
        State temp = States.get(state);
        return temp.getAction(Tokentype);
    }
}
