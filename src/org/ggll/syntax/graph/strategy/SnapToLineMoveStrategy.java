package org.ggll.syntax.graph.strategy;

import java.awt.Point;

import org.ggll.syntax.graph.provider.LineProvider;
import org.ggll.syntax.graph.widget.LineWidget;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.Widget;

public class SnapToLineMoveStrategy implements MoveStrategy
{
	
	private final LineProvider lineProvider;
	
	public SnapToLineMoveStrategy(final LineProvider lineProvider)
	{
		this.lineProvider = lineProvider;
	}
	
	@Override
	public Point locationSuggested(final Widget widget, final Point originalLocation, final Point suggestedLocation)
	{
		final int borderIncTop = widget.getBorder() == null ? 0 : widget.getBorder().getInsets().top;
		final int borderIncLeft = widget.getBorder() == null ? 0 : widget.getBorder().getInsets().left;
		if (suggestedLocation.x < 0)
		{
			suggestedLocation.x = 0 + borderIncLeft;
		}
		if (suggestedLocation.y < 0)
		{
			suggestedLocation.y = 0;
		}
		final LineWidget closestLine = lineProvider.getLine(suggestedLocation.y);
		final Point p = new Point(suggestedLocation.x, closestLine.getPreferredLocation().y + LineProvider.LINE_OFFSET - borderIncTop / 2);// +
																																			// sizeInc);
		return p;
	}
	
}
