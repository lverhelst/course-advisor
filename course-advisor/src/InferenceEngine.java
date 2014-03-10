
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
    private boolean include_specialized_topics = false;
    
    //Example RULE: CPSC300 + 60CreditHours  => CPSC 400    p
    private ArrayList<CourseRule> rules;
    private HashMap<String, String> facts;
    
    private Session session;
    private CourseList courseList;
    private RuleList ruleList;
    private ArrayList<String> interests;
    
    /**
     * Default constructor sets up and initializes the inferDegreeRequirementsence engine
     * @param current_Session the current session (facts)
     * @param cl the list of courses (course_rules)
     * @param rl the list of rules (degree_rules)
     */
    public InferenceEngine(Session current_Session, CourseList cl, RuleList rl, ArrayList<String> interests){
        rules = new ArrayList();
        facts = new HashMap();
        
        this.session = current_Session;
        this.courseList = cl;
        this.ruleList = rl;
        this.interests = interests;
        //loads the prereq rules
        this.loadRules();
        
        //loads the currently selected courses
        for(Course course :current_Session.getSetCourses()){
            facts.put(course.getName(), course.getName());
        }        
    }
    
    /**
     * used to load the rules to be used in the inferDegreeRequirementsence engine
     */
    private void loadRules(){
        //Load Course Prerequisite Rules EX: CourseA+CourseB=>CourseC
        ArrayList<String> pres ;
        for(Course course : courseList.getCourses()){
             pres = new ArrayList<String>();
             for(String str : course.getPrereqs()){
                 pres.add(str);
             }
             rules.add(new CourseRule(course.getName(), pres, course.preString));
        }
        Printer.print("Loaded Rules");
    }
    
    /**
     * Performs forward chaining in order to find what courses should be taken
 based on the current rules and selected courses
     * @return the inferDegreeRequirementsence session for the user
     */
    public Session inferDegreeRequirements(){
        boolean applied_a_rule = true;
        //Stop when the number of credits are satisfied, or there are no more courses
        //to choose from
        while(session.credit_hours <= TOTAL_REQUIRED_CREDIT_HOURS && applied_a_rule){
            //BACKWARDS CHAINING!
            for(Rule rule : ruleList.getRuleSetArray()){
                Printer.print("Applying " + rule.getType().toLowerCase() + " rule : " + rule.getName());
                applied_a_rule = true;
                //Completely apply each rule one at a time
                while(applied_a_rule){
                    applied_a_rule = false;
                    //If the user has not satisfied a rule (ensure that the rule isn't already satisfied)
                    String[] factsKeySet = new String[getFacts().keySet().size()];
                    if(!rule.check(facts.keySet().toArray(factsKeySet))){
                        //rule type: normal
                        if(rule.getType().toLowerCase().equals("normal")){
                             
                            //add next available course from rule set
                            for(String course : rule.getSet()){
                                if(tryApplyCourse(course, rule.getType().toLowerCase())){
                                    applied_a_rule = true;
                                    break;
                                }
                            }
                        }else{
                            //rule type : level
                            //add next available course from rule set
                            for(String course : rule.intersect(courseList.getCourseNames())){
                                if(tryApplyCourse(course, rule.getType().toLowerCase())){
                                    applied_a_rule = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return session;
    }
    /**
     * Infers elective courses
     * Takes note of which courses the user has selected an interest in
     * @return the Session with Electives chosen
     */
    public Session inferElectives(){
        boolean applied_a_rule = true; //TODO: change to TRUE to enable electives!
        while((session.credit_hours <= TOTAL_REQUIRED_CREDIT_HOURS) && applied_a_rule){
            applied_a_rule = false;
            //checks if rule is valid and if it is, fires the rule
            //FORWARD CHAINING ON COURSES 
            //DO THIS SECOND
            for(CourseRule rule : rules) {
                              
                if(session.credit_hours >= TOTAL_REQUIRED_CREDIT_HOURS)
                        break;
                //skip rule if it isn't in the student's interests
                if(interests != null && interests.size() > 0 && !interests.contains(rule.action.substring(0, 4))){
                    continue;
                } 
                if(rule.check()) {  //
                //if(rule.stringCheck(rule.prereqString)) {                  
                    String fire = rule.getAction();
                    //skip 0, 1 and 2 credit courses
                    if(courseList.get(fire).getCredits() < 3){
                        continue;
                    }
                    //ensure that 100 and 110 courses are not added to the same session
                    boolean special = false;
                    if(fire.endsWith("100")){
                        special = getFacts().containsKey(fire.substring(0, 4) + "110");
                    }
                    else if(fire.endsWith("110")){
                        special = getFacts().containsKey(fire.substring(0, 4) + "100");
                    }
                    //filter out specialized courses if requested
                    if(!include_specialized_topics){
                        special |= Integer.parseInt(fire.substring(fire.length() - 2, fire.length())) >= 90;
                    }
                    
                    if(!facts.containsKey(fire.toString()) && !special) {                
                        if(session.addCourse(courseList.get(fire).getAcademic_Year(), courseList.get(fire))){
                            getFacts().put(fire.toString(), fire);
                            applied_a_rule = true;
                        }
                    }else{
                        if(special)
                            Printer.print("Course skipped: " + fire.toString());
                    }
                }
            }
        }
        return session;
    }    
    /**
     * FORWARD CHAINING
     * Attempts to apply a course to facts, ensures that the course satisfies the prerequisites
     * Also, the course must satisfy the custom conditions as set by the user
     * @param course_name The name of the course to be added
     * @param rule_type The name of the rule attempting to add the course
     * @return whether or not the course satisfied enough requirements to be added to the Session
     */
    private boolean tryApplyCourse(String course_name, String rule_type){
        //If we haven't already suggested this course
        //skip 0, 1 and 3 credit courses
        if(courseList.get(course_name).getCredits() < 3){
            return false;
        }
        //ensure that 100 and 110 courses are not added to the same session
        boolean special = false;
        if(course_name.endsWith("100")){
            special = getFacts().containsKey(course_name.substring(0, 4) + "110");
        }else if(course_name.endsWith("110")){
            special = getFacts().containsKey(course_name.substring(0, 4) + "100");
        }
        //filter out specialized courses if requested
        if(!include_specialized_topics){
            special |= Integer.parseInt(course_name.substring(course_name.length() - 2, course_name.length())) >= 90;
        }
        if(!facts.containsKey(course_name) && !special){
            //Check if we can take the course
            boolean canTake = false;
            //This for loop loops n times (max) to get 1 course rule (super silly)
            for(CourseRule cr : rules) {
                //skip rule if it isn't in the student's interests, and its not a normal rule type (course isn't explicitly required)
                if(interests != null && interests.size() > 0 &&!interests.contains(cr.action.substring(0, 4)) && !rule_type.equals("normal")){
                    continue;
                }
               //check prereqs for the rule resulting in this course
                if(cr.getAction().equals(course_name)){   
                    //if(cr.check()) {  //
                    if(cr.stringCheck(cr.prereqString)) {                  
                        canTake = true; 
                        break;
                    }
                }
            }
            if(canTake)
            {       
               
                if(session.addCourse(courseList.get(course_name).getAcademic_Year(), courseList.get(course_name))) {
                    getFacts().put(course_name, course_name);   
                    return true;
                }
            }
       }else{
             if(special)
                Printer.print("Course skipped: " + course_name);
        }
       return false;
    }

    /**
     * @return the facts
     */
    public HashMap<String, String> getFacts() {
        return facts;
    }

    /**
     * @return the include_specialized_topics
     */
    public boolean isInclude_specialized_topics() {
        return include_specialized_topics;
    }

    /**
     * @param include_specialized_topics the include_specialized_topics to set
     */
    public void setInclude_specialized_topics(boolean include_specialized_topics) {
        this.include_specialized_topics = include_specialized_topics;
    }
    
    /**
     * Inner class used to model the rules
     */
    public class CourseRule  {
        public String prereqString;
        public ArrayList<String> premises;
        public String action;
        
        /**
         * Default constructor used to define the action and premises to trigger the action
         * @param action the action to perform
         * @param premises which are needed to trigger
         */
        public CourseRule(String action, ArrayList<String> premises, String preString) {
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
            return premises.contains(course);
        }
        
        /**
         * Check if the rule should fire or not based on the facts
         * @return true if it should fire
         */
        public boolean check() {
            boolean premise = !premises.isEmpty();
            if(premises.isEmpty())
                return true;
            for(String fact: premises) {
                if(fact != null && premise)
                    premise &= getFacts().containsKey(fact.toString());
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
            String[] tokens = prestring.toUpperCase().split(",");
            if(tokens.length > 1){
                String remainder = "";
                for(int i = 3; i < tokens.length; i++){
                    remainder += "," + tokens[i];
                }
                //grab first three tokens
                switch(tokens[1]){
                    
                    case "OR":
                        if(tokens[0].equals("TRUE") || getFacts().containsKey(tokens[0]) || getFacts().containsKey(tokens[2])){
                            return this.stringCheck("TRUE" + remainder);
                        }else{
                             return this.stringCheck("FALSE" + remainder);
                        }
                    case "AND":
                        try{
                        if((tokens[0].equals("TRUE") || getFacts().containsKey(tokens[0])) && getFacts().containsKey(tokens[2])){  
                            return this.stringCheck("TRUE" + remainder);
                        }else{
                            return this.stringCheck("FALSE" + remainder);
                        }
                        }catch(Exception e){
                            Printer.printError(e.toString());
                        }
                    default:
                        return this.stringCheck("FALSE"); //This should never be executed
                }
            }else{    
                //case when 1 prereq
                return getFacts().containsKey(tokens[0]); 
            }
        }
        
        /**
         * Used to get the action of the rule
         * @return the action of the rule
         */
        public String getAction() {
            return action;
        }
        
        @Override
        public String toString(){
            String ret = prereqString + " ||||| ";
            for(String f : premises){
                ret += f + " + ";
            }
            ret += " => " + this.action;
            return ret;
        }
    }
}