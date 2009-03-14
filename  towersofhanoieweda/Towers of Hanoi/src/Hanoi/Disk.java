package Hanoi;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import Paint.Shapes.LineShape;
import Paint.Shapes.OvalShape;
import Paint.Shapes.ShapeIF;

/**
 * 
 * @author Mostafa Mahmoud Mahmoud Eweda
 * @since JDK 1.6
 * @version 1.0
 *
 */
public class Disk {

	private static Color boundColor;
	private static Color inColor;
	/**
	 * the default initial size of the disk
	 */
	private static final int DEFAULT = 40;

	/**
	 * The factor with which the size should be multiplied with to provide appropriate size for the disk
	 */
	private static final int FACTOR = 20;

	/**
	 * The default height of the disk
	 */
	public static final int DEFAULT_HEIGHT = 25;

	/**
	 * The size representative for the disk priority
	 */
	private final int size;

	/**
	 * The size of the plate to be drawn on the screen
	 */
	private final int drawSize;

	/**
	 * The shape of the disk
	 */
	private ShapeIF oval;

	/**
	 * creates a disk with the given relative size
	 * @param size the relative size of the disk
	 */
	public Disk(int size) {
		if (inColor == null) {
			Display display = Display.getCurrent();
			inColor = new Color(display, 79, 73, 41);
			boundColor = new Color(display, 0, 0, 0);
		}
		this.size = size;
		drawSize = size * FACTOR + DEFAULT;
		oval = new OvalShape();
		oval.setColor(inColor);
		oval.setFilled(true);
	}

	/**
	 * @return the size of the disk
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Draws the disk
	 * @param gc the graphical context to draw the shape on
	 */
	public void draw(GC gc) {
		Color prev = oval.getColor();
		oval.draw(gc);
		oval.setFilled(false);
		oval.setColor(boundColor);
		oval.setWidth(5);
		oval.draw(gc);
		oval.setFilled(true);
		oval.setColor(prev);
	}

	/**
	 * @param pt the point to check on
	 * @return true if the shape contains the given point; false otherwise
	 */
	public boolean contains(Point pt) {
		return oval.contains(pt);
	}

	/**
	 * sets the location of the disk above the given point and updates the point location
	 * to the top center of the plate
	 * @param top The location of the highest point on the stack
	 */
	public void setBelow(Point top) {
		ArrayList<Point> points = oval.getPoints();
		points.clear();
		points.add(new Point(top.x - drawSize/2, top.y - DEFAULT_HEIGHT));
		points.add(new Point(drawSize, DEFAULT_HEIGHT));
		top.y -= DEFAULT_HEIGHT;
	}

	/**
	 * Shifts the shape with a given xShift and yShift
	 * @param xShift the x axis shift amount
	 * @param yShift the y axis shift amount
	 */
	public void relocate(int xShift, int yShift) {
		oval.relocate(xShift, yShift);
	}

	/**
	 * @param firstLine the line to test intersection
	 * @return true if the shape intersects with the given line
	 */
	public boolean intersectLine(LineShape firstLine) {
		return ((OvalShape)oval).intersectLine(firstLine);
	}

	/**
	 * dispose the needed UI system resources
	 */
	public static void dispose() {
		inColor.dispose();
		boundColor.dispose();
	}
}
