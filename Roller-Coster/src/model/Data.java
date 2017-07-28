package model;

import java.util.concurrent.Semaphore;

public class Data {

    public static Data instance = null;

    public static Data getInstance(){ return instance == null ? (instance = new Data()) : instance; }

    public static final int RUN_SEMAPHORE = 0;
    public static final int RUN_MONITOR = 1;

    private int maxPassengers;
    private int maxCars;
    private int passengerCount;
    private int carCount;
    private int numThreads;
    private int runType;

    private Semaphore semA;
    private Semaphore semB;
    private Semaphore semC;
    private Semaphore semD;
    private Semaphore semE;

    private Semaphore semM1;
    private Semaphore semM2;

    private Data(){ }

    public void initialize(int numPassengers, int numCars, int runType){
        setMaxCars(numCars);
        setMaxPassengers(numPassengers);
        setRunType(runType);
        setNumThreads(numCars + numPassengers);
        setSemA(new Semaphore(0));
        setSemB(new Semaphore(0));
        setSemC(new Semaphore(0));
        setSemD(new Semaphore(0));
        setSemE(new Semaphore(numCars));
        setSemM1(new Semaphore(1));
        setSemM2(new Semaphore(1));
        setCarCount(0);
        setPassengerCount(0);
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

    public int getCarCount() {
        return carCount;
    }

    public void setCarCount(int carCount) {
        this.carCount= carCount;
    }

    public void incrementCarCount(){
        carCount++;
    }

    public void decrementCarCount(){
        carCount--;
    }

    public Semaphore getSemC() {
        return semC;
    }

    public void setSemC(Semaphore semC) {
        this.semC = semC;
    }

    public Semaphore getSemD() {
        return semD;
    }

    public void setSemD(Semaphore semD) {
        this.semD = semD;
    }

    public Semaphore getSemM1() {
        return semM1;
    }

    public void setSemM1(Semaphore semM1) {
        this.semM1 = semM1;
    }

    public Semaphore getSemM2() {
        return semM2;
    }

    public void setSemM2(Semaphore semM2) {
        this.semM2 = semM2;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }
    public void incrementPassengerCount(){
        passengerCount++;
    }

    public void decrementPassengerCount(){
        passengerCount--;
    }

    public Semaphore getSemE() {
        return semE;
    }

    public void setSemE(Semaphore semE) {
        this.semE = semE;
    }
}
