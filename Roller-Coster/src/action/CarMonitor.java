package action;

import model.Data;
import utils.Debug;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CarMonitor extends Thread implements DemoThread{
    private int carId;

    private boolean isDone = false;
    private int maxCars;

    private static int barrierStart = 0, barrierEnd = 0;
    private static boolean isPassable = true;


    private ReentrantLock lock;
    private Condition conB;
    private Condition conC;
    private Condition conD;
    private Condition conE;
    private Condition conBarrierCar;

    public CarMonitor(int id){
        setCarId(id);
        this.maxCars = Data.getInstance().getMaxCars();
        lock = Data.getInstance().getLock();
        conB = Data.getInstance().getConB();
        conC = Data.getInstance().getConC();
        conD = Data.getInstance().getConD();
        conE = Data.getInstance().getConE();
        conBarrierCar = Data.getInstance().getConBarrierCar();
    }

    @Override
    public void run() {
        while(!isDone){
            load();
            tryRun();
            barrier();
            /*
            runn();
            barrier();
            unload();
            barrier();
            notifyPassengers();
            */
        }
    }

    public void barrier(){
        lock.lock();

        try {
            barrierStart++;
            while(!isPassable)
                conBarrierCar.await();
            barrierEnd++;
            if(barrierStart >= maxCars){
                barrierStart = 0;
                isPassable = false;
                conBarrierCar.signalAll();
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

    public void tryRun() {
        lock.lock();

        try {
            if(Data.getInstance().isRunnable())
                conB.await();
        } catch(Exception e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }

    public void tryUnload() {
        lock.lock();
        try {
            Data.getInstance().incrementCarCount();
            Data.getInstance().setUnloadable(Data.getInstance().getCarCount() >= maxCars);
            while(Data.getInstance().isUnloadable())
                conC.await();
            if(Data.getInstance().getCarCount() >= maxCars)
                Data.getInstance().setCarCount(0);
            conC.signalAll();
        } catch(Exception e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }

    public void notifyPassengers(){
        lock.lock();

        try {

            Data.getInstance().incrementCarCount();
            Data.getInstance().setUnloadable(Data.getInstance().getCarCount() >= maxCars);
            while(Data.getInstance().isUnloadable())
                conD.await();
            if(Data.getInstance().getCarCount() >= maxCars){
                Data.getInstance().setCarCount(0);
                Data.getInstance().setUnboardable(true);
                Data.getInstance().setUnloadable(false);
                Data.getInstance().setRunnable(false);
                conE.signalAll();
            }
            conD.signalAll();


        } catch(Exception e) {
            System.out.println(e.toString());
        } finally{
            lock.unlock();
        }
    }

    public void load(){
        Debug.log("CarSemaphore.load", carId +" Loading passenger");
    }
    public void runn(){
        Debug.log("CarSemaphore.run", carId +" Running rollerccoaster");
    }
    public void unload(){
        Debug.log("CarSemaphore.unload", carId +" Unloading passengers");
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }


    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public void quit() {
        setDone(true);
    }
}
