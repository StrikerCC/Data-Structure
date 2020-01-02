package projects.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>{@link AdjacencyMatrixGraph} is a {@link Graph} implemented as an <b>adjacency matrix</b>. An adjacency matrix
 * is a V x V matrix where M(i, j) is the weight of the edge from i to j. If there is no edge between i and j,
 * the weight should be zero. </p>
 *
 * <p>Adjacency matrices answer {@link #edgeBetween(int, int)} in O(1) time. Insertion and deletion of edges, as well
 * as retrieval of the weight of a given edge  are all O(1) operations as well. Retrieval of all neighbors of a given node
 * happens in O(V) time. </p>
 *
 * <p>The main drawbacks of adjacency matrices are: </p>
 *  <ol>
 *      <li>They occupy O(V^2) <b>contiguous</b> memory space, which for sparse graphs can be a significant memory footprint. </li>
 *      <li>addNode() runs in O(V^2) time, since new array storage needs to be allocated for the extra row and column,
 *      and the old data need be copied to the new array. </li>
 *  </ol>
 *
 * @author --- Yexin Wu---
 * @see Graph
 * @see SparseAdjacencyMatrixGraph
 * @see AdjacencyListGraph
 */
public class AdjacencyMatrixGraph extends Graph {

    /* ****************************************************** */
    /* THE FOLLOWING DATA FIELD IS THE INNER REPRESENTATION */
    /* OF YOUR GRAPH. YOU SHOULD NOT CHANGE THIS DATA FIELD! */
    /* ******************************************************  */

    private int[][] matrix;


    /* ***************************************************** */
    /* PLACE ANY EXTRA PRIVATE DATA MEMBERS OR METHODS HERE: */
    /* ***************************************************** */
    private int num_node;
    /* ************************************************** */
    /* IMPLEMENT THE FOLLOWING PUBLIC METHODS. MAKE SURE  */
    /* YOU ERASE THE LINES THAT THROW EXCEPTIONS.         */
    /* ************************************************** */

    /**
     * A default (no-arg) constructor for {@link AdjacencyMatrixGraph} <b>should</b> exist,
     * even if you don't do anything with it.
     */
    public AdjacencyMatrixGraph()  {
        num_node = 0;
        matrix = new int[num_node][num_node];
    }
    public AdjacencyMatrixGraph(int num_node){
        this.num_node = num_node;
        matrix = new int[num_node][num_node];
    }

    @Override
    public void addNode(){
        num_node = num_node + 1;
        int[][] new_matrix = new int[num_node][num_node];
        /* need a copy process here*/
        for (int row = 0; row < matrix.length; row++){
            for (int column = 0;column < matrix[row].length;column++){
                new_matrix[row][column] = matrix[row][column];
            }
        }
        /* all of items has been copied to the new matrix*/
        matrix = new_matrix;
    }

    @Override
    public void addEdge(int source, int dest, int weight) {
        // I throw an AssertionError if either node isn't within parameters. Behavior open to implementation according to docs.
        if( weight < 0 || weight > INFINITY)
            throw new RuntimeException("weight value is not right");
        else if( source > (num_node - 1) || dest > (num_node - 1)){
            throw new RuntimeException("source and dest do not exist");
        }
        else{
            matrix[source][dest] = weight;
        }

}


    @Override
    public void deleteEdge(int source, int dest) {
        if (source >= 0 && source < num_node && dest >= 0 && dest < num_node)
            matrix[source][dest] = 0;

    }

    @Override
    public boolean edgeBetween(int source, int dest) {
        if(source >= 0 && source < num_node && dest >= 0 && dest < num_node){
            if (matrix[source][dest] > 0)
                return true;
        }
        return false;
    }

    @Override
    public int getEdgeWeight(int source, int dest) {
        if(source >= 0 && source < num_node && dest >= 0 && dest < num_node)
            return matrix[source][dest];
        else
            return 0;
    }

    @Override
    public Set<Integer> getNeighbors(int node) {
        if (node < 0 || node >= num_node)
            throw new RuntimeException("node not exists");

        Set<Integer> neighbour = new HashSet<Integer>();
        /*need to figure out what is returned by for
        * each loop*/
        for (int i = 0; i < num_node ; i++){
            if(matrix[node][i] > 0)
                neighbour.add(i);
        }
        return neighbour;
    }

    @Override
    public int getNumNodes(){
        return num_node;
    }

    @Override
    public int getNumEdges() {
        int num_edge = 0;
        for(int[] row: matrix){
            for(int edge: row){
                if(edge > 0)
                    num_edge += 1;
            }
        }
        return num_edge;
    }

    @Override
    public void clear() {
        matrix = new int[0][0];
        num_node = 0;
    }


    /* Methods specific to this class follow. */

    /**
     * Returns a sparsified representation of the adjacency matrix, i.e a linked list of its non-null elements (non-zero,
     * in this case) and their coordinates. The matrix should be scanned in <b>row-major order</b> to populate
     * the elements of the list (<b>and we test for proper element insertion order!</b>).
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     * <b>cannot</b> implement it with the line of code toAdjacencyListGraph().toSparseAdjacencyMatrixGraph().
     *
     * @return A {@link SparseAdjacencyMatrixGraph} instance.
     */
    public SparseAdjacencyMatrixGraph toSparseAdjacencyMatrixGraph(){
        SparseAdjacencyMatrixGraph sparse_matrix = new SparseAdjacencyMatrixGraph(num_node);
        for(int i=0; i < matrix.length; i++){
            for(int j=0; j < matrix[i].length; j++) {
                sparse_matrix.addEdge(i,j,matrix[i][j]);
            }
        }
        return sparse_matrix;
    }

    /**
     * Returns a representation of the {@link Graph} as an {@link AdjacencyListGraph}. Remember that an {@link AdjacencyListGraph}
     * is implemented as an array of linked lists, where A(i) is a linked list containing the neighbors of node i.  Remember that an
     * {@link AdjacencyListGraph} is implemented as an array of linked lists, where A(i) is a linked list containing the neighbors of node i.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     *    <b>cannot</b> implement it with the line of code toSparseAdjacencyMatrixGraph().toAdjacencyListGraph().
     *
     * @return  An {@link AdjacencyListGraph} instance.
     */
    public AdjacencyListGraph toAdjacencyListGraph(){
        AdjacencyListGraph adlist = new AdjacencyListGraph(num_node);
        for(int i=0; i < matrix.length; i++){
            for(int j=0; j < matrix[i].length; j++) {
                adlist.addEdge(i,j,matrix[i][j]);
            }
        }
        return adlist;
    }
}