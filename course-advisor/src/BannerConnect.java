
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class used to pull course information from the banner site
 * @author Emery
 */
public class BannerConnect {
    private String url = "https://pg-adm-formslb-01.unbc.ca";
//    private String url = "https://pg-adm-formslb-01.unbc.ca/banprod/bwckctlg";
//    private ArrayList<String> list;
    
    public BannerConnect(){
//        list = new ArrayList();
    }
    
    public void update() {  
        URL oracle = null;
        File file = new File("courselist.html");
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream("list.txt")));
            
            String inputLine = in.readLine();
            String[] lines;
            
            while (inputLine != null) {
                lines = inputLine.split(" ");
                
                if(lines.length > 2) {
                    if(lines[1].equals("CLASS=\"nttitle\"")) {
                        String address = lines[4].split("\"")[1].replace("amp;", "");
                        writer.println(pullHTML(address));
                    }      
                }                
                
                inputLine = in.readLine();
            }
            
            in.close();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(BannerConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Communication code not working due to generation of page
            
//        //get connection to website
//        try {
//            oracle = new URL(url);
//        } catch (MalformedURLException e) {
//            System.err.println("Failed to open URL " + e.toString());
//            return;
//        }
//
//        
//        //pull content from site
//        try {
//            URLConnection test = oracle.openConnection();
//            test.setDoOutput(true);
//            test.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
//            test.setRequestProperty("Accept","*/*");
//            
//            OutputStreamWriter out = new OutputStreamWriter(
//                                         test.getOutputStream());
//            out.write(".p_display_courses");
//            out.close();
//        
//            BufferedReader in = new BufferedReader(
//            new InputStreamReader(test.getInputStream()));
//
//            String inputLine;
//            while ((inputLine = in.readLine()) != null)
//                System.out.println(inputLine);
//        in.close();
//        } catch (IOException e) {
//            System.err.println("Failed to retrieve course list " + e.toString());
//        }
    }
    
    public String pullHTML(String address) {
        String output = "";
        
        URL banner = null;
        try {
            banner = new URL(url + address);
        } catch (MalformedURLException e) {
            System.err.println("Failed to open URL " + e.toString());
            return null;
        }

        String inputLine = null;
        String[] lines;
        //pull content from site
        try {
            URLConnection test = banner.openConnection();
            test.setDoOutput(true);
            test.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            test.setRequestProperty("Accept","*/*");
                   
            BufferedReader in = new BufferedReader(
            new InputStreamReader(test.getInputStream()));
            
            while ((inputLine = in.readLine()) != null) { 
                
                if(inputLine.length() > 21) {
                    int first = inputLine.indexOf('"') + 1;
                    
                    if(first > 0) {
                        String temp = inputLine.substring(first, inputLine.indexOf('"', first + 1));   
                        switch(temp) {
                            case "nttitle":
                                temp = inputLine.substring(inputLine.indexOf('>') + 1, inputLine.indexOf('<',20));
                                output += temp.substring(0, 4) + temp.substring(5, 8) + "!" + temp.substring(11) + "!";   
                                break;
                            case "ntdefault":
                                temp = in.readLine();
                                
                                if(temp.charAt(0) != '<')
                                    output += temp.replaceAll("\\<.*?>","") + "!";
                                break;
                            case "fieldlabeltext":
                                temp = inputLine.substring(inputLine.indexOf('>') + 1, inputLine.indexOf('<',20));
                                switch(temp) {
                                    case "Credits:":
                                        temp = inputLine.substring(inputLine.lastIndexOf('>') + 1);
                                        lines = temp.split("\\s+");
                                        if(lines.length >= 3) {                                                                  
                                            output += lines[1].charAt(0) + "," + lines[3].charAt(0) + "!"; 
                                        } else {                                            
                                            output += lines[1].charAt(0) + "!"; 
                                        }
                                        break;
                                    case "Prerequisites: ":
                                        in.readLine();
                                        temp = in.readLine().replace("&amp", "");
                                        
                                        for(String list: temp.split("/A>")) {
                                            for(String blah: list.split(";"))
                                                if(blah.contains("one_subj")) { 
                                                    output += blah.substring(9);
                                                } else if(blah.contains("sel_crse_strt")) {                                                
                                                    output += blah.substring(14);
                                                } else if(blah.contains("<A HREF")) {
                                                    if(blah.contains("or"))
                                                        output += "OR";
                                                    else if(blah.contains("and"))
                                                        output += "AND";
                                                }  
                                        }
                                        break;
                                }
                                break;
                        }
                    }
                }
            }
        in.close();
        } catch (IOException e) {
            System.err.println("Failed to retrieve course list " + e.toString());
        }
        
        return output;
    }
}
