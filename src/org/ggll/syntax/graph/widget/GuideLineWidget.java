package org.ggll.syntax.graph.widget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.ggll.syntax.graph.SyntaxGraph;

public class GuideLineWidget extends LineWidget
{
	
	public static final int DEFAULT_WIDTH = 1;
	public static final int DEFAULT_X_POS = 120;
	public static final Color GUIDE_LINE_COLOR = new Color(240, 100, 100);
	private final SyntaxGraph canvas;
	
	public GuideLineWidget(final SyntaxGraph canvas)
	{
		super(canvas);
		this.canvas = canvas;
	}
	
	@Override
	public Rectangle calculateClientArea()
	{
		if (width == 0 || height == 0)
		{
			height = canvas.getBounds() == null ? canvas.getView().getParent().getHeight() : canvas.getBounds().height;
			width = GuideLineWidget.DEFAULT_WIDTH;
		}
		return new Rectangle(width, height);
	}
	
	@Override
	protected void paintWidget()
	{
		height = canvas.getBounds() == null ? canvas.getView().getParent().getHeight() : canvas.getBounds().height;
		width = GuideLineWidget.DEFAULT_WIDTH;
		final Graphics2D g = getGraphics();
		g.setStroke(new BasicStroke(width));
		g.setColor(GuideLineWidget.GUIDE_LINE_COLOR);
		g.drawLine(0, 0, 0, height);
	}
}
