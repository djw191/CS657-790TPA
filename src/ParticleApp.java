import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.*;

/**
 * Particle App Program: Creates multiple "particles," each assigned to its own thread.  The particles randomly change
 * their position, and a canvas updates their visual position when asked by the particle.  The particles are managed by
 * a scheduled executor service.
 *
 * @author Derek Woodard
 */
public class ParticleApp {
    private static final int SIZE = 768;
    private static final int THREAD_COUNT = 200;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(THREAD_COUNT);
    private static final ParticlePanel panel = new ParticlePanel(SIZE);
    private static final JFrame jFrame = new JFrame("Particle App");
    private static final Random rng = new Random();

    public static void main(String[] args) {
        // JFrame initializes here, adds the canvas, sets the size, makes it visible, and has it close the application if you close the window
        jFrame.add(panel);
        jFrame.setSize(SIZE, SIZE);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Particle[] particles = new Particle[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            Particle tmp = particles[i] = new Particle(SIZE / 2, SIZE / 2);
            scheduler.scheduleAtFixedRate(
                    () -> {
                        tmp.move(10, 10);
                        panel.repaint();
                    },
                    0, rng.nextInt(1, 50), MILLISECONDS);
        }
        panel.setParticles(particles);

        System.out.println("Hit \"Enter\" to exit program");
        try {
            System.in.read();
        } catch (IOException ignored) {
        }
        stop();
    }

    /**
     * Stops threads, and closes JFrame window
     */
    public static synchronized void stop() {
        scheduler.shutdownNow(); //Stop Threads
        jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING)); //Ask JFrame to close
    }
}
