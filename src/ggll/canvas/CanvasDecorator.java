package ggll.canvas;

import ggll.canvas.decorator.AlternativeConnectorDecorator;
import ggll.canvas.decorator.SuccessorConnectorDecorator;
import ggll.canvas.widget.IconNodeWidgetExt;
import ggll.canvas.widget.IconNodeWidgetExt.TextOrientation;
import ggll.canvas.widget.LabelWidgetExt;
import ggll.images.GGLLImages;
import ggll.resource.CanvasResource;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.net.URL;

import javax.swing.ImageIcon;

import org.netbeans.api.visual.action.ConnectDecorator;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;

public class CanvasDecorator
{
	protected  ConnectDecorator ConnectDecoratorAlternative;

	protected  ConnectDecorator ConnectDecoratorSuccessor;

	private String[] iconName = new String[]{ CanvasResource.N_TERMINAL, CanvasResource.TERMINAL, CanvasResource.LEFT_SIDE, CanvasResource.LAMBDA, CanvasResource.START };

	private URL[] icons = new URL[]{ getClass().getResource(GGLLImages.ICON_N_TERMINAL), getClass().getResource(GGLLImages.ICON_TERMINAL), getClass().getResource(GGLLImages.ICON_LEFT_SIDE), getClass().getResource(GGLLImages.ICON_LAMBDA), getClass().getResource(GGLLImages.ICON_START) };
	public CanvasDecorator(AbstractCanvas canvas)
	{
		ConnectDecoratorAlternative = new AlternativeConnectorDecorator(canvas);
		ConnectDecoratorSuccessor = new SuccessorConnectorDecorator(canvas);
	}

	public ConnectionWidget drawConnection(String type, AbstractCanvas canvas, String label)
	{
		ConnectionWidget connection = null;
		if (type.equals(CanvasResource.SUCCESSOR))
		{
			connection = ConnectDecoratorSuccessor.createConnectionWidget(canvas.getMainLayer().getScene());
		}
		else
		{
			connection = ConnectDecoratorAlternative.createConnectionWidget(canvas.getMainLayer().getScene());
		}

		connection.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
		connection.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
		connection.setPaintControlPoints(true);
		connection.setControlPointShape(PointShape.SQUARE_FILLED_BIG);
		connection.getActions();
		return connection;
	}

	public Widget drawIcon(String type, AbstractCanvas canvas, String text) throws Exception
	{
		Widget widget;
		if (type.equals(CanvasResource.LAMBDA))
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
		return ConnectDecoratorAlternative;
	}

	public ConnectDecorator getConnDecoratorSuc()
	{
		return ConnectDecoratorSuccessor;
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
