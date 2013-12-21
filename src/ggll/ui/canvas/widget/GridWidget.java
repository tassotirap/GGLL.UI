package ggll.ui.canvas.widget;

import ggll.ui.canvas.Canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.netbeans.api.visual.widget.Widget;

public class GridWidget extends Widget
{

	public final static int GRID_SIZE = 16;
	public final static Color LINE_COLOR = new Color(221, 221, 221);
	private final Canvas canvas;

	private int height;
	private int width;

	public GridWidget(Canvas canvas)
	{
		super(canvas);
		this.canvas = canvas;
	}

	@Override
	protected void paintWidget()
	{
		this.width = this.canvas.getBounds() == null ? this.canvas.getView().getParent().getWidth() : this.canvas.getBounds().width;
		this.height = this.canvas.getBounds() == null ? this.canvas.getView().getParent().getHeight() : this.canvas.getBounds().height;
		final Graphics2D g = getGraphics();
		g.setStroke(new BasicStroke());
		g.setColor(LINE_COLOR);
		int grid_pos = GRID_SIZE;
		while (grid_pos < this.width)
		{
			g.drawLine(grid_pos, 0, grid_pos, this.height);
			grid_pos += GRID_SIZE;
		}
		grid_pos = GRID_SIZE;
		while (grid_pos < this.height)
		{
			g.drawLine(0, grid_pos, this.width, grid_pos);
			grid_pos += GRID_SIZE;
		}
	}

	@Override
	public Rectangle calculateClientArea()
	{
		return new Rectangle(this.width, this.height);
	}

	@Override
	public void paintBorder()
	{
	}
}
