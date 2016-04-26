/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mengo;

import java.util.ArrayList;

/**
 *
 * @author Jullian
 */
public class TaskObject{
    private final TreeNode TaskParent;
    private final int ParameterSize;
    private final ArrayList <ID> Protocol;
    private final String idName;
    private final TreeNode StatementList;
    public String toString(){
        Traverse.traverse(StatementList);
        return "\n\n"+idName + ": " + Protocol.toString() + "\n\n";
    }
    TaskObject(String Name, TreeNode Task, int ParamSize, ArrayList<ID> ParameterProtocol){
        if(Task.getValue() != "task" || Task.getChild(1).Variable != "statements"){
            idName = null;
            TaskParent = null;
            ParameterSize = 0;
            Protocol = null;
            StatementList = null;
        }else{
            idName = Name;
            TaskParent = Task;
            ParameterSize = ParamSize;
            Protocol = ParameterProtocol;
            StatementList = Task.getChild(1);
        }
    }
    String getIDName(){
        return idName;
    }
    TreeNode getParent(){
        return TaskParent;
    }
    int getParameterSize(){
        return ParameterSize;
    }
    ArrayList<ID> getParameterProtocol(){
        return Protocol;
    }
}
