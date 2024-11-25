/*
Isabella Phung
TA: Neha Nagvekar
CSE 143 BH
Assignment 8: HuffmanCode
3/15/19

This class stores information about a Huffman Code for a message
Creates HuffmanCode object which stores a HuffmanCode and 
   can translate messages
*/

import java.util.*;
import java.io.*;

public class HuffmanCode{
   private HuffmanNode root;  //root of Huffman Code tree
   
   /*
   initializes new HuffmanCode object using given array of frequencies,
      where frequencies[i] is the count of the character w/ ASCII val i
   */
   public HuffmanCode(int[] frequencies){
      //creates PriorityQueue
      Queue<HuffmanNode> frequencyQueue = new PriorityQueue<HuffmanNode>();
      //iterates through given array and stores into PriorityQueue
      for(int i = 0; i < frequencies.length; i++){
         if(frequencies[i] != 0){
            frequencyQueue.add(new HuffmanNode(i, frequencies[i]));
         }
      }
      
      //creates Huffman Node tree by removing two highest frequency nodes
      // and combining
      HuffmanNode val1;
      HuffmanNode val2;
      HuffmanNode combination;
      while(frequencyQueue.size() != 1){
         val1 = frequencyQueue.remove();
         val2 = frequencyQueue.remove();
         combination = new HuffmanNode(val1.frequency + val2.frequency, val1, val2);
         frequencyQueue.add(combination);
      }
      //then stores result into root
      root = frequencyQueue.remove();
   }
   
   /*
   initializes new HuffmanCode object by reading .code file
      assumes scanner != null and data is in standard format
   */
   public HuffmanCode(Scanner input){
      int asciiVal = 0;
      String code = "";
      root = new HuffmanNode();
      //iterates through scanner
      while(input.hasNextLine()){
         asciiVal = Integer.parseInt(input.nextLine());
         code = input.nextLine();
         //feeds asciiVal and corresponding code into scannerHuffmanCode
         // which is the recursive method for developing the tree
         root = scannerHuffmanCode(asciiVal, code, root);
      }
   }
   
   /*
      parses through .code information to initialize HuffmanCode object
         assumes .code is in proper format
      
      @param asciiVal = ascii value of current character
      @param code = code for character from .code file
      @param node = current root/node
      @return root of HuffmanCode tree
   */
   private HuffmanNode scannerHuffmanCode(int asciiVal, String code, HuffmanNode node){
      //finds intended branch by removing first number of code
      int branch = Integer.parseInt(code.substring(0, 1));
      
      //if the current code is only 1 length
      if(code.length() == 1){
         if(branch == 0){ //if the branch is 0, create left node
            node.left = new HuffmanNode(asciiVal, -1);
         } else { //if branch is 1, create right node
            node.right = new HuffmanNode(asciiVal, -1);
         }
      } else { //if current code is further down on tree
         //cut the code by removing the first number for recurison
         code = code.substring(1, code.length());
         if(branch == 0){//if branch is 0, 
            //if current node's left is empty,
            // create node
            if(node.left == null){
               node.left = new HuffmanNode();
            }
            //recurse with left node with new code
            node.left = scannerHuffmanCode(asciiVal, code, node.left);
         } else { //if branch is 1,
            //if current node's right is empty,
            // create node
            if(node.right == null){
               node.right = new HuffmanNode();
            }
            //recurse with leftnode with new code
            node.right = scannerHuffmanCode(asciiVal, code, node.right);
         }
      }
      return node;
   }
   
   /*
   stores current codes into given output stream in standard format
   ex:
   [char ascii]
   [corresponding code in 1 and 0]
   .
   .
   .
   
   @param output = output file to print to
   */
   public void save(PrintStream output){
      save(output, root, "");
   }
   
   /*
   stores current codes into given output stream in standard format
   
   @param output = output file to print to
   @param node = current node
   @param code = Huffman code determined from tree
   */
   private void save(PrintStream output, HuffmanNode node, String code){
      //checks if node is null
      if(node != null){
         //if leaf node,
         if(node.left == null && node.right == null){
            //print char ascii and corresponding HuffmanCode to it
            output.println(node.data);
            output.println(code);
         } else { //if not leaf node
            //feed left node into save with 0 added to code string
            save(output, node.left, code + 0);
            //feed right node into save with 1 added to code string
            save(output, node.right, code + 1);
         }
      }
   }
   
   /*
   reads bits from input stream and translates encoded message to alphabetic characters
   
   @param input = input stream
   @param output = output file
   */
   public void translate(BitInputStream input, PrintStream output){
      while(input.hasNextBit()){
         translate(input, output, root);
      }
   }
   
   /*
   reads bits from input stream and translates encoded message to alphabetic characters
   
   @param input = input stream
   @param output = output file
   @param node = current node
   */
   private void translate(BitInputStream input, PrintStream output, HuffmanNode node){
      //checks if node is null
      if(node != null){
         //if leaf node, write to output
         if(node.left == null && node.right == null){
            output.write(node.data);
         } else { // otherwise check val if next bit
            int val = input.nextBit();
            if(val == 0){ //if val is 0, feed left node into itself
               translate(input, output, node.left);
            } else { //if val is 1, feed right node into itself
               translate(input, output, node.right);
            }
         }
      }
   }
   
   /*
   This class stores information about a node in the HuffmanCode tree
   
   Creates HuffmanNode object which stores information about the nodes
   used in the HuffmanCode tree
   */
   private static class HuffmanNode implements Comparable<HuffmanNode>{
      public int data;  //character data
      public HuffmanNode left; //left node 
      public HuffmanNode right; //right node
      public int frequency; //frequency data
      
      /*
      initializes HuffmanNode without any data or frequency
         and no children
      */
      public HuffmanNode(){
         data = 0;
         frequency = -2;
         left = null;
         right = null;
      }
      
      /*
      initializes HuffmanNode with a data and frequency
         but no children
      */
      public HuffmanNode(int data, int frequency){
         this.data = data;
         this.frequency = frequency;
         left = null;
         right = null;
      }
      
      /*
      initializes HuffmanNode with a frequency, and children
         but no data
      */
      public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right){
         data = 0;
         this.frequency = frequency;
         this.left = left;
         this.right = right;
      }
      
      /*
      compares two nodes for priorityQueue or binary tree creation
         returns negative val if other node > than this node
         returns 0 if =
         returns pos val if other node < than this node
      
      @param otherNode = other node intended for comparison
      */
      public int compareTo(HuffmanNode otherNode){
         return frequency - otherNode.frequency;
      }
   }
}