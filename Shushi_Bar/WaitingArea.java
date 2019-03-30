package OS;

import java.util.ArrayList;

/**
 * This class implements a waiting area used as the bounded buffer, in the producer/consumer problem.
 */
public class WaitingArea {
    private ArrayList<Customer> queue = new ArrayList<>();
    private int size;

    /**
     * Creates a new waiting area.
     *
     * @param size The maximum number of Customers that can be waiting.
     */
    public WaitingArea(int size) {
        this.size = size;
    }

    /**
     * This method should put the customer into the waitingArea
     *
     * @param customer A customer created by Door, trying to enter the waiting area
     */

    public synchronized void enter(Customer customer) {
        queue.add(customer);
    }

    /**
     * @return The customer that is first in line.
     */
    public synchronized Customer next() {
        return queue.remove(0);
    }

    // Add more methods as you see fit

    public boolean isFull(){
        return this.queue.size() == size;
    }

    public boolean isEmpty(){
        return this.queue.size() == 0;
    }
}
