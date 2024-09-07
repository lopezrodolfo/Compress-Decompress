/*The Decompressor class reads from the binary file into the perfect hash
 * table. The code is the key and the pattern with its prefix is the value.
 *It generates the original ASCII file with its contents unchanged.*/
import java.io.*;

public class Decompressor {
    private ObjectInputStream reader;
    private int[] theCode; //array of the code or index values
    private OurPat[] codesTable; //array of our pattern with the repetition prefixed
    private int tableSize = 0;
    private int timesRehashed = 0;
    private static final int MINSIZE = 98; //represents all ascii chars 

    public Decompressor(String fileName){
        getTheCode(fileName); //initializes array of code values read in from the binary file
        initializeTable(); //initializes hash table with ascii chars
        PrintWriter output;

        //writes the original text file 
        try{
            output = new PrintWriter(new FileOutputStream(fileName));
            for(int i= 0; i < theCode.length-1; i++){ //iterates over numbers in binary file
                if(theCode[i+1]<tableSize){
                    char firstChar = getFirstChar(codesTable[theCode[i+1]]); //gets the first character of the next entry in the code
                    add(new OurPat(theCode[i], firstChar));
                }
                else{
                    add(new OurPat(theCode[i], getFirstChar(codesTable[theCode[i]]))); //if we do not know the next entry yet, use the current entry
                }
                output.print(printEntry(codesTable[theCode[i]], "")); //writes original file contents
            }
            add(new OurPat(theCode[theCode.length-1], getFirstChar(codesTable[theCode[theCode.length-1]])));
            output.print(printEntry(codesTable[theCode[theCode.length-1]], "")); //the last bit of the original file contents
            output.close();
        }
        catch (IOException e){
            System.out.println("Error Saving File");
        }
    }

    //turns the binary file into an integer array 
    public void getTheCode(String fileName){
        int i = 0;

        byte[] theBytes = new byte[256-MINSIZE];
        short[] theShorts = new short[0];
        int[] theInts = new int[0];
        try{
            //reads from binary file
            try{
                reader = new ObjectInputStream(new FileInputStream(fileName));
                for( ; i<256 - MINSIZE;i++){
                    theBytes[i] = reader.readByte();
                }
                theShorts = new short[65536-theBytes.length-MINSIZE];
                for( ; i<65536 - MINSIZE; i++){
                    theShorts[i-theBytes.length] =reader.readShort();
                }
                theInts = new int[100000];
                while(true){
                    if (i>=theInts.length-theShorts.length-theBytes.length-MINSIZE){
                        int[] newTheInts = new int[theInts.length*2];
                        for (int j = 0; j < theInts.length; j++){
                            newTheInts[j] = theInts[j];
                        }
                        theInts = newTheInts;
                    }
                    theInts[i-theShorts.length-theBytes.length] = reader.readInt();
                }
            }
            //binary file fully read
            catch(EOFException e){ //merges all bytes and shorts to integers in the array
                theCode = new int[i]; //i incremented until the exception was thrown
                int add;
                if(i<256 - MINSIZE){
                    for(int w = 0; w<i ;w++){
                        add = theBytes[w];
                        if (add < 0){
                            add += 256; //maintain positive index values
                        }
                        theCode[w] = add;
                    }
                }
                else{
                    for(int w = 0; w<256-MINSIZE ;w++){
                        add = theBytes[w];
                        if (add < 0){
                            add += 256;
                        }
                        theCode[w] = add;
                    }
                    if(i<65536-theBytes.length-MINSIZE){
                        for(int w = 256-MINSIZE; w<i ;w++){
                            add = theShorts[w-(256-MINSIZE)];
                            if (add < 0){
                                add += 65536;
                            }
                            theCode[w] = add;
                        }
                    }
                    else{
                        for(int w = 256-MINSIZE; w<65536-theBytes.length-MINSIZE ;w++){
                            add = theShorts[w-(256-MINSIZE)];
                            if (add < 0){
                                add += 65536;
                            }
                            theCode[w] = add;
                        }
                        for(int w = 65536-theBytes.length-MINSIZE;w<i;w++){
                            theCode[w] = theInts[w-(65536-theShorts.length-theBytes.length-MINSIZE)];
                        }
                    }
                }
            }
            reader.close();
        }
        catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    //gets original prefix value of an entry, helper method
    public char getFirstChar(OurPat entry){
        if (entry.getCode() != -1){
            return getFirstChar(codesTable[entry.getCode()]);
        }
        return entry.getChar();
    }

    //adds element to the hash table (the codesTable)
    public void add(OurPat addMe){
        if(tableSize >= codesTable.length){ //hash table capacity reached 
            timesRehashed ++; //rehash counted
            OurPat[] temp = new OurPat[codesTable.length*2]; //hash table size doubled
            for (int i = 0; i<codesTable.length;i++){
                temp[i] = codesTable[i]; //move entries
            }
            codesTable = temp; //reassign table
        }
        codesTable[tableSize] = addMe; //added to hash table
        tableSize++; //number of entries updated 
    }

    //initializes hash table (the codesTable) with ascii chars prefixed with -1 (key: index,
    //value: pattern with prefix) 
    public void initializeTable(){
        codesTable = new OurPat[(int)(MINSIZE*1.5 + theCode.length/2)];
        add(new OurPat("\t".charAt(0)));
        add(new OurPat("\n".charAt(0)));
        add(new OurPat("\r".charAt(0)));
        for(int i = 32; i<127;i++){
            add(new OurPat((char)i));
        }
    }

    public String printEntry(OurPat entry, String print){
        if (entry.getCode() != -1){
            return printEntry(codesTable[entry.getCode()], entry.getChar()+print);
        }
        return entry.getChar() + print;
    }

    //getter for amount of rehashes
    public int getRehashes(){
        return timesRehashed;
    }
}