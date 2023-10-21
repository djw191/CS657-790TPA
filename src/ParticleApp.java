import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.*;

public class ParticleApp {
    private static final int SIZE = 768;
    private static final int THREAD_COUNT = 200;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(THREAD_COUNT);
    private static final ParticlePanel panel = new ParticlePanel(SIZE);
    private static final JFrame jFrame = new JFrame("Particle App");

    public static void main(String[] args) throws IOException {
        // JFrame initializes here, adds the canvas, sets the size, makes it visible, and has it close the application if you close the window
        jFrame.add(panel);
        jFrame.setSize(SIZE, SIZE);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Particle[] particles = new Particle[THREAD_COUNT];
        for(int i = 0; i < THREAD_COUNT; i++){
            Particle tmp = particles[i] = new Particle(SIZE/2, SIZE/2);
            scheduler.scheduleAtFixedRate(() -> { tmp.move(); panel.repaint(); }, 0, 10, MILLISECONDS);
        }
        panel.setParticles(particles);

        System.out.println("Hit \"Enter\" to exit program");
        System.in.read();
        stop();
    }

    // Not useful unless we want to be able to kill all the threads at any point from the threads?
    public static synchronized void stop(){
        scheduler.shutdownNow();
        jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
    }
}
