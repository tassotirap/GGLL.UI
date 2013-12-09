package ggll.ui.canvas.decorator;

import ggll.ui.canvas.Canvas;
import ggll.ui.canvas.UnidirectionalAnchor;

import java.awt.Color;
import java.awt.Point;

import org.netbeans.api.visual.action.ConnectDecorator;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.Anchor.Direction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

public class SuccessorConnectorDecorator implements ConnectDecorator
{
	private Canvas canvas;
	
	public SuccessorConnectorDecorator(Canvas canvas)
	{
		this.canvas = canvas;
	}

	@Override
	public ConnectionWidget createConnectionWidget(Scene scene)
	{
		ConnectionWidget widget = new ConnectionWidget(scene);
		widget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
		widget.setLineColor(Color.BLUE);
		return widget;
	}

	@Override
	public Anchor createFloatAnchor(Point location)
	{
		return AnchorFactory.createFixedAnchor(location);
	}

	@Override
	public Anchor createSourceAnchor(Widget sourceWidget)
	{
		return new UnidirectionalAnchor(canvas, sourceWidget, Direction.RIGHT);
	}

	@Override
	public Anchor createTargetAnchor(Widget targetWidget)
	{
		return new UnidirectionalAnchor(canvas, targetWidget, Direction.LEFT);
	}
	
	public Anchor createTargetAnchor(Widget targetWidget, String edge)
	{
		return new UnidirectionalAnchor(canvas, targetWidget, Direction.LEFT, edge, Direction.TOP);
	}
}
