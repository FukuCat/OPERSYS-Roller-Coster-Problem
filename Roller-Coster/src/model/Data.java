package model;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    private Lock lockA;
    private Lock lockB;
    private Lock lockC;

    private Condition condA1;
    private Condition condB1;
    private Condition condC1;

    private Data(){ }

    public void initialize(int numPassengers, int numCars, int runType){
        setMaxCars(numCars);
        setMaxPassengers(numPassengers);
        setRunType(runType);
        setNumThreads(numCars + numPassengers);
        setSemA(new Semaphore(0, true));
        setSemB(new Semaphore(0, true));
        setSemC(new Semaphore(0, true));
        setSemD(new Semaphore(0, true));
        setSemE(new Semaphore(numCars));
        setSemM1(new Semaphore(1, true));
        setSemM2(new Semaphore(1, true));
        setLockA(new ReentrantLock());
        setLockB(new ReentrantLock());
        setLockC(new ReentrantLock());
        setCondA1(getLockA().newCondition());
        setCondB1(getLockB().newCondition());
        setCondC1(getLockC().newCondition());
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

    public Lock getLockA() {
        return lockA;
    }

    public void setLockA(Lock lockA) {
        this.lockA = lockA;
    }

    public Lock getLockB() {
        return lockB;
    }

    public void setLockB(Lock lockB) {
        this.lockB = lockB;
    }

    public Condition getCondA1() {
        return condA1;
    }

    public void setCondA1(Condition condA1) {
        this.condA1 = condA1;
    }

    public Condition getCondB1() {
        return condB1;
    }

    public void setCondB1(Condition condB1) {
        this.condB1 = condB1;
    }

    public Lock getLockC() {
        return lockC;
    }

    public void setLockC(Lock lockC) {
        this.lockC = lockC;
    }

    public Condition getCondC1() {
        return condC1;
    }

    public void setCondC1(Condition condC1) {
        this.condC1 = condC1;
    }
}
