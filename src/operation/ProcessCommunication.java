package operation;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * 进程同步演示代码
 * <p/>
 * Created by Billin on 2016/11/15.
 */
public class ProcessCommunication {

    private JPanel mainPanel;

    private JButton father;

    private JButton son;

    private JButton mother;

    private JButton daughter;

    private JLabel picture;

    private JLabel message;

    private Function<String, ImageIcon> getImg = name -> {
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("./image/" + name + ".jpg"));
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        return imageIcon;
    };

    public ProcessCommunication() {

        // init jlabel
        picture.setText(null);
        message.setText(null);

        Semaphore nullSemaphore = new Semaphore(1);

        Semaphore appleSemaphore = new Semaphore(0);

        Semaphore orangeSemaphore = new Semaphore(0);

        // init button
        father.addActionListener(e -> new Thread(() -> {

            try {
                nullSemaphore.acquire();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            SwingUtilities.invokeLater(() -> {
                message.setText("父亲放了一个苹果");
                picture.setIcon(getImg.apply("apple"));
            });

            appleSemaphore.release();
            /*
            if (signal == Signal.NULL) {
                signal = Signal.APPLE;
                SwingUtilities.invokeLater(() -> {
                    message.setText("父亲放了一个苹果");
                    picture.setIcon(getImg.apply("apple"));
                });
            } else {
                SwingUtilities.invokeLater(() ->
                        message.setText("盘子已满，请等待..."));
            }*/

        }).start());

        mother.addActionListener(e -> new Thread(() -> {

            try {
                nullSemaphore.acquire();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                message.setText("母亲放了一个桔子");
                picture.setIcon(getImg.apply("orange"));
            });

            orangeSemaphore.release();
            /*
            synchronized (this) {
                if (signal == Signal.NULL) {
                    signal = Signal.ORANGE;
                    SwingUtilities.invokeLater(() -> {
                        message.setText("母亲放了一个桔子");
                        picture.setIcon(getImg.apply("orange"));
                    });
                } else {
                    SwingUtilities.invokeLater(() ->
                            message.setText("盘子已满，请等待..."));
                }
            }*/
        }).start());

        daughter.addActionListener(e -> new Thread(() -> {

            try {
                appleSemaphore.acquire();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                message.setText("女儿吃了苹果");
                picture.setIcon(getImg.apply("panel"));
            });
            nullSemaphore.release();
            /*
            synchronized (this) {
                if (signal == Signal.NULL) {
                    SwingUtilities.invokeLater(() ->
                            message.setText("空盘子，请等待..."));
                } else if (signal == Signal.APPLE) {
                    signal = Signal.NULL;
                    SwingUtilities.invokeLater(() -> {
                        message.setText("女儿吃了苹果");
                        picture.setIcon(getImg.apply("panel"));
                    });
                } else {
                    SwingUtilities.invokeLater(() ->
                            message.setText("女儿：我不吃桔子"));
                }
            }*/
        }).start());

        son.addActionListener(e -> new Thread(() -> {

            try {
                orangeSemaphore.acquire();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                message.setText("儿子：吃了桔子");
                picture.setIcon(getImg.apply("panel"));
            });
            nullSemaphore.release();
            /*
            synchronized (this) {
                if (signal == Signal.NULL) {
                    SwingUtilities.invokeLater(() ->
                            message.setText("空盘子，请等待..."));
                } else if (signal == Signal.APPLE) {
                    SwingUtilities.invokeLater(() ->
                            message.setText("儿子：我不吃苹果"));
                } else {
                    signal = Signal.NULL;
                    SwingUtilities.invokeLater(() -> {
                        message.setText("儿子：吃了桔子");
                        picture.setIcon(getImg.apply("panel"));
                    });
                }
            }*/
        }).start());

    }

    public static void main(String[] args) {

        JFrame jFrame = new JFrame("Process communication");
        jFrame.setContentPane(new ProcessCommunication().mainPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(new Dimension(600, 600));
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

}
