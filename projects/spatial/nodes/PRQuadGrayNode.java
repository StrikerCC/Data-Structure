package projects.spatial.nodes;

import projects.spatial.kdpoint.KDPoint;
import projects.spatial.trees.PRQuadTree;

/** <p>A {@link PRQuadGrayNode} is a gray (&quot;mixed&quot;) {@link PRQuadNode}. It
 * maintains the following invariants: </p>
 * <ul>
 *      <li>Its children pointer buffer is non-null and has a length of 4.</li>
 *      <li>If there is at least one black node child, the total number of {@link KDPoint}s stored
 *      by <b>all</b> of the children is greater than the bucketing parameter (because if it is equal to it
 *      or smaller, we can prune the node.</li>
 * </ul>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 *  @author --- YOUR NAME HERE! ---
 */
public class PRQuadGrayNode extends PRQuadNode{

    private static final RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");

    /* *************************************************************************
     ************** PLACE YOUR PRIVATE METHODS AND FIELDS HERE: ****************
     ***************************************************************************/
    PRQuadNode[] childs;
    double[][] nextcenter;


    /* ***************************************************************************** */
    /* ******************* PUBLIC (INTERFACE) METHODS ****************************** */
    /* ***************************************************************************** */


    /**
     * Creates a {@link PRQuadGrayNode}  with the provided {@link KDPoint} as a centroid;
     * @param centroid A {@link KDPoint} that will act as the centroid of the space spanned by the current
     *                 node.
     * @param k The See {@link PRQuadTree#PRQuadTree(int, int)} for more information on how this parameter works.
     * @param bucketingParam The bucketing parameter fed to this by {@link PRQuadTree}.
     * @see PRQuadTree#PRQuadTree(int, int)
     */
    public PRQuadGrayNode(KDPoint centroid, int k, int bucketingParam){
        super(centroid, k, bucketingParam); // Call to the super class' protected constructor to properly initialize the object!
        childs = new PRQuadNode[4];
        nextcenter = new double[4][2];
        nextcenter[0][0] = centroid.coords[0]- Math.pow(2,(k-2));
        nextcenter[0][1] = centroid.coords[1]+ Math.pow(2,(k-2));
        nextcenter[1][0] = centroid.coords[0]+ Math.pow(2,(k-2));
        nextcenter[1][1] = centroid.coords[1]+ Math.pow(2,(k-2));
        nextcenter[2][0] = centroid.coords[0]- Math.pow(2,(k-2));
        nextcenter[2][1] = centroid.coords[1]- Math.pow(2,(k-2));
        nextcenter[3][0] = centroid.coords[0]+ Math.pow(2,(k-2));
        nextcenter[3][1] = centroid.coords[1]- Math.pow(2,(k-2));
    }


    /**
     * <p>Insertion into a {@link PRQuadGrayNode} consists of navigating to the appropriate child
     * and recursively inserting elements into it. If the child is a white node, memory should be allocated for a
     * {@link PRQuadBlackNode} which will contain the provided {@link KDPoint} If it's a {@link PRQuadBlackNode},
     * refer to {@link PRQuadBlackNode#insert(KDPoint, int)} for details on how the insertion is performed. If it's a {@link PRQuadGrayNode},
     * the current method would be called recursively. Polymorphism will allow for the appropriate insert to be called
     * based on the child object's runtime object.</p>
     * @param p A {@link KDPoint} to insert into the subtree rooted at the current {@link PRQuadGrayNode}.
     * @param k The side length of the quadrant spanned by the <b>current</b> {@link PRQuadGrayNode}. It will need to be updated
     *          per recursive call to help guide the input {@link KDPoint}  to the appropriate subtree.
     * @return The subtree rooted at the current node, potentially adjusted after insertion.
     * @see PRQuadBlackNode#insert(KDPoint, int)
     */
    @Override
    public PRQuadNode insert(KDPoint p, int k) {
        int ori = this.compare(p, centroid);
        if (childs[ori] == null) {
            childs[ori] = new PRQuadBlackNode(new KDPoint(this.nextcenter[ori]), k - 1, this.bucketingParam);
            childs[ori].insert(p,k-1);
        } else
            childs[ori] = childs[ori].insert(p, k - 1);
        return this;
    }

    private int compare(KDPoint p, KDPoint c){
        double x1 = p.coords[0];
        double y1 = p.coords[1];
        double cx = c.coords[0];
        double cy = c.coords[1];
        if(x1 >= cx && y1>= cy){
            return 1;
        }
        else if(x1>=cx && y1 < cy){
            return 3;
        }
        else if(x1 < cx && y1 < cy){
            return 2;
        }
        else {
            return 0;
        }
    }




    /**
     * <p>Deleting a {@link KDPoint} from a {@link PRQuadGrayNode} consists of recursing to the appropriate
     * {@link PRQuadBlackNode} child to find the provided {@link KDPoint}. If no such child exists, the search has
     * <b>necessarily failed</b>; <b>no changes should then be made to the subtree rooted at the current node!</b></p>
     *
     * <p>Polymorphism will allow for the recursive call to be made into the appropriate delete method.
     * Importantly, after the recursive deletion call, it needs to be determined if the current {@link PRQuadGrayNode}
     * needs to be collapsed into a {@link PRQuadBlackNode}. This can only happen if it has no gray children, and one of the
     * following two conditions are satisfied:</p>
     *
     * <ol>
     *     <li>The deletion left it with a single black child. Then, there is no reason to further subdivide the quadrant,
     *     and we can replace this with a {@link PRQuadBlackNode} that contains the {@link KDPoint}s that the single
     *     black child contains.</li>
     *     <li>After the deletion, the <b>total</b> number of {@link KDPoint}s contained by <b>all</b> the black children
     *     is <b>equal to or smaller than</b> the bucketing parameter. We can then similarly replace this with a
     *     {@link PRQuadBlackNode} over the {@link KDPoint}s contained by the black children.</li>
     *  </ol>
     *
     * @param p A {@link KDPoint} to delete from the tree rooted at the current node.
     * @return The subtree rooted at the current node, potentially adjusted after deletion.
     */
    @Override
    public PRQuadNode delete(KDPoint p) {

        int ori = this.compare(p,centroid);
        if(childs[ori] == null)
            return this;
        else{
            childs[ori] = childs[ori].delete(p);
            PRQuadNode temp = null;
            int count = 0;
            int sum = 0;
            for(PRQuadNode e:childs){
                if(e!=null){
                    count++;
                    sum += e.count();
                    temp = e;
                }
            }
            if(count == 1)
                return temp;
            else{
                if(sum<=this.bucketingParam){

                    PRQuadBlackNode new_parent = new PRQuadBlackNode(this.centroid,this.k,this.bucketingParam);
                    for(PRQuadNode e:childs) {
                        if (e != null && (e instanceof PRQuadBlackNode)) {
                            for (KDPoint e2 : ((PRQuadBlackNode) e).getPoints()) {
                                new_parent.insert(e2, k);
                            }
                        }
                    }
                    return new_parent;
                }else
                    return this;

            }
        }

    }

    @Override
    public boolean search(KDPoint p){
        int ori = this.compare(p,centroid);
        if(childs[ori] == null)
            return false;
        else
            return childs[ori].search(p);
    }

    @Override
    public int height(){
        int max = -1;
        for(PRQuadNode e:childs){
            if(e!=null && e.height() > max)
                max = e.height();
        }
        return max+1;
    }

    @Override
    public int count(){
        int sum =0;
        for(PRQuadNode e:childs){
            if(e!=null)
                sum += e.count();
        }
    return sum;
    }
}