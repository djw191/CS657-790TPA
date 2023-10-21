import java.awt.*;
import javax.swing.JPanel;

public class ParticlePanel extends JPanel {
    private Particle[] particles = new Particle[0];

    ParticlePanel(int size){
        setSize(new Dimension(size, size));
    }

    protected synchronized void setParticles(Particle[] particles) {
        if(particles == null)
            throw new IllegalArgumentException("Cannot set null");
        this.particles = particles;
    }

    protected synchronized Particle[] getParticles(){
        return particles;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Particle[] particles = getParticles();
        for (Particle particle : particles) particle.draw(g);
    }
}
