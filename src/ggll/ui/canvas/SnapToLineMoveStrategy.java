package ggll.ui.canvas;

import ggll.ui.canvas.provider.LineProvider;
import ggll.ui.canvas.widget.LineWidget;

import java.awt.Point;

import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.Widget;

public class SnapToLineMoveStrategy implements MoveStrategy
{

	private final LineProvider lp;

	public SnapToLineMoveStrategy(LineProvider lp)
	{
		this.lp = lp;
	}

	@Override
	public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation)
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
		final LineWidget closestLine = this.lp.getLine(suggestedLocation.y);
		final Point p = new Point(suggestedLocation.x, closestLine.getPreferredLocation().y + LineProvider.LINE_OFFSET - borderIncTop / 2);// +
																																			// sizeInc);
		return p;
	}

}
