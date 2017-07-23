package action;

import model.Data;
import thread_pool.ThreadAction;
import utils.Debug;

public class PassengerThreadAction implements ThreadAction {

    private int type;
    private int id;

    public PassengerThreadAction(int id, int type){
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
            Data.getInstance().getSemCar().acquire(1);
            Data.getInstance().getSemSeats().acquire(1);
            board();
            Data.getInstance().incrementSeatsTaken();
            if(Data.getInstance().getSeatsTaken() >= Data.getInstance().getMaxPassengers())
                Data.getInstance().getSemRun().release(Data.getInstance().getMaxCars());
            Data.getInstance().getSemSeats().release(1);



            Data.getInstance().getSemSeats().acquire(1);
            unboard();
            Data.getInstance().decrementSeatsTaken();
            Data.getInstance().getSemSeats().release(1);
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
