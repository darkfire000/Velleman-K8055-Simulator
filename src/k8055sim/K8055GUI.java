package k8055sim;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class K8055GUI extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	private K8055Sim sim;
	
	private Thread thread;
	private boolean running;
	
	private JFrame frame;
	private BufferedImage buffer;
	
	private static final int WIDTH = 640;
	private static final int HEIGHT = 400;
	
	private BufferedImage board;
	
	public K8055GUI(K8055Sim sim) {
		this.sim = sim;
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		frame = new JFrame("Velleman K8055 Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		
		addKeyListener(new Keyboard());
		
		setFocusable(true);
		requestFocus();
		
		try {
			board = ImageIO.read(getClass().getClassLoader().getResourceAsStream("k8055.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		
		while(running) {

			tick();
			render();
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
		
		System.exit(0);
	}
	
	private void tick() {
		if(Keyboard.isKeyClicked(KeyEvent.VK_1)) sim.setDIn(0, true);
		else if(Keyboard.isKeyReleased(KeyEvent.VK_1)) sim.setDIn(0, false);

		if(Keyboard.isKeyClicked(KeyEvent.VK_2)) sim.setDIn(1, true);
		else if(Keyboard.isKeyReleased(KeyEvent.VK_2)) sim.setDIn(1, false);

		if(Keyboard.isKeyClicked(KeyEvent.VK_3)) sim.setDIn(2, true);
		else if(Keyboard.isKeyReleased(KeyEvent.VK_3)) sim.setDIn(2, false);

		if(Keyboard.isKeyClicked(KeyEvent.VK_4)) sim.setDIn(3, true);
		else if(Keyboard.isKeyReleased(KeyEvent.VK_4)) sim.setDIn(3, false);

		if(Keyboard.isKeyClicked(KeyEvent.VK_5)) sim.setDIn(4, true);
		else if(Keyboard.isKeyReleased(KeyEvent.VK_5)) sim.setDIn(4, false);

		Keyboard.update();
	}
	
	private void postRender() {
		
		int[] pixels = ((DataBufferInt)buffer.getRaster().getDataBuffer()).getData();
		
		for(int i = 0; i < pixels.length; i++) {
			boolean[] dout = sim.getDOut();
			boolean[] din = sim.getDIn();
			int[] aout = sim.getAOut();
			
			for(int k = 0; k < dout.length; k++) {
				if(pixels[i] == makeColor(255, 0, 255-(k*5))) {
					pixels[i] = dout[k]?makeColor(255, 100, 100):makeColor(100, 10, 10);
				}
			}
			
			for(int k = 0; k < din.length; k++) {
				if(pixels[i] == makeColor(255, 100, 255-(k*5))) {
					pixels[i] = din[k]?makeColor(255, 100, 100):makeColor(100, 10, 10);
				}
			}
			
			for(int k = 0; k < aout.length; k++) {
				if(pixels[i] == makeColor(255, 0, 215-(k*5))) {
					double perc = 1d * aout[k] / 255d;
					pixels[i] = makeColor(100 + (int)(perc*155), 10 + (int)(perc*90), 10 + (int)(perc*90));
				}
			}
			
		}
		
		
		
	}
	
	private int makeColor(int r, int g, int b) {
		return b | (g << 8) | (r << 16);
	}
	
	private void render() {
		Graphics g = buffer.getGraphics();
		g.drawImage(board, 0, 0, null);
		g.dispose();
		postRender();
		
		g = getGraphics();
		g.drawImage(buffer, 0, 0, null);
		g.dispose();
	}
	
	public void start() {
		if(running) return;
		thread = new Thread(this);
		running = true;
		thread.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stop() {
		if(!running) return;
		running = false;
		try {
			Thread.sleep(15);
		} catch (InterruptedException e) {}
		
		if(thread.isAlive()) thread.stop();
		frame.dispose();
	}
	
}
