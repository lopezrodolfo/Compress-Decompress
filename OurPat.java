/* OurPat objects are used as the key values for each entry in the seperate
 * chain hash table used for file compression.*/
public class OurPat{
    private int code; //attr stores the pattern's repetition value (value)
    private char theChar; //attr stores the ascii character pattern  (key)

    //constructor to set key values, called when we add to to hash table
    public OurPat(int c, char cha){
        code = c;
        theChar = cha;
    }

    //constructor used to initialize key values
    public OurPat(char cha){
        theChar = cha;
        code = -1;
    }

    //computes hash code
    public int hashCode(){
        return (code + 1) * 98 + theChar;
    }

    //getter for code
    public int getCode(){
        return code;
    }

    //getter for char pattern
    public char getChar(){
        return theChar;
    }

    //checks equality of two OurPat objects, called when we have to get an
    //entry stored in a linked list 
    public boolean equals(OurPat other){
        return code == other.getCode() && theChar == other.getChar();
    }

    //string representation of OurPat object
    public String toString(){
        return "(" + String.valueOf(code) + ", " + theChar + ")";
    }
}