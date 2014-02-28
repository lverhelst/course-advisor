import java.util.ArrayList;

/**
 * Interface for the rule class
 * @author Emery
 */
public interface Rule {
    /**
    * Simple method which returns true if the rule passes
    * @param set the set of courses to check against the list
    * @return true if the rule passes
    */
   public boolean check(String[] set);

   /**
    * Used to get the intersection of 2 list of courses
    * @param set the set of courses to compare against
    * @return the list of courses which match (null if no type is invalid)
    */
   public ArrayList<String> intersect(String[] set);
   
   /**
    * @return a string of the rule type 
    */
   public String getType();

   /**
    * @return the name of the rule
    */
   public String getAction();
   
   /**
    * Returns the names of the courses of the rule
    * @return a list of the courses in this rule's premises
    */
   public String[] getSet();
   
   /**
    * @return The name of the rule
    */
   public String getName();
}