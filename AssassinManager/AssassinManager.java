/*
Isabella Phung
TA: Neha Nagvekar
CSE 143 BH
Assignment 3: Assassin Manager
1/30/19

This class stores information about a game of assassin
Creates Assassin Manager object that stores the movements of players
*/

import java.util.*;

public class AssassinManager{
   private AssassinNode killFront; //front of kill ring
   private AssassinNode graveFront; //front of graveyard

   /*
   constructs Assassin Manager object using list of names
   order of killring is same order as given list
   ignores duplicate names, assumes strings are non-empty
   throws IllegalArgumentException if list is empty
   */
   public AssassinManager(List<String> names){
      //if given list is empty, throw IllegalArgumentException
      if(names.size() == 0){
         throw new IllegalArgumentException();
      }
      
      //enters names into assassin manager list backwards
      // to perserve order of given list
      for(int i = names.size() - 1; i >= 0; i--){
         killFront = new AssassinNode(names.get(i), killFront);
      }
   }
   
   /*
   prints names of those in the kill ring and who they're stalking, formated thusly:
   "    <name1> is stalking <name2>"
   prints according to original list order
   if one person in kill ring, prints person is stalking themselves.
   */
   public void printKillRing(){
      //if only one person in kill ring, prints person stalking themselves
      if(killFront.next == null){
         System.out.println("    " + killFront.name + " is stalking " + killFront.name);
      } else { //otherwise iterate through the rest of the kill ring
         AssassinNode current = killFront;
         while(current.next != null){
            System.out.println("    " + current.name + " is stalking " + (current.next).name);
            current = current.next;
         }
         System.out.println("    " + current.name + " is stalking " + killFront.name);
      }
   }
   
   /*
   prints names of those in the graveyard and who they were killed by, formated thusly:
   "    <name1> was killed by <name2>"
   prints with newest kill at top
   nothing printed if graveyard is empty
   */
   public void printGraveyard(){
      if(graveFront != null){ //if graveyard is not empty, print all names within
         AssassinNode current = graveFront;
         while(current != null){
            System.out.println("    " + current.name + " was killed by " + current.killer);
            current = current.next;
         }
      } //otherwise no output
   }
   
   /*
   determines if kill ring contains requested name
   ignores casing of requested name
   
   @param name = requested name
   */
   public boolean killRingContains(String name){
      //for length of kill ring, compare each value to requested name
      AssassinNode current = killFront;
      while(current != null){ 
         //if requested name is in kill ring, return true
         if((current.name).equalsIgnoreCase(name)){
            return true;
         }
         current = current.next;
      }
      //return false if requested name is not found
      return false;
   }
   
   /*
   returns true if graveyard contains requested name
   ignores casing of requested name
   
   @param name = requested name
   */
   public boolean graveyardContains(String name){
      //for length of graveyard, compare each value to requested name
      AssassinNode current = graveFront;
      while(current != null){
         //if requested name is in graveyard, return true
         if((current.name).equalsIgnoreCase(name)){
            return true;
         }
         current = current.next;
      }
      //return false if requested name is not found
      return false;
   }
   
   /*
   returns true if game is over, false otherwise
   */
   public boolean gameOver(){
      //if only one value in kill ring, return true.
      if(killFront.next == null){
         return true;
      }
      //return false otherwise
      return false;
   }
   
   /*
   returns name of game winner
   returns null if game isn't over
   */
   public String winner(){
      //if game is over, return last standing's name
      if(gameOver()){
         return killFront.name;
      }
      //return null otherwise
      return null;
   }
   
   /*
   "kills" player of the requested name
   player is moved from the kill ring to the graveyard
   
   throws IllegalStateException if game is already over
   throws IllegalArgumentException if requested player is not in the kill ring
   
   @param name = name of requested player
   */
   public void kill(String name){
      //if game is over, throw IllegalStateException
      if(gameOver()){
         throw new IllegalStateException();
      }
      
      //if requested player is not in kill ring, throw IllegalArgumentException
      if(!killRingContains(name)){
         throw new IllegalArgumentException();
      }
      
      AssassinNode current = killFront;
      //checks first player in assassin manager against requested player
      if((current.name).equalsIgnoreCase(name)){
         //creates temporary storage of value and rearranges references
         AssassinNode temp = current;
         killFront = temp.next;
         temp.next = graveFront;
         graveFront = temp;
         
         //finds last value of list to find first player's killer
         AssassinNode current2 = killFront;
         while(current2.next != null){
            current2 = current2.next;
         }
         graveFront.killer = current2.name;
      }

      //checks rest of values in assassin manager
      while(current != null && current.next != null){
         if(((current.next).name).equalsIgnoreCase(name)){
            //creates temporary storage of value and rearranges references
            AssassinNode temp = current.next;
            current.next = current.next.next;
            temp.next = graveFront;
            graveFront = temp;
            graveFront.killer = current.name;
            
         }
         current = current.next;
      }
   }
}