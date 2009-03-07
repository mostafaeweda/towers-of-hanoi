package Hanoi;

import java.util.Iterator;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import Paint.Shapes.ShapeIF;
import Paint.Tools.AbstractTool;
import Paint.Tools.ToolIF;

/**
 * Select tool that provides the implementations pf the dunction can be processed when the item is selected
 * @see ToolIF  implemented interface
 * @see ShapeIF
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class MoveTool extends AbstractTool {
	
	private static MoveTool instance;

	private boolean mouseDown;
	private Disk disk;
	private Tower tower;
	private Point currentPoint;

	public static synchronized MoveTool getInstance() {
		if (instance == null)
			return instance = new MoveTool();
		return instance;
	}

	private MoveTool() {
		currentPoint = new Point(0, 0);
		mouseDown = false;
	}

	public void mouseDown(MouseEvent e) {
		Iterator<Tower> towersIter = TowersUI.getInstance().getTowers().iterator();
		while (towersIter.hasNext() && disk == null) {
			tower = towersIter.next();
			disk = tower.removeDisk(e.x, e.y);
		}
		if (disk != null) {
			TowersUI.getInstance().handCursor();
			mouseDown = true;
		}
	}

	public void mouseUp(MouseEvent e) {
		if (mouseDown) {
			TowersUI.getInstance().defaultCursor();
			Iterator<Tower> towersIter = TowersUI.getInstance().getTowers().iterator();
			Tower temp;
			boolean done = false;
			while (towersIter.hasNext()) {
				temp = towersIter.next();
				if (temp.intersect(disk) && temp.push(disk)) {
					done = true;
					TowersUI.getInstance().incrementMoves();
				}
			}
			if (! done)
				tower.push(disk);
			disk = null;
			tower = null;
			TowersUI.getInstance().refresh();
		}
		mouseDown = false;
	}

	public void drawShape(GC gc, Point curent) {
		if (disk != null)
			disk.draw(gc);
	}

	public void mouseMove(MouseEvent e) {
		if (mouseDown) {
			disk.relocate(e.x - currentPoint.x, e.y - currentPoint.y);
			TowersUI.getInstance().refresh();
		}
		else {
			Iterator<Tower> towersIter = TowersUI.getInstance().getTowers().iterator();
			Tower t;
			Disk other = null;
			while (towersIter.hasNext() && other == null) {
				t = towersIter.next();
				other = t.peekDisk(e.x, e.y);
			}
			if (other != null)
				TowersUI.getInstance().handCursor();
			else
				TowersUI.getInstance().defaultCursor();
		}
		currentPoint.x = e.x;
		currentPoint.y = e.y;
	}
}