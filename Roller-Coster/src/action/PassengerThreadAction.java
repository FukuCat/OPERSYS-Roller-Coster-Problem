package action;

import model.Data;
import thread_pool.ThreadAction;
import utils.Debug;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PassengerThreadAction implements ThreadAction {

    private int type;
    private int id;

    private int maxPassengers;
    private int maxCars;

    private Semaphore semA;
    private Semaphore semB;
    private Semaphore semD;
    private Semaphore semE;
    private Semaphore semMutex;

    private Lock lockA;
    private Lock lockC;
    private Condition condA1;
    private Condition condB1;
    private Condition condC1;

    public PassengerThreadAction(int id, int maxPassengers, int maxCars, int type){
        setId(id);
        this.type = type;
        semA = Data.getInstance().getSemA();
        semB = Data.getInstance().getSemB();
        semD = Data.getInstance().getSemD();
        setSemE(Data.getInstance().getSemE());
        semMutex = Data.getInstance().getSemM1();
        this.maxCars = maxCars;
        this.maxPassengers = maxPassengers;
        lockA = Data.getInstance().getLockA();
        lockC = Data.getInstance().getLockC();
        condA1 = Data.getInstance().getCondA1();
        condB1 = Data.getInstance().getCondB1();
        condC1 = Data.getInstance().getCondC1();
    }

    @Override
    public void execute() {
        if(type == 0)
            runSemaphore();
        else
            runMonitor();
    }

    public void runSemaphore(){
        try {
        while(true){
            // ensures only N passengers can ride where N = number of cars
            semE.acquire(1);
            board();
            // make all passengers run before releasing permits for CarThreadAction
            semA.release(1);
            semA.acquire(maxCars);
            semMutex.acquire(1);
                Data.getInstance().incrementPassengerCount();
                if(Data.getInstance().getPassengerCount() < maxCars)
                    semA.release(maxCars);
                else {
                    Data.getInstance().setPassengerCount(0);
                    semA.release(1);
                    semB.release(maxCars);
                }
            semMutex.release(1);
            // wait for all cars to execute run();
            semD.acquire(1);
            unboard();
            semE.release(1);
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void runMonitor(){
        try{
        while(true){
            lockC.lock();
            Data.getInstance().incrementPassengerCount();
            condC1.signalAll();
            while (Data.getInstance().getPassengerCount() > maxCars)
                condC1.await();
            lockC.unlock();
            board();
            lockA.lock();
            condB1.signalAll();
            while (Data.getInstance().getCarCount() < maxCars)
                condA1.await();
            lockA.unlock();
            unboard();
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void board(){
        Debug.log("PassengerThreadAction.board",id+" IN");
    }
    public void unboard(){
        Debug.log("PassengerThreadAction.unboard",id+" OUT");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Semaphore getSemE() {
        return semE;
    }

    public void setSemE(Semaphore semE) {
        this.semE = semE;
    }

    public Condition getCondA1() {
        return condA1;
    }

    public void setCondA1(Condition condA1) {
        this.condA1 = condA1;
    }

    public Condition getCondB1() {
        return condB1;
    }

    public void setCondB1(Condition condB1) {
        this.condB1 = condB1;
    }

    public Condition getCondC1() {
        return condC1;
    }

    public void setCondC1(Condition condC1) {
        this.condC1 = condC1;
    }
}
