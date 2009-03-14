package Paint.Tools;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import Paint.Shapes.ShapeIF;

/**
 * Provides default behavior for the tool interface
 * @see ToolIF  implemented interface
 * @see ShapeIF
 * @author  Mostafa Mahmoud Mahmoud Eweda
 * @version  1.0
 * @since  JDK 1.6
 */
public abstract class AbstractTool implements ToolIF {

	protected ShapeIF element;

	protected AbstractTool() {
	}

	@Override
	public void setElement(ShapeIF shape) {
		element = shape;
	}

	@Override
	public final ShapeIF getElement() {
		return element;
	}

	public void drawShape(GC gc, Point current) {
		if (element.getAddedPoints() > 0)
			element.draw(gc, current);
	}

	public void mouseDown(MouseEvent e) {
		
	}

	public void mouseUp(MouseEvent e) {
	}

	public void mouseMove(MouseEvent e) {
	}

	public final void mouseDoubleClick(MouseEvent e) {
	} // final we malhaash imp??

	public void setData(Color color, int width) {
		if (element.getAddedPoints() == 0) {
			element.setWidth(width);
			element.setColor(color);
		}
	}
}