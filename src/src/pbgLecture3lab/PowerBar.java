package pbgLecture3lab;

import java.awt.*;

public class PowerBar {
    private double currentPower = 0;

    private Vect2D topLeft;
    private Vect2D bottomRight;

    public PowerBar(Vect2D topLeft, Vect2D bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;

        System.out.println(topLeft.toString());
        System.out.println(bottomRight.toString());
    }

    public void draw(Graphics2D g) {
        // System.out.println("This is being drawn");
        int[] topLeft = {(int)(this.topLeft.x), (int)(this.topLeft.y)};
        int[] bottomRight = {(int)(this.bottomRight.x), (int)(this.bottomRight.y)};

        clamp();
        double percent = ((bottomRight[0] - topLeft[0]) * (currentPower / 100));

        g.setColor(Color.RED);
        g.fillRect(topLeft[0], topLeft[1], (int)(percent), 50);

        g.setColor(Color.WHITE);
        g.drawLine(topLeft[0], topLeft[1], bottomRight[0], topLeft[1]);
        g.drawLine(bottomRight[0], topLeft[1], bottomRight[0], bottomRight[1]);
        g.drawLine(topLeft[0], bottomRight[1], bottomRight[0], bottomRight[1]);
        g.drawLine(topLeft[0], topLeft[1], topLeft[0], bottomRight[1]);
        // g.drawLine(topLeft[0], topLeft[1], bottomRight[0], bottomRight[1]);


    }

    public void setPower(double power) {
        this.currentPower = power;
    }

    public double getPower() {
        return this.currentPower;
    }

    private void clamp() {
        if (this.currentPower > 100) {
            this.currentPower = 100;
        }

        if (this.currentPower < 0) {
            this.currentPower = 0;
        }
    }
}
