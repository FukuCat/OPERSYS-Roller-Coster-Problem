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
    private Semaphore semC;

    public PassengerThreadAction(int id, int type){
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
            board();
            semA.acquire(1);
            Data.getInstance().incrementSeatsTaken();
            if(Data.getInstance().getSeatsTaken() >= maxCars){
                Data.getInstance().setSeatsTaken(0);
                semB.release(maxCars);
            }
            semA.release(1);
            semC.acquire(1);
            unboard();
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
}
