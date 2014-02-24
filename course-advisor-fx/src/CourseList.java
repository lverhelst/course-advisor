/**
 * @author Leon Verhelst and Emery Berg
 * This class is used to load classes and handle the structure of classes
 * This class acts as the knowledge base of the classes
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CourseList {      
    private HashMap<String, Course> unbccourses;
    
    /**
     * Default constructor
     */
    CourseList(){}
    
    /**
     * Loads the course list from the default file
     * @return true if the loading was successful
     */
    public boolean loadCourseList(){
        HashMap<String, String[]> requirements = new HashMap(); //stores the reqs
        unbccourses = new HashMap();
        BufferedReader br;
        
        try{
            //open file (This text file is the Knowledge Base for the Expert System)
            br = new BufferedReader(new FileReader("courselist.txt"));
            String line = br.readLine();
            
            //Example Line: CPSC100!Computer Programming I!Long Description!0,4!MATH115!
            Course course;
            String[] component;
            String[] subpart;
            
            while(line != null) {
                component = line.split("!");
                course = new Course(component[0]);
                course.setTitle(component[1]);
                course.setDescription(component[2]);
                
                //find the number of credits
                if(component.length > 3) {
                    subpart = component[3].split(",");
                    if(subpart.length > 0) {                    
                        int num = 0;
                        for(String credits: subpart) {
                            num = Integer.parseInt(credits);
                            //ensure course that have a higher limit for some reason are excluded
                            if(num == 3 || num == 4) {
                                break;
                            }
                        }
                        course.setCredits(num);
                    }
                }
                
                //find the requirements and store for later
                if(component.length > 4) {
                    subpart = component[4].split(",");
                    
                    if(subpart[0].length() == 7) //ensure there is actually text
                    {
                        requirements.put(component[0], subpart);
                        for(String s : subpart)
                            course.addPrereq(s);
                    }
                }
                
                unbccourses.put(component[0], course);
                if(component[0].startsWith("CPSC")){
                    System.out.println(line);
                }
                
                
                line = br.readLine();
            }
          /*
            //add all the requirements to the courses            
            for(String key :requirements.keySet()) {
                course = unbccourses.get(key);
                
                //find and add the requiements for the current course
                for(String reqkey: requirements.get(key)) {
                    if(unbccourses.containsKey(reqkey)) {
                        course.addPrereq(unbccourses.get(reqkey).getName());
                    } else { //if course is missing fill in the blank
                        reqkey = reqkey.replace("XX", "0");
                        course = new Course(reqkey);
                        unbccourses.put(reqkey, course);
                        course.addPrereq(unbccourses.get(reqkey).getName());
                    }
                }
            }
           */
            
            //close connection  
            br.close();
        }catch(IOException | NumberFormatException e){
            System.err.println(e.toString());
            return false;
        }
        return true;
    }
    
    /**
     * Used to retrieve a course from the master list
     * @param course the course name eg CPSC100
     * @return the course object
     */
    public Course get(String course){
        return unbccourses.get(course);
    }
    
    /**
     * Used to retrieve courses from the master list
     * @param courses the courses to get based on name eg CPSC100
     * @return the list of course objects
     */
    public Course[] get(String ... courses){
        Course[] result = new Course[courses.length];
        
        for(int i = 0; i < courses.length; ++i) {
            result[i] = unbccourses.get(courses[i]);
        }
        
        return result;
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
}
