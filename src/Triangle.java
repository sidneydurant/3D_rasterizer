
/* @ author Sidney Durant
 * 
 * a class to represent triangles defined with three vertices
*/

public class Triangle implements GeometricPrimitive{
	
	private Point[] p = new Point[3];
	
	public Triangle( Point p0, Point p1, Point p2 ){
		p[0] = p0;
		p[1] = p1;
		p[2] = p2;
	}
	
	public void draw( Rasterizer r ){
		r.drawTriangle( this );
	}
	
	public Point[] getPoints(){
		return p;
	}
	
	public Point getP0(){
		return p[0];
	}
	
	public Point getP1(){
		return p[1];
	}
	
	public Point getP2(){
		return p[2];
	}
}
