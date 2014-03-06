import java.io.PrintWriter;

/**
 * @author Emery
 */
public class Printer {
    private static PrintWriter printWriter;
    private static boolean verbose;
    private static boolean error;
    private static boolean printFile;
    
    /**
     * Used to set the output device
     * @param printWriter the output device 
    */    
    public static void setTrace(PrintWriter printWriter) {
        Printer.printWriter = printWriter;
        printFile = true;
    }

    /**
     * Used to turn on/off verbose mode
     * @param verbose true for on
     */
    public static void verbose(boolean verbose) {
        Printer.verbose = verbose;
    }
         
    /**
     * Used to check if an error message was printed
     * @return 
     */
    public static boolean error() {
        return error;
    }
    
    /**
     * Used to print messages to the console or file if set to
     * @param line the string to print
     */
    public static void print(String line) {
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
    public static void printError(String line) {
        error = true;
        System.out.println("\u001B[31m" + line + "\u001B[0m");
        if(printFile)
          printWriter.print(line + "\r\n");
    }
}