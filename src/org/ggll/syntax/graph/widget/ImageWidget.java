package org.ggll.syntax.graph.widget;

import java.awt.Image;

import org.netbeans.api.visual.widget.Scene;

public class ImageWidget extends org.netbeans.api.visual.widget.ImageWidget implements TypedWidget
{
	
	private String type;
	
	public ImageWidget(final Scene scene)
	{
		super(scene);
	}
	
	public ImageWidget(final Scene scene, final Image image)
	{
		super(scene, image);
	}
	
	@Override
	public String getType()
	{
		return type;
	}
	
	@Override
	public void setType(final String type)
	{
		this.type = type;
	}
	
}
