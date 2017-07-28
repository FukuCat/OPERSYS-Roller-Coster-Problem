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
    private Semaphore semD;

    private Semaphore semMutex;


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
            semC.release(1);
            semC.acquire(maxCars);
            semMutex.acquire(1);
                Data.getInstance().incrementCarCount();
                if(Data.getInstance().getCarCount() < maxCars)
                    semC.release(maxCars);
                else {
                    Data.getInstance().setCarCount(0);
                    semC.release(1);
                    semD.release(maxCars);
                }
            semMutex.release(1);
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

}
