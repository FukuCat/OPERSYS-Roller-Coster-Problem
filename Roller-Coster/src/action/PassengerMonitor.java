package action;

import model.Data;
import utils.Debug;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PassengerMonitor extends Thread implements DemoThread{

    private static int barrierStart = 0, barrierEnd = 0;
    private static boolean isPassable = true;

    private int passengerId;

    private boolean isDone = false;
    private int maxCars;

    private ReentrantLock lock;
    private Condition conA;
    private Condition conB;
    private Condition conE;
    private Condition conBarrierPassenger;

    public PassengerMonitor(int passengerId){
        setPassengerId(passengerId);
        this.maxCars = Data.getInstance().getMaxCars();
        lock = Data.getInstance().getLock();
        conA = Data.getInstance().getConA();
        conB = Data.getInstance().getConB();
        conE = Data.getInstance().getConE();
        conBarrierPassenger = Data.getInstance().getConBarrierPassenger();

    }

    @Override
    public void run() {
        while(!isDone){
            tryFirstInFirstOut();
            board();
            /*
            // ensures only N passengers can ride where N = number of cars
            tryFirstInFirstOut();

            // make all passengers run before allowing CarMonitor to execute Run
            board();
            notifyRunCar();
            barrier();

            // wait for CarMonitor to execute unload
            tryUnboard();
            unboard();
            notifyFirstInFirstOut();
            */
        }
    }

    public void barrier(){
        lock.lock();

        try {
            barrierStart++;
            while(!isPassable)
                conBarrierPassenger.await();
            barrierEnd++;
            if(barrierStart >= maxCars){
                barrierStart = 0;
                isPassable = false;
                conBarrierPassenger.signalAll();
            }
            if(barrierEnd >= maxCars){
                barrierEnd = 0;
                isPassable = true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }

    }

    public void tryFirstInFirstOut() {
        lock.lock();

        try {
            // check queue if next and if there is space

            Data.getInstance().incrementPassengerThreads();
            while(!(Data.getInstance().getPassengerQueue().head() == passengerId && Data.getInstance().getPassengerThreads() < maxCars))
                conA.await();
            Data.getInstance().getPassengerQueue().rotate();
            if(barrierStart < maxCars - 1)
                conA.signalAll();
            System.out.println("TEST" + passengerId);
        } catch(Exception e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }

    public void notifyFirstInFirstOut() {
        lock.lock();

        try {
            barrierStart++;
            while(!isPassable)
                conBarrierPassenger.await();
            barrierEnd++;
            if(barrierStart >= maxCars){
                barrierStart = 0;
                isPassable = false;
                conBarrierPassenger.signalAll();
            }
            if(barrierEnd >= maxCars){
                barrierEnd = 0;
                isPassable = true;
                Data.getInstance().setUnboardable(false);
                Data.getInstance().setPassengerThreads(0);
                conA.signalAll();
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }

    public void notifyRunCar(){
        lock.lock();
        try {
            Data.getInstance().incrementPassengerCount();
            Data.getInstance().setRunnable(!(Data.getInstance().getPassengerCount() >= maxCars));
            if(Data.getInstance().isRunnable()){
                Data.getInstance().setPassengerCount(0);
                conB.signalAll();
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }

    }

    public void tryUnboard() {
        lock.lock();

        try {
            //if not unloading
            if(Data.getInstance().isUnboardable()) {
                //System.out.println("Not unloading. Waiting for load");
                conE.await();
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }

    public void board(){
        Debug.log("PassengerSemaphore.board", passengerId +" IN");
    }
    public void unboard(){
        Debug.log("PassengerSemaphore.unboard", passengerId +" OUT");
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public void quit() {
        setDone(true);
    }
}
