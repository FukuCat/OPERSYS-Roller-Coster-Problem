import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Scanner;

public class RollerCoasterMonitor {
	private static RollerCoasterMonitor instance = null;
	private ReentrantLock rel;
	private Condition loadQueue;
	private Condition unloadQueue;
	private Condition carEmpty;
	private Condition carFull;
	private boolean loading;
	private boolean unloading;
	private int capacity;
	private int holding;

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("[1] - Fixed Capacity, Fixed N\n[2] - Fixed Time");
		int option = Integer.parseInt(sc.nextLine());
		System.out.print("Enter capacity: ");
		int capacity = Integer.parseInt(sc.nextLine());
		if(option == 1) {
			System.out.print("Enter number of threads: ");
			int threads = Integer.parseInt(sc.nextLine());
			RollerCoasterMonitor.instance(capacity);
			RollerCoaster rc = new RollerCoaster(capacity);
			(new Thread(rc)).start();
			Passenger[] pass = new Passenger[threads];
			for(int i = 0; i < threads; i++) {
				pass[i] = new Passenger(i,rc);
				(new Thread(pass[i])).start();
			}
			sc.nextLine();
			rc.kill();
			for(int i = 0; i < threads; i++) {
				pass[i].kill();
			}
		} else {
			System.out.print("Enter number of seconds: ");
			int seconds = Integer.parseInt(sc.nextLine());
			RollerCoasterMonitor.instance(capacity);
			RollerCoaster rc = new RollerCoaster(capacity);
			(new Thread(rc)).start();
			Passenger[] pass = new Passenger[seconds];
			for(int i = 0; i < seconds; i++) {
				pass[i] = new Passenger(i,rc);
				(new Thread(pass[i])).start();
				try {
					Thread.sleep(1000);
				} catch(InterruptedException ie) {

				}
			}
			rc.kill();
			for(int i = 0; i < seconds; i++) {
				pass[i].kill();
			}
		}
		System.exit(0);
	}

	private RollerCoasterMonitor(int capacity) {
		rel = new ReentrantLock();
		loadQueue = rel.newCondition();
		unloadQueue = rel.newCondition();
		carEmpty = rel.newCondition();
		carFull = rel.newCondition();
		loading = unloading = false;
		this.capacity = capacity;
		holding = 0;
	}

	public static RollerCoasterMonitor instance() {
		if(instance == null) {
			instance = new RollerCoasterMonitor(1);
		}
		return instance;
	}

	public static RollerCoasterMonitor instance(int capacity) {
		if(instance == null) {
			instance = new RollerCoasterMonitor(capacity);
		}
		return instance;
	}

	public void tryBoard() {
		rel.lock();

		try {
			//if not loading
			if(!loading) {
				System.out.println("Not loading. Waiting for load");
				loadQueue.await();
			}

			//while there's no space
			while(holding == capacity) {
				System.out.println("No space.");
				loadQueue.await();
			}

			//update holding to account for new passenger
			holding++;
			System.out.println("Monitor board. On Coaster: " + holding);

			//if full
			if(holding == capacity) {
				loading = false;
				System.out.println("Car Full. Signalling Car");
				carFull.signal();
			}
	
		} catch(Exception e) {
			System.out.println(e.toString());
		} finally{
			rel.unlock();
		}
	}

	public void tryUnboard() {
		rel.lock();

		try {
			//if not unloading
			if(!unloading) {
				System.out.println("Not unloading. Waiting for load");
				unloadQueue.await();
			}
			//update holding to account for one less passenger
			holding--;

			System.out.println("Monitor unboard. On Coaster: " + holding);

			//if car is empty
			if(holding == 0) {
				unloading = false;
				System.out.println("Car empty. Signalling car");
				carEmpty.signal();
			}
		} catch(Exception e) {
			System.out.println(e.toString());
		} finally{
			rel.unlock();
		}
	}

	public void startLoad() {
		rel.lock();

		try {
			//if car is not empty
			if(holding > 0) {
				System.out.println("Car not empty. Waiting...");
				carEmpty.await();
			}
			System.out.println("------------------------------------COASTER RUN---------------------------------");
			System.out.println("LOADING");
			loading = true;
			for(int i = 0; i < capacity; i++) {
				loadQueue.signal();
			}
		
			//wait for car to be full
			System.out.println("Wait for car to be full");
			carFull.await();
		} catch(Exception e) {
			System.out.println(e.toString());
		} finally{
			rel.unlock();
		}
	}

	public void startUnload() {
		rel.lock();

		try {
			unloading = true;
			System.out.println("Unloading. Signalling");
			unloadQueue.signalAll();
			System.out.println("HELLO");
		} catch(Exception e) {
			System.out.println(e.toString());
		} finally{
			rel.unlock();
		}
	}
}

class RollerCoaster implements Runnable {
	private int capacity;
	private int holding;
	private RollerCoasterMonitor rcm;
	private boolean running;

	public RollerCoaster(int capacity) {
		this.capacity = capacity;
		holding = 0;
		rcm = RollerCoasterMonitor.instance();
	}

	public void load() {
		rcm.startLoad();
		// System.out.println("LOADING");
	}

	public void unload() {
		rcm.startUnload();
		// System.out.println("UNLOADING");
	}
	
	public void board() {
		rcm.tryBoard();
		if(holding < capacity)
			holding++;
		// System.out.println("BOARDED! ON COASTER: " + holding);
	}

	public void rcRun() {
		System.out.println("COASTER RUNNING!");
	}

	public void unboard() {
		rcm.tryUnboard();
		if(holding > 0)
			holding--;
		// System.out.println("UNBOARDED! ON COASTER: " + holding);	
	}

	public void kill() {
		running = false;
	}

	public void run() {
		running = true;
		while(running) {
			load();
			rcRun();
			unload();
		}
	}
}

class Passenger implements Runnable {
	private RollerCoasterMonitor rcm;
	private RollerCoaster rc;
	private int passNo;
	private boolean running;

	public Passenger(int passNo, RollerCoaster rc) {
		this.passNo = passNo;
		this.rc = rc;
		rcm = RollerCoasterMonitor.instance();
	}

	public void kill() {
		running = false;
	}

	public void run() {
		running = true;
		while(running) {
			System.out.println("Try Boarding Passenger " + passNo);
			rc.board();
			System.out.println("Try Unboarding Passenger " + passNo);
			rc.unboard();
		}
	}
}

