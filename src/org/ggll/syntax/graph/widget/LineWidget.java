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
	
	public LineWidget(final SyntaxGraph canvas)
	{
		super(canvas);
		this.canvas = canvas;
	}
	
	@Override
	public Rectangle calculateClientArea()
	{
		return new Rectangle(width, height);
	}
	
	/**
	 * @return the number of this line
	 */
	public int getNumber()
	{
		return number;
	}
	
	@Override
	public void paintBorder()
	{
	}
	
	@Override
	protected void paintWidget()
	{
		width = canvas.getBounds() == null ? canvas.getView().getParent().getWidth() : canvas.getBounds().width;
		height = LineWidget.DEFAULT_HEIGHT;
		final Font f = LineWidget.NUMBER_FONT;
		final String s = new String(number.toString());
		final Graphics2D g = getGraphics();
		g.setStroke(new BasicStroke());
		g.setColor(LineWidget.LINE_COLOR);
		g.drawLine(0, 0, width, 0);
		final FontRenderContext frc = g.getFontRenderContext();
		final TextLayout textlayout = new TextLayout(s, f, frc);
		g.setColor(LineWidget.NUMBER_COLOR);
		textlayout.draw(g, LineWidget.LINE_NUMBER_SPACE, height / 2 + LineWidget.NUMBER_FONT.getSize() / 2);
		g.setColor(LineWidget.LINE_COLOR);
		g.drawLine(0, height, width, height);
		canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, LineWidget.LINE_PAINTED_EVENT, null, number));
	}
	
	/**
	 * Set the number of this line
	 * 
	 * @param number
	 *            the number o this line
	 */
	public void setNumber(final int number)
	{
		this.number = number;
	}
}
