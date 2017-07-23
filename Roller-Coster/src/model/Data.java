package model;

import java.util.concurrent.Semaphore;

public class Data {

    public static Data instance = null;

    public static Data getInstance(){ return instance == null ? (instance = new Data()) : instance; }

    public static final int RUN_SEMAPHORE = 0;
    public static final int RUN_MONITOR = 1;

    private int maxPassengers;
    private int maxCars;
    private int seatsTaken;
    private int numThreads;
    private int runType;

    private Semaphore semCar;
    private Semaphore semSeats;
    private Semaphore semRun;

    private Data(){ }

    public void initialize(int numPassengers, int numCars, int runType){
        setMaxCars(numCars);
        setMaxPassengers(numPassengers);
        setRunType(runType);
        setNumThreads(numCars + numPassengers);
        setSemCar(new Semaphore(0));
        setSemSeats(new Semaphore(1));
        setSemRun(new Semaphore(0));
        setSeatsTaken(0);
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public int getMaxCars() {
        return maxCars;
    }

    public void setMaxCars(int maxCars) {
        this.maxCars = maxCars;
    }

    public int getRunType() {
        return runType;
    }

    public void setRunType(int runType) {
        this.runType = runType;
    }

    public Semaphore getSemCar() {
        return semCar;
    }

    public void setSemCar(Semaphore semCar) {
        this.semCar = semCar;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public Semaphore getSemSeats() {
        return semSeats;
    }

    public void setSemSeats(Semaphore semSeats) {
        this.semSeats = semSeats;
    }

    public int getSeatsTaken() {
        return seatsTaken;
    }

    public void setSeatsTaken(int seatsTaken) {
        this.seatsTaken = seatsTaken;
    }

    public void incrementSeatsTaken(){
        seatsTaken++;
    }

    public void decrementSeatsTaken(){
        seatsTaken--;
    }

    public Semaphore getSemRun() {
        return semRun;
    }

    public void setSemRun(Semaphore semRun) {
        this.semRun = semRun;
    }
}
