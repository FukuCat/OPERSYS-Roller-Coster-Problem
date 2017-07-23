package action;

import model.Data;
import thread_pool.ThreadAction;
import utils.Debug;

public class CarThreadAction implements ThreadAction {

    private int type;
    private int id;

    public CarThreadAction(int id, int type){
        setId(id);
        this.type = type;
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
            Data.getInstance().getSemSeats().acquire(1);
            if(Data.getInstance().getSeatsTaken() == 0)
                Data.getInstance().getSemCar().release(Data.getInstance().getMaxPassengers());
            Data.getInstance().getSemSeats().release(1);
            Data.getInstance().getSemRun().acquire(1);
            run();
            Data.getInstance().getSemCar().release(Data.getInstance().getMaxPassengers());
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
