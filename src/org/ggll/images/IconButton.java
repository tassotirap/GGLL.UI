package org.ggll.images;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

public class IconButton extends Icon
{
	
	@Override
	public int getIconHeight()
	{
		return Icon.ICON_SIZE;
	}
	
	@Override
	public int getIconWidth()
	{
		return Icon.ICON_SIZE;
	}
	
	@Override
	public void paintIcon(final Component c, final Graphics g, final int x, final int y)
	{
		final Color oldColor = g.getColor();
		
		g.setColor(Color.BLACK);
		g.fillOval(x, y, Icon.ICON_SIZE, Icon.ICON_SIZE);
		
		g.setColor(oldColor);
	}
}
