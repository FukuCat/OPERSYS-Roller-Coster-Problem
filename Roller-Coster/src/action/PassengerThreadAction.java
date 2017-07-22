package action;

import thread_pool.ThreadAction;

public class PassengerThreadAction implements ThreadAction {

    private int type;

    public PassengerThreadAction(int type){
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
            board();
            unboard();
        }
    }
    public void runMonitor(){
        while(true){
            board();
            unboard();
        }
    }

    public void board(){}
    public void unboard(){}
}
