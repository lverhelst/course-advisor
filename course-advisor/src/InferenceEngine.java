
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Leon Verhelst and Emery Berg
 An Inference Engine applies rules to facts stored in the knowledge base
 Our inference engine uses forward-chaining, that is, it starts from the given facts 
 and applies its rules, asserts new facts and re-runs until it has reached a goal.
 
 The inference engine is limited to rules that us a single premise, or a list of ANDed premises
 */

public class InferenceEngine {
    private final int TOTAL_REQUIRED_CREDIT_HOURS = 120;
    
    //Example RULE: CPSC300 + 60CreditHours  => CPSC 400    p
    private ArrayList<CourseRule> rules;
    private HashMap<String, Fact> facts;
    
    private Session session;
    private CourseList courseList;
    private RuleList ruleList;
    
    /**
     * Default constructor sets up and initializes the inference engine
     * @param current_Session the current session (facts)
     * @param cl the list of courses (course_rules)
     * @param rl the list of rules (degree_rules)
     */
    public InferenceEngine(Session current_Session, CourseList cl, RuleList rl){
        rules = new ArrayList();
        facts = new HashMap();
        
        this.session = current_Session;
        this.courseList = cl;
        this.ruleList = rl;
        
        //loads the prereq rules
        this.loadRules();
        
        //loads the currently selected courses
        Fact newFact = null;
        for(Course course :current_Session.getSetCourses()){
            newFact = new Fact("cr", course.getName()) ;
            facts.put(newFact.toString(), newFact);
        }        
    }
    
    /**
     * used to load the rules to be used in the inference engine
     */
    private void loadRules(){
        //Load Course Prerequisite Rules EX: CourseA+CourseB=>CourseC
        Fact newFact = null;
        ArrayList<Fact> pres ;
        for(Course course : courseList.getCourses()){
             newFact = new Fact("cr", course.getName());
             pres = new ArrayList<Fact>();
             for(String str : course.getPrereqs()){
                 pres.add(new Fact("cr", str));
             }
             rules.add(new CourseRule(newFact, pres, course.preString));
        }
        System.out.println("Loaded Rules");
    }
    
    /**
     * Performs forward chaining in order to find what courses should be taken
 based on the current rules and selected courses
     * @return the inference session for the user
     */
    public Session infer(){
        boolean applied_a_rule = true;
        //Stop when the number of credits are satisfied, or there are no more courses
        //to choose from
        while(session.credit_hours < TOTAL_REQUIRED_CREDIT_HOURS && applied_a_rule){
            applied_a_rule = false;
            //BACKWARDS CHAINING!
            for(Rule rule : ruleList.getRuleSet()){
                //If the user has not satisfied a rule
                String[] factsKeySet = new String[facts.keySet().size()];
                if(!rule.check(facts.keySet().toArray(factsKeySet))){
                    //add next available course from rule set
                    for(String course : rule.getSet()){
                        //If we haven't already suggested this course
                        if(!facts.containsKey("cr:" + course)){
                            //Check if we can take the course
                            boolean canTake = false;
                            //This for loop loops n times (max) to get 1 course rule (super silly)
                            for(CourseRule cr : rules) {
                               //check prereqs for the rule resulting in this course
                                if(cr.getAction().getValue().equals(course)){   
                                    if(cr.check()) {  //if(cr.stringCheck(cr.prereqString)) {                  
                                        canTake = true; 
                                        break;
                                    }
                                }
                            }
                            if(canTake)
                            {
                                //Add to facts, break out so that we don't add extra
                                facts.put("cr:" + course, new Fact("cr", course));                                
                                System.out.println("Adding priority course: " + course + " from rule: " + rule.getName());                                
                                session.addCourse(courseList.get(course).getAcademic_Year(), courseList.get(course)); 
                                applied_a_rule = true;
                                break;
                            }
                       }
                    }
                }
            }
            
        }
        applied_a_rule = false; //TODO: change to TRUE to enable electives!
        while(session.credit_hours < TOTAL_REQUIRED_CREDIT_HOURS && applied_a_rule){
            applied_a_rule = false;
            
            //checks if rule is valid and if it is, fires the rule
            //FORWARD CHAINING ON COURSES 
            //DO THIS SECOND
            for(CourseRule rule : rules) {
                
                
                if(rule.check()) {  //if(cr.stringCheck(cr.prereqString)) {                  
                    Fact fire = rule.getAction();
                    if(!facts.containsKey(fire.toString())) {
                        facts.put(fire.toString(), fire);
                                             
                      //  System.out.println("Adding non-priority course: " + fire.getValue());      
                        session.addCourse(courseList.get(fire.getValue()).getAcademic_Year(), courseList.get(fire.getValue()));                    
                        applied_a_rule = true;
                    }
                }
            }
        }
        
        return session;
    }
    
    //COURSE RULE NEEDS TO INCLUDE ORS
    
    /**
     * Inner class used to model the rules
     */
    public class CourseRule  {
        public String prereqString;
        public ArrayList<Fact> premises;
        public Fact action;
        
        /**
         * Default constructor used to define the action and premises to 
 trigger the action
         * @param action the action to perform
         * @param premises which are needed to trigger
         */
        public CourseRule(Fact action, ArrayList<Fact> premises, String preString) {
            this.premises = premises;
            this.prereqString = preString;
            this.action = action;
        }
        /**
         * Check to see if a course rule contains a specified course as a premise
         * @param course The course to check
         * @return  if a course rule contains a specified course as a premise
         */
        public boolean containsPremise(String course){
            for(Fact fact: premises) {
                if(fact.getValue().equals(course))
                    return true;
            }
            return false;
        }
        
        /**
         * Check if the rule should fire or not based on the facts
         * @return true if it should fire
         */
        public boolean check() {
            boolean premise = !premises.isEmpty();
            if(premises.isEmpty())
                return true;
            for(Fact fact: premises) {
                if(fact != null && premise)
                    premise &= facts.containsKey(fact.toString());
                else //invalid course???
                    return false;
            }
            
            return premise;
        }
        /***
         * Recursively evaluates a boolean expression of courses (ex CPSC100ANDMATH100ORMATH105) 
         * @param prestring Expression to evaluate
         * @return result
         */
        public boolean stringCheck(String prestring){
            //Case when 0 tokens (no prereqs)
            if(prestring == null || prestring.equals(""))
                return true;
            //Case when true or false
            if(prestring.equals("TRUE"))
                return true;
            if(prestring.equals("FALSE"))
                return false;
            String[] tokens = prestring.toUpperCase().split("OR|AND");
            if(tokens.length > 1){
                String remainder = "";
                for(int i = 3; i < tokens.length; i++){
                    remainder += tokens[i];
                }
                //grab first three tokens
                switch(tokens[1]){
                    case "OR":
                        if(facts.containsKey(tokens[0]) || facts.containsKey(tokens[2])){
                            return this.stringCheck("TRUE" + remainder);
                        }else{
                             return this.stringCheck("FALSE" + remainder);
                        }
                    case "AND":
                        if(facts.containsKey(tokens[0]) && facts.containsKey(tokens[2])){  
                            return this.stringCheck("TRUE" + remainder);
                        }else{
                            return this.stringCheck("FALSE" + remainder);
                        }
                    default:
                        return this.stringCheck("FALSE");
                }
            }else{    
                //case when 1 prereq
                return facts.containsKey(tokens[0]); 
            }
        }
        
        
        
        /**
         * Used to get the action of the rule
         * @return the action of the rule
         */
        public Fact getAction() {
            return action;
        }
        
        public String toString(){
            String ret = prereqString + " ||||| ";
            for(Fact f : premises){
                ret += f.getValue() + " + ";
            }
            ret += " => " + this.action.getValue();
            return ret;
        }
    }
}
