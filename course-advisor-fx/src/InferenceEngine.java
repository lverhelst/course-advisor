
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
    private ArrayList<Rule> rules;
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
     * Private class used to load the rules to be used in the inference engine
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
             rules.add(new Rule(newFact, pres));
        }
        System.out.println("Loaded Rules");
    }
    
    /**
     * Performs forward chaining in order to find what courses should be taken
     * based on the current rules and selected courses
     * @return the inference session for the user
     */
    public Session infer(){
        boolean applied_a_rule = true;
        //Stop when the number of credits are satisfied, or there are no more courses
        //to choose from
        while(session.credit_hours < TOTAL_REQUIRED_CREDIT_HOURS && applied_a_rule){
            for(Rule rule : rules) {
                applied_a_rule = false;
                
                //checks if rule is valid and if it is, fires the rule
                if(rule.check()) {                    
                    Fact fire = rule.getAction();
                    if(!facts.containsKey(fire.toString())) {
                        facts.put(fire.toString(), fire);
                                             
                        
                        session.addCourse(courseList.get(fire.getValue()).getAcademic_Year(), courseList.get(fire.getValue()));                    
                        applied_a_rule = true;
                    }
                }
            }
        }
        
        return session;
    }
    
    /**
     * Inner class used to model the rules
     */
    public class Rule {
        public ArrayList<Fact> premises;
        public Fact action;
        
        /**
         * Default constructor used to define the action and premises to 
         * trigger the action
         * @param action the action to perform
         * @param premises which are needed to trigger
         */
        public Rule(Fact action, ArrayList<Fact> premises) {
            this.premises = premises;
            this.action = action;
        }
        
        /**
         * Check if the rule should fire or not based on the facts
         * @return true if it should fire
         */
        public boolean check() {
            boolean premise = !premises.isEmpty();
            
            
          
            
            for(Fact fact: premises) {
                if(fact != null && premise)
                    premise &= facts.containsKey(fact.toString());
                else //invalid course???
                    return false;
            }
            
            return premise;
        }
        
        /**
         * Used to get the action of the rule
         * @return the action of the rule
         */
        public Fact getAction() {
            return action;
        }
    }
}
