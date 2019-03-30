package OS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SushiBar {

    //SushiBar settings
    private static int waitingAreaCapacity = 15;
    private static int waitressCount = 8;
    private static int duration = 4;
    public static int maxOrder = 10;
    public static int waitressWait = 50; // Used to calculate the time the waitress spends before taking the order
    public static int customerWait = 2000; // Used to calculate the time the customer spends eating
    public static int doorWait = 100; // Used to calculate the interval at which the door tries to create a customer
    public static boolean isOpen = true;
    public static boolean hasPrintedStatistics = false;

    //Creating log file
    private static File log;
    private static String path = "../";

    //Variables related to statistics
    public static SynchronizedInteger customerCounter;
    public static SynchronizedInteger servedOrders;
    public static SynchronizedInteger takeawayOrders;
    public static SynchronizedInteger totalOrders;

    
    //An easy way to have different types of orders
    public enum OrderType {
        TAKE_AWAY, EAT_IN_BAR
    }

    public static void main(String[] args) {
        log = new File(path + "sushoBarLog.txt");

        //Initializing shared variables for counting number of orders
        customerCounter = new SynchronizedInteger(0);
        totalOrders = new SynchronizedInteger(0);
        servedOrders = new SynchronizedInteger(0);
        takeawayOrders = new SynchronizedInteger(0);

        WaitingArea WA = new WaitingArea(waitingAreaCapacity);
        new Clock(duration);

        List<Thread> runningThreads = new ArrayList<>();
        Thread door = new Thread(new Door(WA));
        runningThreads.add(door);
        for (int i = 0; i < waitressCount; i++) {
            runningThreads.add(new Thread(new Waitress(WA)));
        }

        runningThreads.forEach(t -> t.start());
    }

    //Writes actions in the log file and console
    public static void write(String str) {
        try {
            FileWriter fw = new FileWriter(log.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(Clock.getTime() + ", " + str + "\n");
            bw.close();
            System.out.println(Clock.getTime() + ", " + str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void printStatistics(){
        SushiBar.write("....................................................");
        SushiBar.write("Number of served orders: " + SushiBar.servedOrders.get());
        SushiBar.write("Number of takeaway orders: " + SushiBar.takeawayOrders.get());
        SushiBar.write("Number of total orders: " + SushiBar.totalOrders.get());
        SushiBar.write("Number of served customers: " + SushiBar.customerCounter.get());
        SushiBar.write("....................................................");

    }
}
