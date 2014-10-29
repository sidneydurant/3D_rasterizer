
import java.awt.Canvas;
import javax.swing.*;

public class Frame extends Canvas {

	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;
	public static final int TICK = 33;

	public static boolean paused = false;

	public static void main(String[] args) {

		final JFrame frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		//frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setFocusable(true);
		
		final Screen world = new Screen(frame.getWidth(), frame.getHeight());
		frame.add( world );

		frame.setVisible(true);

		Thread runThread = new Thread(new Runnable() {
			public void run() {
				try{
					Thread.sleep( 30 ); // sleep to let the frame set up
				} catch ( Exception e ){
				}
				while (true) {
					long time = System.currentTimeMillis();

					world.repaint();

					long endTime = System.currentTimeMillis();
					try {
						Thread.sleep( TICK - endTime + time );
					} catch ( Exception e ) {
						System.out.println("cannot sleep: " + (TICK - (endTime - time)) );
					}
				}
			}
		});

		runThread.start();

	}
}