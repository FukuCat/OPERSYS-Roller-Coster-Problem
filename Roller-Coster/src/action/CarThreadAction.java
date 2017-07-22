package action;

import thread_pool.ThreadAction;

public class CarThreadAction implements ThreadAction {

    private int type;

    public CarThreadAction(int type){
        this.type = type;
    }

    @Override
    public void execute() {
        if(type == 0)
            runSemaphore();
        else
            runMonitor();
    }

    public void runSemaphore(){
        while(true){
            load();
            run();
            unload();
        }
    }
    public void runMonitor(){
        while(true){
            load();
            run();
            unload();
        }
    }

    public void load(){}
    public void run(){}
    public void unload(){}
}
