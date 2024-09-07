/* The Decompress class instantiates the Decompressor object in order to decompress the inputted binary
 * .zzz file back into its original ascii txt file. It also writes a log file
 * with .zzz.log as the extension specifying the decompression file name,
 * time it took to decompress and the number of rehashes.*/

 import java.io.*;
 import java.util.*;
 
 public class Decompress {
     private static Scanner sc;
     public static void main(String[] args) {
         sc = new Scanner(System.in);
         program(args[0]); //inputted binary file from command line passed to decompress program
         while (true){ //decompress program can be ran multiple times
             System.out.println();
             System.out.println("Do you want to decompress another file[y/n]?:");
             if (!sc.nextLine().equalsIgnoreCase("y")){
                 break;
             }
             System.out.println("What file would you like to decompress:");
             program(sc.nextLine());
         }
         sc.close();
     }
 
     //decompression program
     public static void program(String fileName){
         long time;
         int timesHashed;
         String ending;
         if (fileName.length()>4){ //checks that a valid binary file with the .zzz extension is entered
             ending = fileName.substring(fileName.length()-4);
             if(ending.equals(".zzz")){
                 time = System.currentTimeMillis();
                 timesHashed = new Decompressor(fileName).getRehashes(); //decompresses the binary file and calls method to get number of hashes
                 time = System.currentTimeMillis() - time; //time of decompression is calculated
                 makeLog(fileName, time, timesHashed); //decompression log file is created
             }
             else{
                 System.out.println("Invalid file name");
             }
         }
         else{
             System.out.println("Invalid file name");
         }
     }
 
     //creates decompression log file
     public static void makeLog(String fileName, long time, int timesHashed){
         PrintWriter output;
         try{
             output = new PrintWriter(new FileOutputStream(fileName + ".log"));
             output.println("Decompression for file " + fileName);
             if (time>1000){ //for longer files with a variety of ascii chars output time in seconds           
                 output.println("Decompression took " + String.valueOf(((int)(time/100))/10.0) + " seconds.");
             }
             else{ //for shorter files output time in milliseconds
                 output.println("Decompression took " + String.valueOf(time) + " milliseconds.");
             }
             output.println("The table was doubled " + String.valueOf(timesHashed) + " times.");
             output.close();
         }
         catch(IOException e){
             e.printStackTrace();
         }
     }
 }