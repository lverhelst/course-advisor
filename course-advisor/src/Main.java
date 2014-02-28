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
           // Course[] initialCourses = cl.get("CPSC100","CPSC141","MATH100","CPSC242", "MATH220");
                        
            Session session = new Session(5);          
            //session.setInitialCourses(initialCourses);
            InferenceEngine ie = new InferenceEngine(session, cl, rl);
            session = ie.infer();
            
            System.out.println("Credit hours: " + session.credit_hours + "/120 = " + (float)session.credit_hours/120 * 100 + "%");
            System.out.println(session.printSemesters());
        }
        
        //BannerConnect bc = new BannerConnect();
        //bc.update();
        
        //start HUI
        //Launch Application    
	//Application.launch(HUI.class);
    }
    
}
