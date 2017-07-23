import action.CarThreadAction;
import action.PassengerThreadAction;
import model.Data;
import thread_pool.ThreadPoolManager;

public class Main {

    private static int MAX_PASSENGERS = 500;


    public static void main(String[] args) {
        //setup
        Data data = Data.getInstance();

        data.initialize(MAX_PASSENGERS, MAX_PASSENGERS / 2, Data.RUN_SEMAPHORE);
        //initialize thread pool
        ThreadPoolManager.initialize(data.getNumThreads());

        for(int i = 0; i < data.getMaxPassengers(); i++) {
            PassengerThreadAction threadAction = new PassengerThreadAction(i, data.getRunType());
            ThreadPoolManager.getInstance().addThreadAction(threadAction);
        }

        for(int i = 0; i < data.getMaxCars(); i++) {
            CarThreadAction threadAction = new CarThreadAction(i, data.getRunType());
            ThreadPoolManager.getInstance().addThreadAction(threadAction);
        }

        ThreadPoolManager.getInstance().startPoolExecutor();
    }


}
