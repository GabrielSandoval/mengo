/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mengo;

import java.util.ArrayList;

/**
 *
 * @author Chester
 */
public class Rule {
    private final String RuleNumber;
    private final int NumberOfTokens;
    private ArrayList<TreeNode> RHS;
    private final String LHS;
    Rule(String rulenum, String LeftHandSide, int numberOfTokens, ArrayList<TreeNode> production){
        LHS = LeftHandSide;
        RHS = production;
        NumberOfTokens = numberOfTokens;
        RuleNumber = rulenum;
    }
    Rule(String rulenum, String LeftHandSide, int numberOfTokens){
        LHS = LeftHandSide;
        NumberOfTokens = numberOfTokens;
        RuleNumber = rulenum;
    }    
    int getNumberOfTokens(){
        return NumberOfTokens;
    }
    String getRuleNumber(){
        return RuleNumber;
    }
    String getLeftHandSide(){
        return LHS;
    }
}
