
import javafx.application.Application;

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
            Session sesh = new Session(5);
            Course[] initialCourses = {cl.get(100), cl.get(141)};
            sesh.setInitialCourses(initialCourses);
                        
            InferenceEngine ie = new InferenceEngine(sesh, cl);
            ie.infer();
        }
        
        //start HUI
        //Launch Application    
	//Application.launch(HUI.class);
    }
    
}
