
import java.util.ArrayList;

/**
 * Class to represent the classes which can be selected and used for rules
 * @author Leon Verhelst and Emery Berg
 */
public class Course {
    //List of Course numbers that are prerequisites of this course
    private ArrayList<Course> prereqs = new  ArrayList();
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
     * Default constructor for a Course
     * @param number The name of the course (ex
     * @param shortDescription The title of the course (ex Artificial Intelligence)
     * @param pre Prerequisite courses
     * @param post Courses that this course is a prerequisite for
     */
    public Course(int number, String cname, ArrayList<Course> pre){
        this.num = number;
        this.name = cname;
        this.prereqs = pre;
    }
    
    /**
     * Constructor for course with no information of it yet (ie, add a prereq before prereq is defined)
     * @param number 
     */
    public Course(int number){
        this.num = number;
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
     * Set course name
     * @param nam the name of the course 
     */    
    public void setName(String nam){
        this.name = nam;
    }

    /**
     * @return the num
     */
    public int getNum() {
        return num;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
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