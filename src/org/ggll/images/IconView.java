package org.ggll.images;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

public class IconView extends Icon
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
		
		g.setColor(new Color(70, 70, 70));
		g.fillRect(x, y, Icon.ICON_SIZE, Icon.ICON_SIZE);
		
		g.setColor(new Color(100, 230, 100));
		g.fillRect(x + 1, y + 1, Icon.ICON_SIZE - 2, Icon.ICON_SIZE - 2);
		
		g.setColor(oldColor);
	}
	
}
