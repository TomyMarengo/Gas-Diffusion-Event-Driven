import java.text.DecimalFormat;
import java.util.Objects;

public class Particle implements Comparable<Particle> {
    private double x; // X-coordinate of the particle
    private double y; // Y-coordinate of the particle
    private double radius; // Radius of the particle
    private double mass; // Mass of the particle
    private double velocityX; // X-component of the velocity
    private double velocityY; // Y-component of the velocity


    public Particle(double x, double y, double radius, double mass, double velocityX, double velocityY) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public double getVelocityMagnitude() {
        return Math.sqrt(velocityX * velocityX + velocityY * velocityY);
    }

    public double getVelocityAngle() {
        return Math.atan2(velocityY, velocityX);
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        return "{ " + x + ", " + y + " }";
    }

    @Override
    public int compareTo(Particle otherParticle) {
        int xComparison = Double.compare(this.x, otherParticle.x);
        if (xComparison != 0) {
            return xComparison;
        }
        return Double.compare(this.y, otherParticle.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Particle particle = (Particle) obj;
        return Objects.equals(x, particle.x) && Objects.equals(y, particle.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }
}
