/* SeperateChain class defines the hash table used for file compression. The
 * underlying implementation is an array of linkedlists (hence the name
 * SeperateChain) */
import java.util.*;

public class SeperateChain {
    public static class Entry<K, V>{ //attr declares subclass defining the entries stored in the linkedlists (cannot access other private members declare static)
        private K ourPat; //defines generic key for an entry in the linkedlist
        private V index; //defines generic value for an entry in the linkedlist

        //entry constructor
        public Entry(K k, V v){
            ourPat = k;
            index = v;
        }

        //getter for pattern key
        public K getOurPat(){
            return ourPat;
        }

        //getter for index value
        public V getIndex(){
            return index;
        }
    }

    private LinkedList<Entry<OurPat, Integer>>[] ourTable; //attr declares an array of linkedlists storing object entries that store key: our compression pattern and value: our index
    private int size; //size of hash table (the array)

    //hashtable constructor given initial size
    public SeperateChain(int initialSize) {
        size = initialSize;
        ourTable = new LinkedList[initialSize];
    }

    //search method for hashtable 
    public int get(OurPat k){
        int position = k.hashCode() % size; //computes hashtable index value
        if (ourTable[position] == null){
            return -1; //linkedlist not stored at pos
        }
        for (Entry<OurPat, Integer> nextItem: ourTable[position]){ //iterates over linked list stored at target position in the array
            if (nextItem.getOurPat().equals(k)){
                return nextItem.getIndex(); //getting is constant time unless linkedlist is traversed
            }
        }
        return -1; //key not found in linkedlist
    }

    //add method for hashtable
    public void add(OurPat key , int index){
        int position = key.hashCode() % size; //computes hastable index value
        if (ourTable[position] == null){
            ourTable[position] = new LinkedList<Entry<OurPat, Integer>>(); //initialize new linkedlist if index is hashed to for the first time
        }
        ourTable[position].add(new Entry<OurPat, Integer>(key, index)); //uses add method of linkedlist to add new hashtable entry 
    }

    //string representation of the seperate chain table, displays all
    //keys and values 
    public String toString(){
        String text = "";
        int pos = 0;
        text += "Table Length: " + String.valueOf(ourTable.length) + "\n";
        for( LinkedList<Entry<OurPat, Integer>> list : ourTable){
            if (list != null){
                text += String.valueOf(pos);
                for (Entry<OurPat, Integer> nextItem: list){
                    text += " Entry: " + String.valueOf(nextItem.getIndex())  +": " +nextItem.getOurPat().toString();
                }
                text += "\n";
            }
            pos ++;
        }
        return text;
    }

    //adds a linkedlist to the hashtable at empty table locations
    public void addAll(SeperateChain newList){
        for( LinkedList<Entry<OurPat, Integer>> list : ourTable){
            if (list != null){
                for (Entry<OurPat, Integer> nextItem: list){
                    newList.add(nextItem.getOurPat(), nextItem.getIndex());
                }
            }
        }
    }
}