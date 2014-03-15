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
	
	public static LineProvider getInstance(final SyntaxGraph canvas)
	{
		if (!LineProvider.lineProviders.containsKey(canvas))
		{
			LineProvider.lineProviders.put(canvas, new LineProvider(canvas));
		}
		return LineProvider.lineProviders.get(canvas);
	}
	
	private final SyntaxGraph canvas;
	private LineWidget guideLine;
	private LineWidget lastLine;
	private int lastYPos;
	
	private final HashMap<Integer, LineWidget> lines;
	
	private LineProvider(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
		lines = new HashMap<Integer, LineWidget>();
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
		final int height = canvas.getBounds() == null ? canvas.getView().getParent().getHeight() : canvas.getBounds().height;
		return height;
	}
	
	public LineWidget getGuideLine()
	{
		return guideLine;
	}
	
	/**
	 * calculates the closest line to the given y position
	 * 
	 * @param y
	 *            the position close to the line
	 * @return the closest line to the y coordinate
	 */
	public LineWidget getLine(final int y)
	{
		LineWidget ltop;
		LineWidget ldown;
		int diff1 = 0;
		int diff2 = 0;
		if (y > LineProvider.LINE_OFFSET)
		{
			if (y <= lastYPos - (LineWidget.DEFAULT_HEIGHT + LineProvider.LINE_OFFSET))
			{
				while (!lines.containsKey(y - diff1) && y - diff1 >= LineProvider.LINE_OFFSET)
				{
					diff1++;
				}
				ltop = lines.get(y - diff1);
				while (!lines.containsKey(y + diff2) && y + diff2 <= lastYPos - (LineWidget.DEFAULT_HEIGHT + LineProvider.LINE_OFFSET) && diff2 < diff1)
				{
					diff2++;
				}
				ldown = lines.get(y + diff2);
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
				if (y >= lastYPos)
				{
					insertLine(null, null);
					canvas.repaint();
					return lastLine;
				}
				else if (lines.containsKey(lastYPos - (LineWidget.DEFAULT_HEIGHT + LineProvider.LINE_OFFSET))) { return lines.get(lastYPos - (LineWidget.DEFAULT_HEIGHT + LineProvider.LINE_OFFSET)); }
			}
		}
		else
		{
			if (lines.containsKey(LineProvider.LINE_OFFSET)) { return lines.get(LineProvider.LINE_OFFSET); }
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
	public LineWidget insertGuideLine(final Integer xPos)
	{
		final LineWidget lWidget = (LineWidget) canvas.addNode(GuideLineWidget.class.getCanonicalName());
		if (xPos == null)
		{
			lWidget.setPreferredLocation(new Point(GuideLineWidget.DEFAULT_X_POS, 0));
		}
		else
		{
			lWidget.setPreferredLocation(new Point(xPos, 0));
		}
		guideLine = lWidget;
		canvas.repaint();
		return guideLine;
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
			yPos = lastYPos;
		}
		if (lineNumber == null)
		{
			lineNumber = lines.size() + 1;
		}
		final LineWidget lWidget = (LineWidget) canvas.addNode(LineWidget.class.getCanonicalName() + lineNumber);
		lWidget.setPreferredLocation(new Point(0, yPos));
		lWidget.setNumber(lineNumber);
		lines.put(yPos, lWidget);
		lastYPos = yPos + LineWidget.DEFAULT_HEIGHT + LineProvider.LINE_OFFSET;
		lastLine = lWidget;
		return lastLine;
	}
	
	public boolean isGuideVisible()
	{
		if (guideLine == null)
		{
			return false;
		}
		else
		{
			return guideLine.isVisible();
		}
	}
	
	/**
	 * estimates the number of lines inserted in the canvas
	 * 
	 * @return the number of lines in the canvas
	 */
	public int lineCnt()
	{
		return lines.size();
	}
	
	/**
	 * estimates the number of additional lines necessary to fill the entire
	 * canvas
	 * 
	 * @return the number of lines to fill the canvas
	 */
	public int linesToFillCanvas()
	{
		return (calculateCanvasHeight() - lines.size() * (LineWidget.DEFAULT_HEIGHT + LineProvider.LINE_OFFSET)) / (LineWidget.DEFAULT_HEIGHT + LineProvider.LINE_OFFSET);
	}
	
	/**
	 * Insert new lines to cover the entire canvas
	 */
	public void populateCanvas()
	{
		int yPos = LineProvider.LINE_OFFSET;
		int lineNumber = 1;
		int linesToInsert = calculateCanvasHeight() / (LineWidget.DEFAULT_HEIGHT + LineProvider.LINE_OFFSET);
		removeAllLines();
		while (linesToInsert > 0)
		{
			insertLine(yPos, lineNumber);
			lineNumber++;
			yPos += LineWidget.DEFAULT_HEIGHT + LineProvider.LINE_OFFSET;
			linesToInsert--;
		}
		canvas.repaint();
	}
	
	/**
	 * Remove all lines exhibited on the canvas
	 */
	public void removeAllLines()
	{
		for (final LineWidget lw : lines.values())
		{
			canvas.removeNodeSafely(LineWidget.class.getCanonicalName() + lw.getNumber());
		}
		lines.clear();
	}
	
	/**
	 * remove the guide line
	 */
	public void removeGuideLine()
	{
		canvas.removeNodeSafely(GuideLineWidget.class.getCanonicalName());
		guideLine = null;
	}
}
