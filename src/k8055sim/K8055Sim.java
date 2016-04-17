package k8055sim;

public class K8055Sim {
	
	private int dOut = 0;
	private int dIn = 0;
	private int aOut = 0;
	
	private K8055GUI gui;
	
	public int OpenDevice(int address) {
		if(address < 0 || address > 3) return -1;
		
		gui = new K8055GUI(this);
		gui.start();
		
		return address;
	}
	
	public void CloseDevice() {
		dOut = 0;
		dIn = 0;
		aOut = 0;
		
		gui.stop();
	}
	
	public synchronized void WriteAllDigital(int data) {
		dOut = data;
	}
	
	public synchronized void ClearDigitalChannel(int ch) {
		dOut = dOut & ~(1 << ch);
	}
	
	public synchronized void ClearAllDigital() {
		dOut = 0;
	}
	
	public synchronized void SetDigitalChannel(int ch) {
		dOut = dOut | (1 << ch);
	}
	
	public synchronized void SetAllDigital() {
		dOut = 255;
	}
	
	
	
	public synchronized int ReadAllDigital() {
		return dIn;
	}
	
	public synchronized boolean ReadDigitalChannel(int ch) {
		return ((dIn>>ch) & 1) == 1;
	}
	
	
	
	public void OutputAnalogChannel(int ch, int data) {
		aOut = aOut & ~(0xff << 8*ch);
		aOut = aOut | data << 8*ch;
	}
	
	public void OutputAllAnalog(int data1, int data2) {
		aOut = data1 | (data2<<8);
	}
	
	public void ClearAnalogChannel(int ch) {
		aOut = aOut & ~(0xff << 8*ch);
	}
	
	public void ClearAllAnalog(int ch) {
		aOut = 0;
	}
	
	public void SetAnalogChannel(int ch) {
		aOut = aOut | (0xff << 8*ch);
	}
	
	public void SetAllAnalog() {
		aOut = 0xffff;
	}
	
	// Protected Functions
	
	protected synchronized boolean[] getDOut() {
		boolean[] b = new boolean[8];
		for(int i = 0; i < 8; i++) {
			b[i] = ((dOut>>i)&1)==1;
		}
		return b;
	}
	
	protected void setDIn(int ch, boolean b) {
		if(b) dIn = dIn | (1 << ch);
		else dIn = dIn & ~(1 << ch);
	}
	
	protected boolean[] getDIn() {
		boolean[] b = new boolean[5];
		for(int i = 0; i < 5; i++) {
			b[i] = ((dIn>>i)&1)==1;
		}
		return b;
	}
	
	protected int[] getAOut() {
		int[] arr = new int[2];
		arr[0] = aOut & 0xff;
		arr[1] = (aOut >> 8) & 0xff;
		
		return arr;
	}
	
	protected void printStatus() {
		System.out.println(dOut);
		for(int i = 7; i >= 0; i--) {
			System.out.print("" + ((((dOut>>i)&1)==1)?"1":"0"));
		}
		System.out.println();
	}
	
}
