/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fpgrowth;

import java.util.ArrayList;

/**
 *
 * @author Chamila
 */
public class node {
    public int value;
    public int count;
    public ArrayList<node> children;//=new ArrayList<node>();
    public node parent;

    public node(){
        count = 1;
        children=new ArrayList<node>();
        //parent=new node();
    }
}
