import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class ParticleAppTest {

    private ParticleApp particleApp;
    private final int SIZE = 1024;
    private final int THREAD_COUNT = 128;

    /*
    Set the close operation to dispose the window rather than exit the application.
     This is **needed** for testing because of the WindowConstants.EXIT_ON_CLOSE in the app method.
     */
    private void setupJFrameForTest() {
        particleApp.SetupJFrame();
        particleApp.jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }


    @BeforeEach
    public void setUp() {
        particleApp = new ParticleApp(SIZE, THREAD_COUNT);
    }

    @Test
    public void testParticleAppInitializationWithValidArguments() {
        assertNotNull(particleApp.scheduler, "Scheduler should not be null after initialization");
        assertNotNull(particleApp.panel, "Panel should not be null after initialization");
    }

    @Test
    public void testParticleAppInitializationWithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new ParticleApp(-1, THREAD_COUNT));
    }

    @Test
    public void testSetupJFrame() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(this::setupJFrameForTest);
        assertTrue(particleApp.jFrame.isVisible(), "JFrame should be visible after setup");
        assertEquals(SIZE, particleApp.jFrame.getSize().width, "JFrame should have the correct size");
        assertEquals(SIZE, particleApp.jFrame.getSize().height, "JFrame should have the correct size");
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
        assertTrue(hasParticlePanel, "ParticlePanel should be added to JFrame");
    }

    @AfterEach
    public void tearDown(){
        if (particleApp.jFrame != null) {
            particleApp.jFrame.dispose();
        }
        particleApp.Stop();
    }
}