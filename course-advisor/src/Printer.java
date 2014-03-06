import java.io.PrintWriter;

/**
 * @author Emery
 */
public class Printer {
    private PrintWriter printWriter;
    private boolean verbose;
    private boolean error;
    private boolean printFile;
    
    /**
     * Used to set the output device
     * @param printWriter the output device 
    */
    public Printer(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    /**
     * Used to turn on/off verbose mode
     * @param verbose true for on
     */
    public void verbose(boolean verbose) {
        this.verbose = verbose;
    }
        
    /**
     * Used to turn on/off print to file
     * @param printFile true if print to file
     */
    public void printToFile(boolean printFile) {
        this.printFile = printFile;
    }
    
    /**
     * Used to check if an error message was printed
     * @return 
     */
    public boolean error() {
        return error;
    }
    
    /**
     * Used to print messages to the console or file if set to
     * @param line the string to print
     */
    public void print(String line) {
        if(verbose) {  
            System.out.println(line);
            if(printFile)
              printWriter.print(line + "\r\n");
        }
    }
    
    /**
     * Used to print error messages to the console or file if set to
     * @param line the line to print
     */
    public void printError(String line) {
        error = true;
        System.out.println("\u001B[31m" + line + "\u001B[0m");
        if(printFile)
          printWriter.print(line + "\r\n");
    }
}