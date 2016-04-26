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
public class Traverse {

    static void traverse(TreeNode root) {
        TreeNode n = root;
        ArrayList<TreeNode> currentChildren = n.getChildren();
        if (currentChildren.isEmpty()) {
            System.out.print(" [ " + n.getValue() + " ] ");
            return;
        }
        System.out.print("[ " + n.getValue() + " "); // visit(n);
        for (int i = 0; i < currentChildren.size(); i++) {
            traverse(currentChildren.get(i));
        }
        System.out.print("] ");
    }
}
