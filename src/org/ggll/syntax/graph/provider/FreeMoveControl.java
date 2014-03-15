package org.ggll.syntax.graph.provider;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.List;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.state.StateHistory;
import org.netbeans.api.visual.action.MoveControlPointProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;

public class FreeMoveControl implements MoveControlPointProvider
{
	private final StateHistory canvasStateRepository;
	private final MoveControlPointProvider moveProvider;
	
	public FreeMoveControl(final SyntaxGraph canvas)
	{
		canvasStateRepository = canvas.getCanvasStateHistory();
		moveProvider = org.netbeans.api.visual.action.ActionFactory.createFreeMoveControlPointProvider();
	}
	
	@Override
	public List<Point> locationSuggested(final ConnectionWidget connectionWidget, final int arg1, final Point point)
	{
		final List<Point> points = moveProvider.locationSuggested(connectionWidget, arg1, point);
		connectionWidget.setControlPoints(points, true);
		canvasStateRepository.propertyChange(new PropertyChangeEvent(this, "undoable", null, "Move"));
		return points;
	}
	
}
