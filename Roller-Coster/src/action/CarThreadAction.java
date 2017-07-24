package action;

import model.Data;
import model.SimpleQueue;
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

    private Semaphore semPassengerQueue;
    private Semaphore semCarQueue;

    private SimpleQueue<Integer> carQueue;
    private SimpleQueue<Integer> passengerQueue;

    public CarThreadAction(int id, int type){
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
        if(type == 0)
            runSemaphore();
        else
            runMonitor();
    }

    public void runSemaphore(){
        try {
         semCarQueue.acquire(1);
         carQueue.enqueue(id);
         semCarQueue.release(1);
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
