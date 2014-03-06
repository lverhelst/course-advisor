
import java.util.ArrayList;

/**
 * Class to represent the classes which can be selected and used for rules
 * @author Leon Verhelst and Emery Berg
 */
public class Course {
    //List of Course numbers that are prerequisites of this course
    //This stores the course NAME //<degree><number> //EX: CPSC10
    private ArrayList<String> prereqs = new  ArrayList();
    //Key of the course (Subject-num)
    private final String name;
    //Subject of the course
    private final  String subject;
    //Number of the Course
    private final int num;
    //Name of the Course
    private String title;
    //Description of the course
    private String description;
    //number of credits for the course
    private int credits;
    //The suggested minimum semseter for the course to be taken
    private int academic_year;
    //If the course is selected
    public boolean taken;
    public boolean selected;
    
    public String preString;
        
    /**
     * Constructor for course with minimal information, only the course|number is needed
     * for example CPSC100
     * @param courseKey the course name for example CPSC100
     */
    public Course(String courseKey){
        this.name = courseKey;
        this.subject = courseKey.substring(0, 4);
        this.num = Integer.parseInt(courseKey.substring(4));
        this.academic_year = (int)(num/100);
    }
    
    /**
     * Used to get the course name eg CPSC100
     * @return the course name as a string
     */
    public String getName() {
        return name;
    }
    
    /**
     * Used to get the courses subject
     * @return 
     */
    public String getSubject() {
        return subject;
    }
    
    /**
     * Used to set the courses title (Short description)
     * @param title a string of the title of the course (Short description)
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * @return the name
     */
    public String getTitle() {
        return title;
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
    public void setPrereqs(ArrayList<String> prereqs) {
        this.prereqs = prereqs;
    }

    
    /**
     * Get Prerequisites of the current course
     * @return Current Course's Prerequisites
     */
    public ArrayList<String> getPrereqs(){
        return this.prereqs;
    }
    
    public void addPrereq(String prereq){
        if(!this.prereqs.contains(prereq))
            this.prereqs.add(prereq);
    }  

    /**
     * @return the academic_year
     */
    public int getAcademic_Year() {
        return academic_year;
    }

    /**
     * @param academic_Year the academic_year to set
     */
    public void setAcademic_Year(int academic_Year) {
        this.academic_year = academic_Year;
    }    
    
    @Override
    public String toString(){
        return getName()+ ": " + getTitle() + " (" + getCredits() + ")";
    }
}