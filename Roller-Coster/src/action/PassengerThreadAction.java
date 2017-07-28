package action;

import model.Data;
import thread_pool.ThreadAction;
import utils.Debug;

import java.util.concurrent.Semaphore;

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
        while(true){
            board();
            unboard();
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
}
