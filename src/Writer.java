import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Writer {
    private final double mainPerimeterWidth = 0.09; // [m]
    private final double mainPerimeterHeight = mainPerimeterWidth; // [m]
    private final double minorPerimeterWidth = 0.09; // [m]
    private double minorPerimeterHeight = 0.03; // [m] // INPUT
    private final double radius = 0.0015; // [m]
    private final double mass = 1; // [kg]
    private final double initialVelocity = 0.01; // [m/s]
    private final int maxStep = 1000;
    private final int N = 50; // >200


    private void writeStaticFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("txt/static_" + minorPerimeterHeight + ".txt"));
        writer.write("N " + N + "\n");
        writer.write("MAX_STEP " + maxStep + "\n");
        writer.write("RADIUS " + radius + "\n");
        writer.write("MASS " + mass + "\n");
        writer.write("INIT_VELOCITY " + initialVelocity + "\n");
        writer.write("MAIN_WIDTH " + mainPerimeterWidth + "\n");
        writer.write("MAIN_HEIGHT " + mainPerimeterHeight + "\n");
        writer.write("MINOR_WIDTH " + minorPerimeterWidth + "\n");
        writer.write("MINOR_HEIGHT " + minorPerimeterHeight + "\n");

        writer.close();
    }

    private void writeDynamicFile(List<Particle> particles) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("txt/dynamic_" + minorPerimeterHeight + ".txt"));
        for (Particle coordinates : particles) {
            writer.write(coordinates.getX() + " " + coordinates.getY() + " " +
                    coordinates.getVelocityX() + " " + coordinates.getVelocityY() + "\n");
        }
        writer.close();
    }


    // Change numbers in this method to have other inputs
    public void write(double minorPerimeterHeight) {
        this.minorPerimeterHeight = minorPerimeterHeight;

        List<Particle> particles = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < N; i++) {
            double x, y;
            boolean isOverlap;

            do {
                isOverlap = false;
                x = rand.nextDouble() * (mainPerimeterWidth - 2 * radius) + radius;
                y = -(rand.nextDouble() * (mainPerimeterHeight - 2 * radius) + radius);

                for (Particle existingParticle : particles) {
                    double distance = Math.sqrt(Math.pow(x - existingParticle.getX(), 2) + Math.pow(y - existingParticle.getY(), 2));
                    if (distance < 2 * radius) {
                        isOverlap = true;
                        break; // Overlapping, regenerate position
                    }
                }

            } while (isOverlap);

            double velocityMagnitude = initialVelocity;
            double velocityAngle = rand.nextDouble() * 2 * Math.PI; // Random angle between 0 and 2*pi
            double velocityX = velocityMagnitude * Math.cos(velocityAngle);
            double velocityY = velocityMagnitude * Math.sin(velocityAngle);

            Particle particle = new Particle(x, y, radius, 1.0, velocityX, velocityY);
            particles.add(particle);
        }

        try {
            writeStaticFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writeDynamicFile(particles);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}