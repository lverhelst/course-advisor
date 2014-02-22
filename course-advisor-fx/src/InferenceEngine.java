
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Leon Verhelst and Emery Berg
 * An Inference Engine applies rules to facts stored in the knowledge base
 * Our inference engine uses forward-chaining, that is, it starts from the given facts 
 * and applies its rules, asserts new facts and re-runs until it has reached a goal.
 * 
 * The inference engine is limited to rules that us a single premise, or a list of ANDed premises
 */

public class InferenceEngine {
    private final int TOTAL_REQUIRED_CREDIT_HOURS = 120;
    
    //Example RULE: CPSC300 + 60CreditHours  => CPSC 400    p
    private ArrayList<Rule> rules;
    private HashMap<Integer, Course> facts;
    
    private Session session;
    private CourseList courseList;
    
    /**
     * Default constructor sets up and initializes the inference engine
     * @param current_Session the current session (facts)
     * @param cl the list of courses (rules)
     */
    public InferenceEngine(Session current_Session, CourseList cl){
        this.session = current_Session;
        this.courseList = cl;
        rules = new ArrayList();
        this.loadRules();
        facts = new HashMap();
        for(Course course :current_Session.getSetCourses()){
            facts.put(course.getNum(), course);
        }        
    }
    
    private void loadRules(){
        //Load Course Prerequisite Rules
        //EX: CourseA+CourseB=>CourseC
        for(Course course : courseList.getCourses()){
            rules.add(new Rule(course, course.getPrereqs()));
            /*
            String course_rule = "";
            for(int i = 0; i < c.getPrereqs().size(); i++){
                course_rule += c.getPrereqs().get(i);
                if(i != c.getPrereqs().size() - 1){
                    course_rule += "+";
                }
            }
            course_rule += "=>" + c.getNum();
            rules.add(course_rule);
            */
        }
        //TODO: Load Other Rules
    }
    
    public void infer(){
        boolean applied_a_rule = true;
        //Stop when the number of credits are satisfied, or there are no more courses
        //to choose from
        while(session.credit_hours < TOTAL_REQUIRED_CREDIT_HOURS && applied_a_rule){
            for(Rule rule : rules) {
                applied_a_rule = false;
                
                //checks if rule is valid and if it is, fires the rule
                if(rule.check()) {                    
                    Course fire = rule.getAction();
                    if(!facts.containsKey(fire.getNum())) {
                        facts.put(fire.getNum(), fire);
                        session.addCourse(courseList.get(fire.getNum()).getSuggested_semester(), courseList.get(fire.getNum()));                    
                        applied_a_rule = true;
                    }
                }
            }
            /*
            System.out.println("Facts"); 
            for(String f : facts.keySet())
                System.out.println(f);
            System.out.println(" ");
             for(String rule : rules){
                applied_a_rule = false;
                System.out.println(rule + ": " + checkRule(rule));
                if(checkRule(rule)){
                    String res = rule.split("=>")[1];
                    if(!facts.containsKey(res)){
                        applied_a_rule = true;
                        System.out.println("Adding fact: " + rule.split("=>")[1]);
                        facts.put(res, res);
                        session.addCourse(courseList.get(Integer.parseInt(res)).getSuggested_semester(), courseList.get(Integer.parseInt(res)));
                        break;
                    }
                }
             }
             facts.put("ch", session.credit_hours + "ch");
             System.out.println(session.credit_hours + "ch");*/
        }
        System.out.println("Credit hours: " + session.credit_hours + "/120 = " + (float)session.credit_hours/120 * 100 + "%");
        session.printSemesters();
    }
    
    /*
    private boolean checkRule(String rule){
        boolean premise = true;
        
        for(String prem : rule.split("=>")[0].split("\\+")){
            if(!"".equals(prem)) //Ensure that a rule with no premise is true (EX: ""=>CPSC100)
                if(prem.endsWith("ch")){ //Handle credit hour facts
                    premise &= (facts.containsKey("ch") && facts.get("ch").equals(prem)); //take advantage of short circuiting
                }else{
                    premise &= facts.containsKey(prem);
                }
        }
        return premise;
    }*/
    
    /**
     * Inner class used to model the rules
     */
    public class Rule {
        public ArrayList<Course> premises;
        public Course action;
        
        /**
         * Default constructor used to define the action and premises to 
         * trigger the action
         * @param action the action to perform
         * @param premises which are needed to trigger
         */
        public Rule(Course action, ArrayList<Course> premises) {
            this.premises = premises;
            this.action = action;
        }
        
        /**
         * Check if the rule should fire or not based on the facts
         * @return true if it should fire
         */
        public boolean check() {
            boolean premise = true;
            
            for(Course course: premises) {
                if(course != null)
                    premise &= facts.containsKey(course.getNum());
            }
            
            return premise;
        }
        
        /**
         * Used to get the action of the rule
         * @return the action of the rule
         */
        public Course getAction() {
            return action;
        }
    }
}