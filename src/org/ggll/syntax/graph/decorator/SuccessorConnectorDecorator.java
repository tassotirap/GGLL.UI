package org.ggll.syntax.graph.decorator;

import java.awt.Color;
import java.awt.Point;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.UnidirectionalAnchor;
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
	private final SyntaxGraph canvas;

	public SuccessorConnectorDecorator(SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}

	@Override
	public ConnectionWidget createConnectionWidget(Scene scene)
	{
		final ConnectionWidget widget = new ConnectionWidget(scene);
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
		return new UnidirectionalAnchor(this.canvas, sourceWidget, Direction.RIGHT);
	}

	@Override
	public Anchor createTargetAnchor(Widget targetWidget)
	{
		return new UnidirectionalAnchor(this.canvas, targetWidget, Direction.LEFT);
	}

	public Anchor createTargetAnchor(Widget targetWidget, String edge)
	{
		return new UnidirectionalAnchor(this.canvas, targetWidget, Direction.LEFT, edge, Direction.TOP);
	}
}
