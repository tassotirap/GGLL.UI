package org.ggll.canvas.action;

import java.awt.Point;
import java.beans.PropertyChangeSupport;
import java.util.List;

import org.ggll.canvas.CanvasFactory;
import org.ggll.syntax.command.CommandFactory;
import org.netbeans.api.visual.action.MoveControlPointProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;

public class FreeMoveControl implements MoveControlPointProvider
{
	private PropertyChangeSupport monitor;
	MoveControlPointProvider moveProvider;
	
	public FreeMoveControl()
	{
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(CanvasFactory.getVolatileStateManager());
		moveProvider = org.netbeans.api.visual.action.ActionFactory.createFreeMoveControlPointProvider();
	}
	
	@Override
	public List<Point> locationSuggested(ConnectionWidget arg0, int arg1, Point arg2)
	{
		List<Point> points =  moveProvider.locationSuggested(arg0, arg1, arg2);
		arg0.setControlPoints(points, true);
		monitor.firePropertyChange("undoable", null, CommandFactory.createMoveCommand());		
		return points;
	}

}
