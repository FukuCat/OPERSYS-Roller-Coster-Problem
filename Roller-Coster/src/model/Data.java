package model;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Data {

    public static Data instance = null;

    public static Data getInstance(){ return instance == null ? (instance = new Data()) : instance; }

    public static final int RUN_MONITOR = 2;
    public static final int RUN_SEMAPHORE = 1;

    private int maxPassengers;
    private int intervalPassengers;
    private int maxCars;
    private int passengerCount;
    private int passengerThreads;
    private int carCount;
    private int numThreads;
    private int runType;


    private boolean isDone;

    private Semaphore semBoardBarrier;
    private Semaphore semLoadBarrier;
    private Semaphore semRunBarrier;
    private Semaphore semUnboardBarrier;
    private Semaphore semFirstInFirstOut;
    private Semaphore semEndCar;
    private Semaphore semEndPassenger;

    private Semaphore semM1;
    private Semaphore semM2;

    private Semaphore semLog;


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
        setSemLog(new Semaphore(1,true));
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

    public Semaphore getSemLog() {
        return semLog;
    }

    public void setSemLog(Semaphore semLog) {
        this.semLog = semLog;
    }
}
