package example;

import k8055sim.K8055GUI;
import k8055sim.K8055Sim;

public class Main {
	
	public static void main(String[] args) {
		
		/*
		 * The digital inputs can be activated with the keyboard-keys 0 - 
		 */
		
		// Create simulator
		K8055Sim sim = new K8055Sim();
		
		// Create GUI
		K8055GUI gui = new K8055GUI(sim);
		
		// Start GUI rendering
		gui.start();
		
		// At this Point you can use almost all the functions the you would use with a real K8055 Board

		int value = 0;
		
		// Start main loop
		while(true) {
			
			// read digital input channel 0 (keyboard 1)
			if(sim.ReadDigitalChannel(0)) {
				// increase value
				value += 7;
			} 
			// read digital input channel 1
			else if(sim.ReadDigitalChannel(1)) {
				// decrease value
				value -= 7;
			} 
			// read digital input channel 2
			else if(sim.ReadDigitalChannel(2)) {
				// set value to 0 and with that
				// clear analog output channel 0
				value = 0;
			}
			
			// cap value at 255
			if(value > 255) value = 255;
			// cap value at 0
			else if(value < 0) value = 0;
			
			// output value as analog value (0 - 255) to anaglog channel 0
			sim.OutputAnalogChannel(0, value);
			
			try {
				// sleep 100 ms
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		
		
	}
	
}