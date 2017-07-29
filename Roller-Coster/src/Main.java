import action.CarThreadAction;
import action.PassengerThreadAction;
import model.Data;
import thread_pool.ThreadPoolManager;
import utils.Debug;

public class Main {

    private static int MAX_PASSENGERS = 500;


    public static void main(String[] args) {
        Debug.log("Main.main", "Setting up demo...");
        //setup
        Data data = Data.getInstance();

        //data.initialize(MAX_PASSENGERS, MAX_PASSENGERS / 2, Data.RUN_SEMAPHORE);
        data.initialize(MAX_PASSENGERS, MAX_PASSENGERS / 2, Data.RUN_MONITOR);
        //initialize thread pool
        ThreadPoolManager.initialize(data.getNumThreads());

        for(int i = 0; i < data.getMaxCars(); i++) {
            CarThreadAction threadAction = new CarThreadAction(i, MAX_PASSENGERS, MAX_PASSENGERS / 2,data.getRunType());
            ThreadPoolManager.getInstance().addThreadAction(threadAction);
        }
        for(int i = 0; i < data.getMaxPassengers(); i++) {
            PassengerThreadAction threadAction = new PassengerThreadAction(i, MAX_PASSENGERS,MAX_PASSENGERS / 2, data.getRunType());
            ThreadPoolManager.getInstance().addThreadAction(threadAction);
        }

        Debug.log("Main.main", "Running Demo.");

        ThreadPoolManager.getInstance().startPoolExecutor();
    }


}
