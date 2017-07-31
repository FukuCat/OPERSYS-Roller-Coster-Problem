import action.CarSemaphore;
import action.DemoThread;
import action.PassengerSemaphore;
import model.Data;

import java.util.ArrayList;
import java.util.Scanner;

public class Demo extends Thread{

    private static int MAX_PASSENGERS = 500;
    private static ArrayList<Thread> pList = new ArrayList<>();

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        int runType; // semaphores or monitors

        // initialize the demo
        do {
            System.out.println("[" + Data.RUN_SEMAPHORE + "] - Run with Semaphores");
            System.out.println("[" + Data.RUN_MONITOR + "] - Run with Monitors");
            System.out.print("Option: ");
            runType = Integer.parseInt(sc.nextLine());
            System.out.println();
            if (!(runType == Data.RUN_MONITOR || runType == Data.RUN_SEMAPHORE))
                System.out.println("\nInvalid Input!\n");
        } while (runType != Data.RUN_MONITOR && runType != Data.RUN_SEMAPHORE);

        if(runType == Data.RUN_SEMAPHORE)
            runSemaphore(runType);
        else
            runMonitor(runType);
    }

    public static void runMonitor(int option){
        RollerCoasterMonitor.run();
    }

    public static void runSemaphore(int runType){
        Scanner sc = new Scanner(System.in);
        long time = 0L;
        int passengers = 0; // passengers to run
        int seats = 0; // seats to run
        int interval = 0; // seconds until next wave of passengers
        int duration = 0; // seconds until demo ends
        int option; // menu stuff

        // ask how to run
        do{
            System.out.println("[1] - Run with N Passengers and C Seats");
            System.out.println("[2] - Run with fixed time, C Seats and N Passengers");
            System.out.print("Option: ");
            option = Integer.parseInt(sc.nextLine());
            System.out.println();
            if(option != 1 && option != 2)
                System.out.println("Invalid Input!\n");
        }while(option != 1 && option != 2);

        // ask number of passengers / seats / time
        switch(option){
            case 1:
                // passengers
                do{
                    System.out.println("Number of passengers.");
                    System.out.print("Passengers: ");
                    passengers = Integer.parseInt(sc.nextLine());
                    System.out.println();
                    if(passengers <= 0)
                        System.out.println("Invalid Input!\n");
                }while(passengers <= 0);
                // seats
                do{
                    System.out.println("Seats must be at most of size N/2 where N is the number of Passengers");
                    System.out.print("Seats: ");
                    seats = Integer.parseInt(sc.nextLine());
                    System.out.println();
                    if(seats <= 0 && seats/2 > passengers)
                        System.out.println("Invalid Input!\n");
                }while(seats <= 0 && seats/2 > passengers);
                break;
            case 2:
                // passengers
                do{
                    System.out.println("Number of passengers per wave.");
                    System.out.print("Passengers: ");
                    passengers = Integer.parseInt(sc.nextLine());
                    System.out.println();
                    if(passengers <= 0)
                        System.out.println("Invalid Input!\n");
                }while(passengers <= 0);
                // seats
                do{
                    System.out.println("Number of total seats.");
                    System.out.print("Seats: ");
                    seats = Integer.parseInt(sc.nextLine());
                    System.out.println();
                    if(seats <= 0)
                        System.out.println("Invalid Input!\n");
                }while(seats <= 0);
                // interval
                do{
                    System.out.println("Interval is how many seconds until next wave of passengers will be added.");
                    System.out.print("Interval: ");
                    interval = Integer.parseInt(sc.nextLine());
                    System.out.println();
                    if(interval <= 0)
                        System.out.println("Invalid Input!\n");
                }while(interval <= 0);
                // duration
                do{
                    System.out.println("Duration is how many seconds the demo will run.");
                    System.out.print("Duration: ");
                    duration = Integer.parseInt(sc.nextLine());
                    System.out.println();
                    if(duration <= 0)
                        System.out.println("Invalid Input!\n");
                }while(duration <= 0);
                break;
        }

        // run demo
        // create car threads
        Data.getInstance().initialize(passengers, seats, runType);
        Data data = Data.getInstance();
        for(int i = 0; i < data.getMaxCars(); i++) {
            Thread t = null;
            t = new CarSemaphore(i);
            t.start();
            pList.add(t);

        }

        // create passenger threads
        switch (option){
            case 1: // add passengers once
                for(int i = 0; i < passengers; i++) {
                    Thread t = null;
                    t = new PassengerSemaphore(i);
                    t.start();
                    pList.add(t);
                }
                break;
            case 2: // add passengers on interval
                time = System.nanoTime();
                int total = 0;
                while((double)(System.nanoTime() - time)/ 1000000000.0 < (double)duration){
                    total+=passengers;
                    System.out.println("[Time: "+(System.nanoTime() - (double)time)/ 1000000000.0+"]: Adding "+passengers+" passengers" +
                            "\n[Time: "+((double)time - System.nanoTime())/ 1000000000.0+"]: Total "+total+" passengers");
                    for(int i = 0; i < passengers; i++) {
                        Thread t = null;
                        t = new PassengerSemaphore(i);
                        t.start();
                        pList.add(t);
                        try {
                            data.getSemStats().acquire(1);
                            data.setPassengerCount(passengers + Data.getInstance().getPassengerCount());
                            data.getSemStats().release(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // wait a few seconds and add again
                    try {
                        Thread.sleep(interval * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for(int i = 0; i < pList.size(); i++)
                    ((DemoThread)pList.get(i)).quit();
                System.out.println("Demo End!");
                break;
        }


    }
}
