import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Used to load and store the rules imported in for the degree requirements
 * @author Emery Berg
 */
public class RuleList {
    private HashMap<String, Rule> ruleset;
    
     /**
      * Used to load a list of rules from the file passed into the method
      * Format Name!Type!Set!Number
      *     Name: the name of the rule
      *     Type: Normal | Level 
      *     Set: Normal (list of courses) | (the min level of course eg 300)
      *     Number: Normal (number of courses to be taken) | Level (number of credits at that level)
      * @param filename a string of the filename
      * @return boolean value of the success of the loading
      */
    public boolean loadRuleList(String filename){
        ruleset = new HashMap();
        BufferedReader br;
        
        try{
            //open file (This text file is the Knowledge Base for the Expert System)
            br = new BufferedReader(new FileReader(filename));
            String line = br.readLine();
            
            String[] parts;
            String[] set;
            int number;
                        
            while(line != null) {
                parts = line.split("!");
                set = parts[2].split(",");
                number = Integer.parseInt(parts[3]);
                GradRule rule = new GradRule(parts[0], parts[1], number, set);
                ruleset.put(rule.name, rule);
                line = br.readLine();
            }
            br.close();
        }catch(IOException | NumberFormatException e){
            System.err.println(e.toString());
            return false;
        }
        return true;
    }   
    
    /**
     * @return a string array of the rule names 
     */    
    public String[] getList() {
        return ruleset.keySet().toArray(new String[ruleset.keySet().size()]);
    }
    
    public Collection<Rule> getRuleSet(){
        return ruleset.values();
    }
    
    /**
     * Used to get the rule based on the name
     * @param ruleName the name of the rule
     * @return the rule found in the list
     */
    public Rule getRule(String ruleName) {
        return ruleset.get(ruleName);
    }
    
    /**
     * Inner class used to model the rules
     */
    public class GradRule implements Rule {
        public String name;
        public String type;
        public String[] set;
        public int number;
          
        /**
         * Constructor for creating rules
         * @param name (String) name of the rule
         * @param type (Normal | Level) 
         * @param number (integer) Normal (number of courses to be taken) | Level (number of credits at that level)
         * @param set (String[]) Normal (list of courses) | (the min level of course eg 300)
         */
        public GradRule(String name, String type, int number, String ... set) {
            this.name = name;
            this.type = type;
            this.number = number;
            this.set = set;
        }
               
        /**
         * Simple method which returns true if the rule passes
         * @param set the set of courses to check against the list
         * @return true if the rule passes
         */
        @Override
        public boolean check(String[] set) {
            int matched = 0;
            
            for(String crule: this.set) {
                for(String rule: set) {
                    if(crule.equals(rule.split(":")[1]))
                        matched++;
                }
            }
            
            return matched >= number;
        }  
        
        /**
         * Used to get the intersection of 2 list of courses
         * @param set the set of courses to compare against
         * @return the list of courses which match (null if no type is invalid)
         */
        public ArrayList<String> intersect(String[] set) {            
            switch(type) {
                case "Normal":
                    return intersectNorm(set);
                case "Level":
                    return intersectLvl(set);
            }
            
            return null;
        }
        
        /**
         * Used to get the intersection of 2 list of courses
         * @param set the set of courses to compare against
         * @return the list of courses which match
         */
        private ArrayList<String> intersectNorm(String[] set) {
            ArrayList<String> intersection = new ArrayList();
            
            for(String crule: this.set) {
                for(String rule: set) {
                    if(crule.equals(crule))
                        intersection.add(rule);
                }
            }
            
            return intersection;
        }
        
        /**
         * Used to get the intersection of a list of courses with the defined level
         * @param set the set of courses to compare against
         * @return the list of courses which match
         */
        private ArrayList<String> intersectLvl(String[] set) {
            ArrayList<String> intersection = new ArrayList();
                        
            for(String crule: this.set) {
                int cnum = Integer.parseInt(crule);
                for(String rule: set) {
                    int num = Integer.parseInt(rule.substring(5));
                    if(num >= cnum)
                        intersection.add(rule);
                }
            }
            
            return intersection;
        }
        
        /**
        * @return a string of the rule type 
        */
        public String getType(){
            return type;
        }
      
        /**
         * @return the name of the rule
         */
        public String getAction() {
            return name;
        }
        /**
         * @return the Set of Names of Courses for this rule
         */
        public String[] getSet(){
            return this.set;
        }
        /**
         * @return the name of the rule
         */
        public String getName(){
            return this.name;
        }
    }
}
