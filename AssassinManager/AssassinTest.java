import java.util.*;

public class AssassinTest{
   public static void main(String[] args){
      ArrayList<String> testList = new ArrayList<String>();
      /* 
      testList.add("Maya");
      testList.add("Aishah");
      testList.add("Vivian");
      testList.add("Teresa");
      */
      
      AssassinManager manager = new AssassinManager(testList);
      System.out.println(manager);
      System.out.println("kill ring: ");
      manager.printKillRing();
      System.out.println("graveyard: ");
      manager.printGraveyard();
      
      //System.out.println(manager.killRingContains("teresa"));
      //System.out.println(manager.graveyardContains("teresa"));
      
      manager.kill("maya");
      System.out.println("kill ring: ");
      manager.printKillRing();
      System.out.println("graveyard: ");
      manager.printGraveyard();
      
      manager.kill("Aishah");
      System.out.println("kill ring: ");
      manager.printKillRing();
      System.out.println("graveyard: ");
      manager.printGraveyard();
      
      manager.kill("vivian");
      System.out.println("kill ring: ");
      manager.printKillRing();
      System.out.println("graveyard: ");
      manager.printGraveyard();
      
      System.out.println("Winner of the murder game: " + manager.winner());
   }
}