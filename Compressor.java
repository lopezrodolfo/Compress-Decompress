/* The compressor takes in the file name and reads the file then creates a binary file that          
encodes the text file into binary the binary file has the ending of .zzz*/

import java.io.*;

public class Compressor {
    private SeperateChain table;  //declares seperate chain table
    private OurPat[] codesTable; //holds patterns with repetitions value prefixed
    private int[] theCode; //holds index values or codes that will be written to the binary file
    private int size; //table capacity
    private int fill; //number of entries
    private char[] fileText; //char array holding text file contents
    private int newFileSize;
    private int codeLength;
    private int timesHashed = 0;
    private static final int MINSIZE = 98; //represents the initial ascii chars

    public Compressor(String fileName){
        fileText = getFileText(fileName).toCharArray(); //file string converted to char array (1 char = 1 byte)
        size = getNextPrime((int)(MINSIZE*1.5 + fileText.length/2)); //computes initial table size based on file size (always prime) 
        table = new SeperateChain(size); //initializes seperate chain hash table with size
        codesTable = new OurPat[size]; //initializes pattern array
        theCode = new int[size - MINSIZE]; //initializes codes array with size equal to the number of adds
        initializeTable(); //hash table initialized with ascii values

        //compression algorithm
        int base = -1; //repetition value initially is -1
        int p; //current prefix
        codeLength = 0;
        OurPat curPat;
        for (int i = 0; i < fileText.length; i++){ //iterate over inputted file
            curPat = new OurPat(base, fileText[i]);
            p = table.get(curPat);
            if (p == -1){ //pattern not found in hashtable
                addCode(curPat, codeLength + MINSIZE); //entry added to table
                theCode[codeLength] = base; //codes array updated  
                codeLength++;
                base = -1; //repetition value initialized
                i--; //prefix remains the same
            }
            else{ //pattern found in hashtable
                base = p; //repetition value updated
                if(i == fileText.length-1) {
                    theCode[codeLength] = base;
                    codeLength++;
                }
            }
        }
        //writes to codes array to binary file
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName +".zzz"));
            if (codeLength >= 256 - MINSIZE){ //for big and medium files sizes first writes with bytes
                for(int i = 0; i<256 - MINSIZE; i++){
                    output.writeByte(theCode[i]);
                }
                newFileSize += 256 - MINSIZE;
                if (codeLength >= 65536 - MINSIZE){ //big file then writes with shorts
                    for(int i = 256-MINSIZE; i<65536 - MINSIZE; i++){
                        output.writeShort(theCode[i]);
                    }
                    newFileSize += 2*(65536-256);
                    for(int i = 65536-MINSIZE; i<codeLength; i++){
                        output.writeInt(theCode[i]); //big file then writes with ints
                    }
                    newFileSize += 4*(codeLength-65536+MINSIZE);
                }
                else{ //medium files then writes with shorts
                    for(int i = 256-MINSIZE; i<codeLength; i++){
                        output.writeShort(theCode[i]);
                    }
                    newFileSize += 2*(codeLength-(256 - MINSIZE));
                }
            }
            else{ //small files just write bytes
                for(int i = 0; i<codeLength; i++){
                    output.writeByte(theCode[i]);
                }
                newFileSize += codeLength;
            }
            output.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //returns closest prime to the inputted value
    public int getNextPrime(int length){
        if (length % 2 == 0){ //ignores even nums
            length ++;
        }
        for (int i = 3; i <= (int)Math.sqrt(length); i+=2){
            if(length % i == 0){
                return getNextPrime(length + 2); //recursively calls next odd number
            }
        }
        return length;
    }

    //reads the inputted file and returns it as a string 
    public String getFileText(String fileName){
        String fileText = "";
        int currentChar;
        try{
            BufferedReader readFile = new BufferedReader(new FileReader(fileName));
            while ((currentChar = readFile.read()) != -1){
                fileText += (char)currentChar;
            }
            readFile.close();
        }
        catch(IOException e){
            System.out.println("File not found.");
        }
        return fileText;
    }

    //called when entry is added to table
    public void addCode(OurPat key , int v){
        fill++; //entry added
        if (fill*1.5 > size){ //if capacity is reached... time to rehash
            timesHashed += 1;
            size = getNextPrime(size*2+1); //new size computed
            SeperateChain newChain = new SeperateChain(size); //new seperate chain table is created
            table.addAll(newChain); //adds old table linkedlists with entries to new table
            table = newChain; //new table is assigned 
        }
        table.add(key, v); //entry added to table
        if (v >= codesTable.length){ //if capacity is reached, pattern and code array need updating 
            OurPat[] newCodes = new OurPat[codesTable.length*2]; //new size
            int[] newTheCode = new int[newCodes.length-MINSIZE]; //new size
            for (int i = 0; i < codesTable.length; i++){
                newCodes[i] = codesTable[i]; //updates the pattern array
            }
            for (int i = 0; i < theCode.length; i++){
                newTheCode[i] = theCode[i]; //updates codes array used for binary file 
            }
            theCode = newTheCode; //code array reassigned
            codesTable = newCodes; //pattern array reassigned
        }
        codesTable[v] = key; //adds new pattern to pattern array
    }

    //initializes hashtable with ascii values
    public void initializeTable(){
        OurPat addMe = new OurPat("\t".charAt(0));
        addCode(addMe, 0);
        addMe = new OurPat("\n".charAt(0));
        addCode(addMe, 1);
        addMe = new OurPat("\r".charAt(0));
        addCode(addMe, 2);
        for(int i = 32; i<127;i++){
            addCode(new OurPat((char)i),i-29);
        }
    }

    //getter for new file size
    public int getNewFileSize(){
        return newFileSize;
    }

    //getter for initial file size
    public int getOldFileSize(){
        return fileText.length;
    }

    //getter for number of table entries
    public int getDictEntries(){
        return codeLength;
    }

    //getter for times hashed
    public int getTimesHashed(){
        return timesHashed;
    }
}