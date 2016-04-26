/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mengo;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Chester
 */
public class TreeNode {
    //either token or variable have values
    final Token token;
    final String Variable;
    private TreeNode Parent;
    private ArrayList <TreeNode> children = new ArrayList();
    TreeNode(Token Token_Constructor){
        token = Token_Constructor;
        Variable = null;
    }
    TreeNode(String Variable_Constructor){
        Variable = Variable_Constructor;
        token = null;
    }
    @Override
    public String toString(){
        return getValue().toString();
    }
    String getVariable(){
        return Variable;
    }
    ArrayList<TreeNode> getChildren(){
        return children;
    }
    void addChildren(Stack<TreeNode> stackChildren){
        while(!stackChildren.empty()){
           children.add(stackChildren.pop()); 
        }
    }
    Object getValue(){
        if(token == null && Variable == null){
            return null;
            //error
        }
        else if(token == null){
            return Variable;
        }
        else{
            return token;
        }
    }
    void setParent(TreeNode parent){
        Parent = parent;
    }
    TreeNode getChild(int i){
        if(i> children.size() || i < 0 )
        {
            return null;
        }
        return children.get(i);
    }
}

