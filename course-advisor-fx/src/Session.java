
import java.util.ArrayList;

/**
 *
 * @author Leon Verhelst
 */
public class Session {
    private Course[][] semesters;
    public int credit_hours = 0;
    
    public Session(int courses_per_semester){
        semesters = new Course[(int)40/courses_per_semester][courses_per_semester];
        
    }
    
    public void setInitialCourses(Course[] initCourses){
        for(Course c : initCourses)
        {
            this.addCourse(c.getSuggested_semester(), c);
            credit_hours += 3;
        }
    }
    

    public Course[] getSetCourses(){
        ArrayList<Course> taken = new ArrayList<Course>();
        for(int i = 0; i < semesters.length; i++){
            for(int j = 0; j < semesters[i].length; j++){
                if(semesters[i][j] != null)
                    taken.add(semesters[i][j]);
            }
        }
        Course[] ret = new Course[taken.size()];
        return taken.toArray(ret);
    }
    
    public void addCourse(int semester, Course course){
        boolean placed = false;
        semester = semester - 1; // 0-index the semester
        while(!placed){
           for(int i = 0; i < semesters[semester].length; i++){
               if(semesters[semester][i] == null){
                   semesters[semester][i] = course;
                   credit_hours += 3;
                   return;
               }
           } 
           semester++; 
        }
    }
    
    public void printSemesters(){
       
         for(int i = 0; i < semesters.length; i++){
             System.out.println("Semester: " + (i + 1));
             for(int j = 0; j < semesters[i].length; j++){
                System.out.println("    " + ((semesters[i][j] != null)? semesters[i][j].getName() : "No Course"));
             }
        }
    }
}
