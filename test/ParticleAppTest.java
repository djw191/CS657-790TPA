import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class ParticleAppTest {

    private ParticleApp particleApp;
    private final int SIZE = 1024;
    private final int THREAD_COUNT = 128;

    @BeforeEach
    public void setUp() {
        particleApp = new ParticleApp(SIZE, THREAD_COUNT);
    }

    @Test
    public void testParticleAppInitializationWithValidArguments() {
        assertNotNull(particleApp.scheduler,
                "Scheduler should not be null after initialization");
        assertNotNull(particleApp.panel,
                "Panel should not be null after initialization");
    }

    @Test
    public void testParticleAppInitializationWithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new ParticleApp(-1, THREAD_COUNT));
    }

    /**
     * Set the close operation to dispose the window rather than exit the application.
     * This is **needed** for testing because of the WindowConstants.EXIT_ON_CLOSE in the app method.
     */
    private void setupJFrameForTest() {
        particleApp.SetupJFrame();
        particleApp.jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    @Test
    public void testSetupJFrame() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(this::setupJFrameForTest);
        assertTrue(particleApp.jFrame.isVisible(),
                "JFrame should be visible after setup");
        assertEquals(SIZE, particleApp.jFrame.getSize().width,
                "JFrame should have the correct size");
        assertEquals(SIZE, particleApp.jFrame.getSize().height,
                "JFrame should have the correct size");
    }

    @Test
    public void testParticlePanelIsAddedToJFrame() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(this::setupJFrameForTest);
        Component[] components = particleApp.jFrame.getContentPane().getComponents();
        boolean hasParticlePanel = false;
        for (Component component : components) {
            if (component instanceof ParticlePanel) {
                hasParticlePanel = true;
                break;
            }
        }
        assertTrue(hasParticlePanel,
                "ParticlePanel should be added to JFrame");
    }

    @Test
    public void testSetAndGetParticles() {
        TestParticlePanel testParticlePanel = new TestParticlePanel(SIZE);
        Particle[] testParticles = { new Particle(10, 10), new Particle(20, 20) };

        testParticlePanel.setParticles(testParticles);
        assertArrayEquals(testParticles, testParticlePanel.getParticles(), "getParticles should return the set array of particles");
    }

    @Test
    public void testSetParticlesWithNull() {
        TestParticlePanel testParticlePanel = new TestParticlePanel(SIZE);
        assertThrows(IllegalArgumentException.class, () -> testParticlePanel.setParticles(null),
                "setParticles should throw IllegalArgumentException when passed null");
    }

    @Test
    public void testParticlePanelRepaintCalled() throws InterruptedException, InvocationTargetException {
        TestParticlePanel testPanel = new TestParticlePanel(SIZE);
        particleApp.panel = testPanel;

        // Simulate the app starting which should trigger repaint
        particleApp.Start();
        SwingUtilities.invokeAndWait(this::setupJFrameForTest);

        // Verify that repaint was called
        assertTrue(testPanel.repaintCalled,
                "repaint should be called on ParticlePanel");
    }

    @Test
    public void testParticleMoveWithParameters() {
        Particle particle = new Particle(50, 50);
        particle.move(5, 10);
        assertTrue(particle.x >= 45 && particle.x <= 55,
                "Particle should move within the horizontal range");
        assertTrue(particle.y >= 40 && particle.y <= 60,
                "Particle should move within the vertical range");
    }

    @Test
    public void testParticleDefaultMove() {
        Particle particle = new Particle(50, 50);
        particle.move();
        assertTrue(particle.x >= 45 && particle.x <= 55,
                "Particle should move within the default horizontal range");
        assertTrue(particle.y >= 40 && particle.y <= 60,
                "Particle should move within the default vertical range");
    }

    @Test
    public void testParticleDraw() {
        Particle particle = new Particle(50, 50);
        Graphics image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).createGraphics();
        assertDoesNotThrow(() -> particle.draw(image),
                "Draw method should execute without throwing exceptions");
    }

    @AfterEach
    public void tearDown(){
        if (particleApp.jFrame != null) {
            particleApp.jFrame.dispose();
        }
        particleApp.Stop();
    }

    /**
     * Test class for Particle Panel in lieu of setting up a mock environment.
     */
    private static class TestParticlePanel extends ParticlePanel {
        boolean repaintCalled = false;

        public TestParticlePanel(int size) {
            super(size);
        }

        @Override
        public void repaint() {
            repaintCalled = true;
            super.repaint();
        }
    }
}