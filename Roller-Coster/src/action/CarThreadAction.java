package action;

import model.Data;
import thread_pool.ThreadAction;
import utils.Debug;

import java.util.concurrent.Semaphore;

public class CarThreadAction implements ThreadAction {

    private int type;
    private int id;

    private int maxPassengers;
    private int maxCars;

    private Semaphore semA;
    private Semaphore semB;
    private Semaphore semC;

    public CarThreadAction(int id, int type){
        setId(id);
        this.type = type;
        semA = Data.getInstance().getSemA();
        semB = Data.getInstance().getSemB();
        semC = Data.getInstance().getSemC();
        maxPassengers = Data.getInstance().getMaxPassengers();
        maxCars = Data.getInstance().getMaxCars();
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
            load();
            semB.acquire(1);
            run();
            semA.acquire(1);
            Data.getInstance().incrementSeatsTaken();
            if(Data.getInstance().getSeatsTaken() >= maxCars){
                Data.getInstance().setSeatsTaken(0);
                semC.release(maxCars);
            }
            semA.release(1);
            unload();
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void runMonitor(){
        while(true){
            load();
            run();
            unload();
        }
    }

    public void load(){
        Debug.log("CarThreadAction.load","Loading passenger");
    }
    public void run(){
        Debug.log("CarThreadAction.run","Running rollerccoaster");
    }
    public void unload(){
        Debug.log("CarThreadAction.unload","Unloading passengers");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
