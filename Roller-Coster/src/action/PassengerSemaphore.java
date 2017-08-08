package action;

import model.Data;
import utils.Debug;

import java.util.concurrent.Semaphore;

public class PassengerSemaphore extends Thread implements DemoThread{

    private int passengerId;

    private boolean isDone = false;
    private int maxCars;

    private Semaphore semBoardBarrier;
    private Semaphore semLoadBarrier;
    private Semaphore semUnboardBarrier;
    private Semaphore semFirstInFirstOut;
    private Semaphore semEndPassenger;
    private Semaphore semM1;

    private Semaphore semLog;


    public PassengerSemaphore(int passengerId){
        setPassengerId(passengerId);
        semBoardBarrier = Data.getInstance().getSemBoardBarrier();
        semLoadBarrier = Data.getInstance().getSemLoadBarrier();
        semUnboardBarrier = Data.getInstance().getSemUnboardBarrier();
        semFirstInFirstOut = Data.getInstance().getSemFirstInFirstOut();
        semM1 = Data.getInstance().getSemM1();
        semEndPassenger = Data.getInstance().getSemEndPassenger();
        this.maxCars = Data.getInstance().getMaxCars();
        semLog = Data.getInstance().getSemLog();
    }

    @Override
    public void run() {
        try {
        while(!isDone){
            // ensures only N passengers can ride where N = number of cars
            semFirstInFirstOut.acquire(1);
            board();

            // make all passengers run before releasing permits for CarSemaphore
            semM1.acquire(1);
            Data.getInstance().incrementPassengerCount();
            if(Data.getInstance().getPassengerCount() >= maxCars){
                semLoadBarrier.release(maxCars);
                semBoardBarrier.release(maxCars);
            }
            semM1.release(1);
            semBoardBarrier.acquire(1);
            // wait for all cars to execute run();
            semUnboardBarrier.acquire(1);
            unboard();
            // unboard all passengers before proceeding
            semM1.acquire(1);
            Data.getInstance().incrementPassengerCount();
            if(Data.getInstance().getPassengerCount() >= maxCars){
                Data.getInstance().setPassengerCount(0);
                semEndPassenger.release(maxCars);
            }
            semM1.release(1);

            semEndPassenger.acquire(1);
            semFirstInFirstOut.release(1);
            // for checking stats
            //stats();
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void board() throws InterruptedException{
        try {
            semLog.acquire(1);
            Debug.log("PassengerSemaphore.board", passengerId +" IN");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semLog.release(1);
        }
    }
    public void unboard() throws InterruptedException{
        try {
            semLog.acquire(1);
            Debug.log("PassengerSemaphore.unboard", passengerId +" OUT");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semLog.release(1);
        }
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }


    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public void quit() {
        setDone(true);
    }
}
