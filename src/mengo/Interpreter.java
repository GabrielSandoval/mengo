/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mengo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author Jullian
 */
public class Interpreter {

    Boolean ErrorFound;
    HashMap<String, TaskObject> TaskMap = new HashMap();
    ArrayList<Variable> GlobalVariable;
    Stack<ArrayList<Variable>> LocalVariable;
    //working variable
    ArrayList<ID> TemporaryProtocol;
    Interpreter() {
        ErrorFound = false;
    }

    void interpret(TreeNode root) {
        //
        FirstPass(root);
        //if error is found on first pass.
        if (ErrorFound) {
            System.out.println("Error Found");
            
        } else {
            
        }
    }
    void Program(TreeNode root){
        GlobalVariable = new ArrayList();
        LocalVariable = new Stack();
    }
    
    
    
    

    void FirstPass(TreeNode root) {
        //Create map for task id and root reference, parameter protocol.
        createTaskMap(root);
        //System.out.println(TaskMap.toString());
    }
    void addTaskAttribute(String idName, TaskObject taskAttribute){
        //System.out.println(taskAttribute.toString());
        TaskMap.put(idName, taskAttribute);
    }
    void createTaskMap(TreeNode root) {
        //check if main root contains TASK
        while (root.getChildren().size() >= 5 && root.getChildren().get(4).getValue() == "task") {
            //root == Task
            root = root.getChildren().get(4);


            TreeNode TaskDeclaration = root.getChildren().get(0);
            if (TaskDeclaration.getValue() == "taskdeclaration") {
                Token tempID = TaskDeclaration.getChild(1).token;
                if (tempID.getKind() == TokenType.ID) {
                    //System.out.println("task found: " + tempID.getLexeme());
                    //check if taskdeclaration contains parameter passing through 3rd token
                    if(TaskDeclaration.getChild(2).token.getKind() == TokenType.PERIOD){
                        //if taskdeclaration -> task id period
                        addTaskAttribute(tempID.getLexeme(),new TaskObject(tempID.getLexeme(), root, 0, new <ID> ArrayList()));
                    }
                    else if(TaskDeclaration.getChild(2).token.getKind() == TokenType.INVOLVES){
                        TemporaryProtocol = new ArrayList();
                        int NumberOfChild = CheckParamList(TaskDeclaration.getChild(4));
                        addTaskAttribute(tempID.getLexeme(),new TaskObject(tempID.getLexeme(), root, NumberOfChild, TemporaryProtocol));
                    }
                    else{
                        System.out.println("Invalid children for taskcalldeclaration.");
                        ErrorFound = true;
                        return;
                    }
                    
                    
                    //assign the parent and parameter size to ID
                    
                }
            } else {
                System.out.println("Error Invalid Tree for TASK");
                ErrorFound = true;
                return;
            }
        }
    }
    //counts number of parameters
    int CheckParamList(TreeNode TaskCallDec){
        if(TaskCallDec.Variable != "taskcalldec"){
            System.out.println("Error in checking parameter list. Wrong Tree Input.");
            return 0;
        }
        else{
            //traverse declaration child to instantiate
            
            //if production taskcalldec -> DECLARATION
            if (TaskCallDec.getChildren().size() == 1 && TaskCallDec.getChild(0).Variable == "declaration" ){
                TreeNode Declaration = TaskCallDec.getChild(0);
                UpdateTempParamList(Declaration);
                
                return 1;
            }
            //traverse to suceeding taskcalldec
            else if(TaskCallDec.getChildren().size() == 3 && TaskCallDec.getChild(0).Variable == "declaration"){
                TreeNode Declaration = TaskCallDec.getChild(0);
                UpdateTempParamList(Declaration);
                return CheckParamList(TaskCallDec.getChild(2)) + 1;
            }
            else{
                System.out.println("Error in TaskCallDec Tree Child");
                ErrorFound = true;
                return 0;
            }
        }
    }
    void UpdateTempParamList(TreeNode Declaration){
        if(Declaration.Variable == "declaration"){
            //System.out.println(Declaration.getChildren());
            //check if permanent
            if(Declaration.getChild(0).token != null && Declaration.getChild(0).token.getKind() == TokenType.PERMANENT){
                //System.out.println("Permanent");
                String idName = Declaration.getChild(2).token.getLexeme();
                DataType dt = DataType.valueOf(Declaration.getChild(1).getChild(0).token.getKind().name());
                ID tempID = new ID(idName, dt, true);
                TemporaryProtocol.add(tempID);
                //System.out.println(tempID.toString());
            }
            else if(Declaration.getChild(0).getValue() == "datatype"){
                //System.out.println("Not permanent.");
                String idName = Declaration.getChild(1).token.getLexeme();
                DataType dt = DataType.valueOf(Declaration.getChild(0).getChild(0).token.getKind().name());
                ID tempID = new ID(idName, dt, false);
                TemporaryProtocol.add(tempID);
                //System.out.println(tempID.toString());
            }
            else{
                System.out.println("Invalid Children of Declaration.");
                ErrorFound = true;
                return;
            }
        }
        else{
            System.out.println("Invalid Tree for declaration.");
            ErrorFound = true;
            return;
        }
    }
}


