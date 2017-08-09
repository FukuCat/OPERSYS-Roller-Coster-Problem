package action;

import model.Data;
import utils.Debug;

import java.util.concurrent.Semaphore;

public class CarSemaphore extends Thread implements DemoThread{

    private int carId;

    private boolean isDone = false;
    private int maxCars;

    private Semaphore semLoadBarrier;
    private Semaphore semRunBarrier;
    private Semaphore semUnboardBarrier;
    private Semaphore semEndCar;

    private Semaphore semM2;
    private Semaphore semLog;

    private Semaphore semStarve;

    public CarSemaphore(int carId){
        setCarId(carId);
        semLoadBarrier = Data.getInstance().getSemLoadBarrier();
        semRunBarrier = Data.getInstance().getSemRunBarrier();
        semUnboardBarrier = Data.getInstance().getSemUnboardBarrier();
        semEndCar = Data.getInstance().getSemEndCar();
        semM2 = Data.getInstance().getSemM2();
        this.maxCars = Data.getInstance().getMaxCars();
        semLog = Data.getInstance().getSemLog();
        semStarve = Data.getInstance().getSemStarve();
    }

    @Override
    public void run() {
        try {
        while(!isDone){
            load();
            semLoadBarrier.acquire(1);
            runn();

            semM2.acquire(1);
                Data.getInstance().incrementCarCount();
                if(Data.getInstance().getCarCount() >= maxCars){
                    Data.getInstance().setCarCount(0);
                    semRunBarrier.release(maxCars);
                }
            semM2.release(1);
            semRunBarrier.acquire(1);
            unload();

            semM2.acquire(1);
            Data.getInstance().incrementCarCount();
            if(Data.getInstance().getCarCount() >= maxCars){


                // reset and goto next loop
                semStarve.acquire(1);
                Data.getInstance().incrementTimesRan();
                semStarve.release(1);
                Data.getInstance().setCarCount(0);
                semEndCar.release(maxCars);
                semUnboardBarrier.release(maxCars);
            }
            semM2.release(1);
            semEndCar.acquire(1);
            // for checking stats
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        try {
            semLog.acquire(1);
            Debug.log("CarSemaphore.load", carId +" Loading passenger");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semLog.release(1);
        }
    }
    public void runn(){
        try {
            semLog.acquire(1);
            Debug.log("CarSemaphore.runn", carId +" Running rollerccoaster");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semLog.release(1);
        }
    }
    public void unload(){

        try {
            semLog.acquire(1);
            Debug.log("CarSemaphore.unload", carId +" Unloading passengers");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semLog.release(1);
        }
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public void quit() {
        setDone(true);
    }

}
