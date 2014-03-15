package org.ggll.syntax.graph.connector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.ImageIcon;

import org.ggll.images.GGLLImages;
import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.widget.IconNodeWidgetExt;
import org.ggll.syntax.graph.widget.IconNodeWidgetExt.TextOrientation;
import org.ggll.syntax.graph.widget.LabelWidgetExt;
import org.netbeans.api.visual.action.ConnectDecorator;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;

public class ConnectorFactory
{
	protected AlternativeConnector connectAlternative;
	
	protected SuccessorConnector connectSuccessor;
	
	private final String[] iconName = new String[]
	{ CanvasResource.N_TERMINAL, CanvasResource.TERMINAL, CanvasResource.LEFT_SIDE, CanvasResource.LAMBDA, CanvasResource.START };
	
	private final String[] icons = new String[]
	{ GGLLImages.ICON_N_TERMINAL, GGLLImages.ICON_TERMINAL, GGLLImages.ICON_LEFT_SIDE, GGLLImages.ICON_LAMBDA, GGLLImages.ICON_START };
	
	public ConnectorFactory(final SyntaxGraph canvas)
	{
		connectAlternative = new AlternativeConnector(canvas);
		connectSuccessor = new SuccessorConnector(canvas);
	}
	
	public ConnectionWidget drawConnection(final String type, final SyntaxGraph canvas, final String label)
	{
		final ConnectionWidget connection = getConnector(type).createConnectionWidget(canvas.getMainLayer().getScene());
		connection.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
		connection.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
		connection.setControlPointShape(PointShape.SQUARE_FILLED_BIG);
		connection.setPaintControlPoints(true);
		connection.getActions();
		return connection;
	}
	
	public Widget drawIcon(final String type, final SyntaxGraph canvas, final String text)
	{
		Widget widget;
		if (type.equals(CanvasResource.LAMBDA))
		{
			final IconNodeWidgetExt iwidget = new IconNodeWidgetExt(canvas.getMainLayer().getScene(), TextOrientation.RIGHT_CENTER);
			iwidget.setImage(new ImageIcon(findIconPath(type)).getImage());
			iwidget.setOpaque(true);
			iwidget.repaint();
			widget = iwidget;
		}
		else
		{
			final LabelWidgetExt lwidget = new LabelWidgetExt(canvas.getMainLayer().getScene(), text);
			lwidget.setOpaque(true);
			lwidget.setBorder(getIconBorder(type));
			lwidget.setMinimumSize(new Dimension(50, 0));
			lwidget.setVerticalAlignment(LabelWidgetExt.VerticalAlignment.CENTER);
			lwidget.repaint();
			widget = lwidget;
			
		}
		return widget;
	}
	
	public void drawSelected(final Widget widget)
	{
		widget.setBackground(Color.GRAY);
		widget.setForeground(Color.BLACK);
	}
	
	public void drawUnselected(final Widget widget)
	{
		widget.setBackground(Color.BLUE);
		widget.setForeground(Color.WHITE);
	}
	
	public String findIconPath(final String type)
	{
		for (int i = 0; i < icons.length && i < iconName.length; i++)
		{
			if (iconName[i].equals(type)) { return icons[i]; }
		}
		return null;
	}
	
	public ConnectDecorator getConnector(final String type)
	{
		switch (type)
		{
			case CanvasResource.SUCCESSOR:
				return connectSuccessor;
			case CanvasResource.ALTERNATIVE:
				return connectAlternative;
		}
		return null;
	}
	
	public Border getIconBorder(final String type)
	{
		Insets insets = null;
		if (type.equals(CanvasResource.START))
		{
			insets = new Insets(6, 18, 6, 16);
		}
		else if (type.equals(CanvasResource.LEFT_SIDE))
		{
			insets = new Insets(6, 8, 6, 16);
		}
		else
		{
			insets = new Insets(6, 20, 6, 8);
		}
		
		return BorderFactory.createImageBorder(insets, new ImageIcon(findIconPath(type)).getImage());
	}
	
}
