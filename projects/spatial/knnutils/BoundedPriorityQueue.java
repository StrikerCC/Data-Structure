package projects.spatial.knnutils;

import projects.spatial.kdpoint.KDPoint;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Comparator;
import projects.spatial.knnutils.PriorityQueueNode;


/**
 * <p>{@link BoundedPriorityQueue} is an {@link Iterable} priority queue whose number of elements
 * is bounded above. Insertions are such that if the queue's provided capacity is surpassed,
 * its length is not expanded, but rather the maximum priority element is ejected
 * (which could be the element just attempted to be enqueued).</p>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 * @author  ---- YOUR NAME HERE! -----
 *
 */
public class BoundedPriorityQueue<T> implements PriorityQueue<T>{

	private static RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");

	/* *************************************************************************
	 ************** PLACE YOUR PRIVATE METHODS AND FIELDS HERE: ****************
	 ***************************************************************************/
	final int max_size;
	int curr_size;
	int insertion_order;
	java.util.PriorityQueue<PriorityQueueNode<T>> qe;
	boolean moified;




	/* ***************************************************************************** */
	/* ******************* PUBLIC (INTERFACE) METHODS ****************************** */
	/* ***************************************************************************** */


	/**
	 * Standard constructor. Creates a {@link BoundedPriorityQueue} of the provided size.
	 * @param size The number of elements that the {@link BoundedPriorityQueue} instance is allowed to store.
	 * @throws RuntimeException if size &lt; 1.
	 */
	public BoundedPriorityQueue(int size){
		this.max_size = size;
		this.curr_size = 0;
		qe = new java.util.PriorityQueue(this.max_size);
		insertion_order = 0;
		moified = false;
	}


	/**
	 * <p>Insert element in the Priority Queue, according to its priority.
	 * <b>Lower is better.</b> We allow for <b>non-integer priorities</b> such that the Priority Queue
	 * can be used for orderings where the prioritization is <b>not</b> rounded to integer quantities, such as
	 * Euclidean Distances in KNN queries. </p>
	 *
	 * @param element The element to insert in the queue.
	 * @param priority The priority of the element.
	 *
	 * @see projects.spatial.kdpoint.KDPoint#distanceSquared(KDPoint)
	 */
	//use deep copy in this structure
	public void enqueue(T element, double priority) {
		PriorityQueueNode<T> node;
		if(this.curr_size < this.max_size){
			node = new PriorityQueueNode<T>(element,priority,insertion_order);
			qe.add(node);
			moified = true;
			this.curr_size++;
			insertion_order++;
		}
		else{
			PriorityQueueNode<T> lastnode = this.nodelast();
			double p = lastnode.getPriority();
			if(p > priority) {
				node = new PriorityQueueNode<T>(element,priority,insertion_order);
				qe.remove(lastnode);
				qe.add(node);
				moified = true;
				insertion_order++;
			}
		}
	}

	/**
	 * Return the <b>minimum priority element</b> in the queue, <b>simultaneously removing it</b> from the structure.
	 * @return The minimum priority element in the queue, or null if the queue is empty.
	 */
	public T dequeue() {
		if(this.curr_size == 0){
			return null;
		}
		T t = qe.poll().getData();
		if(t!=null){
			this.curr_size--;
			moified = true;
		}
		return t;
	}

	/**
	 * Return, <b>but don't remove</b>, the <b>minimum priority element</b> from the queue.
	 * @return The minimum priority element of the queue, or null if the queue is empty.
	 */
	public T first() {
		if(this.curr_size == 0)
			return null;
		return qe.peek().getData();
	}


	/**
	 * <p>Return, <b>but don't remove</b>, the <b>maximum priority element</b> from the queue. This operation is inefficient
	 * in MinHeap - based Priority Queues. That's fine for the purposes of our project; you should feel free to
	 * implement your priority queue in any way provides correctness and elementary efficiency of operations.</p>
	 * @return The maximum priority element of the queue, or null if the queue is empty.
	 */
	public T last() {
		if(this.size() == 0){
			return null;
		}
		PriorityQueueNode<T> last = qe.peek();
		for(PriorityQueueNode element: qe){
			if(element.compareTo(last) > 0){
				last = element;
			}
		}
		return last.getData();
	}

	public PriorityQueueNode<T> nodelast(){
		if(this.size() == 0){
			return null;
		}
		PriorityQueueNode<T> last = qe.peek();
		for(PriorityQueueNode element: qe){
			if(element.compareTo(last) > 0){
				last = element;
			}
		}
		return last;
	}

	/**
	 * Query the queue about its size. <b>Empty queues have a size of 0.</b>
	 * @return The size of the queue. Returns 0 if the queue is empty.
	 */
	public int size() {
		return this.curr_size;
	}

	/**
	 * Query the queue about emptiness. A queue is empty <b>iff</b> it contains <b>0 (zero)</b> elements.
	 * @return true iff the queue contains <b>0 (zero)</b> elements.
	 */
	public boolean isEmpty() {
		return this.curr_size == 0;
	}

	@Override
	public Iterator<T> iterator() {
		return new CustomIterator<T>(this);// Erase this after you implement the method!
	}

	class CustomIterator<T> implements Iterator<T>{
		java.util.PriorityQueue<PriorityQueueNode<T>> q;
		CustomIterator(BoundedPriorityQueue obj){
			q = new java.util.PriorityQueue(obj.qe);
			moified = false;
		}
		public boolean hasNext(){
			return !q.isEmpty();
		}
		public T next(){
			if(moified == true){
				throw new ConcurrentModificationException("cannot modiferd");
			}
			return q.poll().getData();
		}
		public  void remove(){
			throw new UnsupportedOperationException("remove is not supported");
		}
	}
}