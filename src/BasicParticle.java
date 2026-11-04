package pbgLecture3lab;

import java.awt.Color;
import java.awt.Graphics2D;

public class BasicParticle {
	/* Author: Michael Fairbank
	 * Creation Date: 2016-01-28
	 * Significant changes applied:
	 */
	public final int SCREEN_RADIUS;

	private Vect2D startPos;
	private Vect2D pos;
	private Vect2D vel;
	private final double radius;
	private final double mass;
	public final Color col;

	private final boolean improvedEuler;

	

	public BasicParticle(double sx, double sy, double vx, double vy, double radius, boolean improvedEuler, Color col, double mass) {
		startPos = new Vect2D(sx, sy);
		setPos(new Vect2D(sx,sy));
		setVel(new Vect2D(vx,vy));
		this.radius=radius;
		this.mass=mass;
		this.improvedEuler=improvedEuler;
		this.SCREEN_RADIUS=Math.max(BasicPhysicsEngine.convertWorldLengthToScreenLength(radius),1);
		this.col=col;
	}

	public void step(double deltaT) {
		Vect2D acc = new Vect2D(0, 0);
		setPos(getPos().addScaled(getVel(), deltaT));
		setVel(getVel().addScaled(acc, deltaT));
	}

	public void update(double gravity, double deltaT) {
		double friction = .9;
		if (improvedEuler) {
			Vect2D acc = new Vect2D(0,-gravity);
			Vect2D pos2=getPos().addScaled(getVel(), deltaT);// in theory this could be used,e.g. if acc2 depends on pos - but in this constant gravity field it will not be relevant
			Vect2D vel2=getVel().addScaled(acc, deltaT);
			Vect2D velAv=vel2.add(getVel()).mult(0.5);
			Vect2D acc2=new Vect2D(0,-gravity);//same as acc in this simple example of constant acceleration, but that won't generally be true
			Vect2D accAv=acc2.add(acc).mult(0.5);
			setPos(getPos().addScaled(velAv, deltaT));
			setVel(getVel().addScaled(accAv, deltaT));
		} else {
			// basic Euler
			Vect2D acc = new Vect2D(0, 0);
			setPos(getPos().addScaled(getVel(), deltaT));

			if (getVel().x > 0) {
				acc = acc.add(new Vect2D(-friction, 0));
			} 

			if (getVel().x < 0) {
				acc = acc.add(new Vect2D(friction, 0));
			}

			if (getVel().y > 0) {
				acc = acc.add(new Vect2D(0, -friction));
			}

			if (getVel().y < 0) {
				acc = acc.add(new Vect2D(0, friction));
			}

			setVel(getVel().addScaled(acc, deltaT));

			int power = 100;

			if (getVel().x <= 0.1 / power && getVel().x >= -0.1 / power) {
				setVel(new Vect2D(0, getVel().y));
			}

			if (getVel().y <= 0.1 / power && getVel().y >= -0.1 / power) {
				setVel(new Vect2D(getVel().x, 0));
			}
		}

	}


	public void draw(Graphics2D g) {
		int x = BasicPhysicsEngine.convertWorldXtoScreenX(getPos().x);
		int y = BasicPhysicsEngine.convertWorldYtoScreenY(getPos().y);
		g.setColor(col);
		g.fillOval(x - SCREEN_RADIUS, y - SCREEN_RADIUS, 2 * SCREEN_RADIUS, 2 * SCREEN_RADIUS);
	}

	

	public double getRadius() {
		return radius;
	}

	public Vect2D getPos() {
		return pos;
	}

	public void setPos(Vect2D pos) {
		this.pos = pos;
	}

	public Vect2D getVel() {
		return vel;
	}

	public void setVel(Vect2D vel) {
		this.vel = vel;
	}

	public Color getColor() {
		return this.col;
	}

	public double getMass() {
		return this.mass;
	}

	public void reset() {
		setPos(startPos);
		setVel(new Vect2D(0, 0));
	}

	public BasicParticle copy() {
		return new BasicParticle(pos.x, pos.y, 0, 0, radius, improvedEuler, col, mass);
	}

	public boolean collidesWith(BasicParticle p2) {
		Vect2D vecFrom1to2 = Vect2D.minus(p2.getPos(), getPos());
		boolean movingTowardsEachOther = Vect2D.minus(p2.getVel(), getVel()).scalarProduct(vecFrom1to2)<0;
		return vecFrom1to2.mag()<getRadius()+p2.getRadius() && movingTowardsEachOther;
	}

	public static void implementElasticCollision(BasicParticle p1, BasicParticle p2, double e) {
		Vect2D p1TOp2 = Vect2D.minus(p1.getPos(), p2.getPos());
		Vect2D normal = p1TOp2.normalise();

		double diffImpulse = p1.vel.scalarProduct(normal) - p2.vel.scalarProduct(normal);
		double restitution = e + 1;
		double diffMass = 1/p1.mass + 1/p2.mass;

		double impulse = (restitution * diffImpulse) / diffMass;

		Vect2D p2Vel = p2.vel.addScaled(normal, (impulse/p2.mass));
		Vect2D p1Vel = p1.vel.addScaled(normal, -(impulse/p1.mass));

		p1.setVel(p1Vel);
		p2.setVel(p2Vel);
	}
	

}
