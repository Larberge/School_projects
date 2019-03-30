package OS;

import static java.lang.Thread.sleep;

/**
 * This class implements the consumer part of the producer/consumer problem.
 * One waitress instance corresponds to one consumer.
 */
public class Waitress implements Runnable {
    Customer newCustomer;
    private WaitingArea WA;

    /**
     * Creates a new waitress. Make sure to save the parameter in the class
     *
     * @param waitingArea The waiting area for customers
     */
    public Waitress(WaitingArea waitingArea) {
        this.WA = waitingArea;
    }

    /**
     * This is the code that will run when a new thread is
     * created for this instance
     */
    @Override
    public void run()  {
        while(SushiBar.isOpen || ! this.WA.isEmpty()) {
            synchronized (this.WA) {
                while (this.WA.isEmpty()) {
                    try {
                        this.WA.wait();
                    } catch (Exception e) {
                        SushiBar.write(e.getMessage());
                    }
                }
                this.newCustomer = this.WA.next();
                this.WA.notifyAll();
            }
            SushiBar.write("Customer #" + newCustomer.getCustomerID() + " is now being served.");
            SushiBar.customerCounter.increment();
            try {
                sleep(SushiBar.waitressWait);
                newCustomer.order();
            } catch (InterruptedException e) {

            }
            SushiBar.write("Customer #" + newCustomer.getCustomerID() + " is now leaving.");
        }
        if(! SushiBar.hasPrintedStatistics){
            SushiBar.hasPrintedStatistics = true;
            try {
                sleep(1000);
            }
            catch (Exception e){
                SushiBar.write(e.getMessage());
            }
            SushiBar.printStatistics();
        }
    }
}



