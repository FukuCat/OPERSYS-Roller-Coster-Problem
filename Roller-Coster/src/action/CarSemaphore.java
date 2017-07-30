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
    private Semaphore semD;
    private Semaphore semEndCar;

    private Semaphore semM1;
    private Semaphore semM2;

    public CarSemaphore(int carId){
        setCarId(carId);
        semLoadBarrier = Data.getInstance().getSemLoadBarrier();
        semRunBarrier = Data.getInstance().getSemRunBarrier();
        semD = Data.getInstance().getSemUnboardBarrier();
        semEndCar = Data.getInstance().getSemEndCar();
        semM1 = Data.getInstance().getSemM1();
        semM2 = Data.getInstance().getSemM2();
        this.maxCars = Data.getInstance().getMaxCars();
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
                Data.getInstance().setCarCount(0);
                semEndCar.release(maxCars);
                semD.release(maxCars);
            }
            semM2.release(1);
            semEndCar.acquire(1);

        }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
