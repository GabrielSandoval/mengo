package mengo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LRTable {

    public final String state;
    //KEY (TokenType)
    public HashMap<TokenType, ActionItem> ActionItemList = new HashMap<>();
    //Key (Variable)
    private HashMap<String, GotoItem> GotoItemList = new HashMap<>();

    LRTable(String s) {
        state = s;
    }
    ArrayList<TokenType> getExpectedTokens(){
        ArrayList<TokenType> temp = new ArrayList();
        Iterator it = ActionItemList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<TokenType, ActionItem> pair = (Map.Entry) it.next();
            if(pair.getValue().Act.Action != LRAction.no_action){
                temp.add(pair.getKey());
            }
            //it.remove(); // avoids a ConcurrentModificationException
        }
        return temp;
    }
    Action getLRAction(TokenType tokentype) {
        ActionItem a = ActionItemList.get(tokentype);
        if(a == null){
            System.out.println("Error State:"+state+" token:"+tokentype);
        }
        return a.GetLRAction();
    }
    String getGotoVariable(String variable){
        //System.out.println(GotoItemList.get(variable).GotoState);
        return GotoItemList.get(variable).getGotoItem();
    }
    void addActionItem(TokenType tt, LRAction act, String state) {
        if (state == null) {
            if (act == LRAction.accept || act == LRAction.no_action) {
                ActionItemList.put(tt, new ActionItem(tt, act));
            } else {
                System.out.println("Error. Not able to add action item.");
            }
        } else {
            ActionItemList.put(tt, new ActionItem(tt, act, state));
        }
    }

    void addGotoItem(String v, String gs) {
        GotoItemList.put(v, new GotoItem(v, gs));
    }

    String getAction(String g) {
        return GotoItemList.get(g).getGotoItem();
    }
    Boolean ifActionExist(String state, Object n){
        return true;
    }
}

enum LRAction {
    no_action,
    shift,
    reduce,
    accept
}

class GotoItem {

    //find other data type for variable to ensure to add only variables that exist in the grammar.
    public final String Variable;
    public final String GotoState;

    GotoItem(String v, String gs) {
        if(gs.contains("-")){
            Variable = v;
            GotoState = null;
        }
        else if (v != null && gs != null) {
            Variable = v;
            GotoState = gs;
        } else if (v != null && gs == null) {
            Variable = v;
            GotoState = null;
        } else {
            Variable = "Error";
            GotoState = null;
        }
    }

    public String getGotoItem() {
        return GotoState;
    }
}

class ActionItem {

    //fix objects, create proper constructors
    public final TokenType Token;
    public final Action Act;

    ActionItem(TokenType t, LRAction a) {
        Act = new Action();
        Token = t;
        Act.Action = a;
        Act.ActionState = null;
    }

    ActionItem(TokenType t, LRAction a, String s) {
        Act = new Action();
        Token = t;
        Act.Action = a;
        Act.ActionState = s;
    }

    Action GetLRAction() {
        return Act;
    }

    @Override
    public String toString() {
        switch (Act.Action) {
            case accept:
                return "Accept";
            case no_action:
                return "No Action";
            case reduce:
                return "Reduce" + Act.ActionState;
            case shift:
                return "Shift" + Act.ActionState;
            default:
                return "Parsing Error, no action assigned to token " + Token.toString();
        }
    }

    //kalat here haha create proper constructors
}

class Action {

    public LRAction Action;
    public String ActionState;
}
