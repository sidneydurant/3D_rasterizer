
/* @author Sidney Durant
 * 
 * supplies methods for ARGB color manipulation, although the internal
 * representation uses four floats, the external representation uses the
 * ARGB integer standard to encode the color. Floats are used instead of bytes
 * to avoid dealing with the fact that bytes only store numbers from -128 to
 * 127, causing problems when using multiplication for interpolation, and to
 * avoid issues when doing multiplication and division with integer math
 */

public class Color {

	// the floats a, r, g, b may contain any values, but when returned as ARGB
	// values of the color, they will be from 0 to 255
	
	private float a, r, g, b;
	
	public Color( int c ){
		a = (short) ((c & 0xff000000) >>> 24);
		r = (short) ((c & 0xff0000) >> 16);
		g = (short) ((c & 0xff00) >> 8);
		b = (short) (c & 0xff);
	}
	
	public Color( float a, float r, float g, float b ){
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color( Color c ){
		this.a = c.a;
		this.r = c.r;
		this.g = c.g;
		this.b = c.b;
	}
	
	// return the current color plus the Color c
	public Color plus( Color c ){
		return new Color( a+c.a, r+c.r, g+c.g, b+c.b );
	}
	
	// add Color c to the current color
	public void add( Color c ){
		a += c.a;
		r += c.r;
		g += c.g;
		b += c.b;
	}
	
	// subtract Color c from the current color
	public Color minus( Color c ){
		return new Color( a-c.a, r-c.r, g-c.g, b-c.b );
	}
	
	// multiply each of the ARGB values by a scalar f
	public Color times( float f ){
		return new Color( a*f, r*f, g*f, b*f );
	}
	
	// return the color as an ARGB encoded int
	public int getColor(){
		return (int)a<<24|(int)r<<16|(int)g<<8|(int)b ;
	}
	
}
