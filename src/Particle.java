import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Particle {
    protected int x, y;
    protected final Random rng = new Random();

    final static BufferedImage img; //only necessary if we want images

    static {
        BufferedImage timg = null;
        try { timg = ImageIO.read(new File("firefox32x32.png"));
        } catch (IOException e) { System.err.println("Bad IO On Image Open"); System.exit(-1);}
        img = timg;
    }

    public Particle(int x, int y){
        this.x = x;
        this.y = y;
    }

    public synchronized void move(){
        x += rng.nextInt(11) - 5;
        y += rng.nextInt(21) - 10;
    }

    public void draw(Graphics g){
        int lx, ly;
        synchronized (this) { lx = x; ly = y; }
        //g.drawRect(lx, ly, 10, 10);
        g.drawImage(img, lx-img.getWidth()/2, ly-img.getHeight()/2, null);
    }
}
