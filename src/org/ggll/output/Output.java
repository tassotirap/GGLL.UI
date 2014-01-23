package org.ggll.output;

import java.io.StringReader;

import javax.swing.JComponent;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

import org.ggll.syntax.graph.SyntaxGraph;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.StringFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

public class Output extends HtmlViewer
{
	private static Output instance;

	protected Output()
	{
		super();
	}

	public static Output getInstance()
	{
		if (instance == null)
		{
			instance = new Output();
		}
		return instance;
	}

	@Override
	public void displayTextExt(String input, String font, String size, String cssClass, TOPIC topic)
	{
		final EditorKit eKit = getEditorPane().getEditorKit();
		final Document doc = getEditorPane().getDocument();
		String html = input;
		String text = "";
		String tag1 = "";
		String tag2 = "";
		if (cssClass == null && font != null && size != null)
		{
			tag1 = "<font face=\"" + font + "\" size=\"" + size + "\">";
			tag2 = "</font>";
		}
		else if (cssClass != null)
		{
			tag1 = "<span class=\"" + cssClass + "\">";
			tag2 = "</span>";
		}
		int offset = 0;
		try
		{
			final Parser parser = new Parser();
			parser.setInputHTML(new String(input));
			final StringFilter stringFilter = new StringFilter();
			final NodeList nodeList = parser.parse(stringFilter);
			final SimpleNodeIterator simpleNodeIterator = nodeList.elements();
			while (simpleNodeIterator.hasMoreNodes())
			{
				final Node node = simpleNodeIterator.nextNode();
				if (node.toHtml().length() > 0)
				{
					text = tag1 + node.toHtml() + tag2;
					html = html.substring(0, node.getStartPosition() + offset) + text + html.substring(node.getEndPosition() + offset);
					offset += tag1.length() + tag2.length();
				}
			}
		}
		catch (final ParserException e)
		{
			e.printStackTrace();
		}
		final StringReader reader = new StringReader(html);
		try
		{
			eKit.read(reader, doc, doc.getLength());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void displayTextExt(String st, TOPIC topic)
	{
		displayTextExt(st, DEFAULT_FONT, DEFAULT_SIZE, null, topic);
	}

	public JComponent getView(SyntaxGraph canvas)
	{
		setActiveScene(canvas);
		return getEditorPane();
	}
}
