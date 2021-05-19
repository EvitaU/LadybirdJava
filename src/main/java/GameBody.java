import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GameBody implements ActionListener {
    private static final int SCREEN_WIDTH =(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();

    private static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private static final int PIPE_GAP = SCREEN_HEIGHT/5; //distance between pipes
    private static final int PIPE_WIDTH = SCREEN_WIDTH/8, PIPE_HEIGHT = 4*PIPE_WIDTH; //distance between pipes
    private static final int LADYBIRD_WIDTH = 120, LADYBIRD_HEIGHT = 75;
    private static final int UPDATE_DIFFERENCE = 25; //TIME BETWEEN UPDATES
    private static final int X_MOVEMENT_DIFFERENCE = 5;
    private static final int SCREEN_DELAY = 300;
    private static final int LADYBIRD_X_LOCATION = SCREEN_WIDTH/7;
    private static final int LADYBIRD_JUMP_DIFF = 10, LADYBIRD_FALL_DIFF = LADYBIRD_JUMP_DIFF/2, LADYBIRD_JUMP_HEIGHT = PIPE_GAP - LADYBIRD_HEIGHT - LADYBIRD_JUMP_DIFF*2;

    private boolean loopVar = true;
    private boolean gamePlay = false;
    private boolean ladybirdThrust = false;

    private boolean ladybirdFired = false;
    private boolean released = true;
    private int ladybirdYTracker = SCREEN_HEIGHT/2 - LADYBIRD_HEIGHT;
    private Object buildComplete = new Object();

    private JFrame f = new JFrame("Jumpy Ladybird");
    private JButton startGame;
    private JPanel topPanel;

    private static GameBody gb = new GameBody();
    private static GameScreen pgs;

    public GameBody(){
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gb.buildFrame();
                Thread t = new Thread() {
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
        f.addKeyListener((KeyListener) this);
    }
    private JPanel createContentPane(){
        topPanel = new JPanel();
        topPanel.setBackground(Color.BLACK);

        //layers panels
        LayoutManager overlay = new OverlayLayout(topPanel);
        topPanel.setLayout(overlay);

        //start game button
        startGame = new JButton("Start");
        startGame.setBackground(Color.GRAY);
        startGame.setForeground(Color.LIGHT_GRAY);
        startGame.setFocusable(false);
        startGame.setFont(new Font("Calibri", Font.BOLD, 42));
        startGame.setAlignmentX(0.5f);
        startGame.setAlignmentY(0.5f);
        startGame.addActionListener(this);
        topPanel.add(startGame);

        pgs = new GameScreen(SCREEN_WIDTH, SCREEN_HEIGHT, true);

        topPanel.add(pgs);

        return topPanel;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == startGame){
            loopVar = false;

            fadeOperation();

        }
        else if(e.getSource() == buildComplete){
            Thread t = new Thread(){
                public void run(){
                    loopVar = true;
                    gb.gameScreen(false);
                }
            };
            t.start();
        }
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE && gamePlay == true && released == true){
            if(ladybirdThrust) {
                ladybirdFired = true;
            }
            ladybirdThrust = true;
            released = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_B && gamePlay == false) {
            ladybirdYTracker = SCREEN_HEIGHT/2 - LADYBIRD_HEIGHT;
            ladybirdThrust = false;
            actionPerformed(new ActionEvent(startGame, -1, ""));
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            released = true;
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    private void  fadeOperation(){
        Thread t = new Thread(){
            public void run(){
                topPanel.remove(startGame);
                topPanel.remove(pgs);
                topPanel.revalidate();
                topPanel.repaint();

                //fade
                JPanel temp = new JPanel();
                int alpha = 0;
                temp.setBackground(new Color(0, 0, 0, alpha));
                topPanel.add(temp);
                topPanel.add(pgs);
                topPanel.revalidate();
                topPanel.repaint();

                long currentTime = System.currentTimeMillis();

                while(temp.getBackground().getAlpha() != 255) {
                    if((System.currentTimeMillis() - currentTime) > UPDATE_DIFFERENCE/2) {
                        if(alpha < 255 - 10) {
                            alpha += 10;
                        }
                        else {
                            alpha = 255;
                        }

                        temp.setBackground(new Color(0, 0, 0, alpha));

                        topPanel.revalidate();
                        topPanel.repaint();
                        currentTime = System.currentTimeMillis();
                    }
                }
                topPanel.removeAll();
                topPanel.add(temp);
                pgs = new GameScreen(SCREEN_WIDTH, SCREEN_HEIGHT, false);
                pgs.sendText("");
                topPanel.add(pgs);

                while(temp.getBackground().getAlpha() != 0) {
                    if((System.currentTimeMillis() - currentTime) > UPDATE_DIFFERENCE/2) {
                        if(alpha > 10) {
                            alpha -= 10;
                        }
                        else {
                            alpha = 0;
                        }

                        temp.setBackground(new Color(0, 0, 0, alpha));

                        topPanel.revalidate();
                        topPanel.repaint();
                        currentTime = System.currentTimeMillis();
                    }
                }
                actionPerformed(new ActionEvent(buildComplete, -1, "Build Finished"));
            }
        };
        t.start();
    }

    private void gameScreen(boolean isSplash) {
        BottomPipe bp1 = new BottomPipe(PIPE_WIDTH, PIPE_HEIGHT);
        BottomPipe bp2 = new BottomPipe(PIPE_WIDTH, PIPE_HEIGHT);
        TopPipe tp1 = new TopPipe(PIPE_WIDTH, PIPE_HEIGHT);
        TopPipe tp2 = new TopPipe(PIPE_WIDTH, PIPE_HEIGHT);
        Ladybird ladybird = new Ladybird(LADYBIRD_WIDTH, LADYBIRD_HEIGHT);

        int xLoc1 = SCREEN_WIDTH + SCREEN_DELAY, xLoc2 = (int) ((double) 3.0 / 2.0 * SCREEN_WIDTH + PIPE_WIDTH / 2.0)
        +SCREEN_DELAY;
        int yLoc1 = bottomPipeLoc(), yLoc2 = bottomPipeLoc();
        int ladybirdX = LADYBIRD_X_LOCATION, ladybirdY = ladybirdYTracker;

        long startTime = System.currentTimeMillis();

        while (loopVar) {
            if ((System.currentTimeMillis() - startTime) > UPDATE_DIFFERENCE) {
                if (xLoc1 < (0 - PIPE_WIDTH)) {
                    xLoc1 = SCREEN_WIDTH;
                    yLoc1 = bottomPipeLoc();
                }
                else if(xLoc2 < (0-PIPE_WIDTH)){
                    xLoc2 = SCREEN_WIDTH;
                    yLoc2 = bottomPipeLoc();
                }

                xLoc1 -= X_MOVEMENT_DIFFERENCE;
                xLoc2 -= X_MOVEMENT_DIFFERENCE;

                if (ladybirdFired && !isSplash) {
                    ladybirdYTracker = ladybirdY;
                    ladybirdFired = false;
                }
                if (ladybirdThrust && !isSplash) {
                    if (ladybirdYTracker - ladybirdY - LADYBIRD_JUMP_DIFF < LADYBIRD_JUMP_HEIGHT) {
                        if (ladybirdY - LADYBIRD_JUMP_DIFF > 0) {
                            ladybirdY -= LADYBIRD_JUMP_DIFF;
                        } else {
                            ladybirdY = 0;
                            ladybirdYTracker = ladybirdY;
                            ladybirdThrust = false;
                        }
                    }else {
                        ladybirdYTracker = ladybirdY;
                        ladybirdThrust = false;
                    }
                    } else if (!isSplash) {
                        ladybirdY += LADYBIRD_JUMP_DIFF;
                        ladybirdYTracker = ladybirdY;
                    }

                    bp1.setX(xLoc1);
                    bp1.setY(yLoc1);
                    bp2.setX(xLoc2);
                    bp2.setY(yLoc2);
                    tp1.setX(xLoc1);
                    tp1.setY(yLoc1 - PIPE_GAP - PIPE_HEIGHT);

                    tp2.setX(xLoc2);
                    tp2.setY(yLoc2 - PIPE_GAP - PIPE_HEIGHT);

                    if (!isSplash) {
                        ladybird.setX(ladybirdX);
                        ladybird.setY(ladybirdY);
                        pgs.setLadybird(ladybird);
                    }
                    pgs.setBottomPipe(bp1, bp2);
                    pgs.setTopPipe(tp1, tp2);

                    if (!isSplash && ladybird.getWidth() != -1) {
                        updateScore(bp1, bp2, ladybird);
                    }

                    topPanel.revalidate();
                    topPanel.repaint();

                    startTime = System.currentTimeMillis();
                }
            }
        }
        private int bottomPipeLoc () {
            int temp = 0;
            while (temp <= PIPE_GAP + 50 || temp >= SCREEN_HEIGHT - PIPE_GAP) {
                temp = (int) ((double) Math.random() * ((double) SCREEN_HEIGHT));
            }
            return temp;
        }
        private void updateScore (BottomPipe bp1, BottomPipe bp2, Ladybird ladybird){
            if (bp1.getX() + PIPE_WIDTH < ladybird.getX() && bp1.getX() + PIPE_WIDTH > ladybird.getX() - X_MOVEMENT_DIFFERENCE) {
                pgs.incrementJump();
            } else if (bp2.getX() + PIPE_WIDTH < ladybird.getX() && bp2.getX() + PIPE_WIDTH > ladybird.getX() - X_MOVEMENT_DIFFERENCE) {
                pgs.incrementJump();
            }
        }
    private void collisionDetection(BottomPipe bp1, BottomPipe bp2, TopPipe tp1, TopPipe tp2, Ladybird ladybird) {
        collisionHelper(ladybird.getRectangle(), bp1.getRectangle(), ladybird.getBI(), bp1.getBI());
        collisionHelper(ladybird.getRectangle(), bp2.getRectangle(), ladybird.getBI(), bp2.getBI());
        collisionHelper(ladybird.getRectangle(), tp1.getRectangle(), ladybird.getBI(), tp1.getBI());
        collisionHelper(ladybird.getRectangle(), tp2.getRectangle(), ladybird.getBI(), tp2.getBI());

        if(ladybird.getY() + LADYBIRD_HEIGHT > SCREEN_HEIGHT*7/8) {
            pgs.sendText("Game Over");
            loopVar = false;
            gamePlay = false;
        }
    }
    private void collisionHelper(Rectangle r1, Rectangle r2, BufferedImage b1, BufferedImage b2) {
        if (r1.intersects(r2)) {
            Rectangle r = r1.intersection(r2);

            int firstI = (int) (r.getMinX() - r1.getMinX());
            int firstJ = (int) (r.getMinY() - r1.getMinY());
            int bp1XHelper = (int) (r1.getMinX() - r2.getMinX());
            int bp1YHelper = (int) (r1.getMinY() - r2.getMinY());

            for (int i = firstI; i < r.getWidth() + firstI; i++) { //
                for (int j = firstJ; j < r.getHeight() + firstJ; j++) {
                    if ((b1.getRGB(i, j) & 0xFF000000) != 0x00 && (b2.getRGB(i + bp1XHelper, j + bp1YHelper) & 0xFF000000) != 0x00) {
                        pgs.sendText("Game Over");
                        loopVar = false;
                        gamePlay = false;
                        break;
                    }
                }
            }
        }
    }
}