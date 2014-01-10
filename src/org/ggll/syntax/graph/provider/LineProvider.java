package org.ggll.syntax.graph.provider;

import java.awt.Point;
import java.util.HashMap;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.widget.GuideLineWidget;
import org.ggll.syntax.graph.widget.LineWidget;

/**
 * Responsible for creating and positioning instances of LineWidget.
 * 
 * @author Gustavo H. Braga
 * 
 */
public class LineProvider
{

	private static HashMap<SyntaxGraph, LineProvider> lineProviders = new HashMap<SyntaxGraph, LineProvider>();
	/** the distance between lines **/
	public final static int LINE_OFFSET = 25;
	private final SyntaxGraph canvas;
	private LineWidget guideLine;
	private LineWidget lastLine;
	private int lastYPos;
	private final HashMap<Integer, LineWidget> lines;

	private LineProvider(SyntaxGraph canvas)
	{
		this.canvas = canvas;
		this.lines = new HashMap<Integer, LineWidget>();
	}

	public static LineProvider getInstance(SyntaxGraph canvas)
	{
		if (!lineProviders.containsKey(canvas))
		{
			lineProviders.put(canvas, new LineProvider(canvas));
		}
		return lineProviders.get(canvas);
	}

	/**
	 * Calculate the actual canvas height. If the canvas doesn't have the bounds
	 * set yet, this method deduces the canvas height from the height the
	 * Container that holds it.
	 * 
	 * @return the height of the canvas
	 */
	private int calculateCanvasHeight()
	{
		final int height = this.canvas.getBounds() == null ? this.canvas.getView().getParent().getHeight() : this.canvas.getBounds().height;
		return height;
	}

	public LineWidget getGuideLine()
	{
		return this.guideLine;
	}

	/**
	 * calculates the closest line to the given y position
	 * 
	 * @param y
	 *            the position close to the line
	 * @return the closest line to the y coordinate
	 */
	public LineWidget getLine(int y)
	{
		LineWidget ltop;
		LineWidget ldown;
		int diff1 = 0;
		int diff2 = 0;
		if (y > LINE_OFFSET)
		{
			if (y <= this.lastYPos - (LineWidget.DEFAULT_HEIGHT + LINE_OFFSET))
			{
				while (!this.lines.containsKey(y - diff1) && y - diff1 >= LINE_OFFSET)
				{
					diff1++;
				}
				ltop = this.lines.get(y - diff1);
				while (!this.lines.containsKey(y + diff2) && y + diff2 <= this.lastYPos - (LineWidget.DEFAULT_HEIGHT + LINE_OFFSET) && diff2 < diff1)
				{
					diff2++;
				}
				ldown = this.lines.get(y + diff2);
				if (diff2 >= diff1)
				{
					return ltop;
				}
				else
				{
					return ldown;
				}
			}
			else
			{
				if (y >= this.lastYPos)
				{
					insertLine(null, null);
					this.canvas.repaint();
					return this.lastLine;
				}
				else if (this.lines.containsKey(this.lastYPos - (LineWidget.DEFAULT_HEIGHT + LINE_OFFSET)))
				{
					return this.lines.get(this.lastYPos - (LineWidget.DEFAULT_HEIGHT + LINE_OFFSET));
				}
			}
		}
		else
		{
			if (this.lines.containsKey(LINE_OFFSET))
			{
				return this.lines.get(LINE_OFFSET);
			}
		}
		return null;
	}

	/**
	 * Insert a new guide line
	 * 
	 * @param xPos
	 *            the x position of the guide line, if null, uses the mouse x
	 *            position
	 */
	public LineWidget insertGuideLine(Integer xPos)
	{
		final LineWidget lWidget = (LineWidget) this.canvas.addNode(GuideLineWidget.class.getCanonicalName());
		if (xPos == null)
		{
			lWidget.setPreferredLocation(new Point(GuideLineWidget.DEFAULT_X_POS, 0));
		}
		else
		{
			lWidget.setPreferredLocation(new Point(xPos, 0));
		}
		this.guideLine = lWidget;
		this.canvas.repaint();
		return this.guideLine;
	}

	/**
	 * Insert a new line. The position on the x-axis is assumed to be 0.
	 * 
	 * @param yPos
	 *            the position on the y-axis.
	 * @param lineNumber
	 *            the number of the line
	 */
	public synchronized LineWidget insertLine(Integer yPos, Integer lineNumber)
	{
		if (yPos == null)
		{
			yPos = this.lastYPos;
		}
		if (lineNumber == null)
		{
			lineNumber = this.lines.size() + 1;
		}
		final LineWidget lWidget = (LineWidget) this.canvas.addNode(LineWidget.class.getCanonicalName() + lineNumber);
		lWidget.setPreferredLocation(new Point(0, yPos));
		lWidget.setNumber(lineNumber);
		this.lines.put(yPos, lWidget);
		this.lastYPos = yPos + LineWidget.DEFAULT_HEIGHT + LINE_OFFSET;
		this.lastLine = lWidget;
		return this.lastLine;
	}

	public boolean isGuideVisible()
	{
		if (this.guideLine == null)
		{
			return false;
		}
		else
		{
			return this.guideLine.isVisible();
		}
	}

	/**
	 * estimates the number of lines inserted in the canvas
	 * 
	 * @return the number of lines in the canvas
	 */
	public int lineCnt()
	{
		return this.lines.size();
	}

	/**
	 * estimates the number of additional lines necessary to fill the entire
	 * canvas
	 * 
	 * @return the number of lines to fill the canvas
	 */
	public int linesToFillCanvas()
	{
		return (calculateCanvasHeight() - this.lines.size() * (LineWidget.DEFAULT_HEIGHT + LINE_OFFSET)) / (LineWidget.DEFAULT_HEIGHT + LINE_OFFSET);
	}

	/**
	 * Insert new lines to cover the entire canvas
	 */
	public void populateCanvas()
	{
		int yPos = LINE_OFFSET;
		int lineNumber = 1;
		int linesToInsert = calculateCanvasHeight() / (LineWidget.DEFAULT_HEIGHT + LINE_OFFSET);
		removeAllLines();
		while (linesToInsert > 0)
		{
			insertLine(yPos, lineNumber);
			lineNumber++;
			yPos += LineWidget.DEFAULT_HEIGHT + LINE_OFFSET;
			linesToInsert--;
		}
		this.canvas.repaint();
	}

	/**
	 * Remove all lines exhibited on the canvas
	 */
	public void removeAllLines()
	{
		for (final LineWidget lw : this.lines.values())
		{
			this.canvas.removeNodeSafely(LineWidget.class.getCanonicalName() + lw.getNumber());
		}
		this.lines.clear();
	}

	/**
	 * remove the guide line
	 */
	public void removeGuideLine()
	{
		this.canvas.removeNodeSafely(GuideLineWidget.class.getCanonicalName());
		this.guideLine = null;
	}
}
