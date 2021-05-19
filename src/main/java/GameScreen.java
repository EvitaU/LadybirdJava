import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {

    private static final long serialVersionUID = 1L;

    private int screenWidth, screenHeight;
    private boolean isSplash = true;
    private int successfulJumps = 0;
    private String message = "Jumpy Ladybird";
    private Font primaryFont = new Font("Goudy Stout", Font.BOLD, 56), failFont = new Font("Calibri", Font.BOLD, 56);
    private int messageWidth = 0, scoreWidth = 0;
    private BottomPipe bp1, bp2;
    private TopPipe tp1, tp2;
    private Ladybird ladybird;

    public GameScreen(int screenWidth, int screenHeight, boolean isSplash){
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.isSplash = isSplash;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(new Color(89,81,247));
        g.fillRect(0,0, screenWidth, screenHeight*7/8);
        g.setColor(new Color(147, 136, 9));
        g.fillRect(0, screenHeight*7/8, screenWidth, screenHeight/8);

        g.setColor(Color.BLACK);
        g.drawLine(0, screenHeight*7/8, screenWidth, screenHeight*7/8);

        if(bp1 !=null && bp2 !=null && tp1 != null && tp2 != null) {
            g.drawImage(bp1.getPipe(), bp1.getX(), bp1.getY(), null);
            g.drawImage(bp2.getPipe(), bp2.getX(), bp2.getY(), null);
            g.drawImage(tp1.getPipe(), tp1.getX(), tp1.getY(), null);
            g.drawImage(tp2.getPipe(), tp2.getX(), tp2.getY(), null);
        }
        if(!isSplash && ladybird != null){
            g.drawImage(ladybird.getLadybird(), ladybird.getX(), ladybird.getY(), null);
        }
        try {
            g.setFont(primaryFont);
            FontMetrics metric = g.getFontMetrics(failFont);
            messageWidth = metric.stringWidth(message);
        }
        catch (Exception e){
            g.setFont(failFont);
            FontMetrics metric = g.getFontMetrics(failFont);
            messageWidth = metric.stringWidth(message);
        }
        g.drawString(message, screenWidth/2-messageWidth/2, screenHeight/4);

        if(!isSplash){
            g.drawString(String.format("%d", successfulJumps), screenWidth/2-scoreWidth/2, 50);
        }
    }
    public void setBottomPipe(BottomPipe bp1, BottomPipe bp2){
        this.bp1 = bp1;
        this.bp2 = bp2;
    }

    public void setTopPipe(TopPipe tp1, TopPipe tp2){
        this.tp1 = tp1;
        this.tp2 = tp2;
    }
    public void setLadybird(Ladybird ladybird){
        this.ladybird = ladybird;
    }
    public void incrementJump(){
        successfulJumps++;
    }
    public int getScore(){
        return successfulJumps;
    }

    public void sendText (String message){
        this.message = message;
    }

}
