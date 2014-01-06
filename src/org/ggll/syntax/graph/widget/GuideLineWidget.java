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

	public GuideLineWidget(SyntaxGraph canvas)
	{
		super(canvas);
		this.canvas = canvas;
	}

	@Override
	protected void paintWidget()
	{
		this.height = this.canvas.getBounds() == null ? this.canvas.getView().getParent().getHeight() : this.canvas.getBounds().height;
		this.width = DEFAULT_WIDTH;
		final Graphics2D g = getGraphics();
		g.setStroke(new BasicStroke(this.width));
		g.setColor(GUIDE_LINE_COLOR);
		g.drawLine(0, 0, 0, this.height);
	}

	@Override
	public Rectangle calculateClientArea()
	{
		if (this.width == 0 || this.height == 0)
		{
			this.height = this.canvas.getBounds() == null ? this.canvas.getView().getParent().getHeight() : this.canvas.getBounds().height;
			this.width = DEFAULT_WIDTH;
		}
		return new Rectangle(this.width, this.height);
	}
}
