package operation.banker;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Random;

/**
 * Created by Billin on 2016/11/18.
 */
public class Banker {

    private JTable allocationSheet;

    private JTable sequenceSheet;

    private JButton request;

    private JButton generalSequence;

    private JPanel main;

    private int tableLength = 5;

    private BankerAlgorithm bankerAlgorithm;

    Banker() {
        bankerAlgorithm = new BankerAlgorithm(tableLength);

        Random random = new Random();

        AllocationSheetModule allocationSheetModule = new AllocationSheetModule();
        allocationSheetModule.allocationTables = bankerAlgorithm.getTables();
        allocationSheetModule.available = bankerAlgorithm.getAvailable();
        allocationSheet.setModel(allocationSheetModule);

        SequenceSheetModule sequenceSheetModule = new SequenceSheetModule();
        sequenceSheet.setModel(sequenceSheetModule);

        request.addActionListener(e -> {
            Resource resource = bankerAlgorithm.getRequest(0);
            int allocateIndex = random.nextInt(tableLength);
            if (!bankerAlgorithm.allocate(allocateIndex, resource)) {
                JOptionPane.showMessageDialog(main, String.format("进程%s请求资源%s错误", bankerAlgorithm.getTables()[allocateIndex].processName, resource.toString()));
            } else {
                JOptionPane.showMessageDialog(main,
                        String.format("在进程%s中发起了资源%s的请求", bankerAlgorithm.getTables()[allocateIndex].processName, resource.toString()));
                allocationSheetModule.available = bankerAlgorithm.getAvailable();
                allocationSheetModule.allocationTables = bankerAlgorithm.getTables();
                allocationSheetModule.fireTableDataChanged();
            }
        });

        generalSequence.addActionListener(e -> {
            sequenceSheetModule.allocationTables = bankerAlgorithm.getSequence();
            sequenceSheetModule.fireTableDataChanged();
        });
    }

    private class AllocationSheetModule extends AbstractTableModel {
        private final String[] tableName = new String[]{
                "Process",
                "Allocation",
                "Need",
                "Available"
        };

        AllocationTable[] allocationTables;

        Resource available;

        @Override
        public String getColumnName(int column) {
            return tableName[column];
        }

        @Override
        public int getRowCount() {
            return allocationTables == null ? 0 : allocationTables.length;
        }

        @Override
        public int getColumnCount() {
            return tableName.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            AllocationTable allocationTable = allocationTables[rowIndex];

            switch (columnIndex) {
                case 0:
                    return allocationTable.processName;

                case 1:
                    return allocationTable.allocation;

                case 2:
                    return allocationTable.need();

                case 3:
                    return available;

                default:
                    return null;
            }
        }
    }

    private class SequenceSheetModule extends AbstractTableModel {

        private final String[] tableName = new String[]{
                "Process",
                "Free",
                "Need",
                "Allocation",
                "Free + Allocation",
                "Finish"
        };

        AllocationTable[] allocationTables;

        @Override
        public String getColumnName(int column) {
            return tableName[column];
        }

        @Override
        public int getRowCount() {
            return allocationTables == null ? 0 : allocationTables.length;
        }

        @Override
        public int getColumnCount() {
            return tableName.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            AllocationTable allocationTable = allocationTables[rowIndex];
            switch (columnIndex) {
                case 0:
                    return allocationTable.processName;

                case 1:
                    return allocationTable.free;

                case 2:
                    return allocationTable.need();

                case 3:
                    return allocationTable.allocation;

                case 4:
                    Resource resource = (Resource) allocationTable.free.clone();
                    resource.add(allocationTable.allocation);
                    return resource;

                case 5:
                    return allocationTable.finish;

                default:
                    return null;
            }
        }
    }


    public static void drawUI() {
        JFrame frame = new JFrame("banker's algorithm");
        Banker banker = new Banker();
        frame.setContentPane(banker.main);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Banker::drawUI);
    }

}
