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
        
        if(cl.loadCourseList() && rl.loadRuleList("cpscrules.txt")){
            Course[] initialCourses = cl.get("CPSC100","CPSC141","MATH100","ENGL170", "MATH220", "CPSC242" , "CPSC126");
                        
            Session session = new Session(5);          
            session.setInitialCourses(initialCourses);
            InferenceEngine ie = new InferenceEngine(session, cl, rl);
            session = ie.infer();
            //Print if rules passed
            
            System.out.println("Credit hours: " + session.credit_hours + "/120 = " + (float)session.credit_hours/120 * 100 + "%");
            System.out.println(session.printSemesters());
            int i = 0;
            for(Course c : session.getSetCourses()){
                if(c.getSubject().equals("CPSC")){
                    i++;
                }
            }
          
            
            for(Rule rule : rl.getRuleSet()){
                    //If the user has not satisfied a rule
                    String[] factsKeySet = new String[ie.facts.keySet().size()];
                    System.out.println(rule.getName() + ": " + ((rule.check(ie.facts.keySet().toArray(factsKeySet)))?"PASSED" : "FAILED"));
            }
              System.out.println("120 credit hour requirment: (" + session.credit_hours + ") " +((float)session.credit_hours/120 * 100 >= 100 ? "PASSED" : "FAILED"));
            System.out.println("20 CPSC Course Requirement: (" + i + ") " + ((i >= 20)? "PASSED" : "FAILED"));
        }
        
        //BannerConnect bc = new BannerConnect();
        //bc.update();
        
        //start HUI
        //Launch Application    
	//Application.launch(HUI.class);
    }
    
}
