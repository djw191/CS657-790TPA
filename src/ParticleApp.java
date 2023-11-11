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
    private final int SIZE;
    private final int THREAD_COUNT;
    private final ScheduledExecutorService scheduler;
    private final ParticlePanel panel;
    private final JFrame jFrame = new JFrame("Particle App");
    private final Random rng = new Random();
    public ParticleApp(int SIZE, int THREAD_COUNT){
        this.SIZE = SIZE;
        this.THREAD_COUNT = THREAD_COUNT;
        scheduler = Executors.newScheduledThreadPool(THREAD_COUNT);
        panel = new ParticlePanel(SIZE);
    }
    private void SetupJFrame(){
        // JFrame initializes here, adds the canvas, sets the size, makes it visible, and has it close the application if you close the window
        jFrame.add(panel);
        jFrame.setSize(SIZE, SIZE);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    private void CreateAndStartThreads(){
        Particle[] particles = new Particle[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            Particle tmp = particles[i] = new Particle(SIZE/2, SIZE/2);
            scheduler.scheduleAtFixedRate(
                    () -> {
                        tmp.move(rng.nextInt(10, 21), rng.nextInt(10, 21));
                        panel.repaint();
                    },
                    0, 1000/24 /*fps*/, MILLISECONDS);
        }
        panel.setParticles(particles);
    }
    public void Start(){
        SetupJFrame();
        CreateAndStartThreads();
    }
    /**
     * Stops threads, and closes JFrame window
     */
    public synchronized void Stop() {
        scheduler.shutdownNow(); //Stop Threads
        jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING)); //Ask JFrame to close
    }
    public static void main(String[] args) {
        ParticleApp particleApp = new ParticleApp(1024, 128);
        particleApp.Start();
        System.out.println("Hit \"Enter\" to exit program");
        try {
            System.in.read();
        } catch (IOException ignored) { }
        particleApp.Stop();
    }
}
