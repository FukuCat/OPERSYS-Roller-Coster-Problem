package action;

import model.Data;

import java.util.Map;
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
    private Semaphore semStats;

    private int loopsCompleted;

    public CarSemaphore(int carId){
        setCarId(carId);
        semLoadBarrier = Data.getInstance().getSemLoadBarrier();
        semRunBarrier = Data.getInstance().getSemRunBarrier();
        semD = Data.getInstance().getSemUnboardBarrier();
        semEndCar = Data.getInstance().getSemEndCar();
        semM1 = Data.getInstance().getSemM1();
        semM2 = Data.getInstance().getSemM2();
        this.maxCars = Data.getInstance().getMaxCars();
        semStats = Data.getInstance().getSemStats();
        loopsCompleted = 0;
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

                // for checking stats
                stats();

                // reset and goto next loop
                Data.getInstance().setCarCount(0);
                semEndCar.release(maxCars);
                semD.release(maxCars);
            }
            semM2.release(1);
            semEndCar.acquire(1);
            // for checking stats
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stats() throws InterruptedException {
        loopsCompleted++;
        /*
        Map<Integer, Integer> map1 = Data.getInstance().getPassengerRunCountTable();
        for (Map.Entry<Integer, Integer> entry : map1.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();

        }
        Map<Integer, Long> map2 = Data.getInstance().getPassengerRunTimeTable();
        for (Map.Entry<Integer, Integer> entry : map1.entrySet()) {
            Integer key = entry.getKey();
            Double value = (entry.getValue() - System.nanoTime() / 1000000000.0);

        }*/
        semStats.acquire(1);
        System.out.println("-------- Start --------");
        System.out.println("Starved Threads: "+ Data.getInstance().getStarvedThreads());
        System.out.println("Deadlocked Threads: "+ Data.getInstance().getDeadlockedThreads());
        System.out.println("--------- End ---------");
        semStats.release(1);


    }

    public void load(){
        //Debug.log("CarSemaphore.load", carId +" Loading passenger");
    }
    public void runn(){
        //Debug.log("CarSemaphore.run", carId +" Running rollerccoaster");
    }
    public void unload(){
        //Debug.log("CarSemaphore.unload", carId +" Unloading passengers");
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
