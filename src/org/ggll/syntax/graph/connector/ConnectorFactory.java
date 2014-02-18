package org.ggll.syntax.graph.connector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.net.URL;

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

	private final String[] iconName = new String[]{ CanvasResource.N_TERMINAL, CanvasResource.TERMINAL, CanvasResource.LEFT_SIDE, CanvasResource.LAMBDA, CanvasResource.START };

	private final URL[] icons = new URL[]{ getClass().getResource(GGLLImages.ICON_N_TERMINAL), getClass().getResource(GGLLImages.ICON_TERMINAL), getClass().getResource(GGLLImages.ICON_LEFT_SIDE), getClass().getResource(GGLLImages.ICON_LAMBDA), getClass().getResource(GGLLImages.ICON_START) };

	public ConnectorFactory(SyntaxGraph canvas)
	{
		this.connectAlternative = new AlternativeConnector(canvas);
		this.connectSuccessor = new SuccessorConnector(canvas);
	}

	public ConnectionWidget drawConnection(String type, SyntaxGraph canvas, String label)
	{
		ConnectionWidget connection = getConnector(type).createConnectionWidget(canvas.getMainLayer().getScene());
		connection.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
		connection.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
		connection.setControlPointShape(PointShape.SQUARE_FILLED_BIG);
		connection.setPaintControlPoints(true);
		connection.getActions();
		return connection;
	}

	public Widget drawIcon(String type, SyntaxGraph canvas, String text)
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

	public void drawSelected(Widget widget)
	{
		widget.setBackground(Color.GRAY);
		widget.setForeground(Color.BLACK);
	}

	public void drawUnselected(Widget widget)
	{
		widget.setBackground(Color.BLUE);
		widget.setForeground(Color.WHITE);
	}

	public URL findIconPath(String type)
	{
		for (int i = 0; i < this.icons.length && i < this.iconName.length; i++)
		{
			if (this.iconName[i].equals(type))
			{
				return this.icons[i];
			}
		}
		return null;
	}

	public ConnectDecorator getConnector(String type)
	{		
		switch (type)
		{
			case CanvasResource.SUCCESSOR:
				return this.connectSuccessor;
			case CanvasResource.ALTERNATIVE:
				return this.connectAlternative;
		}
		return null;		
	}

	public Border getIconBorder(String type)
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