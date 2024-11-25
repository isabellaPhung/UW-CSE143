/*
Isabella Phung
TA: Neha Nagvekar
CSE 143 BH
Assignment 6: AnagramSolver
2/28/19

This class stores information about a set of anagrams for a given string.
Creates AnagramSolver Objects that find all possible anagrams
   for a given string using a provided dictionary to select from.
Requires LetterInventory to run
*/

import java.util.*;

public class AnagramSolver{
   //stores dictionary information
   private Map<String, LetterInventory> dictionaryInventory;
   //original dictionary
   private List<String> dictionary;
   
   /*
   constructs AnagramSolver object using a given dictionary
   ignores duplicates, treats them as any other word
   */
   public AnagramSolver(List<String> dictionary){
      //iniate dictionaryInventory
      dictionaryInventory = new HashMap<String, LetterInventory>();
      //iterate through given dictionary and store
      for(String word: dictionary){
         dictionaryInventory.put(word, new LetterInventory(word));
      }
      
      //create pointer to original dictionary
      this.dictionary = dictionary;
   }
   
   /*
   prints anagram solution sets for given string selected from dictionary
   
   if given max is less than 0, throws IllegalArgumentException
   @param text = given string
   @param max = max amount of words in anagram solution set
   */
   public void print(String text, int max){
      //if max less than 0, throw IllegalArgumentException
      if(max < 0){
         throw new IllegalArgumentException();
      }
      
      //creates Letter inventory of given string
      LetterInventory textInventory = new LetterInventory(text);
      //prunes dictionary according to given string
      Map<String, LetterInventory> prunedInventory = prune(textInventory);
      
      //recursive method
      print(max, textInventory, prunedInventory, new ArrayList<String>());
   }
   
   /*
   prints anagram solution sets for given string selected from dictionary
   
   @param max = max amount of words in anagram solution set
   @param textInventory = Inventory for given word
   @param prunedInventory = dictionary of potential anagram words
   @param branches = storage for anagram sets
   */
   private void print(int max, LetterInventory textInventory,
         Map<String, LetterInventory> prunedInventory, ArrayList<String> branches){
      //if there are no remaining words
      if(textInventory.isEmpty()){
         //if there is no limit of words for anagram solution set
         // or if the size of the current branch is equal to the limit of words,
         // print anagram set
         if(max == 0 || branches.size() <= max){
            System.out.println(branches);
         }
      }else{ //else, still remaining words in pruned dictionary
         //iterate through original dictionary to maintain order
         for(String word: dictionary){ 
            //if current word from dictionary is in the pruned dictionary
            // and is an annagram of current word, add word to branch,
            // pass difference of current word and requested text into recursive method
            // once recursive method returns, remove word from branch and return
            if(prunedInventory.containsKey(word) && 
                  textInventory.subtract(prunedInventory.get(word)) != null){ //choose
               branches.add(word);
               print(max, textInventory.subtract(prunedInventory.get(word)),
                  prunedInventory, branches); //explore
               branches.remove(word); //unchoose
            }
         }
      }
   }
   
   /*
   prunes full dictionary to only contain words that could be an anagram of
      the given word.
      
      @param textInventory = inventory for given word
      @return pruned dictionary
   */
   private Map<String, LetterInventory> prune(LetterInventory textInventory){
      //creates a new map to store relevent values
      Map<String, LetterInventory> prunedInventory = new HashMap<String, LetterInventory>();
      
      //iterates through dictionaryInventory and stores relevent anagram-possible
      // words into new map
      for(String word: dictionaryInventory.keySet()){
         LetterInventory wordInventory = dictionaryInventory.get(word);
         if(textInventory.subtract(wordInventory) != null){
            prunedInventory.put(word, wordInventory);
         }
      }
      return prunedInventory;
   }
}