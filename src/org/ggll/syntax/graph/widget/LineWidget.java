package org.ggll.syntax.graph.widget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.beans.PropertyChangeEvent;

import org.ggll.syntax.graph.SyntaxGraph;
import org.netbeans.api.visual.widget.Widget;

public class LineWidget extends Widget
{

	public final static int DEFAULT_HEIGHT = 34;
	public final static Color LINE_COLOR = new Color(221, 221, 221);
	public final static int LINE_NUMBER_SPACE = 5;
	public final static String LINE_PAINTED_EVENT = "Line Painted";

	public final static Color NUMBER_COLOR = new Color(100, 100, 100);

	public final static Font NUMBER_FONT = new Font("Arial", Font.PLAIN, 12);
	private final SyntaxGraph canvas;
	private Integer number;
	protected int height;
	protected int width;

	public LineWidget(SyntaxGraph canvas)
	{
		super(canvas);
		this.canvas = canvas;
	}

	@Override
	protected void paintWidget()
	{
		this.width = this.canvas.getBounds() == null ? this.canvas.getView().getParent().getWidth() : this.canvas.getBounds().width;
		this.height = DEFAULT_HEIGHT;
		final Font f = NUMBER_FONT;
		final String s = new String(this.number.toString());
		final Graphics2D g = getGraphics();
		g.setStroke(new BasicStroke());
		g.setColor(LINE_COLOR);
		g.drawLine(0, 0, this.width, 0);
		final FontRenderContext frc = g.getFontRenderContext();
		final TextLayout textlayout = new TextLayout(s, f, frc);
		g.setColor(NUMBER_COLOR);
		textlayout.draw(g, LINE_NUMBER_SPACE, this.height / 2 + NUMBER_FONT.getSize() / 2);
		g.setColor(LINE_COLOR);
		g.drawLine(0, this.height, this.width, this.height);
		this.canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, LINE_PAINTED_EVENT, null, this.number));
	}

	@Override
	public Rectangle calculateClientArea()
	{
		return new Rectangle(this.width, this.height);
	}

	/**
	 * @return the number of this line
	 */
	public int getNumber()
	{
		return this.number;
	}

	@Override
	public void paintBorder()
	{
	}

	/**
	 * Set the number of this line
	 * 
	 * @param number
	 *            the number o this line
	 */
	public void setNumber(int number)
	{
		this.number = number;
	}
}
