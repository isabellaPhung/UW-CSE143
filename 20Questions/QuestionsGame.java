/*
Isabella Phung
TA: Neha Nagvekar
CSE 143 BH
Assignment 7: 20 Questions
3/10/19

This class stores information about a game of 20 questions,
   however there is no limit to the amount of questions the computer will ask
Creates QuestionsGame object which stores information about the questions
   the computer will ask in the game
*/

import java.io.*;
import java.util.*;

public class QuestionsGame {
   private QuestionNode root; //first question or item that's guessed
   private Scanner console; //user console
   
   /*
   Constructs QuestionsGame object
      default first guess: "computer"
   */
   public QuestionsGame(){
      root = new QuestionNode("computer");
      console = new Scanner(System.in);
   }
 
   /*
   reads given file and stores questions and answers into
      QuestionsGame object
   
   @param input = given file
   */
   public void read(Scanner input){
      root = readHelper(input);
   }
   
   /*
   reads given file and stores questions and answers into
      QuestionsGame object
   file must be in the following format:
   Q: or A:
   data
   
   @param input = given file
   @return question tree
   */
   private QuestionNode readHelper(Scanner input){
      //initiallizes node
      QuestionNode node = null;
      //while there are lines in the file
      if(input.hasNext()){
         //if line is Q:
         if((input.nextLine()).equals("Q:")){
            //creates node for the data on the next line
            node = new QuestionNode(input.nextLine());
            //continues on tree for both branches
            node.yes = readHelper(input);
            node.no = readHelper(input);
         } else { //if line is A:
            node = new QuestionNode(input.nextLine());
         }
      }
      return node;
   }

   /*
   stores current tree into given output file
   
   @param output = output file
   */
   public void write(PrintStream output){
      write(output, root);
   }
   
   /*
   stores current tree into given output file
   
   @param output = output file
   */
   private void write(PrintStream output, QuestionNode node){
      //if not an answer node
      if(!isAnswer(node)){
         //print Q:
         output.println("Q:");
         //print data
         output.println(node.data);
         //print the yes and no trees
         write(output, node.yes);
         write(output, node.no);
      } else { //otherwise just print A: followed by data
         output.println("A:");
         output.println(node.data);
      }
   }

   /*
   uses current stored question/answer information to play game
      with user
   asks user questions according to question/answer tree, until
      it guesses an answer according to question/answer tree
   */
   public void askQuestions(){
      root = askQuestions(root);
   }
   
   /*
   uses current stored question/answer information to play game
      with user
   asks user questions according to question/answer tree, until
      it guesses an answer according to question/answer tree
   */
   private QuestionNode askQuestions(QuestionNode node){
      //checks if node is null
      if(node != null){
         //if node is a question
         if(!isAnswer(node)){
            //if user replies yes to the question
            if(yesTo(node.data)){
               //feeds yes branch into itself
               node.yes = askQuestions(node.yes);
            } else {
               //feeds no branch into itself
               node.no = askQuestions(node.no);
            }
         } else { //if node is an answer
            //if computer guessed right
            if(yesTo("Would your object happen to be " + node.data + "?")){
               System.out.println("Great, I got it right!");
            } else { //if computer guessed incorrectly
               System.out.print("What is the name of your object? ");
               String object = console.nextLine();
               System.out.println("Please give me a yes/no question that");
               System.out.println("distinguishes between your object");
               System.out.print("and mine--> ");
               String question = console.nextLine();
               if(yesTo("And what is the answer for your object?")){
                  //if object is a yes node, create new node and store necessary info
                  node = new QuestionNode(question,
                     new QuestionNode(node.data), new QuestionNode(object));
               } else {
                  //if object is a no node, create new node and store necessary info
                  node = new QuestionNode(question,
                     new QuestionNode(object), new QuestionNode(node.data));
               }
            }
         }
      }
      //System.out.println(node.data);
      return node;
   }

// post: asks the user a question, forcing an answer of "y" or "n";
//       returns true if the answer was yes, returns false otherwise
   public boolean yesTo(String prompt) {
      System.out.print(prompt + " (y/n)? ");
      String response = console.nextLine().trim().toLowerCase();
      while (!response.equals("y") && !response.equals("n")) {
         System.out.println("Please answer y or n.");
         System.out.print(prompt + " (y/n)? ");
         response = console.nextLine().trim().toLowerCase();
      }
      return response.equals("y");
   }

   /*
   returns if node is an answer or not
   
   @return if node is answer or not
   */
   private boolean isAnswer(QuestionNode node){
      return node.yes == null && node.no == null;
   }
   
   /*
   This class stores information about a node in the QuestionsGame tree
   
   Creates QuestionNode object which stores information about the nodes
   used in the QuestionGame tree
   */
   private static class QuestionNode {
      public String data; //stores either question or answer
      public QuestionNode no; //left node
      public QuestionNode yes; //right node

      /*
      constructs QuestionNode object with no children
      */
      public QuestionNode(String data){
         this.data = data;
         this.no = null;
         this.yes = null;
      }
      
      /*
      constructs QuestionNode object, which stores information
      about a node's data and it's children
      */
      public QuestionNode(String data, QuestionNode right, QuestionNode left){
         this.data = data;
         this.no = right;
         this.yes = left;
      }
   }
}
