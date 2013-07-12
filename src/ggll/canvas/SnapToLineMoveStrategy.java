package ggll.canvas;

import ggll.canvas.provider.LineProvider;
import ggll.canvas.widget.LineWidget;

import java.awt.Point;

import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.Widget;

public class SnapToLineMoveStrategy implements MoveStrategy
{

	private LineProvider lp;

	public SnapToLineMoveStrategy(LineProvider lp)
	{
		this.lp = lp;
	}

	@Override
	public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation)
	{
		int borderIncTop = (widget.getBorder() == null) ? 0 : widget.getBorder().getInsets().top;
		int borderIncLeft = (widget.getBorder() == null) ? 0 : widget.getBorder().getInsets().left;
		if (suggestedLocation.x < 0)
		{
			suggestedLocation.x = 0 + borderIncLeft;
		}
		if (suggestedLocation.y < 0)
		{
			suggestedLocation.y = 0;
		}
		LineWidget closestLine = lp.getLine(suggestedLocation.y);
		Point p = new Point(suggestedLocation.x, closestLine.getPreferredLocation().y + LineProvider.LINE_OFFSET - (borderIncTop / 2));// +
																																		// sizeInc);
		return p;
	}

}
