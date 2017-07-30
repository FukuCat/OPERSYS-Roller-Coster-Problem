package model;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Data {

    public static Data instance = null;

    public static Data getInstance(){ return instance == null ? (instance = new Data()) : instance; }

    public static final int RUN_MONITOR = 1;
    public static final int RUN_SEMAPHORE = 2;

    private int maxPassengers;
    private int intervalPassengers;
    private int maxCars;
    private int passengerCount;
    private int passengerThreads;
    private int carCount;
    private int numThreads;
    private int runType;

    private boolean isDone;

    private boolean isUnloadable;
    private boolean isRunnable;
    private boolean isUnboardable;

    private Semaphore semBoardBarrier;
    private Semaphore semLoadBarrier;
    private Semaphore semRunBarrier;
    private Semaphore semUnboardBarrier;
    private Semaphore semFirstInFirstOut;
    private Semaphore semEndCar;
    private Semaphore semEndPassenger;

    private Semaphore semM1;
    private Semaphore semM2;

    private ReentrantLock lock;
    private Condition conA;
    private Condition conB;
    private Condition conC;
    private Condition conD;
    private Condition conE;
    private Condition conBarrierCar;
    private Condition conBarrierPassenger;

    private SimpleQueue<Integer> passengerQueue;

    private Data(){ }

    public void initialize(int numPassengers, int numCars, int runType){
        setDone(false);
        setIntervalPassengers(numPassengers);
        setPassengerThreads(0);
        setMaxCars(numCars);
        Data.getInstance().setMaxPassengers(0);
        setRunType(runType);
        setNumThreads(numCars + numPassengers);
        setSemBoardBarrier(new Semaphore(0));
        setSemLoadBarrier(new Semaphore(0));
        setSemRunBarrier(new Semaphore(0));
        setSemUnboardBarrier(new Semaphore(0));
        setSemFirstInFirstOut(new Semaphore(numCars, true));
        setSemEndCar(new Semaphore(0));
        setSemEndPassenger(new Semaphore(0));
        setSemM1(new Semaphore(1));
        setSemM2(new Semaphore(1));
        setCarCount(0);
        setPassengerCount(0);

        setLock(new ReentrantLock());
        setConA(getLock().newCondition());
        setConB(getLock().newCondition());
        setConC(getLock().newCondition());
        setConD(getLock().newCondition());
        setConE(getLock().newCondition());
        setConBarrierPassenger(getLock().newCondition());
        setConBarrierCar(getLock().newCondition());
        setPassengerQueue(new SimpleQueue<>());

        setUnloadable(true);
        setUnboardable(false);
        setRunnable(true);

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

    public Semaphore getSemBoardBarrier() {
        return semBoardBarrier;
    }

    public void setSemBoardBarrier(Semaphore semBoardBarrier) {
        this.semBoardBarrier = semBoardBarrier;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public Semaphore getSemLoadBarrier() {
        return semLoadBarrier;
    }

    public void setSemLoadBarrier(Semaphore semLoadBarrier) {
        this.semLoadBarrier = semLoadBarrier;
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

    public Semaphore getSemRunBarrier() {
        return semRunBarrier;
    }

    public void setSemRunBarrier(Semaphore semRunBarrier) {
        this.semRunBarrier = semRunBarrier;
    }

    public Semaphore getSemUnboardBarrier() {
        return semUnboardBarrier;
    }

    public void setSemUnboardBarrier(Semaphore semUnboardBarrier) {
        this.semUnboardBarrier = semUnboardBarrier;
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
    public void incrementPassengerThreads(){
        passengerThreads++;
    }

    public void decrementPassengerCount(){
        passengerCount--;
    }

    public Semaphore getSemFirstInFirstOut() {
        return semFirstInFirstOut;
    }

    public void setSemFirstInFirstOut(Semaphore semFirstInFirstOut) {
        this.semFirstInFirstOut = semFirstInFirstOut;
    }

    public int getIntervalPassengers() {
        return intervalPassengers;
    }

    public void setIntervalPassengers(int intervalPassengers) {
        this.intervalPassengers = intervalPassengers;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }

    public Condition getConA() {
        return conA;
    }

    public void setConA(Condition conA) {
        this.conA = conA;
    }

    public Condition getConB() {
        return conB;
    }

    public void setConB(Condition conB) {
        this.conB = conB;
    }

    public Condition getConC() {
        return conC;
    }

    public void setConC(Condition conC) {
        this.conC = conC;
    }

    public Condition getConD() {
        return conD;
    }

    public void setConD(Condition conD) {
        this.conD = conD;
    }

    public SimpleQueue<Integer> getPassengerQueue() {
        return passengerQueue;
    }

    public void setPassengerQueue(SimpleQueue<Integer> passengerQueue) {
        this.passengerQueue = passengerQueue;
    }

    public Semaphore getSemEndCar() {
        return semEndCar;
    }

    public void setSemEndCar(Semaphore semEndCar) {
        this.semEndCar = semEndCar;
    }

    public Semaphore getSemEndPassenger() {
        return semEndPassenger;
    }

    public void setSemEndPassenger(Semaphore semEndPassenger) {
        this.semEndPassenger = semEndPassenger;
    }

    public int getPassengerThreads() {
        return passengerThreads;
    }

    public void setPassengerThreads(int passengerThreads) {
        this.passengerThreads = passengerThreads;
    }
    public boolean isUnloadable() {
        return isUnloadable;
    }

    public void setUnloadable(boolean unloadable) {
        isUnloadable = unloadable;
    }

    public boolean isRunnable() {
        return isRunnable;
    }

    public void setRunnable(boolean runnable) {
        isRunnable = runnable;
    }

    public void setBoardable(boolean boardable) {
    }

    public boolean isUnboardable() {
        return isUnboardable;
    }

    public void setUnboardable(boolean unboardable) {
        isUnboardable = unboardable;
    }

    public Condition getConE() {
        return conE;
    }

    public void setConE(Condition conE) {
        this.conE = conE;
    }

    public Condition getConBarrierCar() {
        return conBarrierCar;
    }

    public void setConBarrierCar(Condition conBarrierCar) {
        this.conBarrierCar = conBarrierCar;
    }

    public Condition getConBarrierPassenger() {
        return conBarrierPassenger;
    }

    public void setConBarrierPassenger(Condition conBarrierPassenger) {
        this.conBarrierPassenger = conBarrierPassenger;
    }
}
