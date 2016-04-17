package k8055sim;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
	
	private static boolean[] keys = new boolean[1024];
	private static boolean[] pkeys = new boolean[1024];
	
	public synchronized static boolean isKeyDown(int k) {
		return keys[k];
	}
	
	public synchronized static boolean isKeyClicked(int k) {
		return keys[k] && !pkeys[k];
	}
	
	public synchronized static boolean isKeyReleased(int k) {
		return !keys[k] && pkeys[k];
	}
	
	public synchronized static void update() {
		for(int i = 0; i < keys.length; i++) {
			pkeys[i] = keys[i];
		}
	}
	
	public synchronized void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public synchronized void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	
	public synchronized void keyTyped(KeyEvent e) {}
	
}
