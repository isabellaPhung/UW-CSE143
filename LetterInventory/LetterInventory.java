/*
Isabella Phung
TA: Neha Nagvekar
CSE 143 BH
Assignment 1: LetterInventory
1/17/19

This class stores information about a given String.
Creates LetterInventory Objects that stores the  number of letters in a given string.
Letter inventory objects can be added and subtracted from one another.
*/

import java.util.*;

class LetterInventory{
   private int size; //size of the letter inventory
   private int[] letterCount; //array containing numbered counts of letters
   public static final int TOTAL_ALPHABET = 26; //total number of letters in the alphabet
   public static final int ASCII_A_DIFFERENCE = 97; //ascii value of lowercase a
   public static final int ASCII_Z_DIFFERENCE = 122; //ascii value of lowercase z
   
   /*
   constructs LetterInventory object with given String
   */
   public LetterInventory(String data){
      //initializes array with number of letters in the alphabet
      letterCount = new int[TOTAL_ALPHABET];       
      String lowercaseData = data.toLowerCase();
      
      //for the length of string, add amount of each alphabetic letter to array
      for (int i = 0; i < data.length(); i++){
         char currentChar = lowercaseData.charAt(i);
         if (currentChar >= ASCII_A_DIFFERENCE && currentChar <= ASCII_Z_DIFFERENCE){
            letterCount[currentChar - ASCII_A_DIFFERENCE]++;
            size++;
         }
      }
   }
   
   /*
   Finds amount of a letter in LetterInventory
      indiscriminant to casing
      returns IllegalArgumentException if letter is nonalphabetic
   
   @param letter = the requested letter
   @return amount of letter in inventory
   */
   public int get(char letter){
      //checks if letter is valid and finds letter's index within letterInventory
      int letterIndex = findLetterIndex(letter);
      
      //if count for the letter isn't 0, return count
      if (letterCount[letterIndex] != 0){
         return letterCount[letterIndex];
      } else { //otherwise return 0
         return 0;
      }
   }
   
   /*
   changes amount of a requested character in the inventory
      indiscriminant to casing
      returns IllegalArgumentException if letter is nonalphabetic
   
   @param letter = the requested letter
   @param value = amount of letter to be added to the inventory
   */
   public void set(char letter, int value){
      //checks if letter is valid and finds letter's index within letterInventory
      int letterIndex = findLetterIndex(letter);
      
      size -= letterCount[letterIndex]; //subtracts old amount of letter from size
      letterCount[letterIndex] = value; //updates letter amount to the new value
      size += value; //adds new value to size
   }
   
   /*
   returns how many letters are in the inventory
   
   @return size of inventory as an integer
   */
   public int size(){
      return size;
   }
   
   /*
   returns if the inventory is empty
   
   @return boolean according to if inventory is empty
   */
   public boolean isEmpty(){
      return size == 0;
   }
   
   /*
   returns a string representation of inventory
      surrounded with square brackets
      format example: [abc]
   
   @return string representation of inventory in alphabetic order
   */
   public String toString(){
      String result = ""; //creates empty string to build upon
      //iterates through inventory and adds necessary amount of characters to result
      for (int i = 0; i < letterCount.length; i++){ 
         for (int j = 0; j < letterCount[i]; j++){ 
            char character = (char)(i + ASCII_A_DIFFERENCE);
            result += character;
         }
      }
      return "[" + result + "]";
   }
   
   /*
   creates a new LetterInventory combining two LetterInventory together
   
   @param other = the other LetterInventory to be combined with
   @return new LetterInventory with combined LetterInventory
   */
   public LetterInventory add(LetterInventory other){
      LetterInventory sum = new LetterInventory(this.toString() + other.toString());
      return sum;
   }
   
   /*
   creates a new LetterInventory with the difference of two LetterInventory
      returns null if other LetterInventory contains a letter that this
      LetterInventory does not contain
   
   @param other = the LetterInventory that will be subtracted
   @return new LetterInventory with difference of two LetterInventory
   */
   public LetterInventory subtract(LetterInventory other){
      //creates copy of this LetterInventory
      LetterInventory difference = new LetterInventory(this.toString());
      int val;
      //iterates through letterCount and 
      //   subtracts other's letterCount from this letterCount
      for (int i = 0; i < letterCount.length; i++){
         val = this.letterCount[i] - other.letterCount[i];
         if (val < 0){ //if the value is negative, return null
            return null;
         }
         difference.letterCount[i] = val;
         difference.size -= other.letterCount[i];
      }
      
      return difference;
   }
   
   /*
   finds place of requested alphabet in letterCount
   if letter is not a lowercase alphabetic character,
      returns IllegalArgumentException.
      
   @param other = the LetterInventory that will be subtracted
   @return new LetterInventory with difference of two LetterInventory
   */
   private int findLetterIndex(char letter){
      //changes character to lowercase
      char lowercaseLetter = Character.toLowerCase(letter);
      
      //if letter not within ASCII bounds for a standard lowercase letter
         //throw IllegalArgumentException
      if (letter <= ASCII_A_DIFFERENCE && letter >= ASCII_Z_DIFFERENCE){  
         throw new IllegalArgumentException();
      }
      
      //converts ascii letter value to index value of letterCount
      int letterDifference = lowercaseLetter - ASCII_A_DIFFERENCE;
      
      return letterDifference;
   }
}