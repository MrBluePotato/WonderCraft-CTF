
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jacob
 */
public class FindNullStats {
    public static void main(String[] args)
    {
        File d = new File("./players");
        for(String filename : d.list())
        {
            try {
                String contents = readFileAsString("./players/"+filename);
                if(!contents.contains("wins") || !contents.contains("captures") || !contents.contains("mines")) {
                    System.out.println(filename);
                }
            } catch (IOException ex) {
                System.err.println("Error reading "+filename);
            }
        }
    }
    public static String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            fileData.append(buf, 0, numRead);
        }
        reader.close();
        return fileData.toString();
    }
}
