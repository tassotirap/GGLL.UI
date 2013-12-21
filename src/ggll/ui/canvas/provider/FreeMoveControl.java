package ggll.ui.canvas.provider;

import ggll.ui.canvas.Canvas;
import ggll.ui.canvas.state.CanvasStateRepository;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.List;

import org.netbeans.api.visual.action.MoveControlPointProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;

public class FreeMoveControl implements MoveControlPointProvider
{
	private final CanvasStateRepository canvasStateRepository;
	private final MoveControlPointProvider moveProvider;

	public FreeMoveControl(Canvas canvas)
	{
		this.canvasStateRepository = canvas.getCanvasStateRepository();
		this.moveProvider = org.netbeans.api.visual.action.ActionFactory.createFreeMoveControlPointProvider();
	}

	@Override
	public List<Point> locationSuggested(ConnectionWidget connectionWidget, int arg1, Point point)
	{
		final List<Point> points = this.moveProvider.locationSuggested(connectionWidget, arg1, point);
		connectionWidget.setControlPoints(points, true);
		this.canvasStateRepository.propertyChange(new PropertyChangeEvent(this, "undoable", null, "Move"));
		return points;
	}

}
