package projects.phonebook.hashes;
import projects.phonebook.utils.KVPair;
import projects.phonebook.utils.PrimeGenerator;

import java.security.Key;
import java.util.LinkedList;

/**
 * <p>{@link LinearProbingHashTable} is an Openly Addressed {@link HashTable} implemented with <b>Linear Probing</b> as its
 * collision resolution strategy: every key collision is resolved by moving one address over. It is
 * the most famous collision resolution strategy, praised for its simplicity, theoretical properties
 * and cache locality. It <b>does</b>, however, suffer from the &quot; clustering &quot; problem:
 * collision resolutions tend to cluster collision chains locally, making it hard for new keys to be
 * inserted without collisions. {@link QuadraticProbingHashTable} is a {@link HashTable} that
 * tries to avoid this problem, albeit sacrificing cache locality.</p>
 *
 * @author YOUR NAME HERE!
 *
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see QuadraticProbingHashTable
 * @see CollisionResolver
 */
public class LinearProbingHashTable implements HashTable{

    /* *******************************************************************/
    /* ***** PRIVATE FIELDS / METHODS PROVIDED TO YOU: DO NOT EDIT! ******/
    /* ****************************************************** ***********/

    private static final RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");
    private KVPair[] table;
    private PrimeGenerator primeGenerator;
    private int count = 0;

    private int hash(String key){
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    /*  YOU SHOULD ALSO IMPLEMENT THE FOLLOWING METHOD ACCORDING TO THE SPECS
     * PROVIDED IN THE PROJECT WRITEUP, BUT KEEP IT PRIVATE!  */
    private void enlarge(){
        KVPair[] temp_table = table;
        int new_cap = primeGenerator.getNextPrime();
        table  = new KVPair[new_cap];
        for(KVPair e: temp_table){
                if(e!= null)
                    this.copy(e.getKey(),e.getValue());

        }
    }

    /* ******************/
    /*  PUBLIC METHODS: */
    /* ******************/


    /**
     *  Default constructor. Initializes the internal storage with a size equal to the default of {@link PrimeGenerator}.
     *  This constructor is <b>given to you: DO NOT EDIT IT.</b>
     */
    public LinearProbingHashTable(){
        primeGenerator = new PrimeGenerator();
        table = new KVPair[primeGenerator.getCurrPrime()];
        count = 0;
    }

    /**
     * Inserts the pair &lt;key, value&gt; into this. The container should <b>not</b> allow for null
     * keys and values, and we <b>will</b> test if you are throwing a {@link IllegalArgumentException} from your code
     * if this method is given null arguments! It is important that we establish that no null entries
     * can exist in our database because the semantics of {@link #get(String)} and {@link #remove(String)} are that they
     * return null if, and only if, their key parameter is null. This method is expected to run in <em>amortized
     * constant time</em>.
     *
     * Instances of {@link LinearProbingHashTable} will follow the writeup's guidelines about how to internally resize
     * the hash table when the capacity exceeds 50&#37;
     * @param key The record's key.
     * @param value The record's value.
     * @throws IllegalArgumentException if either argument is null.
     */


    private void copy(String key, String value) {
        if( key==null || value == null)
            throw new IllegalArgumentException("key or value is illegal argument");
        int curr = hash(key);
        while(table[curr]!= null){
            curr++;
            if(curr > table.length - 1)
                curr = curr - table.length;
        }
        table[curr] = new KVPair(key,value);
    }

    @Override
    public void put(String key, String value) {
        if( key==null || value == null)
            throw new IllegalArgumentException("key or value is illegal argument");
        if((float)(count) /this.capacity() > 0.5)
            this.enlarge();
        count++;
        int curr = hash(key);
        while(table[curr]!= null){
            curr++;
            if(curr > table.length - 1)
                curr = curr - table.length;
        }
        table[curr] = new KVPair(key,value);
    }

    /*this is not amortized time*/
    @Override
    public String get(String key) {
        if(key == null)
            return  null;
        int curr = hash(key);
        while (table[curr] != null){
            if(table[curr].getKey().equals(key))
                return table[curr].getValue();
            curr = curr_wrap(curr);
        }
        return  null;
    }


    /**
     * <b>Return</b> and <b>remove</b> the value associated with key in the {@link HashTable}. If key does not exist in the database
     * or if key = null, this method returns null. This method is expected to run in <em>amortized constant time</em>.
     *
     * @return The associated value if key is non-null <b>and</b> exists in our database, null
     * otherwise.
     */
    /*we should use hard deletion for this implementation*/
    /*first we need to rehash everything and th*/


    /**
     * <b>Return</b> and <b>remove</b> the value associated with key in the {@link HashTable}. If key does not exist in the database
     * or if key = null, this method returns null. This method is expected to run in <em>amortized constant time</em>.
     *
     * @return The associated value if key is non-null <b>and</b> exists in our database, null
     * otherwise.
     */
    /*we should use hard deletion for this implementation*/
    /*first we need to rehash everything and th*/


    private int curr_wrap(int curr){
        curr++;
        if(curr > table.length - 1)
            curr = curr - table.length;
        return curr;
    }


    @Override
    public String remove(String key) {
       if(key == null)
           return null;
       int curr = hash(key);
       String t = null;
       LinkedList<KVPair> storage = new LinkedList<KVPair>();

       while (table[curr] !=  null){
           if(table[curr].getKey().equals(key)) {
               t = table[curr].getValue();
               table[curr] = null;
               curr = curr_wrap(curr);
               break;
           }
           curr = curr_wrap(curr);
       }
       /*if the key that we are trying to search is in the list,in the next step we need to erase all keys in the cluster */
       if(t!=null){
           /*erase all keys  and  add all deleted key into the storage*/
           while (table[curr] != null){
               storage.add(table[curr]);   /*add to the storage first and then delete it*/
               table[curr] = null;
               curr = curr_wrap(curr);
           }
           /*re-insert all keys*/
           while(!storage.isEmpty()){
               KVPair temp = storage.poll();
               this.put(temp.getKey(),temp.getValue());
           }

       }

       return t;
/* we need to set all to be  null and we need to rehash everything*/

    }


    @Override
    public boolean containsKey(String key) {
        if(this.get(key) != null)
            return true;
        return false;
    }

    @Override
    public boolean containsValue(String value) {
        for(KVPair p: table){
            if(p!= null && p.getValue().equals(value))
                return true;
        }
        return false;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public int capacity() {
        return table.length;// <---- ERASE THIS LINE WHEN YOU IMPLEMENT THIS METHOD!
    }
}
