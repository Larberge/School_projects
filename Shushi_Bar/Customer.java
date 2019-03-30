package OS;

import static java.lang.Thread.sleep;
import java.util.Random;


//This class implements a customer, which is used for holding data and update the statistics
public class Customer {
    private static SynchronizedInteger customerCounter = new SynchronizedInteger(1);
    private Random ran = new Random();
    private int customerID;
    private int sizeOfOrder;
    private static SushiBar.OrderType[] orderTypes = {SushiBar.OrderType.TAKE_AWAY, SushiBar.OrderType.EAT_IN_BAR};
    private SushiBar.OrderType myOrder;

    /**
     *  Creates a new Customer.
     *  Each customer should be given a unique ID
     */
    public Customer() {
        this.customerID = newCustomerID();
        this.sizeOfOrder = ran.nextInt(SushiBar.maxOrder);
        this.myOrder = orderTypes[ran.nextInt(2)];
    }

    private int newCustomerID(){
        int ID = customerCounter.get();
        customerCounter.increment();
        return ID;
    }
    /**
     * Here you should implement the functionality for ordering food as described in the assignment.
     */
    public synchronized void order() throws InterruptedException {
        SushiBar.write("Customer #" + getCustomerID() + " is now eating.");
        if(myOrder == SushiBar.OrderType.TAKE_AWAY){
            SushiBar.takeawayOrders.add(this.sizeOfOrder);
        }
        else{
            SushiBar.servedOrders.add(this.sizeOfOrder);
        }
        SushiBar.totalOrders.add(this.sizeOfOrder);

        sleep(SushiBar.customerWait);

    }

    /**
     *
     * @return Should return the customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    // Add more methods as you see fit
}
