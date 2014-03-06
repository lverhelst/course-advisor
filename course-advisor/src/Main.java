
import java.io.File;
import java.util.ArrayList;

/**
 * @author Leon "The Night" Verhelst
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CourseList cl = new CourseList();
        RuleList rl = new RuleList();
        
        if(cl.loadCourseList() && rl.loadRuleList(new File("cpscrules.txt"))){
            Course[] initialCourses = cl.get("CPSC100","CPSC141");
            ArrayList<String> interests = new ArrayList<String>(); 
            interests.add("CPSC");
            interests.add("MATH");
            interests.add("PHYS");
            interests.add("NREM");
            
            Session session = new Session(5);          
           // session.setInitialCourses(initialCourses);
            InferenceEngine ie = new InferenceEngine(session, cl, rl, interests);
            ie.setInclude_specialized_topics(false);
            session = ie.inferDegreeRequirements();
            session = ie.inferElectives();
            
            
            //Print if rules passed
            System.out.println("Credit hours: " + session.credit_hours + "/120 = " + (float)session.credit_hours/120 * 100 + "%");
            System.out.println(session.printSemesters());
            int i = 0;
            for(Course c : session.getSetCourses()){
                if(c.getSubject().equals("CPSC")){
                    i++;
                }
            }
            //If the user has not satisfied a rule
            String[] factsKeySet = new String[ie.getFacts().keySet().size()];
            for(Rule rule : rl.getRuleSetArray()){ 
                    System.out.println(rule.getName() + ": " + ((rule.check(ie.getFacts().keySet().toArray(factsKeySet))) ? "PASSED" : "FAILED"));
            }
            System.out.println("120 credit hour requirment: (" + session.credit_hours + ") " +((float)session.credit_hours/120 * 100 >= 100 ? "PASSED" : "FAILED"));
            System.out.println("20 CPSC Course Requirement: (" + i + ") " + ((i >= 20)? "PASSED" : "FAILED"));
        }
        
        //BannerConnect bc = new BannerConnect();
        //bc.update();
    }
    
}
