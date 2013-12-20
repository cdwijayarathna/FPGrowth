/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fpgrowth;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chamila
 */
public class Main {

    ArrayList<String> users=new ArrayList<String>();
    ArrayList<ArrayList<String>> transactions=new ArrayList<ArrayList<String>>();
    ArrayList<String> items=new ArrayList<String>();
    ArrayList<Integer> itemCount=new ArrayList<Integer>();
    ArrayList<ArrayList<Integer>> orderedTransactions=new ArrayList<ArrayList<Integer>>();
    int support;
    node head;//=new node();


    public Main(){
        
    }




    public void preProcess(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("D:\\semester 5\\Intelligent Systems\\workspace\\Fpgrowth\\src\\fpgrowth\\access_log_Aug95"));
            String line=reader.readLine();
            String[] components;
            int x = 0;
            int index;
            int itemIndex;

            while(line!=null){
                //System.out.println(line);
                components=line.split(" ");
                index = users.indexOf(components[0]);
                if(index==-1){
                   users.add(components[0]);
                   index = users.indexOf(components[0]);
                   transactions.add(new ArrayList<String>());
                }
                transactions.get(index).add(components[6]);
                itemIndex = items.indexOf(components[6]);
                if(itemIndex==-1){
                   items.add(components[6]);
                   itemCount.add(1);
                }else{
                    itemCount.add(itemIndex, itemCount.remove(itemIndex)+1);
                }
                
                x++;
                if (x==500) {
                    break;
                }
                //System.out.println("");
                //break;
                line=reader.readLine();
            }
            int check = 0;
//            for (int i = 0; i < items.size(); i++) {
//                System.out.println(items.get(i) + " " + itemCount.get(i) );
//                check = check + itemCount.get(i);
//            }
            support = (int) Math.ceil(.3 * transactions.size());

//           System.out.println(items.size());
//            System.out.println(check);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void prioritize(){
        int min;
        int index;
        int val;
        String item;
        for (int i = items.size()-1; i >= 0; i--) {
            min=itemCount.get(0);
            for (int j = 0; j <= i; j++) {
                if(itemCount.get(j)<min){
                    min=itemCount.get(j);
                }
            }
            index=itemCount.indexOf(min);
            val=itemCount.remove(index);
            item=items.remove(index);
            itemCount.add(i, val);
            items.add(i, item);

        }
//        for (int i = 0; i < items.size(); i++) {
//                System.out.println(items.get(i) + " " + itemCount.get(i) );
//                //check = check + itemCount.get(i);
//            }
    }
    ArrayList<Integer> temp=new ArrayList<Integer>();
    int index;
    public void order(){
        for (int i = 0; i < transactions.size(); i++) {
            orderedTransactions.add(new ArrayList<Integer>());
            //temp=orderedTransactions.get(i);
            outer:
            for (int j = 0; j < transactions.get(i).size(); j++) {
                index=items.indexOf(transactions.get(i).get(j));
                if(j==0){
                    orderedTransactions.get(i).add(index);
                    continue outer;
                }
                for (int k = 0; k < j; k++) {
                  if(index<orderedTransactions.get(i).get(k)){
                      orderedTransactions.get(i).add(k, index);
                      continue outer;
                  }
                  if(k==j-1){
                      orderedTransactions.get(i).add(index);
                  }
                }
                //if()
            }
           // orderedTransactions.add(i,temp);
            
        }
        for (int j = 0; j < transactions.get(5).size(); j++) {
                System.out.print(transactions.get(5).get(j) + " ");
            }System.out.println("");
            for (int j = 0; j < transactions.get(5).size(); j++) {
                System.out.print(items.indexOf(transactions.get(5).get(j)) + " ");
            }System.out.println("");
            for (int j = 0; j < orderedTransactions.get(5).size(); j++) {
                System.out.print(orderedTransactions.get(5).get(j) + " ");
            }System.out.println("");
    }

    public void createTree(){
        head= new node();
        head.value=-1;
        head.parent=null;
        node current;
        node neww;
        for (int i = 0; i < orderedTransactions.size(); i++) {
            current = head;
            outer:
            for (int j = 0; j < orderedTransactions.get(i).size(); j++) {
                for (int k = 0; k < current.children.size(); k++) {
                    if(current.children.get(k).value==orderedTransactions.get(i).get(j)){
                        current=current.children.get(k);
                        current.count++;
                        continue outer;
                    }
                    else{
                        continue;
                    }
                }
                neww=new node();
                neww.value=orderedTransactions.get(i).get(j);
                neww.parent=current;
                current.children.add(neww);
                current=current.children.get(current.children.size()-1);
                
            }
        }
        current = head;
//        while(current.children.size()>0){
//            for (int i = 0; i < current.children.size(); i++) {
//                System.out.println(current.children.get(i).count);
//            }
//            System.out.println("");
//            current=current.children.get(0);
//        }

    }

    ArrayList<ArrayList<node>> nodes=new ArrayList<ArrayList<node>>();

    public void findPaterns(){
        for(int i=0;i<items.size();i++){
            nodes.add(new ArrayList<node>());
        }
        ArrayDeque<node> queue=new ArrayDeque<node>();
        queue.addFirst(head);
        node current;
        while(!queue.isEmpty()){
            current=queue.poll();
            if(current.value>=0){
                nodes.get(current.value).add(current);
            }
            //System.out.println(current.value);
            for(int i=0;i<current.children.size();i++){
                queue.add(current.children.get(i));
            }
        }
        int[] appliedCount;
        int[] sortedIndexes;
        int appearance;
        ArrayList<ArrayList<Integer>> patterns=new ArrayList<ArrayList<Integer>>();
        for (int i = items.size()-1; i >=0; i--) {
            appliedCount=new int[items.size()];
            sortedIndexes=new int[items.size()];
            for (int j = 0; j < nodes.get(i).size(); j++) {
                current=nodes.get(i).get(j);
                patterns.add(new ArrayList<Integer>());
                appearance = current.count;
                while(current.value>=0){
                    //System.out.println(current.value);
                    patterns.get(j).add(current.value);
                    appliedCount[current.value]+=appearance;
                    current=current.parent;
                }


            }
            
            //FIND MOST APPEARING ITEMS
            outer:
            for (int k = 0; k < items.size(); k++) {
                for (int j = 0; j < k; j++) {
                    if(appliedCount[k]<=appliedCount[sortedIndexes[j]]){
                        continue;
                    }else{
                        for (int l = k-1; l > j; l--) {
                           sortedIndexes[l+1]=sortedIndexes[l];
                        }
                        sortedIndexes[j]=k;
                        continue outer;
                    }

                }
                sortedIndexes[k]=k;
            }
            System.out.println("1");
            //ORDER PATTERNS
            int cur;
            for (int j = 0; j < patterns.size(); j++) {
                outer:
                for (int k = 0; k < patterns.get(j).size(); k++) {
                    cur=patterns.get(j).remove(k);

                    for (int l = 0; l < k-1; l++) {
                        for (int m = 0; m <sortedIndexes.length ; m++) {
                            if(sortedIndexes[m]==cur){
                                patterns.get(j).add(l, cur);
                                continue outer;
                            }
                            else if(sortedIndexes[m]==patterns.get(j).get(l)){
                                break;
                            }
                        }

                    }
                    patterns.get(j).add(k, cur);

                }
                System.out.println("2");
                for (int k = 0; k < patterns.get(j).size(); k++) {
                    if(patterns.get(j).get(k)< support){
                        patterns.get(j).remove(k);
                        k--;
                    }
                }
            }




        }
    }

    public void recurse(){
        
    }

    public static void main(String[] args) {
        Main mainObj=new Main();
        mainObj.preProcess();
        mainObj.prioritize();
        mainObj.order();
        mainObj.createTree();
        mainObj.findPaterns();
    }




}
