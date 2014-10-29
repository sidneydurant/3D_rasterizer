
/* @author Sidney Durant
 * 
 * This class can rasterize geometric primitives onto a 2D array of pixels
 * represented as a 1D array of integers of size WIDTH*HEIGHT
 * 
 * implemented:  points, lines, and triangles
 */

// find normal of triangle from three points

public class Rasterizer {

	private static int WIDTH, HEIGHT;
	private static int[] raster;
	private static float[] zBuffer;

	public Rasterizer(int width, int height, int[] raster) {

		WIDTH = width;
		HEIGHT = height;
		this.raster = raster;
		zBuffer = new float[WIDTH*HEIGHT];
	}
	
	// clears all of the pixels to be opaque white 
	public void clear() {
		for (int i = 0; i < raster.length; i++){
			raster[i] = 0xffffffff;
			zBuffer[i] = Float.MAX_VALUE;
		}
	}
	
	// rasterizes a point if its inside the frame
	public void drawPoint( Point p ) {
		int x = (int)p.getX();
		int y = (int)p.getY();
		Color c = p.getC();
		if (x < WIDTH && x >= 0 && y < HEIGHT && y >= 0) {
			fillPoint( x, y, c );
		}
	}

	// draws a point with given info, checking to see if it is inside the frame
	private void drawPoint( int x, int y, Color c ){
		if (x < WIDTH && x >= 0 && y < HEIGHT && y >= 0) {
			fillPoint( x, y, c );
		}
	}
	
	// draws a point with given info, checking to see if it is inside the frame
	private void drawPointZ( int x, int y, Color c, float z ){
		if (x < WIDTH && x >= 0 && y < HEIGHT && y >= 0) {
			if( z < zBuffer[x + WIDTH * y] ){
				fillPoint( x, y, c );
				zBuffer[x+ WIDTH * y] = z;
			}
		}
	}
	
	// draws a point with given info, doesn't check to see if pixel is on frame
	// checks z-buffer
		private void fillPointZ( int x, int y, Color c, float z ){
			if( z < zBuffer[x + WIDTH * y] ){
				fillPoint( x, y, c );
				zBuffer[x+ WIDTH * y] = z;
			}
		}
	
	// attempts to fill a pixel whether or not it exists. May 
	// throw arrayIndexOutOfBoundsException if the pixel is invalid, may fill
	// the incorrect pixel if x is too large (in which case it wraps around)
	// drawPoint() is a safe alternative
	private void fillPoint(int x, int y, Color c) {
		raster[x + WIDTH * y] = c.getColor();
	}
	
	// rasterizes a colored line segment
	public void drawLine( Line l ){
		
		Point p0 = l.getP0();
		Point p1 = l.getP1();
		
		int x0 = (int)p0.getX();
		int y0 = (int)p0.getY();
		Color c0 = p0.getC();
		int x1 = (int)p1.getX();
		int y1 = (int)p1.getY();
		Color c1 = p1.getC();
		
		if (x0 < WIDTH && x0 >= 0 && y0 < HEIGHT && y0 >= 0 && x1 < WIDTH
				&& x1 >= 0 && y1 < HEIGHT && y1 >= 0) {
			fillLine( x0, y0, c0, x1, y1, c1 );
		}else{
			drawLine( x0, y0, c0, x1, y1, c1 );
		}
		
	}

	// for use when parts of the line may be off screen, calls drawPoint
	// draws line from (x0, y0) to (x1, y1) with a smooth gradient from (c0, c1)
	private void drawLine(int x0, int y0, Color c0, int x1, int y1, Color c1) {

		float dx = x1 - x0;
		float dy = y1 - y0;
		Color dc = c1.minus(c0);

		// compare dx and dy to see if slope is steep ( slope > .5 )
		if ( abs(dy) > abs(dx) ) { // slope steep

			float xChangePerStep = dx / dy;
			Color cChangePerStep = dc.times(1 / dy);

			if (y0 < y1) { // iterate from y0 to y1
				float x = x0;
				Color c = new Color(c0);
				for (int y = y0; y <= y1; y++) {
					fillPoint((int) x, y, c);
					x += xChangePerStep;
					c.add(cChangePerStep);
				}
			} else { // iterate from y1 to y0
				float x = x1;
				Color c = new Color(c1);
				for (int y = y1; y <= y0; y++) {
					fillPoint((int) x, y, c);
					x += xChangePerStep;
					c.add(cChangePerStep);
				}

			}

		} else { // not steep

			float yChangePerStep = dy / dx;
			Color cChangePerStep = dc.times(1 / dx);

			if (x0 < x1) { // iterate from x0 to x1
				float y = y0;
				Color c = new Color(c0);
				for (int x = x0; x <= x1; x++) {
					fillPoint(x, (int) y, c);
					y += yChangePerStep;
					c.add(cChangePerStep);
				}
			} else { // iterate from x1 to x0
				float y = y1;
				Color c = new Color(c1);
				for (int x = x1; x <= x0; x++) {
					fillPoint(x, (int) y, c);
					y += yChangePerStep;
					c.add(cChangePerStep);
				}
			}
		}
	}
	
	// for use when the entire line is on the screen, calls fillPoint
	// draws line from (x0, y0) to (x1, y1) with a smooth gradient from (c0, c1)
	private void fillLine(int x0, int y0, Color c0, int x1, int y1, Color c1){

		float dx = x1 - x0;
		float dy = y1 - y0;
		Color dc = c1.minus(c0);
		
		// compare dx and dy to see if slope is steep ( slope > .5 )
		if ( abs(dy) > abs(dx) ){ // slope steep
			
			float xChangePerStep = dx/dy;
			Color cChangePerStep = dc.times(1/dy);
			
			if( y0 < y1 ){ // iterate from y0 to y1
				float x = x0;
				Color c = new Color(c0);
				for( int y = y0; y <= y1; y++ ){
					
					fillPoint( (int) x, y, c );
					x += xChangePerStep;
					c.add(cChangePerStep);
				}
			}else{ // iterate from y1 to y0
				float x = x1;
				Color c = new Color(c1);
				for( int y = y1; y <= y0; y++ ){
					fillPoint( (int) x, y, c );
					x += xChangePerStep;
					c.add(cChangePerStep);
				}
			
			}
			
		} else { // not steep
			
			float yChangePerStep = dy/dx;
			Color cChangePerStep = dc.times(1/dx);
			
			if( x0 < x1){ // iterate from x0 to x1
				float y = y0;
				Color c = new Color(c0);
				for ( int x = x0; x <= x1; x++ ){
					fillPoint( x, (int)y, c );
					y += yChangePerStep;
					c.add(cChangePerStep);
				}
			}else{ // iterate from x1 to x0
				float y = y1;
				Color c = new Color(c1);
				for ( int x = x1; x <= x0; x++ ){
					fillPoint( x, (int)y, c );
					y += yChangePerStep;
					c.add(cChangePerStep);
				}
			}
		}
		
	}
	
	// rasterizes a colored triangle
	public void drawTriangle( Triangle tri ){
		
		float[] y = new float[3];
		
		Point[] p = tri.getPoints();
		
		// sort by y position, lowest y values first
		y[0] = p[0].getY();
		y[1] = p[1].getY();
		y[2] = p[2].getY();
		
		int lowest = y[2]<(y[0]<y[1]?y[0]:y[1])?2:(y[0]<y[1]?0:1); // lowest y
		int highest = y[2]>(y[0]>y[1]?y[0]:y[1])?2:(y[0]>y[1]?0:1); // highest y
		int middle = (lowest+1)%3 != highest? (lowest+1)%3: (lowest+2)%3;// mid
		
		Point p0 = p[lowest];
		Point p1 = p[middle];
		Point p2 = p[highest];
		
		
		if( p0.getY() > 0 && p2.getY() < HEIGHT &&
				p0.getX() < WIDTH && p0.getX() > 0 &&
				p1.getX() < WIDTH && p1.getX() > 0 &&
				p2.getX() < WIDTH && p2.getX() > 0 ){
			
			fillTriangle( p0, p1, p2 );
			
		}else{
			drawTriangle( p0, p1, p2 );
		}
	}
	
	// uses 'standard triangle drawing' algorithm to draw a triangle
	// is safe, and checks if points are on screen before drawing
	private void drawTriangle( Point p0, Point p1, Point p2 ) {

		// e0 = long edge = p2 - p0
		// e1 = short edge 1 = p1 - p0
		// e2 = short edge 2 = p2-p1
		
		int yCurr = (int)p0.getY(); // current y position
		int yEnd = (int)p1.getY(); // final y position
		float e0dy = p2.getY()-p0.getY(); // dy of entire e0
		float e1dy = p1.getY()-p0.getY(); // dy of entire e1
		float e0dx_dy = (p2.getX()-p0.getX())/e0dy; // dx/dy of e0
		float e1dx_dy = (p1.getX()-p0.getX())/e1dy; // dx/dy of e1
		float x0 = p0.getX(); // x value along e0
		float x1 = p0.getX(); // x value along e1
		
		Color c0 = new Color(p0.getC()); // c value along e0
		Color c1 = new Color(p0.getC()); // c value along e1
		Color e0dc_dy = new Color((p2.getC().minus(p0.getC())).times(1/e0dy)); // dc/dy e0
		Color e1dc_dy = new Color((p1.getC().minus(p0.getC())).times(1/e1dy)); // dc/dy e1
		
		float z0 = p0.getZ(); // z value along e0
		float z1 = p0.getZ(); // z value along e1
		float e0dz_dy = (p2.getZ()-p0.getZ())/e0dy; // dz/dy of e0
		float e1dz_dy = (p1.getZ()-p0.getZ())/e1dy; // dz/dy of e1
		
		int xCurr; // current x position for each span
		int xEnd; // final x position for each span
		int dxCurr = 1;

		Color cCurr; // current c for each span
		Color spandc_dx; // dc_dx of for each span
		float zCurr;
		float spandz_dx;
		
		while( yCurr < yEnd  ){
			xCurr = (int)x0;
			xEnd = (int)x1;
			
			
			cCurr = new Color(c0);
			spandc_dx = new Color(c0.minus(c1).times(1/(x0-x1)));
			zCurr = z0;
			spandz_dx = (z0-z1)/(x0-x1);
			
			if( xCurr > xEnd ){
				dxCurr = -1;
				spandc_dx = spandc_dx.times(-1);
				spandz_dx = spandz_dx*-1;
			}
			
			// draw span at yCurr, lerping between x0 and x1, and c0 and c1
			while( xCurr != xEnd ){
				
				drawPointZ( xCurr, yCurr, cCurr, zCurr);
				
				cCurr.add(spandc_dx);
				zCurr+= spandz_dx;
				xCurr+=dxCurr;
			}
			
			x0 += e0dx_dy;
			x1 += e1dx_dy;
			c0.add(e0dc_dy);
			c1.add(e1dc_dy);
			z0 += e0dz_dy;
			z1 += e1dz_dy;
			yCurr++;
		}
		
		yEnd = (int)p2.getY(); // final y position
		e1dy = p2.getY()-p1.getY(); // dy of entire e2
		e1dx_dy = (p2.getX()-p1.getX())/e1dy; // dx/dy of e2
		x1 = p1.getX(); // x value along e2
		
		c1 = new Color(p1.getC()); // c value along e2
		e1dc_dy = new Color((p2.getC().minus(p1.getC())).times(1/e1dy)); // dc/dy e2
		z1 = p1.getZ();
		e1dz_dy = (p2.getZ()-p1.getZ()) / e1dy;
		
		dxCurr = 1;
		
		while( yCurr < yEnd  ){
			
			if( x0 > x1 ){
				
				xCurr = (int)x1;
				xEnd = (int)x0;

				cCurr = new Color(c1);
				spandc_dx = new Color(c1.minus(c0).times(1/(x1-x0)));
				zCurr = z1;
				spandz_dx = (z1-z0)/(x1-x0); // NOT SAFE, could divide by zero.
				
				while( xCurr <= xEnd ){ // TODO: need to add color correct <=
					drawPointZ( xCurr, yCurr, cCurr, zCurr);
					
					cCurr.add(spandc_dx);
					zCurr += spandz_dx;
					xCurr += dxCurr;
				}
			}else{
				
				xCurr = (int)x0;
				xEnd = (int)x1;

				cCurr = new Color(c0);
				spandc_dx = new Color(c0.minus(c1).times(1/(x0-x1)));
				zCurr = z0;
				spandz_dx = (z0-z1)/(x0-x1);
				
				while( xCurr <= xEnd ){ // TODO: need to add color correct <=
					drawPointZ( xCurr, yCurr, cCurr, zCurr);
					
					cCurr.add(spandc_dx);
					zCurr+= spandz_dx;
					xCurr += dxCurr;
				}
			}
			
			x0 += e0dx_dy;
			x1 += e1dx_dy;
			c0.add(e0dc_dy);
			c1.add(e1dc_dy);
			z0 += e0dz_dy;
			z1 += e1dz_dy;
			yCurr++;
		}
		
	}
	
	// uses 'standard triangle drawing' algorithm to fill a triangle
	// isn't safe, doesn't check if points are onscreen before drawing
	private void fillTriangle( Point p0, Point p1, Point p2 ) {

		// e0 = long edge = p2 - p0
		// e1 = short edge 1 = p1 - p0
		// e2 = short edge 2 = p2-p1
		
		int yCurr = (int)p0.getY(); // current y position
		int yEnd = (int)p1.getY(); // final y position
		float e0dy = p2.getY()-p0.getY(); // dy of entire e0
		float e1dy = p1.getY()-p0.getY(); // dy of entire e1
		float e0dx_dy = (p2.getX()-p0.getX())/e0dy; // dx/dy of e0
		float e1dx_dy = (p1.getX()-p0.getX())/e1dy; // dx/dy of e1
		float x0 = p0.getX(); // x value along e0
		float x1 = p0.getX(); // x value along e1
		
		Color c0 = new Color(p0.getC()); // c value along e0
		Color c1 = new Color(p0.getC()); // c value along e1
		Color e0dc_dy = new Color((p2.getC().minus(p0.getC())).times(1/e0dy)); // dc/dy e0
		Color e1dc_dy = new Color((p1.getC().minus(p0.getC())).times(1/e1dy)); // dc/dy e1
		
		float z0 = p0.getZ(); // z value along e0
		float z1 = p0.getZ(); // z value along e1
		float e0dz_dy = (p2.getZ()-p0.getZ())/e0dy; // dz/dy of e0
		float e1dz_dy = (p1.getZ()-p0.getZ())/e1dy; // dz/dy of e1
		
		int xCurr; // current x position for each span
		int xEnd; // final x position for each span
		int dxCurr = 1;

		Color cCurr; // current c for each span
		Color spandc_dx; // dc_dx of for each span
		float zCurr;
		float spandz_dx;
		
		while( yCurr < yEnd  ){
			xCurr = (int)x0;
			xEnd = (int)x1;
			
			
			cCurr = new Color(c0);
			spandc_dx = new Color(c0.minus(c1).times(1/(x0-x1)));
			zCurr = z0;
			spandz_dx = (z0-z1)/(x0-x1);
			
			if( xCurr > xEnd ){
				dxCurr = -1;
				spandc_dx = spandc_dx.times(-1);
				spandz_dx = spandz_dx*-1;
			}
			
			// draw span at yCurr, lerping between x0 and x1, and c0 and c1
			while( xCurr != xEnd ){
				
				fillPointZ( xCurr, yCurr, cCurr, zCurr);
				
				cCurr.add(spandc_dx);
				zCurr+= spandz_dx;
				xCurr+=dxCurr;
			}
			
			x0 += e0dx_dy;
			x1 += e1dx_dy;
			c0.add(e0dc_dy);
			c1.add(e1dc_dy);
			z0 += e0dz_dy;
			z1 += e1dz_dy;
			yCurr++;
		}
		
		yEnd = (int)p2.getY(); // final y position
		e1dy = p2.getY()-p1.getY(); // dy of entire e2
		e1dx_dy = (p2.getX()-p1.getX())/e1dy; // dx/dy of e2
		x1 = p1.getX(); // x value along e2
		
		c1 = new Color(p1.getC()); // c value along e2
		e1dc_dy = new Color((p2.getC().minus(p1.getC())).times(1/e1dy)); // dc/dy e2
		z1 = p1.getZ();
		e1dz_dy = (p2.getZ()-p1.getZ()) / e1dy;
		
		dxCurr = 1;
		
		while( yCurr < yEnd  ){
			
			if( x0 > x1 ){
				
				xCurr = (int)x1;
				xEnd = (int)x0;

				cCurr = new Color(c1);
				spandc_dx = new Color(c1.minus(c0).times(1/(x1-x0)));
				zCurr = z1;
				spandz_dx = (z1-z0)/(x1-x0); // NOT SAFE, could divide by zero.
				
				while( xCurr <= xEnd ){ // TODO: need to add color correct <=
					fillPointZ( xCurr, yCurr, cCurr, zCurr);
					
					cCurr.add(spandc_dx);
					zCurr += spandz_dx;
					xCurr += dxCurr;
				}
			}else{
				
				xCurr = (int)x0;
				xEnd = (int)x1;

				cCurr = new Color(c0);
				spandc_dx = new Color(c0.minus(c1).times(1/(x0-x1)));
				zCurr = z0;
				spandz_dx = (z0-z1)/(x0-x1);
				
				while( xCurr <= xEnd ){ // TODO: need to add color correct <=
					fillPointZ( xCurr, yCurr, cCurr, zCurr);
					
					cCurr.add(spandc_dx);
					zCurr+= spandz_dx;
					xCurr += dxCurr;
				}
			}
			
			x0 += e0dx_dy;
			x1 += e1dx_dy;
			c0.add(e0dc_dy);
			c1.add(e1dc_dy);
			z0 += e0dz_dy;
			z1 += e1dz_dy;
			yCurr++;
		}
		
		
	}
	
	// returns absolute value of float n
	private float abs( float n ){
		return n>0?n:-n;
	}

}
