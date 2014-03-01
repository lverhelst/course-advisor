
import java.util.ArrayList;

/**
 * Session the current users courses by semester
 * @author Leon Verhelst and Emery Berg
 */
public class Session {
    private final Course[][] semesters;
    public int credit_hours = 0;
    
    /**
     * Constructor which creates the session based on the number of courses the 
     * user wants to take each semester
     * @param courses_per_semester 
     */
    public Session(int courses_per_semester){
        semesters = new Course[(int)(Math.ceil(40f/courses_per_semester))][courses_per_semester];        
    }
    
    /**
     * Sets the courses which the user has already taken
     * @param initCourses an array of courses already taken by the user
     */
    public void setInitialCourses(Course[] initCourses){
        int courses = semesters[0].length;
        
        for(int i = 0; i < initCourses.length; ++i) {
                System.out.println("ADDING INITIAL COURSE: " +  initCourses[i] + " to: "+ (int)i/courses + "," +  i % courses);
                semesters[(i/courses)][ i % courses] = initCourses[i];  
                this.credit_hours += initCourses[i].getCredits();
        }        
    }
    
    /**
     * Used to get the courses which have been added to the session
     * @return an array of the courses added to the session
     */
    public Course[] getSetCourses(){
        ArrayList<Course> taken = new ArrayList();
        for(Course[] courses: semesters) {
            for(Course course: courses) {
                if(course != null)
                    taken.add(course);
            }
        }
        Course[] ret = new Course[taken.size()];
        return taken.toArray(ret);
    }
    
    /**
     * Used to add a course to the session
     * @param year the suggested academic year for the course
     * @param course the course itself
     */
    public boolean addCourse(int year, Course course){
        boolean placed = false;
        int semester = (year - 1) * 2; // 0-index the semester, set to years
       // System.out.println(course.getName());
        while(!placed){
           if(semester >= semesters.length)
               break;
           
           for(int i = 0; i < semesters[semester].length; i++){
                if(semesters[semester][i] == null){
                   semesters[semester][i] = course;
                   credit_hours += course.getCredits();
                   System.out.println(course.getName() + " placed successfully!");
                   return true;
               }else{
                   //if a prerequisite of this course is in the current semester, break (do not add)
                   if(course.getPrereqs().contains(semesters[semester][i].getName()))
                   {
                      // System.out.println("INCREMENT SEMESTER");
                       break;
                   }
               }
           } 
           semester++; 
        }
        System.out.println(course.getName() + " could not be placed!");
        return placed;
    }
    
    /**
     * Used to get the current number of credits
     * @return the current number of credits
     */
    public int getCurrentCredits() {
        return credit_hours;
    }
    
    /**
     * Print friendly version of the semester, showing the current state of the
     * session
     * @return the current session state as a string 
     */
    public String printSemesters(){
        String results = "";
        for(int i = 0; i < semesters.length; i++){
            results += "Semester: " + (i + 1) + "\r\n";
            for(Course course: semesters[i]) {
                results +=  "    " + ((course != null) ? course.toString() : "No Course") + "\r\n";
            }
        }
        return results;
    }
}
