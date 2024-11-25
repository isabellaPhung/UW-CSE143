/*
Isabella Phung
TA: Neha Nagvekar
CSE 143 BH
Assignment 2: Guitar37
1/24/19

This class stores information about a guitar-like instrument with 37 strings
Creates Guitar37 object that stores the vibrations of each string
amount of strings can be changed
*/

public class Guitar37 implements Guitar {
   private int ticNum; //number of times tic is called
   private GuitarString[] guitar; //guitar object
   private double CONCERT_A = 440.0;
   public static final int NUM_OF_STRINGS = 37;
   public static final String KEYBOARD =
   "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";  // keyboard layout
   
   /*
   constructs Guitar37 object
   */
   public Guitar37(){
      //initiallizes guitar array with num of Strings
      guitar = new GuitarString[NUM_OF_STRINGS];
      
      //fills guitar array with GuitarString objects
      // with frequencies based off of CONCERT_A
      for(int i = 0; i < NUM_OF_STRINGS; i++){
         double frequency = CONCERT_A * Math.pow(2, (i-24)/12.0);
         GuitarString string = new GuitarString(frequency);
         guitar[i] = string;
      }
      
      ticNum = 0;
   }
   
   /*
   plucks string according to requested pitch
   
   @param pitch = requested pitch
   */
   public void playNote(int pitch){
      //if within index range of guitar37 array
      if(pitch >= -24 && pitch <= 12){
         pitch += 24;
         guitar[pitch].pluck();
      }
   }
   
   /*
   returns if key press is valid or not
   
   @param key = requested key press
   @return if key press is valid or not
   */
   public boolean hasString(char key){
      return KEYBOARD.indexOf(key) != -1;
   }
   
   /*
   plays note according to given keypress
   throws IllegalArgumentException if key is invalid
   
   @param key = the requested key press
   */
   public void pluck(char key){
      //if invalid key, throw IllegalArgumentException
      if(!(hasString(key))){
         throw new IllegalArgumentException();
      }
      
      guitar[KEYBOARD.indexOf(key)].pluck();
   }
   
   /*
   finds total sample amount for all strings in guitar37 object
   
   @return total sample amount
   */
   public double sample(){
      double total = 0;
      for(int i = 0; i < NUM_OF_STRINGS; i++){
         total += guitar[i].sample();
      }
      return total;
   }
   
   /*
   advances time forward by 1 tic for guitar37 object
   */
   public void tic(){
      for(int i = 0; i < NUM_OF_STRINGS; i++){
         guitar[i].tic();
      }
      ticNum++;
   }
   
   /*
   returns current time for Guitar37 object playing a song
   */
   public int time(){
      return ticNum;
   }
}