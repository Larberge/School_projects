package OS;

/**
 * This class implements the Door component of the sushi bar assignment
 * The Door corresponds to the Producer in the producer/consumer problem
 */
import java.util.Random;

import static java.lang.Thread.sleep;


public class Door implements Runnable {
    private WaitingArea WA;
    private Random ran = new Random();


    /**
     * Creates a new Door. Make sure to save the
     * @param waitingArea   The customer queue waiting for a seat
     */
    public Door(WaitingArea waitingArea) {
        this.WA = waitingArea;
    }

    /**
     * This method will run when the door thread is created (and started)
     * The method should create customers at random intervals and try to put them in the waiting area
     */
    @Override
    public void run() {
        while(SushiBar.isOpen) {
            int timeUntilNextCustomer = ran.nextInt(SushiBar.doorWait);
            try {
                sleep(timeUntilNextCustomer);
            } catch (InterruptedException e) {
                SushiBar.write("Error when waiting for door");
            }
            Customer newCustomer = new Customer();
            synchronized (WA) {
                while (WA.isFull()) {
                    try {
                        WA.wait();
                    } catch (InterruptedException e) {
                        SushiBar.write(e.getMessage());

                    }
                }
                WA.enter(newCustomer);
                WA.notifyAll();
            }
            SushiBar.write("Customer #" + newCustomer.getCustomerID() + " is now waiting.");
        }
        SushiBar.write("The restaurant is not taking in any new customers, door is now closing.");
        SushiBar.write("Number of customers served: " + SushiBar.customerCounter.get());
    }
}
