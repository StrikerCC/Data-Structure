package projects.spatial;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import projects.spatial.kdpoint.KDPoint;
import projects.spatial.knnutils.PriorityQueue;
import projects.spatial.trees.KDTree;
import projects.spatial.trees.PRQuadTree;
import projects.spatial.knnutils.BoundedPriorityQueue;

import java.util.*;

import static org.junit.Assert.*;

/**
 * <p>A testing framework for {@link projects.spatial.trees.KDTree} and {@link projects.spatial.trees.PRQuadTree}</p>.
 * You should extend it with your own tests.
 *
 * @author  --- YOUR NAME HERE! ---
 *
 */
public class StudentTests {

    /* Private fields */

    private PRQuadTree prQuadTree;
    private KDTree kdTree;
    private int MAX_DIM = 10;
    private static final long SEED=47;
    private double EPSILON = Math.pow(10, -8);
    private int SCALE=10;
    private Random r;
    private static final int MAX_ITER = 200;


    /* Private utilities */


    private int getRandomSign(){
        return 2 * r.nextInt(2) - 1;
    }
    private double[] getRandomDoubleCoords(int num){
        double[] coords = new double[num];
        for(int i = 0; i < num; i++)
            coords[i] = getRandomSign() * SCALE * r.nextDouble();
        return coords;
    }

    private KDPoint getRandomPoint(int dim){
        return new KDPoint(getRandomDoubleCoords(dim));
    }

    private boolean checkRangeQuery(KDTree tree, KDPoint origin, double range, KDPoint... candidates){
        Collection<KDPoint> rangeQueryResults = tree.range(origin, range);
        List<KDPoint> candidateList = Arrays.asList(candidates);
        return rangeQueryResults.containsAll(candidateList); // Order not important in range queries: only containment.
    }

    /* Setup and teardown methods; those are run before and after every jUnit test. */

    @Before
    public void setUp(){
        r = new Random(SEED);
        prQuadTree = new PRQuadTree(r.nextInt(), r.nextInt());
    }

    @After
    public void tearDown(){
        r = null;
        kdTree = null;
        prQuadTree = null;
        System.gc();
    }

    /* BPQ Tests.... */


    /* KD-Tree Tests.... */

    @Test
    public void testKDTreeIsEmpty(){
        kdTree = new KDTree(10);
        assertTrue("A freshly created KD-Tree should be empty!", kdTree.isEmpty());
    }

    @Test
    public void testKDTreeFewInsertions(){
        kdTree = new KDTree(2);
        kdTree.insert(new KDPoint(10, 30));
        kdTree.insert(new KDPoint(12, 18));
        kdTree.insert(new KDPoint(-20, 300));

        assertEquals("The first point inserted should be our root.", new KDPoint(10, 30), kdTree.getRoot());
        assertEquals("The height of this KD-Tree should be 1.", 1, kdTree.height());
        assertEquals("The number of nodes in this tree should be 3.", 3, kdTree.count());
    }

    @Test
    public void testKDTreedeletion(){
        kdTree = new KDTree(2);
        kdTree.insert(new KDPoint(10,20));
        kdTree.insert(new KDPoint(11,5));
        kdTree.insert(new KDPoint(15,2));
        kdTree.insert(new KDPoint(20,1));
        kdTree.insert(new KDPoint(5,10));
        kdTree.insert(new KDPoint(5,8));
        kdTree.delete(new KDPoint(10,20));
        assertEquals("The height of this KD-Tree should be 1.", 2, kdTree.height());
        assertEquals(kdTree.count(),5);
        kdTree.delete(new KDPoint(5,8));
        assertEquals(kdTree.count(),4);
        kdTree.delete(new KDPoint(5,5));
        assertEquals(kdTree.count(),4);
    }

    @Test
    public void testKDTreedeletion2(){
        kdTree = new KDTree(2);
        kdTree.insert(new KDPoint(8,10));
        kdTree.insert(new KDPoint(12,7));
        kdTree.insert(new KDPoint(8,6));
        kdTree.insert(new KDPoint(11,6));
        kdTree.insert(new KDPoint(10,2));
        kdTree.delete(new KDPoint(12,7));
        assertEquals(3,kdTree.height());

    }
    @Test
    public void testRangeQuery(){
        kdTree = new KDTree(2);
        kdTree.insert(new KDPoint(0,-2));
        kdTree.insert(new KDPoint(-2,1));
        kdTree.insert(new KDPoint(7,0));
        kdTree.insert(new KDPoint(4,-3));
        kdTree.insert(new KDPoint(10,4));
        kdTree.insert(new KDPoint(4,5));
        Collection<KDPoint> c = kdTree.range(new KDPoint(11,5),Math.sqrt(41));

    }

    @Test
    public void nearest(){
        kdTree = new KDTree(2);
        assertEquals(kdTree.nearestNeighbor(new KDPoint(7,6)),null);
        kdTree.insert(new KDPoint(0,-2));
        assertEquals(kdTree.nearestNeighbor(new KDPoint(7,6)),new KDPoint(0,-2));
        assertEquals(kdTree.nearestNeighbor(new KDPoint(0,-2)),null);
        kdTree.insert(new KDPoint(-2,1));
        kdTree.insert(new KDPoint(7,0));
        assertEquals(kdTree.nearestNeighbor(new KDPoint(0,-2)),new KDPoint(-2,1));
        kdTree.insert(new KDPoint(4,-3));
        kdTree.insert(new KDPoint(10,4));
        kdTree.insert(new KDPoint(4,5));
        assertEquals(kdTree.nearestNeighbor(new KDPoint(7,6)),new KDPoint(4,5));
        assertEquals(kdTree.nearestNeighbor(new KDPoint(10,5)),new KDPoint(10,4));
        assertEquals(kdTree.nearestNeighbor(new KDPoint(0,-1)),new KDPoint(0,-2));
        assertEquals(kdTree.nearestNeighbor(new KDPoint(0,-2)),new KDPoint(-2,1));
    }
    @Test
    public void mnearest(){
        kdTree = new KDTree(2);
        kdTree.insert(new KDPoint(0,-2));
        kdTree.insert(new KDPoint(-2,1));
        kdTree.insert(new KDPoint(7,0));
        kdTree.insert(new KDPoint(4,-3));
        kdTree.insert(new KDPoint(10,4));
        kdTree.insert(new KDPoint(4,5));
        kdTree.insert(new KDPoint(8,-3));
        kdTree.insert(new KDPoint(9,-6));
        kdTree.insert(new KDPoint(5,-5));
        kdTree.insert(new KDPoint(7,-7));
        kdTree.insert(new KDPoint(12,-7));
        kdTree.insert(new KDPoint(13,-4));
        kdTree.insert(new KDPoint(7,-2.5));
        kdTree.insert(new KDPoint(11,-2));
        kdTree.kNearestNeighbors(4,new KDPoint(8,6));
    }


    @Test
    public void simplenearest(){
        kdTree = new KDTree(1);
        kdTree.insert(new KDPoint(0.0));
        kdTree.kNearestNeighbors(4,new KDPoint(0.0));
        for(KDPoint e: kdTree.kNearestNeighbors(4,new KDPoint(0.0))) {
            System.out.println(e);
        }
        kdTree.insert(new KDPoint(1.0));
        kdTree.insert(new KDPoint(2.0));
        kdTree.kNearestNeighbors(4,new KDPoint(0.0));
        for(KDPoint e: kdTree.kNearestNeighbors(4,new KDPoint(0.0))) {
            System.out.println(e);
        }

    }



    @Test
    public void testKDTreeSimpleRange(){
        for(int dim = 1; dim <= MAX_DIM; dim++){ // For MAX_DIM-many trees...
            for(int i = 0; i < MAX_ITER; i++){ // For MAX_ITER-many points...
                KDPoint originInDim = new KDPoint(dim);
                KDTree tree = new KDTree(dim);
                KDPoint p = getRandomPoint(dim);
                tree.insert(p);
                assertTrue("Failed a range query for a " + dim + "-D tree which only contained " +
                        p + ", KDPoint #" + i + ".", checkRangeQuery(tree, originInDim, Math.sqrt(p.distanceSquared(originInDim)) + EPSILON, p)); // Note the Math.sqrt()
            }
        }
    }

    @Test
    public void concurrencymodification(){
        PriorityQueue<KDPoint> pq = new BoundedPriorityQueue<>(5);
        pq.enqueue(new KDPoint(3, 4), 1);
        pq.enqueue(new KDPoint(2, 5), 2);
        pq.enqueue(new KDPoint(3,3),3);
        pq.enqueue(new KDPoint(7,6),0.5);
        pq.enqueue(new KDPoint(8,8),0.5);
        Iterator<KDPoint> i = pq.iterator();
        if(i.hasNext()){
            pq.enqueue(new KDPoint(7,6),3.1);
        }

    }


    /* PRQ Tests .... */

    @Test
    public void testEmptyPRQuadTree(){
        assertNotNull("Tree reference should be non-null by setUp() method.", prQuadTree);
        assertTrue("A freshly created PR-QuadTree should be empty!", prQuadTree.isEmpty());
    }


    @Test
    public void testSimpleQuadTree(){
        prQuadTree = new PRQuadTree(4, 2); // Space from (-8, -8) to (8, 8), bucketing parameter = 2.
        prQuadTree.insert(new KDPoint(1, 1));
        prQuadTree.insert(new KDPoint(4, 2)); // Should fit
        assertEquals("After two insertions into a PR-QuadTree with b = 2, the result should be a quadtree consisting of a single black node.",
            0, prQuadTree.height());
        assertEquals("After two insertions into a PR-QuadTree, the count should be 2.", 2, prQuadTree.count());

        // The following deletion should work just fine...

        try {
            prQuadTree.delete(new KDPoint(1, 1));
        } catch(Throwable t){
            fail("Caught a " + t.getClass().getSimpleName() + " with message: " + t.getMessage() + " when attempting to delete a KDPoint that *should*" +
                    " be in the PR-QuadTree.");
        }

        assertFalse("After deleting a point from a PR-QuadTree, we should no longer be finding it in the tree.",
                prQuadTree.search(new KDPoint(1, 1)));
        // The following two insertions should split the root node into a gray node with 2 black node children and 2 white node children.

        prQuadTree.insert(new KDPoint(-5, -6));
        prQuadTree.insert(new KDPoint(0, 0)); // (0, 0) should go to the NE quadrant after splitting.
        assertEquals("After inserting three points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 1);
        for(KDPoint p: new KDPoint[]{new KDPoint(0, 0), new KDPoint(4, 2), new KDPoint(-5, -6)})
            assertTrue("After inserting a point into a PR-QuadTree without subsequently deleting it, we should be able to find it.", prQuadTree.search(p));

    }


    @Test
    public void testBPQ1() {
        PriorityQueue<KDPoint> pq = new BoundedPriorityQueue<>(5);
        pq.enqueue(new KDPoint(3, 4), 1);
        pq.enqueue(new KDPoint(2, 5), 2);
        pq.enqueue(new KDPoint(3,3),3);
        pq.enqueue(new KDPoint(7,6),0.5);
        pq.enqueue(new KDPoint(8,8),0.1);
        assertEquals(pq.dequeue(),new KDPoint(8,8));
        assertEquals(pq.dequeue(),new KDPoint(7,6));
        pq.enqueue(new KDPoint(7,6),0.5);
        pq.enqueue(new KDPoint(8,8),0.1);
        pq.enqueue(new KDPoint(1,9),0.1);
        assertEquals(pq.first(),new KDPoint(8,8));
        assertEquals(((BoundedPriorityQueue<KDPoint>) pq).last(),new KDPoint(2,5));
        assertEquals(pq.size(),5);
        assertEquals(pq.dequeue(),new KDPoint(8,8));
        assertEquals(pq.size(),4);
        pq.enqueue(new KDPoint(6,6),0.2);
        assertEquals(pq.first(),new KDPoint(1,9));
        pq.enqueue(new KDPoint(6,7),0.05);
        assertEquals(pq.first(),new KDPoint(6,7));
        assertEquals(((BoundedPriorityQueue<KDPoint>) pq).last(),new KDPoint(3,4));
    }
    @Test
    public void testBPQ2() {
        PriorityQueue<KDPoint> pq = new BoundedPriorityQueue<>(5);
        pq.enqueue(new KDPoint(3, 4), 1);
        pq.enqueue(new KDPoint(2, 5), 2);
        pq.enqueue(new KDPoint(3,3),3);
        pq.enqueue(new KDPoint(7,6),0.5);
        pq.enqueue(new KDPoint(8,8),0.5);
        assertEquals(pq.first(),new KDPoint(7,6));
        pq.enqueue(new KDPoint(12,12),0.4);
        assertEquals(pq.first(),new KDPoint(12,12));
        pq.enqueue(new KDPoint(9,9),0.6);
        assertEquals(pq.dequeue(),new KDPoint(12,12));
        assertEquals(pq.dequeue(),new KDPoint(7,6));
    }
    @Test
    public void testBPQ3(){
        PriorityQueue<KDPoint> pq = new BoundedPriorityQueue<>(5);
        assertEquals(pq.first(),null);
        assertEquals(pq.dequeue(),null);
        assertEquals(((BoundedPriorityQueue<KDPoint>) pq).last(),null);
    }
    @Test
    public void testBPQ4(){
        PriorityQueue<KDPoint> pq = new BoundedPriorityQueue<>(5);
        pq.enqueue(new KDPoint(3, 4), 1);
        pq.enqueue(new KDPoint(2, 5), 2);
        pq.enqueue(new KDPoint(3,3),3);
        pq.enqueue(new KDPoint(7,6),0.5);
        assertEquals(pq.first(),new KDPoint(7,6));
        pq.enqueue(new KDPoint(8,8),0.5);
        pq.enqueue(new KDPoint(9,9),0.6);

    }





    @Test
    public void testhardQuadTree(){
        prQuadTree = new PRQuadTree(7,2);
        prQuadTree.insert(new KDPoint(-33, 40));
        prQuadTree.insert(new KDPoint(5, 20));
        assertEquals("After inserting 2 points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 0);
        prQuadTree.insert(new KDPoint(-40, 16));
        assertEquals("After inserting 2 points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 1);
        prQuadTree.insert(new KDPoint(-38, -10));
        assertEquals("After inserting 2 points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 1);
        prQuadTree.insert(new KDPoint(8, -8));
        assertEquals("After inserting 2 points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 1);
        prQuadTree.insert(new KDPoint(-38, -28));
        assertEquals("After inserting 2 points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 1);
        prQuadTree.insert(new KDPoint(48, -48));
        assertEquals("After inserting 2 points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 1);
        prQuadTree.insert(new KDPoint(10, 28));
         prQuadTree.insert(new KDPoint(8, -2));
        assertEquals("After inserting 2 points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 2);
        prQuadTree.insert(new KDPoint(26, -2));
        prQuadTree.insert(new KDPoint(8, -12));
        prQuadTree.insert(new KDPoint(-16, -48));
        prQuadTree.insert(new KDPoint(10, -20));
        prQuadTree.insert(new KDPoint(11, -20));
        prQuadTree.insert(new KDPoint(11, 30));
        assertEquals("After inserting 15 points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 4);
        prQuadTree.delete(new KDPoint(8,-12));
        assertEquals("After inserting 15 points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 4);
        prQuadTree.delete(new KDPoint(11,30));
        assertEquals("After inserting 15 points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 3);
        prQuadTree.delete(new KDPoint(26,-2));
        prQuadTree.delete(new KDPoint(10,-20));
        assertEquals("After inserting 15 points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 3);
        prQuadTree.delete(new KDPoint(11,-20));
        assertEquals("After inserting 15 points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 2);
        prQuadTree.delete(new KDPoint(8,-8));
        prQuadTree.delete(new KDPoint(8,-2));
        prQuadTree.delete(new KDPoint(48,-48));
        prQuadTree.delete(new KDPoint(-16,-48));
        prQuadTree.delete(new KDPoint(-38,-10));
        prQuadTree.delete(new KDPoint(-38,-28));
        prQuadTree.delete(new KDPoint(5,20));
        prQuadTree.delete(new KDPoint(-33,40));
        prQuadTree.delete(new KDPoint(-40,16));
        prQuadTree.delete(new KDPoint(10,28));
        assertEquals(prQuadTree.isEmpty(),true);


    }
}
