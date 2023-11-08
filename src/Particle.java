import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Object which hold's it's position, as well as functions required to move it, and to draw it on a canvas.
 */
public class Particle {
    final static BufferedImage img; //only necessary if we want images
    static {
        BufferedImage timg = null;
        try {
            timg = ImageIO.read(new File("firefox32x32.png"));
        } catch (IOException e) {
            System.err.println("Bad IO On Image Open");
            System.exit(-1);
        }
        img = timg;
    }

    protected final Random rng = new Random();
    protected int x, y;

    /**
     * @param x Initial X Coordinate on Canvas
     * @param y Initial Y Coordinate on Canvas
     */
    public Particle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Moves the particle by a set amount
     *
     * @param x Range to move particle horizontally (+/- x)
     * @param y Range to move particle vertically (+/- y)
     */
    public synchronized void move(int x, int y) {
        this.x += rng.nextInt(x * 2 + 1) - x;
        this.y += rng.nextInt(y * 2 + 1) - y;
    }

    /**
     * Moves the particle by a random amount (Default Range +/- 5 horizontally, +/- 10 vertically)
     */
    @SuppressWarnings("unused")
    public synchronized void move() {
        move(5, 10);
    }


    /**
     * Function intended to be called by the canvas when it's told to update.  Will draw the particle to the canvas,
     * based on its current location.
     *
     * @param g Holds the Graphics object required to draw on
     */
    public void draw(Graphics g) {
        int lx, ly;
        synchronized (this) {
            lx = x;
            ly = y;
        }
        //g.drawRect(lx, ly, 10, 10);
        g.drawImage(img, lx - img.getWidth() / 2, ly - img.getHeight() / 2, null);
    }
}
