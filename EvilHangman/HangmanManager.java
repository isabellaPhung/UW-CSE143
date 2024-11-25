/*
Isabella Phung
TA: Neha Nagvekar
CSE 143 BH
Assignment 4: Evil Hangman
2/10/19

This class stores information about a game of Hangman
Creates Hangman Manager object that manages an unfair game of hangman
*/
import java.util.*;

public class HangmanManager{
   private Set<String> words; //set of words of appropriate length
   private int guessAmount; //amount of guesses player is allowed
   private Set<Character> guesses; //player's guessed letters
   private String pattern; //the word being filled out
   
   /*
   creates Hangman Manager object using a given collection of strings,
      a given word length, and a given amount of guesses
   throws IllegalArgumentException if dictionary is less than 1
      or if max amount is less than 0
   */
   public HangmanManager(Collection<String> dictionary, int length, int max){
      //if dictionary's size is less than 1 or max less than 0,
      // throw IllegalArgumentException
      if (dictionary.size() < 1 || max < 0){
         throw new IllegalArgumentException();
      }
      
      //fill set of words from dictionary with words of appropriate size
      words = new TreeSet<String>();
      for(String word: dictionary){
         if(word.length() == length){
            words.add(word);
         }
      }

      //initiating data fields
      guessAmount = max;
      guesses = new TreeSet<Character>();
      
      //create empty pattern
      pattern = new String();
      for(int i = 0; i < length; i++){
         pattern += "-";
      }
   }
   
   /*
   returns set of words hangman manager is considering for the game
   */
   public Set<String> words(){
      return words;
   }
   
   /*
   returns amount of guesses user has left in the game
   */
   public int guessesLeft(){
      return guessAmount;
   }
   
   /*
   returns set of characters the player has guessed in the game
   */
   public Set<Character> guesses(){
      return guesses;
   }
   
   /*
   returns a representation of the final word
      dashes where the character hasn't been filled in
   */
   public String pattern(){
      return formattedPattern();
   }
   
   /*
   throws IllegalArgumentExcpetion if guessed character was already guessed
   throws IllegalStateException if player has no more guesses
      or if the remaining words the HangmanManager is considering
      is empty
   
   @param guess = guessed letter
   @return occurances of the letter in the selected pattern
   */
   public int record(char guess){
      //if player has no more guesses or if the remaining words
      // the HangmanManager is considering is empty, throw IllegalStateException
      if(guessAmount < 1 || words.size() == 0){
         throw new IllegalStateException();
      }
      
      //if guessed character already guessed,
      // throw IllegalArgumentException
      if(guesses.contains(guess)){
         throw new IllegalArgumentException();
      }
      
      //creates new map to sort words according to their patterns
      Map<String, Set<String>> wordMap = new TreeMap<String, Set<String>>();
      //iterates through each word in the words being considered
      for (String word : words){
         //builds a pattern for current word based off of where guessed letter appears
         String currentPattern = buildPattern(guess, word);
         
         //if word map does not contain the pattern just created,
         // create a key where pattern exists
         if(!wordMap.containsKey(currentPattern)){
            wordMap.put(currentPattern, new TreeSet<String>());
         }
         //insert word where pattern key is appropriate
         Set<String> wordSet = wordMap.get(currentPattern);
         wordSet.add(word);
      }
      
      //finds the new set of words for Hangman Manager to consider
      // based off of word map
      findNewWordSet(wordMap);
      
      //counts occurance of guessed character in new pattern
      int guessCharCount = 0;
      for(int i = 0; i < pattern.length(); i++){
         if(pattern.charAt(i) == guess){
            guessCharCount++;
         }
      }

      //if the guessed character was within the pattern
      // decrement user guess amount
      if(guessCharCount == 0){
         guessAmount--;
      }
      //add guessed character to set of guessed characters
      guesses.add(guess);
      
      return guessCharCount;
   }
   
   /*
   finds new set of words for Hangman Manager to consider
      based off of word map
   
   @param wordMap = map of words according to guessed character pattern
   */
   private void findNewWordSet(Map<String, Set<String>> wordMap){
      //compares size of each set to find largest set
      // then sets the words considered by Hangman Manager to the largest set
      int largestSetSize = 0;
      for(String wordMapKey: wordMap.keySet()){
         if((wordMap.get(wordMapKey)).size() > largestSetSize){
            largestSetSize = (wordMap.get(wordMapKey)).size();
            words = wordMap.get(wordMapKey);
            pattern = wordMapKey;
         }
      }
   }
   
   /*
   builds a pattern for given word based off of where guessed letter appears
   
   @param guess = guessed character
   @param word = givenword
   @return pattern of occurances of guessed character in word
   */
   private String buildPattern(char guess, String word){
      //iterates through given word to create a pattern,
      // dashes where character does not appear,
      // the character where the character does appear
      String currentPattern = new String();
      for(int i = 0; i < word.length(); i++){
         if(word.charAt(i) == guess){
            currentPattern += guess;
         } else {
            currentPattern += pattern.substring(i, i+1);
         }
      }
      return currentPattern;
   }
   
   /*
   inserts spaces in pattern to create an easier to read pattern
   
   @return formatted pattern with spaces in between characters
   */
   private String formattedPattern(){
      //iterates through pattern and inserts spaces
      String formattedPattern = new String();
      for(int i = 0; i < pattern.length(); i++){
         formattedPattern += (pattern.charAt(i) + " ");
      }
      return formattedPattern;
   }
}