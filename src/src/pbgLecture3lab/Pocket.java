package pbgLecture3lab;

import java.awt.Color;
import java.awt.Graphics2D;

public class Pocket {

    private double centrex;
    private double centrey;
    private double radius;
    private Color color;

    // private int SCREEN_RADIUS;

    public Pocket(double centrex, double centrey, double radius, Color color) {
        this.centrex = centrex;
        this.centrey = centrey;
        this.radius = radius;
        this.color = color;

        // this.SCREEN_RADIUS = BasicPhysicsEngine.convertWorldLengthToScreenLength(radius);

        // System.out.println(SCREEN_RADIUS);
        // System.out.println(radius);
    }

    public void draw(Graphics2D g) {
        int x = BasicPhysicsEngine.convertWorldXtoScreenX(this.centrex);
		int y = BasicPhysicsEngine.convertWorldYtoScreenY(this.centrey);
		g.setColor(this.color);

        // g.fillOval(x - SCREEN_RADIUS, y - SCREEN_RADIUS, SCREEN_RADIUS * 2, SCREEN_RADIUS * 2);

        // System.out.println(SCREEN_RADIUS);

        int newRadius = (int)(radius);
		
        g.fillOval(x - newRadius, y - newRadius, newRadius * 2, newRadius * 2);
    }
    
    public boolean collidesWith(BasicParticle p) {
        Vect2D pos = new Vect2D(centrex, centrey);
        Vect2D vecFrom1to2 = Vect2D.minus(p.getPos(), pos);
		return vecFrom1to2.mag() < p.getRadius();
    }
}
