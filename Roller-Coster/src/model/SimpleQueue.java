package model;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleQueue<T> {
	private Queue<T> queue;
	private int size;
	
	public SimpleQueue(){
		setQueue(new LinkedList<>());
		size = 0;
	}
	
	// queue operations
	public T enqueue(T e){
		if(queue.add(e)){
			size++;
			return e;}
		return null;
	}
	
	public T dequeue(){
		T result = queue.poll();
		if(result != null)
			size--;
		return result;
	}
	
	public T head(){
		return queue.peek();
	}
	
	public T tail(){
		T result = null;
		for (T temp : queue)
		     result = temp;
		return result;
	}
	
	public boolean isEmpty(){
		return queue.isEmpty();
	}
	
	// other

	public Queue<T> getQueue() {
		return queue;
	}

	public void setQueue(Queue<T> queue) {
		this.queue = queue;
	}
	
	// derived from normal queue functions
	
	public T rotate(){
		T temp = dequeue();
		if(temp != null)
			enqueue(temp);
		return temp;
	}

	public int getSize() {
		return size;
	}
}
