package projects.avlg;

import com.sun.media.sound.RIFFInvalidDataException;
import projects.avlg.exceptions.EmptyTreeException;
import projects.avlg.exceptions.InvalidBalanceException;

/** <p>An <tt>AVL-G Tree</tt> is an AVL Tree with a relaxed balance condition. Its constructor receives a strictly
 * positive parameter which controls the <b>maximum</b> imbalance allowed on any subtree of the tree which
 * it creates. So, for example:</p>
 *  <ul>
 *      <li>An AVL-1 tree is a classic AVL tree, which only allows for perfectly balanced binary
 *      subtrees (imbalance of 0 everywhere), or subtrees with a maximum imbalance of 1 (somewhere). </li>
 *      <li>An AVL-2 tree relaxes the criteria of AVL-1 trees, by also allowing for subtrees
 *      that have an imbalance of 2.</li>
 *      <li>AVL-3 trees allow an imbalance of 3</li>
 *      <li>...</li>
 *  </ul>
 *
 *  <p>The idea behind AVL-G trees is that rotations cost time, so maybe we would be willing to
 *  accept bad search performance now and then if it would mean less rotations.</p>
 *
 * @author <a href="https://github.com/JasonFil">Jason Filippou</a>
 */
public class AVLGTree<T extends Comparable<T>> {


    private static RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");

    /* *************************************************************************
     ************** PLACE YOUR PRIVATE METHODS AND FIELDS HERE: ****************
     ***************************************************************************/
    private int Max_Im;
    private int count;
    private Node root;
    private T deletekey;

    /*for here we still need to fix the height problem when we rotate our trees*/
    /*this is most import part we need to finish,because rotation will make the height imbalance*/
    /*previous root height also need to be updated*/
    private Node rotateRight(Node n) {
        if(n.left == null)
            throw new RuntimeException("you can't do right rotation, right node is empty");
        Node temp = n.left;
        n.left = temp.right;
        temp.right = n;

        temp.right.height = (height(temp.right.left) > height(temp.right.right)) ? height(temp.right.left)+1:
                height(temp.right.right)+1;

        temp.height = (height(temp.left) > height(temp.right)) ? height(temp.left)+1:
                height(temp.right)+1;


        return temp;
    }
    private Node rotateLeft(Node n){
        if(n.right == null){
            throw new RuntimeException("you can't do left rotation, left node is empty");
        }
        Node temp = n.right;
        n.right = temp.left;
        temp.left = n;
        temp.left.height = (height(temp.left.left) > height(temp.left.right)) ? height(temp.left.left)+1:
                height(temp.left.right)+1;
        temp.height = (height(temp.left) > height(temp.right)) ? height(temp.left)+1:
                height(temp.right)+1;
        return temp;
    }

    /*some height need to be fixed here*/
    private Node rotateLR(Node n){
        n.left = rotateLeft(n.left);
        n.height = (height(n.left) > height(n.right)) ? height(n.left)+1:height(n.right)+1;
        n = rotateRight(n);
        n.height = (height(n.left) > height(n.right)) ? height(n.left)+1:height(n.right)+1;
        return n;
    }

    /*some height need to be fixed here*/
    private Node rotateRL(Node n){
        n.right = rotateRight(n.right);
        n.height = (height(n.left) > height(n.right)) ? height(n.left)+1:height(n.right)+1;
        n = rotateLeft(n);
        n.height = (height(n.left) > height(n.right)) ? height(n.left)+1:height(n.right)+1;
        return n;
    }

    class Node {
        T key;
        Node left, right;
        int height;
        Node(T key) {
            left = right = null;
            this.key = key;
            height = 0;
        }
        Node inSucc(){
            assert right != null; // Otherwise the caller is making the wrong application
            Node curr = right;
            while(curr.left != null)
                curr = curr.left;
            return curr;
        }
    }


    /* *********************************************************************
     ************************* PUBLIC (INTERFACE) METHODS *******************
     **********************************************************************/

    /**
     * The class constructor provides the tree with its maximum maxImbalance allowed.
     * @param maxImbalance The maximum maxImbalance allowed by the AVL-G Tree.
     * @throws InvalidBalanceException if <tt>maxImbalance</tt> is a value smaller than 1.
     */
    public AVLGTree(int maxImbalance) throws InvalidBalanceException {
        if(maxImbalance < 1)
            throw new InvalidBalanceException("maxImbalance is less 1");
        else
            this.Max_Im = maxImbalance;
            this.root = null;
            this.count = 0;
    }

    /**
     * Insert <tt>key</tt> in the tree.
     * @param key The key to insert in the tree.
     */
    private int height(Node n) {
        return (n == null) ? -1: n.height;
    }

    private Node recinsert(T key, Node root){
        if(root == null){
            /*only this occur will make the node increase by one*/
            this.count += 1;
            return new Node(key);
        }
        else if (key.compareTo(root.key)<0){
             root.left = recinsert(key, root.left);
             root.height = (height(root.left) > height(root.right)) ? height(root.left)+1:height(root.right)+1;
             if(height(root.left) - height(root.right) > Max_Im){
                 /*what if equals = 0, think about this problem, same for the next*/
                 if(key.compareTo(root.left.key)<0){
                     root = rotateRight(root);
                 }else
                     root = rotateLR(root);
             }

        }
        else if(key.compareTo(root.key)>0){
            root.right = recinsert(key, root.right);
            root.height = (height(root.left) > height(root.right)) ? height(root.left)+1:height(root.right)+1;
            if(height(root.right) - height(root.left) > Max_Im) {
                if (key.compareTo(root.right.key) > 0)
                    root = rotateLeft(root);
                else
                    root = rotateRL(root);

            }
        }
        /*already increase the case that T is already in the Tree*/
        return root;
    }

    public void insert(T key) {

        root = recinsert(key,root);
    }

    /**
     * Delete the key from the data structure and return it to the caller.
     * @param key The key to delete from the structure.
     * @return The key that was removed, or <tt>null</tt> if the key was not found.
     * @throws EmptyTreeException if the tree is empty.
     */

    /*do not forget reduce count and reduce height,we should return the key that was removed*/
    /*reduce height and count is very important in this project*/

    private Node deleteRec(T key,Node curr){
        if(curr == null)
            return null;

        if(curr.key.compareTo(key) > 0) {
            curr.left = deleteRec(key, curr.left);
            curr.height = (height(curr.left) > height(curr.right)) ? height(curr.left)+1:height(curr.right)+1;
            if(height(curr.right) - height(curr.left) > Max_Im){
                /*what if equals = 0, think about this problem, same for the next*/
                if(height(curr.right.left) - height(curr.right.right) <= 0){
                    curr = rotateLeft(curr);
                }else
                    curr = rotateRL(curr);
            }

        }

        else if(curr.key.compareTo(key) < 0) {
            curr.right = deleteRec(key, curr.right);
            curr.height = (height(curr.left) > height(curr.right)) ? height(curr.left)+1:height(curr.right)+1;
            if(height(curr.left) - height(curr.right) > Max_Im){
                /*what if equals = 0, think about this problem, same for the next*/
                if(height(curr.left.right) - height(curr.left.left) <= 0){
                    curr = rotateRight(curr);
                }else
                    curr = rotateLR(curr);
            }

        }
        /*Still need to fix the change of height here!!!! important*/
        else { // All actual deletion cases will be implemented here.
            if((curr.right == null) && (curr.left == null)) {// pure leaf;
                if(deletekey == null)
                    deletekey = curr.key;
                curr = null;
                count--;
            }
            else if (curr.right == null){ // Has a left subtree - return that
                if(deletekey == null)
                    deletekey = curr.key;

                /* all left brances should minus 1*/
                curr = curr.left;
                /*you do not need to fix the height here*/
                count--;
            }
            else { // Has a right subtree. Swap with inorder successor.
                if(deletekey == null)
                    deletekey = curr.key;
                curr.key = curr.inSucc().key;
                curr.right = deleteRec(curr.key,curr.right);
                curr.height = (height(curr.left) > height(curr.right)) ? height(curr.left)+1:height(curr.right)+1;
                if(height(curr.left) - height(curr.right) > Max_Im){
                    /*what if equals = 0, think about this problem, same for the next*/
                    if(height(curr.left.right) - height(curr.left.left) <= 0){
                        curr = rotateRight(curr);
                    }else
                        curr = rotateLR(curr);
                }

            }
        }
        return curr;
    }




    public T delete(T key) throws EmptyTreeException{
        deletekey = null;
        if(root == null)
            throw new EmptyTreeException("the tee is empty and node can not be deleted");
        else{
            root = deleteRec(key,root);
            return deletekey;
        }
    }

    /**
     * <p>Search for <tt>key</tt> in the tree. Return a reference to it if it's in there,
     * or <tt>null</tt> otherwise.</p>
     * @param key The key to search for.
     * @return <tt>key</tt> if <tt>key</tt> is in the tree, or <tt>null</tt> otherwise.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T search(T key) throws EmptyTreeException {
        Node curr = root;
        if(root == null)
            throw new EmptyTreeException("this tree is empty");
        else{
            while(curr!=null){
                if(curr.key.compareTo(key) == 0)
                    return curr.key;
                else if(curr.key.compareTo(key)>0)
                    curr = curr.left;
                else
                    curr = curr.right;
            }
        return null;
        }

    }

    /**
     * Retrieves the maximum imbalance parameter.
     * @return The maximum imbalance parameter provided as a constructor parameter.
     */
    public int getMaxImbalance(){
        return Max_Im;
    }


    /**
     * <p>Return the height of the tree. The height of the tree is defined as the length of the
     * longest path between the root and the leaf level. By definition of path length, a
     * stub tree has a height of 0, and we define an empty tree to have a height of -1.</p>
     * @return The height of the tree. If the tree is empty, returns -1.
     */
    public int getHeight() {
        return height(root);
    }

    /**
     * Query the tree for emptiness. A tree is empty iff it has zero keys stored.
     * @return <tt>true</tt> if the tree is empty, <tt>false</tt> otherwise.
     */
    public boolean isEmpty() {
        return root == null? true: false;
    }

    /**
     * Return the key at the tree's root nodes.
     * @return The key at the tree's root nodes.
     * @throws  EmptyTreeException if the tree is empty.
     */
    public T getRoot() throws EmptyTreeException{
       if(root == null)
           throw new EmptyTreeException("the root is empty");
       else
           return root.key;
    }


    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the BST condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return <tt>true</tt> if the tree satisfies the Binary Search Tree property,
     * <tt>false</tt> otherwise.
     */

    public boolean recisBST(Node root) {
            if (root == null)
                return true;

            if(root.right == null && root.left==null)
                return true;

            else if(root.right == null){
                if(root.key.compareTo(root.left.key) > 0)
                    return recisBST(root.left);
                else
                    return false;
            }

            else if(root.left == null){
                if(root.key.compareTo(root.right.key) < 0)
                    return recisBST(root.right);
                else
                    return false;
            }
            else{
                if(root.key.compareTo(root.right.key)<0 && root.key.compareTo(root.left.key)>0){
                    return recisBST(root.right) && recisBST(root.left);
                }
                else
                    return false;
            }
    }


    public boolean isBST(){
        return recisBST(root);
    }

    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the AVL-G condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return <tt>true</tt> if the tree satisfies the Binary Search Tree property,
     * <tt>false</tt> otherwise.
     */
    public  boolean recisAVLGBalanced(Node root){
        if(root == null)
            return true;
        else if (height(root.left) - height(root.right) > Max_Im || height(root.left) - height(root.right)< (0-Max_Im))
                return false;
        else
            return recisAVLGBalanced(root.left) && recisAVLGBalanced(root.right);
    }

    public boolean isAVLGBalanced() {
        return recisAVLGBalanced(root);
    }

    /**
     * <p>Empties the <tt>AVLGTree</tt> of all its elements. After a call to this method, the
     * tree should have <b>0</b> elements.</p>
     */
    public void clear(){
         root = null;
         count = 0;
    }


    /**
     * <p>Return the number of elements in the tree.</p>
     * @return  The number of elements in the tree.
     */
    public int getCount(){
        return count;

    }
}
