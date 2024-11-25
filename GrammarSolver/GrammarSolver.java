/*
Isabella Phung
TA: Neha Nagvekar
CSE 143 BH
Assignment 5: Grammar Solver
2/14/19

This class stores information about a random grammar generator.
Creates GrammarSolver Objects that randomly generates elements
   of a given Backus-Naur Form grammar in the following format:
   "<nonterminal symbol>::= <rule> | <rule> | <rule> |...| <rule>"
*/
import java.util.*;

public class GrammarSolver{
   private Map<String, String> grammarStore; //stores grammar info
   
   /*
   Creates Grammar Solver object using given List of Strings
   req: one occurance of "::=" per string,
      non-terminal doesn't contain "|" or space
   all tokens to the right of "::=" are considered terminals
   
   repeated rules are accepted, but throws IllegalArgumentException if grammar is empty
      or if there are multiple entries for same nonterminal
   */
   public GrammarSolver(List<String> grammar){
      //if grammar is empty, throw IllegalArgumentException
      if(grammar.size() == 0){
         throw new IllegalArgumentException();
      }
      
      //initiallizes map data field
      // non-terminals stored as keys, terminals stored as values
      grammarStore = new TreeMap<String, String>();
      //for all grammars, split the grammar at "::=" and store
      for(int i = 0; i < grammar.size(); i++){
         String[] separated = (grammar.get(i)).split("::=");
         
         //removes unnecessary white space from rule
         String separatedRule = ruleProcessing(separated[1]);
         //if non-terminal already exists in map, throw IllegalArgumentException
         if(grammarContains(separated[0])){
            throw new IllegalArgumentException();
         }
         grammarStore.put(separated[0], separatedRule);
      }
   }
   
   /*
   returns if given symbol is a non-terminal within the grammar
   */
   public boolean grammarContains(String symbol){
      return grammarStore.containsKey(symbol);
   }
   
   /*
   randomly generates occurances of the given symbol a specified number of times
   when generating elements, the resulting sentence will equivocate
      large spaces in between symbols as just single spaces
   
   @param symbol = given symbol
   @param times = number of generated sentences requested
   @return array of generated elements of grammar
   */
   public String[] generate(String symbol, int times){
      //if requested symbol doesn't exist in current grammar
      // or if requested times is less than 0, throw IllegalArgumentException
      if(!grammarContains(symbol) || times < 0){
         throw new IllegalArgumentException();
      }
      
      //creates array to store generated sentences
      String[] sentenceStorage = new String[times];
      Random rand = new Random();
      
      //for requested number of times, generate sentence and trim any extra space
      for(int i = 0; i < times; i++){
         sentenceStorage[i] = (generate(symbol, rand)).trim();
      }
      return sentenceStorage;
   }
   
   /*
   returns String representation of non-terminals in grammar
   */
   public String getSymbols(){
      return (grammarStore.keySet()).toString();
   }
   
   /*
   removes any additional spacing in rule
   
   @param rule = requested rule
   @return processed rule
   */
   private String ruleProcessing(String rule){
      //first removes any spacing around vertical bars
      //splits rule by vertical bars
      String[] barRule = rule.split("\\|");
      String newRule = "";
      int endIndex = barRule.length - 1;
      //reassembles rule without the additional spacing around "|"
      for(int i = 0; i < endIndex; i++){
         barRule[i] = barRule[i].trim();
         newRule += (barRule[i] + "|");
      }
      barRule[endIndex] = barRule[endIndex].trim();
      newRule += barRule[endIndex];
      
      //then removes remaining additional spacing
      //splits rule by spaces
      String[] spaceRule = newRule.split("\\s+");
      newRule = "";
      endIndex = spaceRule.length - 1;
      //reassembles rule with single spaces in between
      for(int i = 0; i < endIndex; i++){
         newRule += (spaceRule[i] + " ");
      }
      spaceRule[endIndex] = spaceRule[endIndex].trim();
      newRule += spaceRule[endIndex];

      return newRule;
   }
   
   /*
   randomly generates occurances of the given symbol
   
   @param symbol = requested symbol
   @param rand = random number generator
   */
   private String generate(String symbol, Random rand){
      //if symbol contains "|", split by "|", randomly select one of the rules
      // then feed rule back into generate
      if(symbol.contains("|")){
         String[] orRule = symbol.split("\\|");
         int randomRule = rand.nextInt(orRule.length);
         return generate(orRule[randomRule], rand);
      }
      
      //if symbol contains " ", split by " ",
      // feed each terminal back into generate, then return resulting sentence
      if(symbol.contains(" ")){
         String word = new String("");
         String[] splitRule = symbol.split("\\s+");
         for(int i = 0; i < splitRule.length; i++){
            word += (generate(splitRule[i], rand));
         }
         return word;
      }
      
      //if symbol is a non-terminal with a definition in grammarStore,
      // feed it's definition into generate
      if(grammarContains(symbol)){
         return generate(grammarStore.get(symbol), rand);
      } else { //base case
         //if it has no definition in grammarStore, return symbol
         return symbol + " ";
      }
   }
}