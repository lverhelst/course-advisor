
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Leon Verhelst
 * An Inference Engine applies rules to facts stored in the knowledge base
 * Our inference engine uses forward-chaining, that is, it starts from the given facts 
 * and applies its rules, asserts new facts and re-runs until it has reached a goal.
 */

public class InferenceEngine {
    private final int TOTAL_REQUIRED_CREDIT_HOURS = 120;
    
    
    
    
    //Example RULE: CPSC300 + 60CreditHours  => CPSC 400
    private ArrayList<String> rules;
    private HashMap<String, Course> facts;
    
    private Session session;
    private CourseList courseList;
    
    
    public InferenceEngine(Session current_Session, CourseList cl){
        this.session = current_Session;
        this.courseList = cl;
        rules = new ArrayList<String>();
        this.loadRules();
        facts = new HashMap<String, Course>();
        for(Course s :current_Session.getSetCourses()){
            facts.put(s.getNum() + "", s);
        }
        
    }
    
    private void loadRules(){
        for(Course c : courseList.getCourses()){
            String course_rule = "";
            for(int i = 0; i < c.getPrereqs().size(); i++){
                course_rule += c.getPrereqs().get(i);
                if(i != c.getPrereqs().size() - 1){
                    course_rule += "_";
                }
            }
            course_rule += "=>" + c.getNum();
            rules.add(course_rule);
        }
    }
    
    public void infer(){
        boolean applied_a_rule = true;
        
       
        while(session.credit_hours < TOTAL_REQUIRED_CREDIT_HOURS && applied_a_rule){
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
                        facts.put(res, courseList.get(Integer.parseInt(res)));
                        session.addCourse(courseList.get(Integer.parseInt(res)).getSuggested_semester(), courseList.get(Integer.parseInt(res)));
                        break;
                    }
                    
                }
             }
        }
        System.out.println("Credit hours: " + session.credit_hours + "/120 = " + (float)session.credit_hours/120 * 100 + "%");
        session.printSemesters();
    }
    
    private boolean checkRule(String rule){
        boolean premise = true;
        for(String prem : rule.split("=>")[0].split("_")){
            premise &= facts.containsKey(prem);
        }
        return premise;
    }
    
    
}
