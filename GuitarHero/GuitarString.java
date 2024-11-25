/*
Isabella Phung
TA: Neha Nagvekar
CSE 143 BH
Assignment 2: Guitar String
1/24/19

This class stores information about a guitar string.
Creates GuitarString Objects that stores the ring buffer
of a guitar string.
GuitarStrings can be plucked, sampled, and
the Karplus-Strong algorithm can be applied with tic.
*/

import java.util.*;

public class GuitarString{
   private Queue<Double> ringBuffer; //queue to store ringBuffer values
   public static final double DECAY_FACTOR = 0.996;
   
   /*
   constructs GuitarString object ring buffer with given frequency
   throws IllegalArgumentException if given frequency is negative
   throws IllegalArgumentException if given frequency makes
   the length of the ring buffer too short
   */
   public GuitarString(double frequency){
      //checks if frequency is negative
      // throws IllegalArgumentException if it is
      if(frequency <= 0){
         throw new IllegalArgumentException();
      }
      
      //calculates integer capacity for queue using StdAudio's sample rate
      int capacity = (int)Math.round(StdAudio.SAMPLE_RATE/frequency);
      
      //checks if capacity is too small to be guitar string
      // throws IllegalArgumentException of it is
      if(capacity < 2){
         throw new IllegalArgumentException();
      }
      ringBuffer = new LinkedList<Double>();
      
      //fills queue with 0.0 based off of capacity
      for(int i = 0; i < capacity; i++){
         ringBuffer.add(0.0);
      }
   }
   
   /*
   constructs GuitarString object ring buffer with given array
   throws IllegalArgumentException if array is too small to make GuitarString
   */
   public GuitarString(double[] init){
      //checks if array is too small to be guitar string
      // throws IllegalArgumentException of it is
      if(init.length < 2){
         throw new IllegalArgumentException();
      }
      
      ringBuffer = new LinkedList<Double>();
      
      //fills queue with values in init array
      for(int i = 0; i < init.length; i++){
         ringBuffer.add(init[i]);
      }
   }
   
   /*
   plucks guitarString which fills ring buffer with
   random values from -0.5 to 0.5
   */
   public void pluck(){
      Random rand = new Random();
      
      for(int i = 0; i < ringBuffer.size(); i++){
         //random double from -0.5 inclusive to 0.5 exclusive
         double randVal = rand.nextDouble() - 0.5;
         
         //replaces values in queue with new random displacements
         ringBuffer.remove();
         ringBuffer.add(randVal);
      }
   }
   
   /*
   Performs Karplus-Strong algorithm on GuitarString object,
   inserts the new value at the end of the ring buffer
   */
   public void tic(){
      //stores first two values and removes first value from queue
      double firstVal = ringBuffer.remove();
      double secondVal = ringBuffer.peek();
      //performs Karplus-Strong algorithm
      double update = ((firstVal + secondVal) / 2) * DECAY_FACTOR;
      
      ringBuffer.add(update); //adds updated value
   }
   
   /*
   returns sample from ring buffer
   */
   public double sample(){
      return ringBuffer.peek();
   }
}