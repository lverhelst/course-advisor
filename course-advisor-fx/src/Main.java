/**
 * @author Leon "The Night" Verhelst
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CourseList cl = new CourseList();
        if(cl.loadCourseList()){
            Session session = new Session(5);
            Course[] initialCourses = cl.get("CPSC100","CPSC141","CPSC340","CPSC371");
            session.setInitialCourses(initialCourses);
                        
            InferenceEngine ie = new InferenceEngine(session, cl);
            session = ie.infer();
            
            System.out.println("Credit hours: " + session.credit_hours + "/120 = " + (float)session.credit_hours/120 * 100 + "%");
            System.out.println(session.printSemesters());
        }
        
        //start HUI
        //Launch Application    
	//Application.launch(HUI.class);
    }
    
}
