package pbgLecture3lab;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

public class BasicView extends JComponent {
	/* Author: Michael Fairbank
	 * Creation Date: 2016-01-28
	 * Significant changes applied:
	 */
	// background colour
	public static final Color BG_COLOR = Color.BLACK;

	private BasicPhysicsEngine game;

	public BasicView(BasicPhysicsEngine game) {
		this.game = game;
	}
	
	@Override
	public void paintComponent(Graphics g0) {
		BasicPhysicsEngine game;
		synchronized(this) {
			game=this.game;
		}
		Graphics2D g = (Graphics2D) g0;
		// paint the background
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
		for (AnchoredBarrier b : game.barriers)
			b.draw(g);
		for (AnchoredBarrier c : game.cup) 
			c.draw(g);
		for (Pocket pk : game.pockets)
			pk.draw(g);
		for (BasicParticle p : game.particles)
			p.draw(g);
		for (BasicParticle p : game.potted)
			p.draw(g);

		game.powerBar.draw(g);

		if (game.holdingPower) {
			game.drawArrow(g);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return BasicPhysicsEngine.FRAME_SIZE;
	}
	
	public synchronized void updateGame(BasicPhysicsEngine game) {
		this.game=game;
	}
}