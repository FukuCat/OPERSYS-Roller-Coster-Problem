import action.CarThreadAction;
import action.PassengerThreadAction;
import thread_pool.ThreadPoolManager;

public class Main {

    private static int MAX_PASSENGERS = 500;
    private static int RUN_SEMAPHORE = 0;
    private static int RUN_MONITOR = 1;


    public static void main(String[] args) {
        //setup
        int numPassengers = MAX_PASSENGERS;
        int numCars = numPassengers / 2;
        int runType = RUN_SEMAPHORE;
        //initialize thread pool
        ThreadPoolManager.initialize(numPassengers + numCars);

        for(int i = 0; i < numPassengers; i++) {
            PassengerThreadAction threadAction = new PassengerThreadAction(runType);
            ThreadPoolManager.getInstance().addThreadAction(threadAction);
        }

        for(int i = 0; i < numCars; i++) {
            CarThreadAction threadAction = new CarThreadAction(runType);
            ThreadPoolManager.getInstance().addThreadAction(threadAction);
        }

        ThreadPoolManager.getInstance().startPoolExecutor();
    }


}
