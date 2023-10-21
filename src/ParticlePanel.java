import java.awt.*;
import javax.swing.JPanel;

/**
 * Designed as a canvas to draw particles onto.
 */
public class ParticlePanel extends JPanel {
    private Particle[] particles = new Particle[0];

    ParticlePanel(int size){
        setSize(new Dimension(size, size));
    }

    /**
     * @param particles Array of Particle to be passed in that is managed by this canvas.
     */
    protected synchronized void setParticles(Particle[] particles) {
        if(particles == null)
            throw new IllegalArgumentException("Cannot set null");
        this.particles = particles;
    }

    /**
     * @return Returns Particle array from this canvas to be used.
     */
    protected synchronized Particle[] getParticles(){
        return particles;
    }

    /**
     * Updates the canvas, not to called directly, but through repaint.
     * @param g the <code>Graphics</code> context in which to paint.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Particle[] particles = getParticles();
        for (Particle particle : particles) particle.draw(g);
    }
}
