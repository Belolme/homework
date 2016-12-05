package operation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 模拟售票的程序
 * <p/>
 * Created by Billin on 2016/11/15.
 */
public class MultiThread {

    private JPanel main;

    private JLabel titile;

    private JPanel body;

    private JButton exit;

    private JButton commit;

    private JComboBox<Line> lineCombox;

    private JList<Ticket> lineList;

    private JComboBox<String> window1TicketId;

    private JComboBox<String> window2TicketId;

    private JTextField window1Ticket;

    private JTextField window2Ticket;

    private JButton buyWindow1;

    private JButton buyWindow2;

    private Map<String, Ticket> ticketMap = new HashMap<>();

    public MultiThread() {

        ticketMap.put("2001", new Ticket("2001", "7:00", 35, 40));
        ticketMap.put("2002", new Ticket("2002", "8:30", 35, 40));
        ticketMap.put("2003", new Ticket("2003", "12:30", 35, 40));
        ticketMap.put("2004", new Ticket("2004", "15:00", 35, 40));
        ticketMap.put("2005", new Ticket("2005", "7:00", 35, 40));
        ticketMap.put("2006", new Ticket("2006", "8:30", 35, 40));
        ticketMap.put("2007", new Ticket("2007", "12:30", 35, 40));
        ticketMap.put("2008", new Ticket("2008", "15:00", 35, 40));

        Line line1 = new Line(1,
                "GuangZhou -> JiangMeng",
                new Ticket[]{
                        ticketMap.get("2001"),
                        ticketMap.get("2002"),
                        ticketMap.get("2003"),
                        ticketMap.get("2004"),
                }
        );

        Line line2 = new Line(2,
                "JiangMeng -> GuangZhou",
                new Ticket[]{
                        ticketMap.get("2005"),
                        ticketMap.get("2006"),
                        ticketMap.get("2007"),
                        ticketMap.get("2008")
                }
        );

        // init combo box
        lineCombox.addItem(line1);
        lineCombox.addItem(line2);
        lineCombox.addActionListener(e -> {

            lineList.setListData(((Line) lineCombox.getSelectedItem()).tickets);

            window1TicketId.removeAllItems();
            window2TicketId.removeAllItems();

            for (Ticket ticket : ((Line) lineCombox.getSelectedItem()).tickets) {
                window1TicketId.addItem(ticket.id);
                window2TicketId.addItem(ticket.id);
            }

        });


        buyWindow1.addActionListener(e -> {
            int number = Integer.parseInt(window1Ticket.getText());
            String id = (String) window1TicketId.getSelectedItem();

            synchronized (this) {
                if (ticketMap.get(id).remain < number) {
                    JOptionPane.showMessageDialog(null, "buy number is large than remain");
                } else {
                    int origin = ticketMap.get(id).remain;
                    ticketMap.get(id).remain = origin - number;
                    lineList.setListData(((Line) lineCombox.getSelectedItem()).tickets);
                }
            }
        });
        buyWindow2.addActionListener(e -> {
            int number = Integer.parseInt(window2Ticket.getText());
            String id = (String) window2TicketId.getSelectedItem();

            synchronized (this) {
                if (ticketMap.get(id).remain < number) {
                    JOptionPane.showMessageDialog(null, "buy number is large than remain");
                } else {
                    int origin = ticketMap.get(id).remain;
                    ticketMap.get(id).remain = origin - number;
                    lineList.setListData(((Line) lineCombox.getSelectedItem()).tickets);
                }
            }
        });

        // init jlist
        lineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lineList.setLayoutOrientation(JList.VERTICAL);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("网上售票系统");
        frame.setContentPane(new MultiThread().main);
        frame.setLocationRelativeTo(null);
        frame.setPreferredSize(new Dimension(500, 500));
        frame.pack();
        frame.setVisible(true);
    }

    private static class Line {
        int id;

        String name;

        Ticket[] tickets;

        Line(int id, String name, Ticket[] tickets) {
            this.id = id;
            this.name = name;
            this.tickets = tickets;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class Ticket {

        String id;

        String time;

        int price;

        int remain;

        Ticket(String id, String time, int price, int remain) {
            this.id = id;
            this.time = time;
            this.price = price;
            this.remain = remain;
        }

        @Override
        public String toString() {
            return String.format("%-8s %-10s %-8d $%d", id, time, price, remain);
        }
    }
}
