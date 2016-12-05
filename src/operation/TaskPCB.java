package operation;

/**
 * Created by Billin on 2016/11/17.
 */

public class TaskPCB {

    public enum Status {
        RUNNING, WAIT
    }

    Status status;

    String taskName;

    long needTime;

    long blockTime;

    long progress;

    int priory;

    TaskPCB(String taskName, long needTime) {
        this(Status.WAIT, taskName, needTime);
    }

    TaskPCB(String taskName, long needTime, int priory) {
        this(Status.WAIT, taskName, needTime, priory);
    }

    TaskPCB(Status status, String taskName, long needTime) {
        this(status, taskName, needTime, -1);
    }

    TaskPCB(Status status, String taskName, long needTime, int priory) {
        this.status = status;
        this.taskName = taskName;
        this.needTime = needTime;
        this.priory = priory;
        progress = 0;
    }
}
