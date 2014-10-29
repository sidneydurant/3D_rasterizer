
/* @ author Sidney Durant
 * 
 * a class to represent points defined by x, y, z coordinates and a Color c
*/

public class Point implements GeometricPrimitive{

	private Point[] p = new Point[1];
	private float x, y, z;
	Color c;
	
	public Point( float x, float y, float z, Color c ){
		this.x = x;
		this.y = y;
		this.z = z;
		this.c = c;
		p[0] = this;
	}
	
	public void draw( Rasterizer r ){
		r.drawPoint( this );
	}
	
	public Point[] getPoints(){
		return p;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getZ(){
		return z;
	}
	
	public Color getC(){
		return c;
	}
	
}
