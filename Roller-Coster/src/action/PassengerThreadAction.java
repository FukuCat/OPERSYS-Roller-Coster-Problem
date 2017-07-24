package action;

import model.Data;
import model.SimpleQueue;
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

    private Semaphore semPassengerQueue;
    private Semaphore semCarQueue;

    private SimpleQueue<Integer> carQueue;
    private SimpleQueue<Integer> passengerQueue;

    public PassengerThreadAction(int id, int type){
        setId(id);
        this.type = type;
        semA = Data.getInstance().getSemA();
        semB = Data.getInstance().getSemB();
        semC = Data.getInstance().getSemC();
        semCarQueue = Data.getInstance().getSemCarQueue();
        semPassengerQueue = Data.getInstance().getSemPassengerQueue();
        maxPassengers = Data.getInstance().getMaxPassengers();
        maxCars = Data.getInstance().getMaxCars();
        setCarQueue(Data.getInstance().getCarQueue());
        setPassengerQueue(Data.getInstance().getPassengerQueue());
    }

    @Override
    public void execute() {
        passengerQueue.enqueue(id);
        if(type == 0)
            runSemaphore();
        else
            runMonitor();
    }

    public void runSemaphore(){
        try {
        semPassengerQueue.acquire(1);
        passengerQueue.enqueue(id);
        semPassengerQueue.release(1);
        while(true){
            board();
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

    public SimpleQueue<Integer> getCarQueue() {
        return carQueue;
    }

    public void setCarQueue(SimpleQueue<Integer> carQueue) {
        this.carQueue = carQueue;
    }

    public SimpleQueue<Integer> getPassengerQueue() {
        return passengerQueue;
    }

    public void setPassengerQueue(SimpleQueue<Integer> passengerQueue) {
        this.passengerQueue = passengerQueue;
    }

    public Semaphore getSemPassengerQueue() {
        return semPassengerQueue;
    }

    public void setSemPassengerQueue(Semaphore semPassengerQueue) {
        this.semPassengerQueue = semPassengerQueue;
    }

    public Semaphore getSemCarQueue() {
        return semCarQueue;
    }

    public void setSemCarQueue(Semaphore semCarQueue) {
        this.semCarQueue = semCarQueue;
    }
}
