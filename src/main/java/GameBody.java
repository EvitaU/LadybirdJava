import javax.swing.*;
import java.awt.*;

public class GameBody {
    private static final int SCREEN_WIDTH =(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();

    private static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private JFrame f = new JFrame("Jumpy Ladybird");

    private static GameBody gb = new GameBody();

    public GameBody(){
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gb.buildFrame();
                Thread t = new Thread(){
                    public void run(){
                    }
                };
                t.start();
            }
        });
    }
    private void buildFrame(){
        Image icon = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/ladybird.png"));
        f.setContentPane(createContentPane());
        f.setResizable(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setAlwaysOnTop(false);
        f.setVisible(true);
        f.setMinimumSize(new Dimension(SCREEN_WIDTH*1/4, SCREEN_HEIGHT*1/4));
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setIconImage(icon);
    }
    private JPanel createContentPane(){
        topPanel = new JPanel(); //
        return topPanel;
    }
}
