package Hanoi;

import java.util.Iterator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import Paint.Shapes.LineShape;

/**
 * Tower unit representing the tower that holds disks
 * @author Mostafa Mahmoud Mahmoud Eweda
 * @since JDK 1.6
 * @version 1.0
 */
public class Tower {

	/**
	 * The color of the towers
	 */
	private static Color towersColor;

	/**
	 * The width of the lines of the towers
	 */
	private static final int LINE_WIDTH = 5;

	/**
	 * A stack of disks the towers has
	 */
	private Stack disks;

	/**
	 * The top point of the stack
	 */
	private Point top;

	/**
	 * The base line of the tower
	 */
	private LineShape firstLine;

	/**
	 * The vertical line of the tower
	 */
	private LineShape secondLine;

	/**
	 * creates a tower with the given co-ordinates
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 * @param g
	 * @param h
	 */
	public Tower(int a, int b, int c, int d, int e, int f, int g, int h) {
		disks = new Stack();
		if (towersColor == null) {
			towersColor = new Color(Display.getCurrent(), 89, 106, 49);
		}
		firstLine = new LineShape();
		secondLine = new LineShape();
		firstLine.addPoint(a, b);
		firstLine.addPoint(c, d);
		firstLine.setColor(towersColor);
		firstLine.setWidth(LINE_WIDTH);
		secondLine.addPoint(e, f);
		secondLine.addPoint(g, h);
		secondLine.setColor(towersColor);
		secondLine.setWidth((int) (LINE_WIDTH * 1.5));
		top = secondLine.calculateCenter();
		top.y -= 3;
	}

	/**
	 * push a disk above of all disks at the top of the tower
	 * @param disk the disk to push to the stack of the tower
	 * @return true if the operation succeeded; false otherwise
	 */
	public boolean push(Disk disk) {
		boolean done = disks.push(disk);
		if (done)
			disk.setBelow(top);
		return done;
	}

	/**
	 * Draws the tower
	 * @param gc the graphical context to draw the tower on
	 */
	public void draw(GC gc) {
		firstLine.draw(gc);
		secondLine.draw(gc);
		Iterator<Disk> iter = disks.iterator();
		while (iter.hasNext())
			iter.next().draw(gc);
	}

	/**
	 * removes a disk from the top of the tower stack id its co-ordinates
	 * coincidence with the given x and y location
	 * @param x the x location of the point
	 * @param y the y location of the point
	 * @return the removed disk or null if none coincidence with the given location
	 */
	public Disk removeDisk(int x, int y) {
		if (disks.isEmpty())
			return null;
		else if (disks.peek().contains(new Point(x, y))) {
			top.y += Disk.DEFAULT_HEIGHT;
			return disks.pop();
		}
		else
			return null;
	}

	/**
	 * returns true if the tower intersects with disk
	 * @param disk the disk to test intersection on
	 * @return true if the tower intersects with given disk
	 */
	public boolean intersect(Disk disk) {
		return disk.intersectLine(firstLine);
	}

	/**
	 * clears the tower of the disks
	 */
	public void clear() {
		top = secondLine.calculateCenter();
		top.y -= 3;
		disks.clear();
	}

	/**
	 * removes the top element from the towers stack
	 * @return the disk at the top of the tower and removes it from the stack
	 */
	public Disk pop() {
		top.y += Disk.DEFAULT_HEIGHT;
		return disks.pop();
	}

	/**
	 * @return true if the tower is empty from disks
	 */
	public boolean isEmpty() {
		return disks.isEmpty();
	}

	/**
	 * @param x the x location of a point
	 * @param y the y location of a point
	 * @return the top disk if it contains the point described by (x, y) without removing it from the stack
	 */
	public Disk peekDisk(int x, int y) {
		if (disks.isEmpty())
			return null;
		else if (disks.peek().contains(new Point(x, y))) {
			return disks.peek();
		}
		else
			return null;
	}

	/**
	 * free the consumed UI resources to the system
	 */
	public static void dispose() {
		towersColor.dispose();
	}
}
