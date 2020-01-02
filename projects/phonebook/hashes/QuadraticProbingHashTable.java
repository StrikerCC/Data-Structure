package projects.phonebook.hashes;

import projects.phonebook.utils.KVPair;
import projects.phonebook.utils.PrimeGenerator;

import java.util.LinkedList;

/**
 * <p>{@link QuadraticProbingHashTable} is an Openly Addressed {@link HashTable} which uses <b>Quadratic
 * Probing</b> as its collision resolution strategy. Quadratic Probing differs from <b>Linear</b> Probing
 * in that collisions are resolved by taking &quot; jumps &quot; on the hash table, the length of which
 * determined by an increasing polynomial factor. For example, during a key insertion which generates
 * several collisions, the first collision will be resolved by moving 1^2 + 1 = 2 positions over from
 * the originally hashed address (like Linear Probing), the second one will be resolved by moving
 * 2^2 + 2= 6 positions over from our hashed address, the third one by moving 3^2 + 3 = 12 positions over, etc.
 * </p>
 *
 * <p>By using this collision resolution technique, {@link QuadraticProbingHashTable} aims to get rid of the
 * &quot;key clustering &quot; problem that {@link LinearProbingHashTable} suffers from. Leaving more
 * space in between memory probes allows other keys to be inserted without many collisions. The tradeoff
 * is that, in doing so, {@link QuadraticProbingHashTable} sacrifices <em>cache locality</em>.</p>
 *
 * @author YOUR NAME HERE!
 *
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see LinearProbingHashTable
 * @see CollisionResolver
 */
public class QuadraticProbingHashTable implements HashTable{

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


    private void copy(String key, String value) {
        if( key==null || value == null)
            throw new IllegalArgumentException("key or value is illegal argument");
        int curr = hash(key);
        int i = 1;
        int k = (curr + (i-1) + (i-1)*(i-1)) % table.length;
        while(table[k]!= null){
            i++;
            k = (curr + (i-1) + (i-1)*(i-1)) % table.length;
        }
        table[k] = new KVPair(key,value);
    }

    /* ******************/
    /*  PUBLIC METHODS: */
    /* ******************/

    /**
     *  Default constructor. Initializes the internal storage with a size equal to the default of {@link PrimeGenerator}.
     *  This constructor is <b>given to you: DO NOT EDIT IT.</b>
     */
    public QuadraticProbingHashTable(){
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
     * Instances of {@link QuadraticProbingHashTable} will follow the writeup's guidelines about how to internally resize
     * the hash table when the capacity exceeds 50&#37;
     * @param key The record's key.
     * @throws IllegalArgumentException if either argument is null.
     */
    @Override
    public void put(String key, String value) {
        if( key==null || value == null)
            throw new IllegalArgumentException("key or value is illegal argument");
        if((float)count /this.capacity() > 0.5)
            this.enlarge();
        count++;
        int curr = hash(key);
        int i = 1;
        int k = (curr + (i-1) + (i-1)*(i-1)) % table.length;
        while(table[k]!= null){
            i++;
            k = (curr + (i-1) + (i-1)*(i-1)) % table.length;
        }
        table[k] = new KVPair(key,value);
    }


    @Override
    public String get(String key) {
        if(key == null)
            return  null;
        int curr = hash(key);
        int i = 1;
        while (table[(curr + (i-1) + (i-1)*(i-1)) % table.length] != null){
            if(table[(curr + (i-1) + (i-1)*(i-1)) % table.length].getKey().equals(key))
                return table[(curr + (i-1) + (i-1)*(i-1)) % table.length].getValue();
            i++;
        }
        return  null;
    }


    /**
     * <b>Return</b> and <b>remove</b> the value associated with key in the {@link HashTable}. If key does not exist in the database
     * or if key = null, this method returns null. This method is expected to run in <em>amortized constant time</em>.
     *
     * @param key The key to search for.
     * @return The associated value if key is non-null <b>and</b> exists in our database, null
     * otherwise.
     */
    @Override
    public String remove(String key) {
        if(key == null)
            return null;
        int curr = hash(key);
        int i = 1;
        String t = null;
        LinkedList<KVPair> storage = new LinkedList<KVPair>();

        while (table[(curr + (i-1) + (i-1)*(i-1)) % table.length] != null){
            if(table[(curr + (i-1) + (i-1)*(i-1)) % table.length].getKey().equals(key)) {
                t = table[(curr + (i-1) + (i-1)*(i-1)) % table.length].getValue();
                table[(curr + (i-1) + (i-1)*(i-1)) % table.length]= null;
                i++;
                break;
            }
            i++;
        }
        /*if the key that we are trying to search is in the list,in the next step we need to erase all keys in the cluster */
        if(t!=null){
            /*erase all keys  and */
            while (table[(curr + (i-1) + (i-1)*(i-1)) % table.length] != null){
                int k = (curr + (i-1) + (i-1)*(i-1)) % table.length;
                storage.add(table[k]);
                table[k] = null;
                i++;
            }
            /*re-insert all keys*/
            while(!storage.isEmpty()){
                KVPair temp = storage.poll();
                this.put(temp.getKey(),temp.getValue());
            }

        }

        return t;
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