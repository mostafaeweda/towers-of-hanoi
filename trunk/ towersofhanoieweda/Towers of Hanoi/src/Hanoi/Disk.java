package Hanoi;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import Paint.Shapes.OvalShape;

public class Disk {

	private static final int DEFAULT = 40;
	private static final int FACTOR = 20;
	public static final int DEFAULT_HEIGHT = 25;

	private final int size;
	private final int drawSize;
	private OvalShape oval;

	public Disk(int size) {
		this.size = size;
		drawSize = size * FACTOR + DEFAULT;
		oval = new OvalShape();
		oval.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
		oval.setFilled(true);
	}

	public int getSize() {
		return size;
	}

	public void draw(GC gc) {
		Color prev = oval.getColor();
		oval.draw(gc);
		oval.setFilled(false);
		oval.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
		oval.setWidth(5);
		oval.draw(gc);
		oval.setFilled(true);
		oval.setColor(prev);
	}

	public boolean contains(Point pt) {
		return oval.contains(pt);
	}

	public void setBelow(Point top) {
		ArrayList<Point> points = oval.getPoints();
		points.clear();
		points.add(new Point(top.x - drawSize/2, top.y - DEFAULT_HEIGHT));
		points.add(new Point(drawSize, DEFAULT_HEIGHT));
		top.y -= DEFAULT_HEIGHT;
	}

	public void relocate(int xShift, int yShift) {
		oval.relocate(xShift, yShift);
	}

	public OvalShape getOval() {
		return oval;
	}
}
