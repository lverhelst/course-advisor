/**
 * @author Leon Verhelst and Emery Berg
 * This class is used to load classes and handle the structure of classes
 * Used as the Inference Engine for the Expert System
 * The Rules are the relationships stored in the Courses Class
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CourseList {      
    private HashMap<Integer, Course> unbccourses;
    
    /**
     * Default constructor
     */
    CourseList(){}
    
    /**
     * Loads the course list from the default file
     * @return true if the loading was successful
     */
    public boolean loadCourseList(){
        unbccourses = new HashMap();
        BufferedReader br;
        try{
            //open file (This text file is the Knowledge Base for the Expert System)
            br = new BufferedReader(new FileReader("courses.txt"));
            String line = br.readLine();
            
            //Line: Course Number|Name|Prereqs|Opens|Suggested Semester
            //Example Line: 100|Introduction to CPSC||101,244,346|1
            Course current;
            String[] components;
            String[] requirments;
            int courseNum;
            
            //Parse the file into individual courses
            while(line != null) {
                components = line.split("!");
                courseNum = Integer.parseInt(components[0]);
                
                current = new Course(courseNum);
                current.setName(components[1]);
                
                //parse the course requirements
                if(!components[2].equals("")) {
                    for(String prereq: components[2].split(",")) {
                        if(prereq.matches("[1-9]*")) {
                            int reqNum = Integer.parseInt(prereq);                            
                            current.addPrereq(unbccourses.get(reqNum));
                        } 
                    }
                }
                current.setSuggested_semester(Integer.parseInt(components[4]));
                
                //add to list (map)
                unbccourses.put(courseNum, current);
                line = br.readLine();
            }
            
            //close connection  
            br.close();
        }catch(IOException | NumberFormatException e){
            System.out.println(e.toString());
            return false;
        }
        return true;
    }
    
    /**
     * Used to retrieve a course from the master list
     * @param course_num the course number
     * @return the course object
     */
    public Course get(int course_num){
        return unbccourses.get(course_num);
    }
    
    /**
     * Used to set a course to selected state
     * @param courses the course numbers to set as selected
     */
    public void selectCourses(int[] courses){
        for(int coursenum : courses){
            if(!unbccourses.containsKey(coursenum)){
                System.err.print("ERROR: Missing COURSE -> CPSC" + coursenum);
            }else{
                unbccourses.get(coursenum).selected = true;
            }
        }
    }

    /**
     * Used to set course as taken
     * @param courses the course to set as taken
     */
    public void setCoursesTaken(int[] courses){
        for(int coursenum : courses){
            if(!unbccourses.containsKey(coursenum)){
                System.err.print("ERROR: Missing COURSE -> CPSC" + coursenum);
            }else{
                unbccourses.get(coursenum).taken = true;
            }
        }
    }
    
    /**
     * Checks to see if the given course can be taken given the current state of the course list
     * @param coursenum the number of the course to be checked
     * @return true if the course can be taken
     */
    public boolean checkPrereqs(int coursenum){
        boolean cantake = true;
        if(!unbccourses.containsKey(coursenum)){
                System.err.print("ERROR: Missing COURSE -> CPSC" + coursenum);
                return false;
        }else{
            Course toCheck = unbccourses.get(coursenum);
            for(Course course : toCheck.getPrereqs()){
                if(!unbccourses.containsKey(course.getNum())){
                    System.err.print("ERROR: Missing COURSE -> CPSC" + coursenum);
                    cantake &= false;
                }else{
                    cantake &= (unbccourses.get(course.getNum()).taken || unbccourses.get(course.getNum()).selected);
                }
            }
        }
        return cantake;
    }
    
    /**
     * Used to get an array of all the course which can be taken
     * @return an array of the all the courses
     */
    public Course[] getCourses(){
        return this.unbccourses.values().toArray(new Course[unbccourses.values().size()]);
    }
    
    /**
     * Used to get an array of courses which have been set to taken
     * @return an array of courses which have been taken
     */
    public Course[] getTakenCourses(){
        ArrayList<Course> ctaken = new ArrayList<Course>();
        for(Course c : unbccourses.values()){
            if(c.taken)
                ctaken.add(c);
        }
        return ctaken.toArray(new Course[ctaken.size()]);
    }
    
    /**
     * Used to get a array of the courses which have been selected
     * @return an array of the course which are selected
     */
    public Course[] getSelectedCourses(){
        ArrayList<Course> cselected = new ArrayList<Course>();
        for(Course c : unbccourses.values()){
            if(c.selected)
                cselected.add(c);
        }
        return cselected.toArray(new Course[cselected.size()]);
    }
    
    /**
     * Get an array of course which are selected based on the pre-requirements 
     * of courses selected
     * @return the array of courses which can be selected
     */
    public Course[] getSelectableCourses(){
        ArrayList<Course> cselectable = new ArrayList<Course>();
        for(Course c : unbccourses.values()){
            if(this.checkPrereqs(c.getNum()) && !(c.selected) && !(c.taken)){
                cselectable.add(c);
            }
        }
        return cselectable.toArray(new Course[cselectable.size()]);
    }
}
