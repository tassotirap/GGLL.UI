package org.ggll.util.html;

import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;

public class CustomHTMLEditorKit extends HTMLEditorKit
{
	private static final long serialVersionUID = 1L;
	
	private final String applicationImagePath;
	
	public CustomHTMLEditorKit(final String ApplicationImagePath)
	{
		applicationImagePath = ApplicationImagePath;
	}
	
	@Override
	public ViewFactory getViewFactory()
	{
		return new CustomHTMLFactory(applicationImagePath);
	}
}
