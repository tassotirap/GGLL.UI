package ggll.ui.util.html;

import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit.HTMLFactory;

public class CustomHTMLFactory extends HTMLFactory implements ViewFactory
{
	private final String applicationImagePath;

	public CustomHTMLFactory(String applicationImagePath)
	{
		this.applicationImagePath = applicationImagePath;
	}

	@Override
	public View create(Element elem)
	{
		final Object o = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
		if (o instanceof HTML.Tag)
		{
			final HTML.Tag kind = (HTML.Tag) o;
			if (kind == HTML.Tag.IMG)
			{
				return new CustomImageView(elem, this.applicationImagePath);
			}
		}
		return super.create(elem);
	}
}
