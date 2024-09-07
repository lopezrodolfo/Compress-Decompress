/* Compress class instantiates a Compressor object that is used to compress the
 * inputted ascii text file into a smaller binary file with an appended .zzz extension. 
 * It also writes a log file with an appended .log extension 
 * that specifies the compressed file name, time it took to compress, the file
 * size before and after, number of table entries, and the number of rehashes.*/
import java.io.*;
import java.util.*;

public class Compress {
    static Scanner sc;
    public static void main(String[] args) {
        sc = new Scanner(System.in);
        program(args[0]); //starts compress program with textfile from the command line
        while (true){ //allows program to be ran multiple times
            System.out.println();
            System.out.println("Do you want to compress another file[y/n]?:");
            if (!sc.nextLine().equalsIgnoreCase("y")){
                break;
            }
            System.out.println("What file would you like to compress:");
            program(sc.nextLine());
        }
        sc.close();
    }

    public static void program(String fileName){
        long time;
        int timesHashed;
        int dictEntries;
        int initialSize;
        int compressedSize;
        PrintWriter output;

        time = System.currentTimeMillis();
        Compressor theCompress = new Compressor(fileName); //file is compressed
        time = System.currentTimeMillis() - time; //calculates time it took to compress the file
        //calls compressor getter methods for log file info
        timesHashed = theCompress.getTimesHashed();
        dictEntries = theCompress.getDictEntries();
        initialSize = theCompress.getOldFileSize();
        compressedSize = theCompress.getNewFileSize();

        //writes to compression log file
        try{
            output = new PrintWriter(new FileOutputStream(fileName + ".log"));
            output.println("Compression for file " + fileName);
            if (initialSize>1000){ //uses kilobytes for bigger sizes
                output.println("Compressed from " + String.valueOf(((int)(initialSize/100))/10.0) + " Kilobytes to " +
                        String.valueOf(((int)(compressedSize/100))/10.0) + " Kilobytes.");
            }
            else{ //uses bytes for smaller sizes
                output.println("Compressed from " + String.valueOf(initialSize) + " bytes to " +
                        String.valueOf(compressedSize) + " bytes.");
            }
            if (time>1000){ //uses seconds for bigger sizes         
                output.println("Compression took " + String.valueOf(((int)(time/100))/10.0) + " seconds.");
            }
            else{ //uses milliseconds for smaller sizes
                output.println("Compression took " + String.valueOf(time) + " milliseconds.");
            }
            output.println("The dictionary contains " + String.valueOf(dictEntries) + " entries.");
            output.println("The table was rehashed " + String.valueOf(timesHashed) + " times.");
            output.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}