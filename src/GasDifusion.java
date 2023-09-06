import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GasDifusion {
    private final List<Particle> particles;
    private final double[][] timeToCollision;

    private final double mainPerimeterWidth;
    private final double mainPerimeterHeight;
    private final double minorPerimeterWidth;
    private final double minorPerimeterHeight;
    private final int maxStep;

    private final int NUMBER_OF_WALLS = 8;
    public GasDifusion(List<Particle> particles, double mainPerimeterWidth, double mainPerimeterHeight,
            double minorPerimeterWidth, double minorPerimeterHeight, int maxStep) {
        this.particles = particles;
        this.timeToCollision = new double[particles.size() + NUMBER_OF_WALLS][particles.size() + NUMBER_OF_WALLS];
        this.mainPerimeterWidth = mainPerimeterWidth;
        this.mainPerimeterHeight = mainPerimeterHeight;
        this.minorPerimeterWidth = minorPerimeterWidth;
        this.minorPerimeterHeight = minorPerimeterHeight;
        this.maxStep = maxStep;
    }

    private double collisionToWall0(Particle particle) {
        if (particle.getVelocityX() > 0) {
            return (0 + particle.getRadius() - particle.getX()) / particle.getVelocityX();
        } else {
            return Double.MAX_VALUE;
        }
    }

    private double collisionToWall1(Particle particle) {
        if (particle.getVelocityY() > 0) {
            return (0 + particle.getRadius() - particle.getY()) / particle.getVelocityY();
        } else {
            return Double.MAX_VALUE;
        }
    }

    private double collisionToWall2(Particle particle) {
        if (particle.getVelocityX() > 0) {
            return (mainPerimeterWidth - particle.getRadius() - particle.getX()) / particle.getVelocityX();
        } else {
            return Double.MAX_VALUE;
        }
    }

    private void calculateTimeToCollision() {
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                double distance = Math.sqrt(Math.pow(particles.get(i).getX() - particles.get(j).getX(), 2) +
                        Math.pow(particles.get(i).getY() - particles.get(j).getY(), 2));
                double relativeVelocity = particles.get(i).getVelocityMagnitude() - particles.get(j).getVelocityMagnitude();
                double relativePosition = particles.get(i).getVelocityAngle() - particles.get(j).getVelocityAngle();
                double timeToCollision = (distance - particles.get(i).getRadius() - particles.get(j).getRadius()) /
                        relativeVelocity;
                if (timeToCollision > 0) {
                    this.timeToCollision[i][j] = timeToCollision;
                    this.timeToCollision[j][i] = timeToCollision;
                }
            }

            timeToCollision[i][particles.size()] = collisionToWall0(particles.get(i));
            timeToCollision[i][particles.size() + 1] = collisionToWall1(particles.get(i));
            timeToCollision[i][particles.size() + 2] = collisionToWall2(particles.get(i));
            timeToCollision[i][particles.size() + 3] = collisionToWall3(particles.get(i));
            timeToCollision[i][particles.size() + 4] = collisionToWall4(particles.get(i));
            timeToCollision[i][particles.size() + 5] = collisionToWall5(particles.get(i));
            timeToCollision[i][particles.size() + 6] = collisionToWall6(particles.get(i));
            timeToCollision[i][particles.size() + 7] = collisionToWall7(particles.get(i));

        }
    }

    private void doAStep() {
        calculateTimeToCollision();
    }

    private void writeOutputStep(int step) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    "../GasDifusion/output_" + minorPerimeterHeight + ".txt", true));
            writer.write("TIEMPO " + step + "\n");

            for (Particle particle : particles) {
                writer.write(particle.getX() + " " + particle.getY() + " " +
                        particle.getVelocityX() + " " + particle.getVelocityY() + "\n");
            }
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        //Write outputs step by step
        writeOutputStep(0);

        long startTime, endTime, elapsedTime = 0;
        int i;
        for (i = 1; i <= maxStep; i++) {
            startTime = System.currentTimeMillis();
            doAStep();
            endTime = System.currentTimeMillis();
            elapsedTime = elapsedTime + endTime - startTime;
            writeOutputStep(i);
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    "../GasDifusionAnimation/output_" + minorPerimeterHeight + ".txt", true));

            writer.write("N " + particles.size() + "\n");
            writer.write("MAXSTEP " + i + "\n");
            writer.write("ELAPSEDTIME " + elapsedTime + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int N, maxStep;
        double mainPerimeterWidth, mainPerimeterHeight;
        double minorPerimeterWidth, minorPerimeterHeight;
        double radius, mass, initialVelocity;

        double[] inputs = {0.03, 0.05, 0.07, 0.09};
        Writer writer = new Writer();
        for(double input : inputs) {
            writer.write(input);
            List<Particle> particles = new ArrayList<>();

            try {
                // Read static file
                BufferedReader staticReader = new BufferedReader(new FileReader("txt/static_" + input + ".txt"));
                N = Integer.parseInt(staticReader.readLine().split(" ")[1]);
                maxStep = Integer.parseInt(staticReader.readLine().split(" ")[1]);
                radius = Double.parseDouble(staticReader.readLine().split(" ")[1]);
                mass = Double.parseDouble(staticReader.readLine().split(" ")[1]);
                initialVelocity = Double.parseDouble(staticReader.readLine().split(" ")[1]);
                mainPerimeterWidth = Double.parseDouble(staticReader.readLine().split(" ")[1]);
                mainPerimeterHeight = Double.parseDouble(staticReader.readLine().split(" ")[1]);
                minorPerimeterWidth = Double.parseDouble(staticReader.readLine().split(" ")[1]);
                minorPerimeterHeight = Double.parseDouble(staticReader.readLine().split(" ")[1]);


                staticReader.close();

                // Read dynamic file
                BufferedReader dynamicReader = new BufferedReader(new FileReader("txt/dynamic_" + input + ".txt"));
                String line;

                while ((line = dynamicReader.readLine()) != null) {
                    String[] position = line.split(" ");
                    double x = Double.parseDouble(position[0]);
                    double y = Double.parseDouble(position[1]);
                    double velocityX = Double.parseDouble(position[2]);
                    double velocityY = Double.parseDouble(position[3]);
                    particles.add(new Particle(x, y, radius, mass, velocityX, velocityY));
                }
                dynamicReader.close();

                GasDifusion gd = new GasDifusion(particles, mainPerimeterWidth, mainPerimeterHeight,
                        minorPerimeterWidth, minorPerimeterHeight, maxStep);
                gd.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
