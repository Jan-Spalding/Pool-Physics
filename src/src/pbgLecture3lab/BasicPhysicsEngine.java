package pbgLecture3lab;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;


public class BasicPhysicsEngine {
	/* Author: Michael Fairbank
	 * Creation Date: 2016-01-28
	 * Significant changes applied:
	 */
	
	// frame dimensions
	// public static final int SCREEN_HEIGHT = 680/2;
	// public static final int SCREEN_WIDTH = 640/2;

	// public static final int SCREEN_HEIGHT = 680;
	// public static final int SCREEN_WIDTH = 640;

	public static final int SCREEN_HEIGHT = 1080 / 2;
	public static final int SCREEN_WIDTH = 1920 / 2;

	public static final Dimension FRAME_SIZE = new Dimension(
			SCREEN_WIDTH, SCREEN_HEIGHT);
	public static final double WORLD_WIDTH=19.2 ;//metres
	public static final double WORLD_HEIGHT=10.8 ;// meters - keeps world dimensions in same aspect ratio as screen dimensions, so that circles get transformed into circles as opposed to ovals
	public static final double GRAVITY=9.8;

	// sleep time between two drawn frames in milliseconds 
	public static final int DELAY = 20;
	public static final int NUM_EULER_UPDATES_PER_SCREEN_REFRESH=10;
	// estimate for time between two frames in seconds 
	public static final double DELTA_T = DELAY / 1000.0 / NUM_EULER_UPDATES_PER_SCREEN_REFRESH ;
	
	
	public static int convertWorldXtoScreenX(double worldX) {
		return (int) (worldX/WORLD_WIDTH*SCREEN_WIDTH);
	}
	public static int convertWorldYtoScreenY(double worldY) {
		// minus sign in here is because screen coordinates are upside down.
		return (int) (SCREEN_HEIGHT-(worldY/WORLD_HEIGHT*SCREEN_HEIGHT));
	}
	public static int convertWorldLengthToScreenLength(double worldLength) {
		return (int) (worldLength/WORLD_WIDTH*SCREEN_WIDTH);
	}
	public static double convertScreenXtoWorldX(int screenX) {
		// throw new RuntimeException("Not implemented");
		// to get this to work you need to program the inverse function to convertWorldXtoScreenX
		// this means rearranging the equation z=(worldX/WORLD_WIDTH*SCREEN_WIDTH) to make worldX the subject, 
		// and then returning worldX
		return screenX * WORLD_WIDTH / SCREEN_WIDTH;
	}
	public static double convertScreenYtoWorldY(int screenY) {
		// throw new RuntimeException("Not implemented");
		// to get this to work you need to program the inverse function to convertWorldYtoScreenY
		// this means rearranging the equation z= (SCREEN_HEIGHT-(worldY/WORLD_HEIGHT*SCREEN_HEIGHT)) to make 
		// worldY the subject, and then returning worldY
		return -((screenY - SCREEN_HEIGHT) * WORLD_HEIGHT / SCREEN_HEIGHT);
	}
	
	
	
	public List<BasicParticle> particles;
	public List<AnchoredBarrier> barriers;
	public List<AnchoredBarrier> cup;
	public List<Pocket> pockets;
	public PowerBar powerBar;

	public static enum LayoutMode {CONVEX_ARENA, CONCAVE_ARENA, CONVEX_ARENA_WITH_CURVE, PINBALL_ARENA, RECTANGLE, SNOOKER_TABLE};
	public BasicPhysicsEngine() {
		barriers = new ArrayList<AnchoredBarrier>();
		cup = new ArrayList<AnchoredBarrier>();
		// empty particles array, so that when a new thread starts it clears current particle state:
		particles = new ArrayList<BasicParticle>();
		pockets = new ArrayList<Pocket>();
		LayoutMode layout=LayoutMode.SNOOKER_TABLE;
		
		double r = 0.2;
		
		
		// particles.add(new BasicParticle(r+WORLD_WIDTH/2-1,WORLD_HEIGHT/2,3.5,5.2, r,true, Color.RED, 2));
		// particles.add(new BasicParticle(r+WORLD_WIDTH/2-2,WORLD_HEIGHT/2,-3.5,5.2, r*2,true, Color.PINK, 4));		
		// particles.add(new BasicParticle(r+WORLD_WIDTH/2,WORLD_HEIGHT/2,3.5,-5.2, r*6,true, Color.BLUE, 10));
		
		

		
		barriers = new ArrayList<AnchoredBarrier>();
		
		switch (layout) {
			case RECTANGLE: {
				// rectangle walls:
				// anticlockwise listing
				barriers.add(new AnchoredBarrier_StraightLine(0, 0, WORLD_WIDTH, 0, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH, 0, WORLD_WIDTH, WORLD_HEIGHT, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH, WORLD_HEIGHT, 0, WORLD_HEIGHT, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(0, WORLD_HEIGHT, 0, 0, Color.WHITE));
				break;
			}
			case CONVEX_ARENA: {
				barriers.add(new AnchoredBarrier_StraightLine(0,WORLD_HEIGHT/3, WORLD_WIDTH/2, 0, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH/2, 0, WORLD_WIDTH, WORLD_HEIGHT/3, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH, WORLD_HEIGHT/3, WORLD_WIDTH, WORLD_HEIGHT, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH, WORLD_HEIGHT, 0, WORLD_HEIGHT, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(0, WORLD_HEIGHT, 0, WORLD_HEIGHT/3, Color.WHITE));
				break;
			}
			case CONCAVE_ARENA: {
				barriers.add(new AnchoredBarrier_StraightLine(0,WORLD_HEIGHT/3, WORLD_WIDTH/2, 0, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH/2, 0, WORLD_WIDTH, WORLD_HEIGHT/3, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH, WORLD_HEIGHT/3, WORLD_WIDTH, WORLD_HEIGHT, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH, WORLD_HEIGHT, 0, WORLD_HEIGHT, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(0, WORLD_HEIGHT, 0, WORLD_HEIGHT/3, Color.WHITE));
				double width=WORLD_HEIGHT/20;
				barriers.add(new AnchoredBarrier_StraightLine(0, WORLD_HEIGHT*2/3, WORLD_WIDTH/2, WORLD_HEIGHT*1/2, Color.WHITE,width/10));
				barriers.add(new AnchoredBarrier_Point(WORLD_WIDTH/2, WORLD_HEIGHT*1/2, width/10));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH/2, WORLD_HEIGHT*1/2, WORLD_WIDTH/2, WORLD_HEIGHT*1/2-width, Color.WHITE,width/10));
				barriers.add(new AnchoredBarrier_Point(WORLD_WIDTH/2, WORLD_HEIGHT*1/2-width, width/10));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH/2, WORLD_HEIGHT*1/2-width, 0, WORLD_HEIGHT*2/3-width, Color.WHITE,width/10));
				break;
			}
			case CONVEX_ARENA_WITH_CURVE: {
				barriers.add(new AnchoredBarrier_StraightLine(0,WORLD_HEIGHT/3, WORLD_WIDTH/2, 0, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH/2, 0, WORLD_WIDTH, WORLD_HEIGHT/3, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH, WORLD_HEIGHT/3, WORLD_WIDTH, WORLD_HEIGHT, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH, WORLD_HEIGHT, 0, WORLD_HEIGHT, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(0, WORLD_HEIGHT, 0, 0, Color.WHITE));
				barriers.add(new AnchoredBarrier_Curve(WORLD_WIDTH/2, WORLD_HEIGHT-WORLD_WIDTH/2, WORLD_WIDTH/2, 0.0, 180.0,true, Color.WHITE));
				break;
			}
			case PINBALL_ARENA: {
				// simple pinball board
				barriers.add(new AnchoredBarrier_StraightLine(0, 0, WORLD_WIDTH, 0, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH, 0, WORLD_WIDTH, WORLD_HEIGHT, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH, WORLD_HEIGHT, 0, WORLD_HEIGHT, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(0, WORLD_HEIGHT, 0, 0, Color.WHITE));
				barriers.add(new AnchoredBarrier_Curve(WORLD_WIDTH/2, WORLD_HEIGHT-WORLD_WIDTH/2, WORLD_WIDTH/2, 0.0, 200.0,true, Color.WHITE));
				barriers.add(new AnchoredBarrier_Curve(WORLD_WIDTH/2, WORLD_HEIGHT*3/4, WORLD_WIDTH/15, -0.0, 360.0,false, Color.WHITE));
				barriers.add(new AnchoredBarrier_Curve(WORLD_WIDTH*1/3, WORLD_HEIGHT*1/2, WORLD_WIDTH/15, -0.0, 360.0,false, Color.WHITE));
				barriers.add(new AnchoredBarrier_Curve(WORLD_WIDTH*2/3, WORLD_HEIGHT*1/2, WORLD_WIDTH/15, -0.0, 360.0,false, Color.WHITE));
				break;
			}
			case SNOOKER_TABLE: {
				double snookerTableHeight = 137 /15; //cm
				double snookerTableWidth = 236 /15; //cm
				double cue = 3.76 /15; //cm
				double ball = 4.08 /15; //cm
				double pocketSize = 25.0; //mm
				double sideDepth = 8.60 / 15; //mm
				double endDepth = 8.90 / 15; //mm
				double playWidth = 189 / 15; //cm
				double playHeight = 99 / 15; //cm
				
				// WHOLE TABLE
				barriers.add(new AnchoredBarrier_StraightLine(0, WORLD_HEIGHT , snookerTableWidth, WORLD_HEIGHT, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(snookerTableWidth, WORLD_HEIGHT, snookerTableWidth, WORLD_HEIGHT - snookerTableHeight, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(snookerTableWidth, WORLD_HEIGHT - snookerTableHeight, 0, WORLD_HEIGHT - snookerTableHeight, Color.WHITE));
				barriers.add(new AnchoredBarrier_StraightLine(0, WORLD_HEIGHT - snookerTableHeight, 0, WORLD_HEIGHT, Color.WHITE));

				// PLAY AREA
				double offsetX = (snookerTableWidth - playWidth) /2;
				double offsetY = (snookerTableHeight - playHeight) /2;

				double pocketOpening = ball * 2.3;

				// INSIDE BORDER
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth/2 - pocketOpening, WORLD_HEIGHT - offsetY,offsetX + pocketOpening , WORLD_HEIGHT - offsetY, Color.GREEN));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth - pocketOpening, WORLD_HEIGHT - offsetY, offsetX + playWidth/2 + pocketOpening, WORLD_HEIGHT - offsetY, Color.GREEN));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth, WORLD_HEIGHT - offsetY - playHeight + pocketOpening, offsetX + playWidth, WORLD_HEIGHT - offsetY - pocketOpening, Color.GREEN));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth/2 + pocketOpening, WORLD_HEIGHT - offsetY - playHeight, offsetX + playWidth - pocketOpening , WORLD_HEIGHT - offsetY - playHeight, Color.GREEN));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + pocketOpening, WORLD_HEIGHT - offsetY - playHeight, offsetX + playWidth/2 - pocketOpening , WORLD_HEIGHT - offsetY - playHeight, Color.GREEN));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX, WORLD_HEIGHT - offsetY - pocketOpening, offsetX, WORLD_HEIGHT - offsetY - playHeight + pocketOpening , Color.GREEN));

				

				// OUTSIDE BORDER
				barriers.add(new AnchoredBarrier_StraightLine(offsetX, WORLD_HEIGHT - offsetY + sideDepth, offsetX + playWidth, WORLD_HEIGHT - offsetY + sideDepth, Color.GREEN));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth + endDepth, WORLD_HEIGHT - offsetY, offsetX + playWidth + endDepth, WORLD_HEIGHT - offsetY - playHeight, Color.GREEN));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth, WORLD_HEIGHT - offsetY - playHeight - sideDepth, offsetX, WORLD_HEIGHT - offsetY - playHeight - sideDepth, Color.GREEN ));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX - endDepth, WORLD_HEIGHT - offsetY - playHeight, offsetX - endDepth, WORLD_HEIGHT - offsetY, Color.GREEN));

				// CURVED BARRIERS
				
				// 		TOP LEFT
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + pocketOpening, WORLD_HEIGHT - offsetY, offsetX, WORLD_HEIGHT - offsetY + sideDepth, Color.GREEN ));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX - endDepth, WORLD_HEIGHT - offsetY , offsetX , WORLD_HEIGHT - offsetY - pocketOpening, Color.GREEN));
				barriers.add(new AnchoredBarrier_Point(offsetX + pocketOpening, WORLD_HEIGHT - offsetY));
				barriers.add(new AnchoredBarrier_Point(offsetX , WORLD_HEIGHT - offsetY - pocketOpening));

				//		TOP MIDDLE
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth/2 - endDepth/2 , WORLD_HEIGHT - offsetY + sideDepth, offsetX + playWidth/2 - pocketOpening, WORLD_HEIGHT - offsetY , Color.GREEN));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth/2 + pocketOpening , WORLD_HEIGHT - offsetY, offsetX + playWidth/2 + endDepth/2, WORLD_HEIGHT - offsetY + sideDepth, Color.GREEN));
				barriers.add(new AnchoredBarrier_Point(offsetX + playWidth/2 - pocketOpening, WORLD_HEIGHT - offsetY));
				barriers.add(new AnchoredBarrier_Point(offsetX + playWidth/2 + pocketOpening , WORLD_HEIGHT - offsetY));

				//		TOP RIGHT
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth , WORLD_HEIGHT - offsetY + sideDepth, offsetX + playWidth - pocketOpening, WORLD_HEIGHT - offsetY , Color.GREEN ));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth, WORLD_HEIGHT - offsetY - pocketOpening, offsetX + playWidth + endDepth, WORLD_HEIGHT - offsetY, Color.GREEN));
				barriers.add(new AnchoredBarrier_Point(offsetX + playWidth - pocketOpening, WORLD_HEIGHT - offsetY));
				barriers.add(new AnchoredBarrier_Point(offsetX + playWidth, WORLD_HEIGHT - offsetY - pocketOpening));


				// 		BOTTOM LEFT
				barriers.add(new AnchoredBarrier_StraightLine(offsetX , WORLD_HEIGHT - offsetY - playHeight - sideDepth, offsetX + pocketOpening, WORLD_HEIGHT - offsetY - playHeight , Color.GREEN ));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX, WORLD_HEIGHT - offsetY - playHeight + pocketOpening, offsetX - endDepth, WORLD_HEIGHT - offsetY - playHeight, Color.GREEN));
				barriers.add(new AnchoredBarrier_Point(offsetX + pocketOpening, WORLD_HEIGHT - offsetY - playHeight));
				barriers.add(new AnchoredBarrier_Point(offsetX, WORLD_HEIGHT - offsetY - playHeight + pocketOpening));


				//		BOTTOM MIDDLE
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth/2 - pocketOpening , WORLD_HEIGHT - offsetY - playHeight, offsetX + playWidth/2 - endDepth/2, WORLD_HEIGHT - offsetY - playHeight - sideDepth, Color.GREEN));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth/2 + endDepth/2, WORLD_HEIGHT - offsetY - playHeight - sideDepth, offsetX + playWidth/2 + pocketOpening, WORLD_HEIGHT - offsetY - playHeight , Color.GREEN));
				barriers.add(new AnchoredBarrier_Point(offsetX + playWidth/2 - pocketOpening , WORLD_HEIGHT - offsetY - playHeight));
				barriers.add(new AnchoredBarrier_Point(offsetX + playWidth/2 + pocketOpening, WORLD_HEIGHT - offsetY - playHeight));


				// 		BOTTOM RIGHT
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth - pocketOpening, WORLD_HEIGHT - offsetY - playHeight, offsetX + playWidth, WORLD_HEIGHT - offsetY - playHeight - sideDepth, Color.GREEN ));
				barriers.add(new AnchoredBarrier_StraightLine(offsetX + playWidth + endDepth, WORLD_HEIGHT - offsetY - playHeight , offsetX + playWidth , WORLD_HEIGHT - offsetY - playHeight + pocketOpening, Color.GREEN));
				barriers.add(new AnchoredBarrier_Point(offsetX + playWidth - pocketOpening, WORLD_HEIGHT - offsetY - playHeight));
				barriers.add(new AnchoredBarrier_Point( offsetX + playWidth , WORLD_HEIGHT - offsetY - playHeight + pocketOpening));

				pockets.add(new Pocket(offsetX - endDepth/2, WORLD_HEIGHT - offsetY + sideDepth/2, pocketSize, Color.WHITE));
				pockets.add(new Pocket(offsetX + playWidth/2, WORLD_HEIGHT - offsetY + sideDepth*1.25, pocketSize, Color.WHITE));
				pockets.add(new Pocket(offsetX + playWidth + endDepth/2, WORLD_HEIGHT - offsetY + sideDepth/2, pocketSize, Color.WHITE));

				pockets.add(new Pocket(offsetX - endDepth/2, WORLD_HEIGHT - offsetY - playHeight - sideDepth/2, pocketSize, Color.WHITE));
				pockets.add(new Pocket(offsetX + playWidth/2, WORLD_HEIGHT - offsetY - playHeight - sideDepth*1.25, pocketSize, Color.WHITE));
				pockets.add(new Pocket(offsetX + playWidth + endDepth/2, WORLD_HEIGHT - offsetY - playHeight - sideDepth/2, pocketSize, Color.WHITE));
				
				// CUE BALL
				particles.add(new BasicParticle(offsetX + .2 * playWidth, WORLD_HEIGHT - offsetY - .5*playHeight, 0, 0, cue, false, Color.WHITE, 0.10));

				// BALL SETUP

				double ballPosX = offsetX + .7 * playWidth;
				double ballPosY = WORLD_HEIGHT - offsetY - .6* playHeight;

				Color[][] colours = {
					{},
					{Color.YELLOW,},
					{Color.RED, Color.YELLOW},
					{Color.YELLOW, Color.DARK_GRAY, Color.RED},
					{Color.RED, Color.YELLOW, Color.RED, Color.YELLOW},
					{Color.YELLOW, Color.RED, Color.RED, Color.YELLOW, Color.RED}
				};

				for (int i = 0; i <= 5; i++) {
					for (int j = 0; j < i; j++) {
						particles.add(new BasicParticle(ballPosX, ballPosY, 0,0, ball,false, colours[i][j], 0.08));
						ballPosY += (ball * 2);
					}

					ballPosX += (ball * 2) / 1.2;
					ballPosY = WORLD_HEIGHT - offsetY - .5 * playHeight - ((ball * 2)/2 * i);

				}

				powerBar = new PowerBar(new Vect2D(0, SCREEN_HEIGHT * .85), new Vect2D(SCREEN_WIDTH * .78, SCREEN_HEIGHT * .85 + 50));

				// CUP

				cup.add(new AnchoredBarrier_StraightLine(WORLD_WIDTH - .5, .6, WORLD_WIDTH - .5, WORLD_HEIGHT -.5, Color.WHITE));
				cup.add(new AnchoredBarrier_StraightLine(snookerTableWidth + .3, .6, WORLD_WIDTH - .5, .6, Color.WHITE));
				cup.add(new AnchoredBarrier_StraightLine(snookerTableWidth + .3, WORLD_HEIGHT -.5, snookerTableWidth + .3, .6,  Color.WHITE));

				break;
			}
		}
			
			
	}
	
	public static void main(String[] args) throws Exception {
		final BasicPhysicsEngine game = new BasicPhysicsEngine();
		final BasicView view = new BasicView(game);
		JEasyFrame frame = new JEasyFrame(view, "Basic Physics Engine");
		view.addMouseMotionListener(new BasicMouseListener());
		game.startThread(view);
	}

	private void startThread(final BasicView view) throws InterruptedException {
		final BasicPhysicsEngine game=this;
		while (true) {
			for (int i=0;i<NUM_EULER_UPDATES_PER_SCREEN_REFRESH;i++) {
				game.update();
			}
			view.repaint();
			Toolkit.getDefaultToolkit().sync();
			
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public boolean holdingPower = false;
	public boolean onFirstClick = true;
	public boolean onBreak = true;
	public Vect2D startingPos;
	public List<BasicParticle> potted = new ArrayList<BasicParticle>();

	public void update() {
		double e=0.80; // coefficient of restitution for all particle pairs

		for (BasicParticle p : particles) {
			p.update(GRAVITY, DELTA_T); // tell each particle to move
		}

		for (BasicParticle p : potted) {
			p.update(GRAVITY, DELTA_T);
		}

		for (BasicParticle particle : particles) {
			for (AnchoredBarrier b : barriers) {
				if (b.isCircleCollidingBarrier(particle.getPos(), particle.getRadius())) {
					Vect2D bouncedVel=b.calculateVelocityAfterACollision(particle.getPos(), particle.getVel(),0.70);
					particle.setVel(bouncedVel);
				}
			}
		}

		for (BasicParticle particle : potted) {
			for (AnchoredBarrier c : cup) {
				if (c.isCircleCollidingBarrier(particle.getPos(), particle.getRadius())) {
					Vect2D bouncedVel=c.calculateVelocityAfterACollision(particle.getPos(), particle.getVel(),.97);
					particle.setVel(bouncedVel);
				}
			}
		}

		for (int n=0;n<potted.size();n++) {
			for (int m=0;m<n;m++) {// avoids double check by requiring m<n
				BasicParticle p1 = potted.get(n);
				BasicParticle p2 = potted.get(m);
				if (p1.collidesWith(p2)) {
					BasicParticle.implementElasticCollision(p1, p2, .97);
				}
			}
		}
		
		for (int n=0;n<particles.size();n++) {
			for (int m=0;m<n;m++) {// avoids double check by requiring m<n
				BasicParticle p1 = particles.get(n);
				BasicParticle p2 = particles.get(m);
				if (p1.collidesWith(p2)) {
					BasicParticle.implementElasticCollision(p1, p2, e);
				}
			}
		}

		for(Pocket p : pockets) {
			for(int i = 0; i < particles.size(); i++) {
				// System.out.println(p.collidesWith(b));
				if (p.collidesWith(particles.get(i))) {
					// System.out.println("Ball in collision");
					if (i == 0) { // cue ball
						particles.get(i).reset();
					} else {
						BasicParticle pottedBall = particles.get(i);
						potted.add(new BasicParticle(WORLD_WIDTH - 1 - Math.random() * 2, WORLD_HEIGHT - 1, 0, 0, pottedBall.getRadius(), true, pottedBall.getColor(), pottedBall.getMass()));
						particles.remove(i);
					}
				}
			}
		}

		if (BasicMouseListener.isMouseButtonPressed()) {

			holdingPower = true;

			if (onFirstClick) {
				startingPos = BasicMouseListener.getWorldCoordinatesOfMousePointer();
				onFirstClick = false;
			}

			Vect2D pointerPos = BasicMouseListener.getWorldCoordinatesOfMousePointer();
			double magnitude = Vect2D.minus(startingPos, pointerPos).mag();
			
			powerBar.setPower( (magnitude / 4) * 100);

		}

		if (!BasicMouseListener.isMouseButtonPressed() && holdingPower) {

			holdingPower = false;

			Vect2D pointerPos = BasicMouseListener.getWorldCoordinatesOfMousePointer();
			Vect2D direction = Vect2D.minus(pointerPos, particles.get(0).getPos());

			Vect2D directionInvert = Vect2D.minus(new Vect2D(0, 0), direction.normalise());

			if (onBreak) {
				particles.get(0).setVel(directionInvert.mult(powerBar.getPower()/2));
				onBreak = false;
			} else {
				particles.get(0).setVel(directionInvert.mult(powerBar.getPower() / 8));
			}
			

			onFirstClick = true;
			powerBar.setPower(0);
		}
	}
	
	public void drawArrow(Graphics2D g) {
		g.setColor(Color.WHITE);

		Vect2D currentPos = particles.get(0).getPos();

		Vect2D pointerPos = BasicMouseListener.getWorldCoordinatesOfMousePointer();
		Vect2D direction = Vect2D.minus(pointerPos, currentPos);
		Vect2D directionInvert = Vect2D.minus(new Vect2D(0, 0), direction.normalise());

		BasicParticle copy = particles.get(0).copy();
		copy.setVel(directionInvert);

		boolean inCollision = false;

		while(!inCollision) {
			for (int i = 1; i < particles.size(); i++) { // skips collision with cue ball
				if(copy.collidesWith(particles.get(i))) {
					inCollision = true;
				}
			}

			for (AnchoredBarrier b : barriers) {
				if (b.isCircleCollidingBarrier(copy.getPos(), copy.getRadius())) {
					inCollision = true;
				}
			}

			for (Pocket p : pockets) {
				if (p.collidesWith(copy)) {
					inCollision = true;
				}
			}

			copy.step(DELTA_T);
		}

		int posX = BasicPhysicsEngine.convertWorldXtoScreenX(currentPos.x);
		int posY = BasicPhysicsEngine.convertWorldYtoScreenY(currentPos.y);

		int copyX = BasicPhysicsEngine.convertWorldXtoScreenX(copy.getPos().x);
		int copyY = BasicPhysicsEngine.convertWorldYtoScreenY(copy.getPos().y);

		g.drawLine(posX, posY, copyX, copyY);

	}
	

}


