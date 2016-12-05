package operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * dynamic priority schedule
 * <p/>
 * Created by Billin on 2016/11/17.
 */
class DynamicPrioritySchedule {

    private List<TaskPCB> list;

    private Random random;

    DynamicPrioritySchedule() {
        list = new ArrayList<>();
        random = new Random();
    }

    synchronized void addTask(String name) {
        TaskPCB taskPCB = new TaskPCB(name, random.nextInt(50) + 50, random.nextInt(11));
        taskPCB.blockTime = System.currentTimeMillis();
        list.add(taskPCB);
    }

    synchronized TaskPCB getTask(int index) {
        return list.get(index);
    }

    synchronized int size() {
        return list.size();
    }

    private synchronized Integer getToRunTaskIndex() {
        if (list.isEmpty()) {
            return null;
        }

        List<Integer> beRunning = new ArrayList<>();
        int max = 0;
        for (TaskPCB task : list) {
            task.status = TaskPCB.Status.WAIT;
            if (task.priory > max) {
                max = task.priory;
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (getTask(i).priory == max)
                beRunning.add(i);
        }

        // FIFS algorithm
//        getTask(beRunning.get(0)).status = TaskPCB.Status.RUNNING;
//        return beRunning.get(0);

        // SJF algorithm
        long shortest = Integer.MAX_VALUE;
        int returnValue = 0;
        for (int i : beRunning) {
            TaskPCB taskPCB = getTask(i);
            long shortestTime = taskPCB.needTime - taskPCB.progress;
            if (shortestTime < shortest) {
                shortest = shortestTime;
                returnValue = i;
            }
        }
        return returnValue;
    }

    synchronized void runTask() {
        Integer index = getToRunTaskIndex();
        if (index == null) {
            return;
        }

        TaskPCB taskPCB = getTask(index);
        taskPCB.status = TaskPCB.Status.RUNNING;

        if (taskPCB.priory > 0) {
            taskPCB.priory--;
        }

        taskPCB.blockTime = System.currentTimeMillis();
        int maxBlock = 0;
        long maxBlockTime = Long.MAX_VALUE;
        for (int i = 0; i < list.size(); i++) {
            if (getTask(i).blockTime < maxBlockTime) {
                maxBlockTime = getTask(i).blockTime;
                maxBlock = i;
            }
        }
        getTask(maxBlock).priory++;

        if (++taskPCB.progress > taskPCB.needTime) {
            list.remove(index.intValue());
        }
    }
}
