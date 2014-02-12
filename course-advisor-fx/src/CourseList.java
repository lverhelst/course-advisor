/**
 * @author Leon Verhelst
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
    
    CourseList(){
        
    }
    
    /**
     * Loads course list from file
     */
    public boolean loadCourseList(){
        unbccourses = new HashMap<Integer, Course>();
        BufferedReader br;
        try{
            //open file (This text file is the Knowledge Base for the Expert System)
            br = new BufferedReader(new FileReader("courses.txt"));
            //read file
            String line = br.readLine();
            Course current;
            //Line: Course Number|Name|Prereqs|Opens|Suggested Semester
            //Example Line: 100|Introduction to CPSC||101,244,346|1
            while(line != null){
                System.out.println(line);
                String[] components = line.split("!");
                int coursenum = Integer.parseInt(components[0]);
                current = new Course(coursenum);
                current.setName(components[1]);
                if(!components[2].equals(""))
                    for(String prereqnum : components[2].split(",")){
                        int cnum = Integer.parseInt(prereqnum);
                        //Check if the course exits in the hash map
                        if(unbccourses.containsKey(cnum)){
                            //add current course to other courses prereq
                            unbccourses.get(cnum).addPrereqOf(cnum);

                        }else{
                            Course prereq = new Course(cnum);
                            prereq.addPrereqOf(cnum);
                            unbccourses.put(cnum, new Course(cnum));
                        }
                        //add prereq to current course's prereq list
                        current.addPrereq(cnum);
                    }
                //Deal with courses that have this course as a prereq
                if(!components[3].equals(""))
                    for(String allowednum : components[3].split(",")){
                        int cnum = Integer.parseInt(allowednum);
                        //Check if the course exits in the hash map
                        if(unbccourses.containsKey(cnum)){
                            //add current course to other courses prereq
                            unbccourses.get(cnum).addPrereqOf(cnum);

                        }else{
                            Course prereq = new Course(cnum);
                            prereq.addPrereq(cnum);

                            unbccourses.put(cnum, new Course(cnum));
                        }
                        //add prereq to current course's prereq list
                        current.addPrereqOf(cnum);
                    }
                current.setSuggested_semester(Integer.parseInt(components[4]));
                //add to list (map)
                unbccourses.put(coursenum, current);
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
    
    public void selectCourses(int[] courses){
        for(int coursenum : courses){
            if(!unbccourses.containsKey(coursenum)){
                System.err.print("ERROR: Missing COURSE -> CPSC" + coursenum);
            }else{
                unbccourses.get(coursenum).selected = true;
            }
        }
    }

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
     * @param coursenum 
     * @return 
     */
    public boolean checkPrereqs(int coursenum){
        boolean cantake = true;
        if(!unbccourses.containsKey(coursenum)){
                System.err.print("ERROR: Missing COURSE -> CPSC" + coursenum);
                return false;
        }else{
            Course toCheck = unbccourses.get(coursenum);
            for(int num : toCheck.getPrereqs()){
                if(!unbccourses.containsKey(num)){
                    System.err.print("ERROR: Missing COURSE -> CPSC" + coursenum);
                    cantake &= false;
                }else{
                    cantake &= (unbccourses.get(num).taken || unbccourses.get(num).selected);
                }
            }
        }
        return cantake;
    }
    
    public Course[] getCourses(){
        return this.unbccourses.values().toArray(new Course[unbccourses.values().size()]);
    }
    
    public Course[] getTakenCourses(){
        ArrayList<Course> ctaken = new ArrayList<Course>();
        for(Course c : unbccourses.values()){
            if(c.taken)
                ctaken.add(c);
        }
        return ctaken.toArray(new Course[ctaken.size()]);
    }
    
    public Course[] getSelectedCourses(){
        ArrayList<Course> cselected = new ArrayList<Course>();
        for(Course c : unbccourses.values()){
            if(c.selected)
                cselected.add(c);
        }
        return cselected.toArray(new Course[cselected.size()]);
    }
    //Gets a list of courses that have not been selected, nor taken, but whose prereqs are satisfied
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
