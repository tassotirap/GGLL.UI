package org.ggll.syntax.graph.connector;

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

public class AlternativeConnector implements ConnectDecorator
{
	private final SyntaxGraph canvas;
	
	public AlternativeConnector(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	@Override
	public ConnectionWidget createConnectionWidget(final Scene scene)
	{
		final ConnectionWidget widget = new ConnectionWidget(scene);
		widget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
		widget.setLineColor(Color.RED);
		return widget;
	}
	
	@Override
	public Anchor createFloatAnchor(final Point location)
	{
		return AnchorFactory.createFixedAnchor(location);
	}
	
	@Override
	public Anchor createSourceAnchor(final Widget sourceWidget)
	{
		return new UnidirectionalAnchor(canvas, sourceWidget, Direction.BOTTOM);
	}
	
	@Override
	public Anchor createTargetAnchor(final Widget targetWidget)
	{
		return new UnidirectionalAnchor(canvas, targetWidget, Direction.TOP);
	}
	
	public Anchor createTargetAnchor(final Widget targetWidget, final String edge)
	{
		return new UnidirectionalAnchor(canvas, targetWidget, Direction.TOP, edge, Direction.LEFT);
	}
}
