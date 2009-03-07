package Hanoi;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import Paint.Shapes.LineShape;

public class Tower {

	private static Color towersColor;
	private static final int LINE_WIDTH = 5;

	private Stack disks;
	private Point top;
	private LineShape firstLine;
	private LineShape secondLine;

	public Tower(int a, int b, int c, int d, int e, int f, int g, int h) {
		disks = new Stack();
		if (towersColor == null)
			towersColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
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

	public boolean push(Disk disk) {
		boolean done = disks.push(disk);
		if (done)
			disk.setBelow(top);
		return done;
	}

	public void draw(GC gc) {
		firstLine.draw(gc);
		secondLine.draw(gc);
		Iterator<Disk> iter = disks.iterator();
		while (iter.hasNext())
			iter.next().draw(gc);
	}

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

	public boolean intersect(Disk disk) {
		return disk.getOval().intersectLine(firstLine);
	}

	public void clear() {
		top = secondLine.calculateCenter();
		top.y -= 3;
		disks.clear();
	}

	public Disk pop() {
		top.y += Disk.DEFAULT_HEIGHT;
		return disks.pop();
	}

	public boolean isEmpty() {
		return disks.isEmpty();
	}

	public Disk peekDisk(int x, int y) {
		if (disks.isEmpty())
			return null;
		else if (disks.peek().contains(new Point(x, y))) {
			return disks.peek();
		}
		else
			return null;
	}
}
