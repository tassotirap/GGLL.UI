package org.ggll.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.net.URL;

import javax.swing.ImageIcon;

import org.ggll.canvas.widget.IconNodeWidgetExt;
import org.ggll.canvas.widget.LabelWidgetExt;
import org.ggll.canvas.widget.IconNodeWidgetExt.TextOrientation;
import org.netbeans.api.visual.action.ConnectDecorator;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;

public class CanvasDecorator
{

	protected static final ConnectDecorator CONNECT_DECORATOR_ALTERNATIVE = new AlternativeConnectorDecorator();

	protected static final ConnectDecorator CONNECT_DECORATOR_SUCCESSOR = new SuccessorConnectorDecorator();

	private String[] iconName = new String[]{ CanvasData.N_TERMINAL, CanvasData.TERMINAL, CanvasData.LEFT_SIDE, CanvasData.LAMBDA, CanvasData.START };
	private URL[] icons = new URL[]{ getClass().getResource("/org/ggll/images/n_terminal.png"), getClass().getResource("/org/ggll/images/terminal.png"), getClass().getResource("/org/ggll/images/left_side.png"), getClass().getResource("/org/ggll/images/lambda.png"), getClass().getResource("/org/ggll/images/start.png") };

	public ConnectionWidget drawConnection(String type, Canvas canvas, String label)
	{
		ConnectionWidget connection = null;
		if (type.equals(CanvasData.SUCCESSOR))
		{
			connection = CONNECT_DECORATOR_SUCCESSOR.createConnectionWidget(canvas.getMainLayer().getScene());
		}
		else
		{
			connection = CONNECT_DECORATOR_ALTERNATIVE.createConnectionWidget(canvas.getMainLayer().getScene());
		}
		
		connection.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
		connection.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
		connection.setPaintControlPoints(true);
		connection.setControlPointShape(PointShape.SQUARE_FILLED_BIG);
		connection.getActions();
		return connection;
	}

	public Widget drawIcon(String type, Canvas canvas, String text) throws Exception
	{
		Widget widget;
		if (type.equals(CanvasData.LAMBDA))
		{
			IconNodeWidgetExt iwidget = new IconNodeWidgetExt(canvas.getMainLayer().getScene(), TextOrientation.RIGHT_CENTER);
			
			iwidget.setImage(new ImageIcon(findIconPath(type)).getImage());
			
			iwidget.setOpaque(true);
			iwidget.repaint();
			widget = iwidget;
		}
		else
		{
			LabelWidgetExt lwidget = new LabelWidgetExt(canvas.getMainLayer().getScene(), text);
			try
			{
				lwidget.setOpaque(true);
				lwidget.setBorder(getIconBorder(type));
				lwidget.setMinimumSize(new Dimension(50, 0));
				lwidget.setVerticalAlignment(LabelWidgetExt.VerticalAlignment.CENTER);
				lwidget.repaint();
				widget = lwidget;
			}
			catch (Exception e)
			{
				throw e;
			}
		}
		return widget;
	}
	
	public Border getIconBorder(String type)
	{
		Insets insets = null;		
		if(type.equals(CanvasData.START))
		{
			insets = new Insets(6, 18, 6, 16);
		}
		else if(type.equals(CanvasData.LEFT_SIDE))
		{
			insets = new Insets(6, 8, 6, 16);
		}
		else
		{
			insets = new Insets(6, 20, 6, 8);
		}
		
		return BorderFactory.createImageBorder(insets, new ImageIcon(findIconPath(type)).getImage());
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
		for (int i = 0; i < icons.length && i < iconName.length; i++)
		{
			if (iconName[i].equals(type))
			{
				return icons[i];
			}
		}
		return null;
	}

	public ConnectDecorator getConnDecoratorAlt()
	{
		return CONNECT_DECORATOR_ALTERNATIVE;
	}

	public ConnectDecorator getConnDecoratorSuc()
	{
		return CONNECT_DECORATOR_SUCCESSOR;
	}

}
