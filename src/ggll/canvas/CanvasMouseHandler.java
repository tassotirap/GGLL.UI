package ggll.canvas;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CanvasMouseHandler implements MouseListener
{
	AbstractCanvas canvas;

	public CanvasMouseHandler(AbstractCanvas canvas)
	{
		this.canvas = canvas;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent event)
	{
		canvas.setFocused();
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
	}

}
