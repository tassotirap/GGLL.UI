package ggll.canvas.decorator;

import ggll.canvas.UnidirectionalAnchor;

import java.awt.Color;
import java.awt.Point;

import org.netbeans.api.visual.action.ConnectDecorator;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.Anchor.Direction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

public class AlternativeConnectorDecorator implements ConnectDecorator
{

	private GraphScene scene;
	private ConnectionWidget thisConnection;

	@Override
	public ConnectionWidget createConnectionWidget(Scene scene)
	{
		ConnectionWidget widget = new ConnectionWidget(scene);
		widget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
		widget.setLineColor(Color.RED);
		thisConnection = widget;
		this.scene = (scene instanceof GraphScene) ? (GraphScene) scene : null;
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

		return new UnidirectionalAnchor(sourceWidget, Direction.BOTTOM);
	}

	@Override
	public Anchor createTargetAnchor(Widget targetWidget)
	{
		String connection = (String) scene.findObject(thisConnection);
		String target = (String) scene.findObject(targetWidget);
		return new UnidirectionalAnchor(targetWidget, Direction.TOP, connection, target, Direction.LEFT);
	}
}
