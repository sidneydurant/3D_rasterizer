
// Panel
import javax.swing.JPanel;

// Graphics
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

// The class that sets up the primitives, and that draws the image on the JPanel
// currently this just sets up a demo scene that shows off the capabilities
public class Screen extends JPanel{

	private static BufferedImage screenImage;
	private static int[] screenRaster;
	
	private Rasterizer renderer;
	private OrthographicModel scene;
	
	private OrthographicModel hexa, tetra, octa;
	
	public Screen( int width, int height ){
		
		screenImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		screenRaster = ((DataBufferInt) screenImage.getRaster().getDataBuffer()).getData();

		renderer = new Rasterizer( width, height, screenRaster);
		
		renderer.clear();
		
		// the points on a tetrahedron
		Point p0 = new Point( 50, 0, -.707f*50, new Color( 0xff0000ff ) );
		Point p1 = new Point( -50, 0, -.707f*50, new Color( 0xff00ff00 ) );
		Point p2 = new Point( 0, 50, .707f*50, new Color( 0xffff0000 ) );
		Point p3 = new Point( 0, -50, .707f*50, new Color( 0xffffffff ) );
		
		// construct tetrahedron model
		tetra = new OrthographicModel( renderer );
		tetra.add( new Triangle( p0, p1, p2 ) );
		tetra.add( new Triangle( p1, p2, p3) );
		tetra.add( new Triangle( p0, p1, p3) );
		tetra.add( new Triangle( p0, p2, p3) );

		// the points on a hexahedron
		p0 = new Point( -50, -50, -50, new Color(0xffffffff) );
		p1 = new Point( -50, -50, 50, new Color(0xff0000ff) );
		p2 = new Point( -50, 50, -50, new Color(0xff00ff00) );
		p3 = new Point( -50, 50, 50, new Color(0xff00ffff) );
		Point p4 = new Point( 50, -50, -50, new Color(0xffff0000) );
		Point p5 = new Point( 50, -50, 50, new Color(0xffff00ff) );
		Point p6 = new Point( 50, 50, -50, new Color(0xffffff00) );
		Point p7 = new Point( 50, 50, 50, new Color(0xff000000) );
		
		// construct hexahedron model
		hexa = new OrthographicModel( renderer );
		hexa.add( new Triangle( p0, p1, p2 ) );
		hexa.add( new Triangle( p1, p2, p3 ) );
		hexa.add( new Triangle( p0, p1, p5 ) );
		hexa.add( new Triangle( p0, p4, p5 ) );
		hexa.add( new Triangle( p0, p6, p2 ) );
		hexa.add( new Triangle( p0, p6, p4 ) );
		hexa.add( new Triangle( p3, p1, p7 ) );
		hexa.add( new Triangle( p7, p1, p5 ) );
		hexa.add( new Triangle( p3, p6, p2 ) );
		hexa.add( new Triangle( p6, p7, p3 ) );
		hexa.add( new Triangle( p4, p5, p6 ) );
		hexa.add( new Triangle( p5, p6, p7 ) );
		
		// the points on an octahedron
		p0 = new Point( 50, 0, 0, new Color( 0xff0000ff ) );
		p1 = new Point( -50, 0, 0, new Color( 0xff00ff00 ) );
		p2 = new Point( 0, 50, 0, new Color( 0xff00ffff ) );
		p3 = new Point( 0, -50, 0, new Color( 0xffff0000 ) );
		p4 = new Point( 0, 0, 50, new Color( 0xffff00ff ) );
		p5 = new Point( 0, 0, -50, new Color( 0xffffffff ) );
		
		// construct octahedron model
		octa = new OrthographicModel( renderer );
		octa.add( new Triangle( p1, p2, p5 ) );
		octa.add( new Triangle( p1, p2, p4 ) ); 
		octa.add( new Triangle( p2, p4, p0 ) ); 
		octa.add( new Triangle( p5, p2, p0 ) ); 
		octa.add( new Triangle( p1, p5, p3 ) ); 
		octa.add( new Triangle( p1, p4, p3 ) ); 
		octa.add( new Triangle( p0, p4, p3 ) ); 
		octa.add( new Triangle( p0, p3, p5  ) ); 
		
	}
	
	private float theta;
	
	// is called every frame through repaint(), from Frame
	public void paint( Graphics g ){
		
		renderer.clear();
		
		theta += .01;

		OrthographicModel m;
		
		// goes through and performs scalings/rotations/transformations that
		// show off everything the rasterizer is capable of
		for( int xi = 0; xi < 5; xi++ ){
			for( int yi = 0; yi < 5; yi++ ){
				switch( (xi + yi)%3 ){
				case 0: m = hexa;
					break;
				case 1: m = tetra;
					break;
				default: m = octa;
				}
				m.setRotation( xi*theta, yi*theta, theta*(xi+yi)/2);
				m.setTranslation( 190*xi+100, 185*yi+120, 0 );
				m.setScaling( 1, 1, 1);
				if( xi == 3 ){
					m.setScaling( (float)Math.cos(yi*theta), 1, 1);
				}
				if( yi == 3 ){
					m.setScaling( 1, (float)Math.cos(xi*theta), 1);
				}
				if( yi == 3 && xi == 3 ){
					m.setScaling( (float)Math.cos(theta), (float)Math.cos(theta), 1);
				}
				m.draw();
			}
		}
		
		
		g.drawImage( screenImage, 0, 0, screenImage.getWidth(), screenImage.getHeight(), null );
		
	}
	
}
