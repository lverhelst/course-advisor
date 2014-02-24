/**
 *
 * @author Leon Verhelst
 * The facts class represents a fact that can be a premise or an action of a rule
 * 
 * Examples:
 *  ch:90  // This fact requires that the student has 90 or more credithours
 *  cs:CPSC101 // This fact requires that the student has taken the CPSC101 course
 */
public class Fact {
    private String type;
    private String value;

    
    Fact(String type, String val){
        this.type = type;
        this.value = val;
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString(){
        return this.type + ":" + this.value;
    }
}
