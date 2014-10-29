
import java.util.ArrayList;

// contains an arrayList of geometric primitives, and the cumulative transforms
// that are applied to every triangle every frame to draw it orthographically

public class OrthographicModel {

	private ArrayList<GeometricPrimitive>  model;
	
	private float cx, cy, cz; // scaling factor
	private float thetaX, thetaY, thetaZ; // rotation angles
	private float dx, dy, dz; // translation factors
	private Rasterizer r;
	
	public OrthographicModel( Rasterizer r ){
		// everything is stored in an arrayList, don't EVER plan on removing
		// just one object from this (for now), will only ever clear
		model = new ArrayList<GeometricPrimitive>();
		this.r = r;
		cx = cy = cz = 1;
		thetaX = thetaY = thetaZ = 0;
		dx = dy = dz = 0;
	}
	
	public void clear(){
		// clear the model
	}
	
	public void add( GeometricPrimitive primitive){
		model.add(primitive);
	}

	// cn = amount to scale along n axis
	public void setScaling( float cx, float cy, float cz ){
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
	}
	
	// tn == theta of rotation around n axis
	public void setRotation( float tx, float ty, float tz ){
		thetaX = tx;
		thetaY = ty;
		thetaZ = tz;
	}

	// dn == amount to translate along n axis post rotation, in other words,
	// position of model in world space (as opposed to model or screen space)
	public void setTranslation( float dx, float dy, float dz ){
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}
	
	// draws the model, taking into account the rotation 
	public void draw(){
		// for every piece
		// 		for every point of that thing
		//			rotate, transform, scale
		//		draw altered piece
		
		Point[] points;
		Point p;
		float x, y, z;
		float x1, y1, z1;
		
		for( GeometricPrimitive primitive : model ){
			points = primitive.getPoints();

			Point[] newPoints = new Point[points.length]; 
			
			for( int i = 0; i <  points.length; i++ ){
				
				// now I have a point that needs transforming
				x = points[i].getX();
				y = points[i].getY();
				z = points[i].getZ();
				
				// rotation around x axis
				y1 = (float)( y*Math.cos( thetaX ) - z*Math.sin( thetaX ) );
				z1 = (float)( y*Math.sin( thetaX ) + z*Math.cos( thetaX ) );
			
				//rotation around y axis
				x1 = (float)( x*Math.cos(thetaY) + z1*Math.sin(thetaY) );
				z = (float)(-x*Math.sin(thetaY) + z1*Math.cos(thetaY) );
				//System.out.println(thetaY );

				// rotation around z axis
				x = (float)( x1*Math.cos( thetaZ ) - y1*Math.sin( thetaZ ) );
				y = (float)( x1*Math.sin( thetaZ ) + y1*Math.cos( thetaZ ) );
				
				x*= cx;
				y*= cy;
				z*= cz;
				
				x += dx;
				y += dy;
				z += dz;
				
				//System.out.println( "x: " + x + " y: " + y + " z: " + z );
				
				newPoints[i] = new Point( x, y, z, points[i].getC() );
				
			}
			
			GeometricPrimitive geo;
			
			switch( points.length ){
				case 1: geo = newPoints[0];
				break;
				case 2: geo = new Line( newPoints[0], newPoints[1] );
				break;
				default: geo = new Triangle(newPoints[0], newPoints[1], newPoints[2]);
				break;
			}
			
			geo.draw( r );
			
		}
		
	}
	
}
