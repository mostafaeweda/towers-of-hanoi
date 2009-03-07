package Paint.Shapes;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;


/**
 * Circle shape
 * 
 * @see AbstractShape extending from
 * @author Mostafa Eweda & Mohammed Abd El Salam
 * @version 1.0
 * @since JDK 1.6
 */
public class OvalShape extends AbstractShape {

	private static final int POINTS_NUM = 2;

	public OvalShape() {
		super();
		points = new ArrayList<Point>(POINTS_NUM);
	}

	@Override
	public void draw(GC gc, Point pt) {
		super.draw(gc);
		if (points.size() == 1)
			points.add(new Point(pt.x, pt.y));
		gc.drawOval(points.get(0).x, points.get(0).y, pt.x, pt.y);
		points.get(1).x = pt.x;
		points.get(1).y = pt.y;
	}

	@Override
	public void draw(GC gc) {
		super.draw(gc);
		if (!filled)
			gc.drawOval(points.get(0).x, points.get(0).y, points.get(1).x,
					points.get(1).y);
		else
			gc.fillOval(points.get(0).x, points.get(0).y, points.get(1).x,
					points.get(1).y);
	}

	public Rectangle getBounds() {
		return new Rectangle(points.get(0).x, points.get(0).y, points.get(1).x,
				points.get(1).y);
	}

	public void relocate(int xShift, int yShift) {
		points.get(0).x += xShift;
		points.get(0).y += yShift;
	}

	public ShapeIF copy() {
		return clone();
	}

	@Override
	public boolean addable() {
		return points.size() < POINTS_NUM;
	}

	@Override
	public Point calculateCenter() {
		return new Point(points.get(0).x + points.get(1).x / 2, points.get(0).y
				+ points.get(1).y / 2);
	}

	@Override
	public String getToolType() {
		return "drag";
	}

	public static String getShapeName() {
		return "oval";
	}

	public static String getIconPath() {
		return "oval.png";
	}

	public static String getToolTip() {
		return "Draw Oval";
	}

	protected ShapeIF clone() {
		Color newColor = new Color(color.getDevice(), color.getRed(), color
				.getGreen(), color.getBlue());
		OvalShape shape = new OvalShape();
		shape.setColor(newColor);
		shape.setWidth(width);
		ArrayList<Point> p = new ArrayList<Point>();
		Iterator<Point> iter = points.iterator();
		Point current;
		while (iter.hasNext()) {
			current = iter.next();
			p.add(new Point(current.x, current.y));
		}
		shape.points = p;
		return shape;
	}

	@Override
	public void addPoint(int ptX, int ptY) {
		if (points.size() == 0) {
			points.add(new Point(ptX, ptY));
		}
	}

	// el mo3ayyan elli gowwa el oval
	public boolean contains(Point pt) {
		Point first = points.get(1);
		int width = first.x;
		int height = first.y;
		first = points.get(0);
		RectangleShape shape = new RectangleShape();
		shape.points.add(new Point(first.x, first.y + height/2));
		shape.points.add(new Point(first.x + width/2, first.y));
		shape.points.add(new Point(first.x + width, first.y + height/2));
		shape.points.add(new Point(first.x + width/2, first.y + height));
		return shape.contains(pt);
	}

	public boolean intersectLine(LineShape firstLine) {
		Point first = points.get(1);
		int width = first.x;
		int height = first.y;
		first = points.get(0);
		Point p1 = new Point(first.x, first.y + height/2),
			p3 = new Point(first.x + width, first.y + height/2),
			l1 = firstLine.points.get(0), l2 = firstLine.points.get(1);
		Point intersect;
		intersect = GraphicsUtils.intersection(l1, l2, p1, p3);
		if (GraphicsUtils.onLine(l1, l2, intersect) && GraphicsUtils.onLine(p1, p3, intersect))
			return true;
		return false;
	}
}
