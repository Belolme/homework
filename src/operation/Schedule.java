package operation;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Timer;

/**
 * 調度器實驗
 * <p/>
 * Created by Billin on 2016/11/16.
 */
public class Schedule {

    private JTable timeSliceScheduleTable;

    private JTable dynamicPriorityScheduleTable;

    private JPanel main;

    private JButton newDynamicScheduleButton;

    private JButton newSliceScheduleButton;

    private final List<TaskPCB> sliceTasks;
    private int nextSliceTask;

    private DynamicPrioritySchedule dynamicPrioritySchedule;

    private Schedule() {

        sliceTasks = new ArrayList<>();
        nextSliceTask = 0;

        dynamicPrioritySchedule = new DynamicPrioritySchedule();

        Random random = new Random(System.currentTimeMillis() + 100);

        // init needTime slice schedule
        SliceScheduleModel sliceScheduleModel = new SliceScheduleModel(sliceTasks);
        timeSliceScheduleTable.setModel(sliceScheduleModel);
        newSliceScheduleButton.addActionListener(e -> {
            String taskName = JOptionPane.showInputDialog("请输入作业名称");
            if (taskName == null || taskName.equals("")) {
                return;
            }

            sliceTasks.add(new TaskPCB(taskName, random.nextInt(50) + 50));
            sliceScheduleModel.fireTableDataChanged();
        });
        timeSliceScheduleTable.getColumnModel().getColumn(3).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, (int) sliceTasks.get(row).needTime);
            progressBar.setValue((int) sliceTasks.get(row).progress);
            return progressBar;
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (sliceTasks.isEmpty()) {
                    return;
                }

                synchronized (sliceTasks) {

//                    sliceTasks.get((nextSliceTask + sliceTasks.size() - 1) % sliceTasks.size());
                    for (TaskPCB task : sliceTasks) {
                        task.status = TaskPCB.Status.WAIT;
                    }

                    TaskPCB task = sliceTasks.get(nextSliceTask);
                    task.status = TaskPCB.Status.RUNNING;

                    if (++task.progress > task.needTime) {
                        sliceTasks.remove(nextSliceTask);
                        nextSliceTask--;
                        if (sliceTasks.size() == 0) {
                            nextSliceTask = 0;
                            SwingUtilities.invokeLater(sliceScheduleModel::fireTableDataChanged);
                            return;
                        }
                    }

                    SwingUtilities.invokeLater(sliceScheduleModel::fireTableDataChanged);
                    nextSliceTask = (nextSliceTask + 1) % sliceTasks.size();
                }
            }

        }, 0, 100);

        // init dynamic schedule table
        DynamicScheduleModel dynamicScheduleModel = new DynamicScheduleModel();
        dynamicPriorityScheduleTable.setModel(dynamicScheduleModel);
        dynamicPriorityScheduleTable.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, (int) dynamicPrioritySchedule.getTask(row).needTime);
            progressBar.setValue((int) dynamicPrioritySchedule.getTask(row).progress);
            return progressBar;
        });
        newDynamicScheduleButton.addActionListener(e -> {
            String taskName = JOptionPane.showInputDialog("请输入作业名称");
            if (taskName == null || taskName.equals("")) {
                return;
            }

            dynamicPrioritySchedule.addTask(taskName);
            dynamicScheduleModel.fireTableDataChanged();
        });

        Timer dynamicThread = new Timer();
        dynamicThread.schedule(new TimerTask() {
            @Override
            public void run() {
                dynamicPrioritySchedule.runTask();
                try {
                    SwingUtilities.invokeAndWait(dynamicScheduleModel::fireTableDataChanged);
                } catch (InterruptedException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 100);
    }

    private class DynamicScheduleModel extends AbstractTableModel {

        private final String[] title = new String[]{
                "作业名", "作业状态", "作业优先级", "作业长度", "进度", "完成率"
        };

        @Override
        public int getRowCount() {
            return dynamicPrioritySchedule.size();
        }

        @Override
        public int getColumnCount() {
            return title.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return title[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            TaskPCB task = dynamicPrioritySchedule.getTask(rowIndex);

            switch (columnIndex) {
                case 0:
                    return task.taskName;

                case 1:
                    return task.status;

                case 2:
                    return task.priory;

                case 3:
                    return task.needTime;

                case 4:
                    return task.progress;

                case 5:
                    return task.progress + "/" + task.needTime;

                default:
                    return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            super.setValueAt(aValue, rowIndex, columnIndex);
        }
    }

    private static class SliceScheduleModel extends AbstractTableModel {

        private List<TaskPCB> taskList;

        private static final String[] tableTitle = new String[]{
                "作业名", "作业状态", "作业长度", "进度", "完成率"
        };

        SliceScheduleModel(List<TaskPCB> tasks) {
            taskList = tasks;
        }

        @Override
        public int getRowCount() {
            return taskList.size();
        }

        @Override
        public int getColumnCount() {
            return tableTitle.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return tableTitle[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            TaskPCB task = taskList.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return task.status;

                case 1:
                    return task.taskName;

                case 2:
                    return task.needTime;

                case 3:
                    return task.progress;

                case 4:
                    return String.format(Locale.getDefault(), "%d/%d", task.progress, task.needTime);

                default:
                    return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            TaskPCB task = taskList.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    task.status = (TaskPCB.Status) aValue;
                    break;

                case 1:
                    task.taskName = (String) aValue;
                    break;

                case 2:
                    task.needTime = (int) aValue;
                    break;

                case 3:
                    task.progress = (int) aValue;
                    break;
            }
        }

    }

    private static void showUI() {
        JFrame frame = new JFrame("处理机调度");
        Schedule schedule = new Schedule();
        frame.setContentPane(schedule.main);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
/*
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //通过调用GraphicsEnvironment的getDefaultScreenDevice方法获得当前的屏幕设备了
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        // 全屏设置
        gd.setFullScreenWindow(frame);*/
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Schedule::showUI);
    }

}
