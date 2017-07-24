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

    private Semaphore semA;
    private Semaphore semB;
    private Semaphore semC;

    private Data(){ }

    public void initialize(int numPassengers, int numCars, int runType){
        setMaxCars(numCars);
        setMaxPassengers(numPassengers);
        setRunType(runType);
        setNumThreads(numCars + numPassengers);
        setSemA(new Semaphore(1));
        setSemB(new Semaphore(0));
        setSemC(new Semaphore(0));
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

    public Semaphore getSemA() {
        return semA;
    }

    public void setSemA(Semaphore semA) {
        this.semA = semA;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public Semaphore getSemB() {
        return semB;
    }

    public void setSemB(Semaphore semB) {
        this.semB = semB;
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

    public Semaphore getSemC() {
        return semC;
    }

    public void setSemC(Semaphore semC) {
        this.semC = semC;
    }
}
