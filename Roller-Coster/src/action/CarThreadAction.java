package action;

import model.Data;
import thread_pool.ThreadAction;
import utils.Debug;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class CarThreadAction implements ThreadAction {

    private int type;
    private int id;

    private int maxPassengers;
    private int maxCars;
    private int maxTurns;

    private Semaphore semA;
    private Semaphore semB;
    private Semaphore semC;
    private Semaphore semD;

    private Semaphore[] semArray;

    private Semaphore semMutex;

    private Lock lockB;
    private Condition condA1;
    private Condition condB1;
    private Condition condC1;

    public CarThreadAction(int id, int maxPassengers, int maxCars, int type){
        setId(id);
        this.type = type;
        semA = Data.getInstance().getSemA();
        semB = Data.getInstance().getSemB();
        semC = Data.getInstance().getSemC();
        semD = Data.getInstance().getSemD();
        semMutex = Data.getInstance().getSemM2();
        this.maxCars = maxCars;
        this.maxPassengers = maxPassengers;
        lockB = Data.getInstance().getLockB();
        condA1 = Data.getInstance().getCondA1();
        condB1 = Data.getInstance().getCondB1();
        condC1 = Data.getInstance().getCondC1();
        semArray = Data.getInstance().getSemArray();
        maxTurns = Data.getInstance().getMaxTurns();
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
            semArray[Data.getInstance().getCurrTurn()].release(1);
            load();
            semB.acquire(1);
            run();
            semC.release(1);
            semC.acquire(maxCars);
            unload();
            semMutex.acquire(1);
                Data.getInstance().incrementCarCount();
                if(Data.getInstance().getCarCount() == 0)
                    Data.getInstance().incrementCurrTurn();
                if(Data.getInstance().getCarCount() < maxCars) {
                    semC.release(maxCars);
                } else {
                    Data.getInstance().setCarCount(0);
                    semC.release(1);
                    semD.release(maxCars);
                }
            semMutex.release(1);
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void runMonitor(){
        try {
        while(true){
            load();
            lockB.lock();
            while(Data.getInstance().getPassengerCount() < maxCars)
                condB1.await();
            run();
            unload();
            Data.getInstance().incrementCarCount();
            condA1.signalAll();
            lockB.unlock();
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        Debug.log("CarThreadAction.load",id+" Loading passenger");
    }
    public void run(){
        Debug.log("CarThreadAction.run",id+" Running rollerccoaster");
    }
    public void unload(){
        Debug.log("CarThreadAction.unload",id+" Unloading passengers");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
