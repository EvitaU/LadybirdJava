import java.awt.*;
import java.awt.image.BufferedImage;

public class Ladybird {
    private Image ladybird;
    private int xLoc = 0, yLoc = 0;

    public Ladybird(int initialWidth, int initialHeight) {
        ladybird = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/ladybird.png"));
        scaleLadybird(initialWidth, initialHeight);

    }

    public void scaleLadybird(int width, int height) {
        ladybird = ladybird.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public Image getBird() {
        return ladybird;
    }

    public int getWidth() {
        try {
            return ladybird.getWidth(null);
        } catch (Exception e) {
            return -1;
        }
    }

    public int getHeight() {
        try {
            return ladybird.getHeight(null);
        } catch (Exception e) {
            return -1;
        }
    }

    public void setX(int x) {
        xLoc = x;
    }

    public int getX() {
        return xLoc;
    }

    public void setY(int y) {
        yLoc = y;
    }

    public int getY() {
        return yLoc;
    }

    public Rectangle getRectangle() {
        Rectangle rectangle = new Rectangle(xLoc, yLoc, ladybird.getWidth(null), ladybird.getHeight(null));
        return rectangle;
    }

    public BufferedImage getBI() {
        BufferedImage bi = new BufferedImage(ladybird.getWidth(null), ladybird.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.drawImage(ladybird, 0, 0, null);
        g.dispose();
        return bi;
    }
}