package ggll.ui.util.html;

import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;

public class CustomHTMLEditorKit extends HTMLEditorKit
{
	private static final long serialVersionUID = 1L;
	
	private String applicationImagePath;
	
	public CustomHTMLEditorKit(String ApplicationImagePath)
	{
		this.applicationImagePath = ApplicationImagePath;
	}	

	@Override
	public ViewFactory getViewFactory()
	{
		return new CustomHTMLFactory(applicationImagePath);
	}
}

