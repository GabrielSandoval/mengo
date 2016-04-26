/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mengo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Jullian
 */
public class RuleMatch {

    HashMap<String, Rule> RuleSet;

    public void FillNumberOfProductionsPerStates() {

        //sample rules for pseudo grammar
        //production number, new rule(production number, left hand side, rhs items)
        //RuleTable.put("1", new Rule("1", "A", 3));
        //RuleTable.put("2", new Rule("2", "A", 1));
        //RuleTable.put("0", new Rule("0", "S'", 1));
        //RuleTable.put("1", new Rule("1", "S", 2));
        //RuleTable.put("2", new Rule("2", "X", 2));
        //RuleTable.put("3", new Rule("3", "X", 1));
        //rules for mengo grammar
        ArrayList<TreeNode> tempprod = new ArrayList();
        tempprod.clear();
        tempprod.add(new TreeNode(new Token(TokenType.HELLO)));
        tempprod.add(new TreeNode(new Token(TokenType.PERIOD)));
        tempprod.add(new TreeNode("initialization"));
        tempprod.add(new TreeNode("main"));
        tempprod.add(new TreeNode("task"));
        tempprod.add(new TreeNode(new Token(TokenType.GOODBYE)));
        tempprod.add(new TreeNode(new Token(TokenType.PERIOD)));
        
        RuleSet.put("1", new Rule("1", "program", 7, tempprod));
        RuleSet.put("2", new Rule("2", "program", 6));
        RuleSet.put("3", new Rule("3", "program", 6));
        RuleSet.put("4", new Rule("4", "program", 5));
        RuleSet.put("5", new Rule("5", "main", 5));
        RuleSet.put("6", new Rule("6", "main", 4));
        RuleSet.put("7", new Rule("7", "task", 5));
        RuleSet.put("8", new Rule("8", "task", 4));
        RuleSet.put("9", new Rule("9", "taskdeclaration", 3));
        RuleSet.put("10", new Rule("10", "taskdeclaration", 7));
        RuleSet.put("11", new Rule("11", "taskcalldec", 3));
        RuleSet.put("12", new Rule("12", "taskcalldec", 1));
        RuleSet.put("13", new Rule("13", "taskcall", 1));
        RuleSet.put("14", new Rule("14", "taskcall", 5));
        RuleSet.put("15", new Rule("15", "paramlist", 3));
        RuleSet.put("16", new Rule("16", "paramlist", 1));
        RuleSet.put("17", new Rule("17", "declaration", 3));
        RuleSet.put("18", new Rule("18", "declaration", 2));
        RuleSet.put("19", new Rule("19", "datatype", 1));
        RuleSet.put("20", new Rule("20", "datatype", 1));
        RuleSet.put("21", new Rule("21", "datatype", 1));
        RuleSet.put("22", new Rule("22", "initialization", 3));
        RuleSet.put("23", new Rule("23", "initialization", 5));
        RuleSet.put("24", new Rule("24", "initialization", 2));
        RuleSet.put("25", new Rule("25", "initialization", 4));
        RuleSet.put("26", new Rule("26", "statements", 2));
        RuleSet.put("27", new Rule("27", "statements", 2));
        RuleSet.put("28", new Rule("28", "statements", 2));
        RuleSet.put("29", new Rule("29", "statements", 2));
        RuleSet.put("30", new Rule("30", "statements", 2));
        RuleSet.put("31", new Rule("31", "statements", 2));
        RuleSet.put("32", new Rule("32", "statements", 2));
        RuleSet.put("33", new Rule("33", "statements", 2));
        RuleSet.put("34", new Rule("34", "statements", 1));
        RuleSet.put("35", new Rule("35", "statements", 1));
        RuleSet.put("36", new Rule("36", "statements", 1));
        RuleSet.put("37", new Rule("37", "statements", 1));
        RuleSet.put("38", new Rule("38", "statements", 1));
        RuleSet.put("39", new Rule("39", "statements", 1));
        RuleSet.put("40", new Rule("40", "statements", 1));
        RuleSet.put("41", new Rule("41", "statements", 1));
        RuleSet.put("42", new Rule("42", "whilestatement", 6));
        RuleSet.put("43", new Rule("43", "ifstatement", 8));
        RuleSet.put("44", new Rule("44", "ifstatement", 6));
        RuleSet.put("45", new Rule("45", "assignmentstatement", 4));
        RuleSet.put("46", new Rule("46", "forloopstatement", 13));
        RuleSet.put("47", new Rule("47", "returnstatement", 3));
        RuleSet.put("48", new Rule("48", "iostatement", 3));
        RuleSet.put("49", new Rule("49", "iostatement", 3));
        RuleSet.put("50", new Rule("50", "concatstatement", 5));
        RuleSet.put("51", new Rule("51", "concatprime", 3));
        RuleSet.put("52", new Rule("52", "concatprime", 3));
        RuleSet.put("53", new Rule("53", "concatprime", 1));
        RuleSet.put("54", new Rule("54", "concatprime", 1));
        RuleSet.put("55", new Rule("55", "relationalexpr", 3));
        RuleSet.put("56", new Rule("56", "relationalexpr", 1));
        RuleSet.put("57", new Rule("57", "logicalexpr", 3));
        RuleSet.put("58", new Rule("58", "logicalexpr", 2));
        RuleSet.put("59", new Rule("59", "logicalexpr", 1));
        RuleSet.put("60", new Rule("60", "E", 3));
        RuleSet.put("61", new Rule("61", "E", 3));
        RuleSet.put("62", new Rule("62", "E", 1));
        RuleSet.put("63", new Rule("63", "T", 3));
        RuleSet.put("64", new Rule("64", "T", 3));
        RuleSet.put("65", new Rule("65", "T", 3));
        RuleSet.put("66", new Rule("66", "T", 1));
        RuleSet.put("67", new Rule("67", "F", 3));
        RuleSet.put("68", new Rule("68", "F", 1));
        RuleSet.put("69", new Rule("69", "F", 1));
        RuleSet.put("70", new Rule("70", "value", 1));
        RuleSet.put("71", new Rule("71", "value", 2));
        RuleSet.put("72", new Rule("72", "value", 1));
        RuleSet.put("73", new Rule("73", "value", 1));
        RuleSet.put("74", new Rule("74", "value", 1));
        RuleSet.put("75", new Rule("75", "unaryoperator", 1));
        RuleSet.put("76", new Rule("76", "unaryoperator", 1));

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
}
