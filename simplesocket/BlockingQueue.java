package simplesocket;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<T> {
	private Queue<T> queue;
	private long size;
	Lock lock;
	Condition isFullCondition;
	Condition isEmptyCondition;

	BlockingQueue() {
		this(Integer.MAX_VALUE);
	}

	BlockingQueue(int size) {
		queue = new LinkedList<T>();
		this.size = size;
		lock = new ReentrantLock();
		isFullCondition = lock.newCondition();
		isEmptyCondition = lock.newCondition();
	}

	public void add(T element) {
		lock.lock();
		try {
			while (isFull()) {
				try {
					isFullCondition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			queue.add(element);
			System.out.println("after add size: " + queue.size());
			isEmptyCondition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public T poll() {
		T ret = null;
		lock.lock();
		try {
			while (isEmpty()) {
				try {
					isEmptyCondition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			ret = queue.poll();
			System.out.println("after poll size:" + queue.size());
			isFullCondition.signalAll();
		} finally {
			lock.unlock();
		}
		return ret;
	}

	private boolean isFull() {
		return queue.size() == size;
	}

	private boolean isEmpty() {
		return queue.isEmpty();
	}
}
