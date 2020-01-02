package projects.bpt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.lang.*;
/**
 * <p>{@link BinaryPatriciaTrie} is a Patricia Trie over the binary alphabet &#123;	 0, 1 &#125;. By restricting themselves
 * to this small but terrifically useful alphabet, Binary Patricia Tries combine all the positive
 * aspects of Patricia Tries while shedding the storage cost typically associated with Tries that
 * deal with huge alphabets.</p>
 *
 * @author ---- YOUR NAME HERE! -----
 */
public class BinaryPatriciaTrie {

    /* **************************************************************************************************  */
    /* ********************* PLACE YOUR PRIVATE MEMBERS AND METHODS BELOW: ******************************  */
    /*  ************************************************************************************************** */

    private static final RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");
    private class BPTNode{
        BPTNode left,right;  /*left is 0 and right is 1*/
        boolean isKey;
        String keyRef ;

        BPTNode() {
            isKey = false;
            left = null;
            keyRef = "";
            right = null;
        }
        BPTNode(String keyRef){
            this.keyRef = keyRef;
        }

    }
    private BPTNode root;
    private int size;
    /* ************************************************************************************************** */
    /* ***********************   IMPLEMENT THE FOLLOWING PUBLIC METHODS:   ******************************** */
    /* ************************************************************************************************** */

    /**
     * Simple constructor that will initialize the internals of this.
     */
    /**
     I need to test this case///
     */
    public BinaryPatriciaTrie() {
        root = null;
    }

    /**
     * Searches the trie for a given key.
     *
     * @param key The input String key.
     * @return true if and only if key is in the trie, false otherwise.
     */
    public boolean search(String key) {
        BPTNode x = search(root,key);
        return (x != null) ? true : false;



    }

    private BPTNode search(BPTNode root, String key){
        if(root == null) return null;
        else if (root.keyRef.length() > key.length()) return null;
        else if (root.keyRef.length() == key.length())
            return root.isKey && root.keyRef.equals(key) ? root: null;
        else {
            String commonPrefix = commonPrefix(key,root.keyRef);
            String substring = key.substring(commonPrefix.length());
            if(substring.charAt(0) == '0')
                return search(root.left, substring);
            else
                return search(root.right,substring);
        }

    }


    /**
     * Inserts key into the trie.
     *
     * @param key The input String key.
     * @return true if and only if the key was not already in the trie, false otherwise.
     */
    public boolean insert(String key) {
        Boolean result = search(key);
        if(!result) this.size++;
        root = rec_insert(root,key);
        return !result;
    }

    private  BPTNode rec_insert(BPTNode root, String key){
         if(root == null) {
             root = new BPTNode(key);
             root.isKey = true;

         }
         else if(root.keyRef.equals(key))
             root.isKey = true;
         else if(key.indexOf(root.keyRef)  == 0){
             String com = commonPrefix(key, root.keyRef);
             String substring = key.substring(com.length());
             if(key.charAt(com.length()) == '0')
                 root.left = rec_insert(root.left,substring);
             else
                 root.right = rec_insert(root.right,substring);
         }
         else if(root.keyRef.indexOf(key) == 0){
             BPTNode new_root = new BPTNode(key);
             String com = commonPrefix(root.keyRef , key);
             String substring = root.keyRef.substring(com.length());
             root.keyRef = substring;
             if(substring.charAt(0) == '0')
                 new_root.left = root;
             else
                 new_root.right = root;
             new_root.isKey = true;
             root = new_root;
         }
         else {
             /* problem which one is longer*/
             String com;
             if(root.keyRef.length() > key.length()){
                 com = commonPrefix(root.keyRef, key);}
             else {
                 com = commonPrefix(key,root.keyRef);
             }
             String new_one = key.substring(com.length());
             String old_one = root.keyRef.substring(com.length());
             root.keyRef = old_one;
             BPTNode parnet = new BPTNode(com);
             if(new_one.charAt(0) == '0'){
                 parnet.left = new BPTNode(new_one);
                 parnet.left.isKey = true;
                 parnet.right = root;

             }else{
                 parnet.right = new BPTNode(new_one);
                 parnet.right.isKey = true;
                 parnet.left = root;

             }
             root = parnet;
         }


         return root;

    }

    /**
     * always assume string a is longer than string b
     * @param a
     * @param b
     */

    private String commonPrefix(String a, String  b) throws RuntimeException{
        if (a.length() < b.length()) throw new RuntimeException("length is smaller than length b");
        String result = "";
        for(int i=0 ; i< b.length() ; i++)
            if (a.charAt(i) == b.charAt(i))
                result = result + a.charAt(i);
            else
                break;
        return  result;
    }
    /**
     * Deletes key from the trie.
     *
     * @param key The String key to be deleted.
     * @return True if and only if key was contained by the trie before we attempted deletion, false otherwise.
     */
    public boolean delete(String key) {
        Boolean result = search(key);
        if(result == true)
            this.size --;
        root = rec_delete(root,key);
        return result;
    }


    private BPTNode rec_delete(BPTNode root , String key){
        if(root == null){}

        else if (root.keyRef.length() != key.length() && root.keyRef.indexOf(key)==0){}

        else if (root.keyRef.length() == key.length() && !root.keyRef.equals(key)){}

        else if (
                (root.keyRef.length() != key.length() && key.indexOf(root.keyRef) == 0)
        ){
            String com = commonPrefix(key,root.keyRef);
            String remaininig = key.substring(com.length());
            if(remaininig.charAt(0) == '0') {
                root.left = rec_delete(root.left, remaininig);
                if(!root.isKey){
                    if(root.left != null && root.right != null){}
                    else if(root.left != null && root.right == null){
                        root.left.keyRef = root.keyRef + root.left.keyRef;
                        root = root.left;

                    }
                    else if(root.right != null && root.left == null){
                        root.right.keyRef = root.keyRef + root.right.keyRef;
                        root = root.right;
                    }
                    else{
                        root = null;
                    }

                  }
            }
            else {
                root.right = rec_delete(root.right, remaininig);
                if (!root.isKey) {
                    if (root.left != null && root.right != null) {
                    } else if (root.left != null && root.right == null) {
                        root.left.keyRef = root.keyRef + root.left.keyRef;
                        root = root.left;

                    } else if (root.right != null && root.left == null) {
                        root.right.keyRef = root.keyRef + root.right.keyRef;
                        root = root.right;
                    } else {
                        root = null;
                    }
                }
            }
        }
        else if(root.keyRef.equals(key)){
            if(root.left != null && root.right != null){root.isKey = false;}
            else if(root.left != null && root.right == null){
                root.left.keyRef = root.keyRef + root.left.keyRef;
                root = root.left;

            }
            else if(root.right != null && root.left == null){
                root.right.keyRef = root.keyRef + root.right.keyRef;
                root = root.right;
            }
            else{
                root = null;
            }


        }
        return root;

    }

    /**
     * Queries the trie for emptiness.
     *
     * @return true if and only if {@link #getSize()} == 0, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;

    }

    /**
     * Returns the number of keys in the tree.
     *
     * @return The number of keys in the tree.
     */
    public int getSize() {
        return size;
    }

    /**
     * <p>Performs an <i>inorder (symmetric) traversal</i> of the Binary Patricia Trie. Remember from lecture that inorder
     * traversal in tries is NOT sorted traversal, unless all the stored keys have the same length. This
     * is of course not required by your implementation, so you should make sure that in your tests you
     * are not expecting this method to return keys in lexicographic order. We put this method in the
     * interface because it helps us test your submission thoroughly and it helps you debug your code! </p>
     *
     * <p>We <b>neither require nor test </b> whether the {@link Iterator} returned by this method is fail-safe or fail-fast.
     * This means that you  do <b>not</b> need to test for thrown {@link java.util.ConcurrentModificationException}s and we do
     * <b>not</b> test your code for the possible occurrence of concurrent modifications.</p>
     *
     * <p>We also assume that the {@link Iterator} is <em>immutable</em>, i,e we do <b>not</b> test for the behavior
     * of {@link Iterator#remove()}. You can handle it any way you want for your own application, yet <b>we</b> will
     * <b>not</b> test for it.</p>
     *
     * @return An {@link Iterator} over the {@link String} keys stored in the trie, exposing the elements in <i>symmetric
     * order</i>.
     */
    public Iterator<String> inorderTraversal() {
        ArrayList<String>  s = new ArrayList<String>();
        rec_inorder(root,s,"");
        return s.iterator();
    }

    private void rec_inorder(BPTNode root, ArrayList s , String prev){
        if(root == null) return;
        prev = prev + root.keyRef;
        rec_inorder(root.left, s , prev );
        if(root.isKey == true){s.add(prev);}
        rec_inorder(root.right,s,prev);
    }


    /**
     * Finds the longest {@link String} stored in the Binary Patricia Trie.
     *
     * @return <p>The longest {@link String} stored in this. If the trie is empty, the empty string &quot;&quot; should be
     * returned. Careful: the empty string &quot;&quot;is <b>not</b> the same string as &quot; &quot;; the latter is a string
     * consisting of a single <b>space character</b>! It is also <b>not the same as the</b> null <b>reference</b>!</p>
     *
     * <p>Ties should be broken in terms of <b>value</b> of the bit string. For example, if our trie contained
     * only the binary strings 01 and 11, <b>11</b> would be the longest string. If our trie contained
     * only 001 and 010, <b>010</b> would be the longest string.</p>
     */
    public String getLongest() {
        if(root == null) return "";
        Iterator<String> it = inorderTraversal();
        String longest = "";
        String curr = "";
        while(it.hasNext()){
            curr = it.next();
            if(curr.length() > longest.length())
                longest = curr;
            if(curr.length() == longest.length()){  /*when these two things are equal*/
                if(curr.compareTo(longest) > 0)
                    longest = curr;
            }

        }
        return longest;
    }

    /**
     * Makes sure that your trie doesn't have splitter nodes with a single child. In a Patricia trie, those nodes should
     * be pruned. Be careful with the implementation of this method, since our tests call it to make sure your deletions work
     * correctly! That is to say, if your deletions work well, but you have made an error in this (far easier) method,
     * you will <b>still</b> not be passing our tests!
     *
     * @return true iff all nodes in the trie either denote stored strings or split into two subtrees, false otherwise.
     */
    public boolean isJunkFree(){
        return rec_isjunkfree(root);

    }
    public boolean rec_isjunkfree(BPTNode root){
        if(root == null) return true;
        if(root.right!=null && root.left != null)
            return rec_isjunkfree(root.left) && rec_isjunkfree(root.right);
        return root.isKey == true && rec_isjunkfree(root.left) &&rec_isjunkfree(root.right);

    }
}
