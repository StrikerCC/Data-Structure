package projects.graph;

import projects.graph.utils.Neighbor;
import projects.graph.utils.NeighborList;

import java.util.HashSet;
import java.util.Set;
/**
 * <p>{@link AdjacencyListGraph} is a {@link Graph} implemented as an adjacency list, i.e a one-dimensional array of linked lists,
 * where A(i) is a linked list containing the neighbors of node i and the corresponding edges' weights. <b>The neighbors of a given node are defined as the nodes it  points to</b> (if any). </p>
 *
 * <p>Other implementations besides linked lists are possible (e.g BSTs over the weight of the edge), yet for this project we
 * will keep it simple and stick to that basic implementation. One of its advantages is that, because the lists do not need
 * to be sorted in any way, the insertion of a new edge is a O(1) operation (find the list corresponding to the source node in O(1)
 * and add the new list node up front.</p>
 *
 * @author --- Yexin Wu ---
 *
 * @see Graph
 * @see AdjacencyMatrixGraph
 * @see SparseAdjacencyMatrixGraph
 * @see NeighborList
 */
public class AdjacencyListGraph extends Graph {

    /* *********************************************************** */
    /* THE FOLLOWING DATA FIELD MAKES UP THE INNER REPRESENTATION */
    /* OF YOUR GRAPH. YOU SHOULD NOT CHANGE THIS DATA FIELD!      */
    /* *********************************************************  */

    private NeighborList[] list;

    /* ***************************************************** */
    /* PLACE ANY EXTRA PRIVATE DATA MEMBERS OR METHODS HERE: */
    /* ***************************************************** */
    private int num_node;
    /* ************************************************** */
    /* IMPLEMENT THE FOLLOWING PUBLIC METHODS. MAKE SURE  */
    /* YOU ERASE THE LINES THAT THROW EXCEPTIONS.         */
    /* ************************************************** */

    /**
     * A default (no-arg) constructor for {@link AdjacencyListGraph} <b>should</b> exist,
     * even if you don't do anything with it.
     */
    public AdjacencyListGraph(){
        num_node = 0;
        list = new NeighborList[num_node];
    }
    public AdjacencyListGraph(int num_node){
        this.num_node = num_node;
        list = new NeighborList[num_node];
        for(int i = 0; i < list.length; i++)
            list[i] = new NeighborList();

    }

    @Override
    public void addNode() {
        num_node += 1;
        NeighborList[] new_list = new NeighborList[num_node];

        for(int i = 0; i < new_list.length; i++)
            new_list[i] = new NeighborList();

        for(int i =0; i < list.length; i++)
            new_list[i] = list[i];

        list = new_list;
    }



    @Override
    /*when we want to add edge, we need to check if it already exits, then it will cosr O(E)*/
    public void addEdge(int source, int dest, int weight) {
        if( weight < 0 || weight > INFINITY)
            throw new RuntimeException("weight value is not right");
        else if( source > (num_node - 1) || dest > (num_node - 1)){
            throw new RuntimeException("source and dest do not exist");
        }
        else{
            if(list[source].containsNeighbor(dest)){
                if(weight == 0) {
                    list[source].remove(dest);
                }
                else
                    list[source].setWeight(dest,weight);
            }
            else
                if (weight!=0)
                    list[source].addFront(dest,weight);
        }
    }

    public  boolean check_node(int node){
        if(node >= 0 && node < num_node)
            return true;
        else
            return false;
    }
    @Override
    public void deleteEdge(int source, int dest) {
        if(this.check_node(source))
           list[source].remove(dest);
    }

    @Override
    public boolean edgeBetween(int source, int dest) {
        if(this.check_node(source) && this.check_node(dest))
            return list[source].containsNeighbor(dest);
        else
            return false;
    }

    @Override
    public int getEdgeWeight(int source, int dest) {
        if(this.check_node(source) && this.check_node(dest))
            return list[source].getWeight(dest);
        else
            return 0;
    }

    @Override
    public Set<Integer> getNeighbors(int node) {
        if(!this.check_node(node))
            throw new RuntimeException("node not exists");
        else{
            Set<Integer> neighbour = new HashSet<Integer>();
            for(Neighbor e: list[node] )
                neighbour.add(e.getNode());
            return neighbour;
        }
    }

    @Override
    public int getNumNodes() {
         return num_node;
    }

    @Override
    public int getNumEdges() {
        int num_edge = 0;
        for (NeighborList e: list){
            num_edge += e.getCount();
        }
        return num_edge;
    }

    @Override
    public void clear() {
        num_node = 0;
        list = new NeighborList[num_node];
    }

    /* Methods specific to this class follow. */

    /**
     * Transforms this into an instance of {@link AdjacencyMatrixGraph}. This is an O(E) operation.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     *   <b>cannot</b> implement it with the line of code toSparseAdjacencyMatrixGraph().toAdjacencyMatrixGraph().
     *
     * @return An instance of {@link AdjacencyMatrixGraph}.
     */
    public AdjacencyMatrixGraph toAdjacencyMatrixGraph(){
        AdjacencyMatrixGraph am = new AdjacencyMatrixGraph(num_node);
        for (int i = 0;i < list.length;i++){
            for(Neighbor e: list[i]){
                am.addEdge(i,e.getNode(),e.getWeight());
            }
        }
        return am;

    }

    /**
     * Transforms this into an instance of {@link AdjacencyMatrixGraph}. This is an O(E) operation.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     * <b>cannot</b> implement it with the line of code toAdjacencyMatrixGraph().toSparseAdjacencyMatrixGraph().
     *
     * @return An instance of {@link AdjacencyMatrixGraph}.
     */
    public SparseAdjacencyMatrixGraph toSparseAdjacencyMatrixGraph(){
        SparseAdjacencyMatrixGraph spag = new SparseAdjacencyMatrixGraph(num_node);
        for (int i = 0;i < list.length;i++){
            for(Neighbor e: list[i]){
                spag.addEdge(i,e.getNode(),e.getWeight());
            }
        }
        return spag;


    }
}