
import java.util.ArrayList;

/**
 *
 * @author Leon Verhelst
 */
public class Course {
    //List of Course numbers that are prerequisites of this course
    private ArrayList<Integer> prereqs = new  ArrayList<Integer>();
    //List of Course numbers this Course is a prerequisite of
    private ArrayList<Integer>prereqOf = new  ArrayList<Integer>();
    //Number of the Course
    private final int num;
    //Name of the Course
    private String name;
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
    Course(int number, String cname, ArrayList<Integer> pre, ArrayList<Integer> post){
        this.num = number;
        this.name = cname;
        this.prereqs = pre;
        this.prereqOf = post;
    }
    
    /**
     * Constructor for course with no information of it yet (ie, add a prereq before prereq is defined)
     * @param number 
     */
    Course(int number){
        this.num = number;
    }
    
    /**
     * @param prereqs the prereqs to set
     */
    public void setPrereqs(ArrayList<Integer> prereqs) {
        this.prereqs = prereqs;
    }

    /**
     * @param prereqOf the prereqOf to set
     */
    public void setPrereqOf(ArrayList<Integer> prereqOf) {
        this.prereqOf = prereqOf;
    }
    
    /**
     * Get Prerequsites of the current course
     * @return Current Course's Prerequisites
     */
    public ArrayList<Integer> getPrereqs(){
        return this.prereqs;
    }
    
    /**
     * Get courses this course is the prereq for
     * @return Courses this course opens
     */
    public ArrayList<Integer> getPrereqOf(){
        return this.prereqOf;
    }
    
    public void addPrereqOf(int num){
        if(!this.prereqOf.contains(num))
            this.prereqOf.add(num);
    }
    
    public void addPrereq(int num){
        if(!this.prereqs.contains(num))
            this.prereqs.add(num);
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
    
    public String toString(){
        return this.getNum() + " " + this.getName();
    }
}
