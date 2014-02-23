
import java.util.ArrayList;

/**
 * Class to represent the classes which can be selected and used for rules
 * @author Leon Verhelst and Emery Berg
 */
public class Course {
    //List of Course numbers that are prerequisites of this course
    private ArrayList<Course> prereqs = new  ArrayList();
    //Key of the course (Subject-num)
    private String courseKey;
    //Subject of the course
    private String subject;
    //Name of the Course
    private String name;
    //Description of the course
    private String description;
    //Number of the Course
    private final int num;
    //number of credits for the course
    private int credits;
    //The suggested minimum semseter for the course to be taken
    private int suggested_semester;
    //If the course is selected
    public boolean taken;
    public boolean selected;
        
    /**
     * Constructor for course with minimal information, only the course|number is needed
     * for example CPSC100
     * @param courseKey the course name for example CPSC100
     */
    public Course(String courseKey){
        this.courseKey = courseKey;
        this.subject = courseKey.substring(0, 4);
        this.num = Integer.parseInt(courseKey.substring(4));
        this.suggested_semester = (int)(num/100);
    }
    
    /**
     * Used to get the course name eg CPSC100
     * @return the course name as a string
     */
    public String getKey() {
        return courseKey;
    }
    
    /**
     * Used to set the courses name (Short description)
     * @param name a string of the name of the course (Short description)
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }  

    /**
     * @return the num
     */
    public int getNum() {
        return num;
    }
    
    /**
     * Used to set the number of credits for the course
     * @param credits the number of credits
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    /**
     * @return the number of credits
     */
    public int getCredits() {
        return credits;
    }
    
    /**
     * Used to set the description of the course
     * @param desc 
     */
    public void setDescription(String desc) {
        this.description = desc;
    }
    
    /**
     * Used to get the current course description
     * @return a string of the current description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @param prereqs the prereqs to set
     */
    public void setPrereqs(ArrayList<Course> prereqs) {
        this.prereqs = prereqs;
    }

    
    /**
     * Get Prerequisites of the current course
     * @return Current Course's Prerequisites
     */
    public ArrayList<Course> getPrereqs(){
        return this.prereqs;
    }
    
    public void addPrereq(Course prereq){
        if(!this.prereqs.contains(prereq))
            this.prereqs.add(prereq);
    }  
    
        

    /**
     * @return the suggested_semester
     */
    public int getSuggested_semester() {
        return suggested_semester;
    }

    /**
     * @param suggested_semester the suggested_semester to set
     */
    public void setSuggested_semester(int suggested_semester) {
        this.suggested_semester = suggested_semester;
    }
    
    @Override
    public String toString(){
        return this.getNum() + " " + this.getName();
    }
}