package org.ggll.output;

import java.awt.Cursor;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.ggll.images.GGLLImages;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.util.html.CustomHTMLEditorKit;

public abstract class HtmlViewer implements HyperlinkListener
{
	
	protected class Page
	{
		final static String HEAD = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + "<html>" + "<head>" + "<meta http-equiv=\"Content-Type\" content=\"text/html\">" + "<title>Gr View gerenerated page</title>" + "</head>";
		final static String TAIL = "</body></html>";
		StringBuffer content = new StringBuffer(Page.HEAD + Page.TAIL);
		
		String getContent()
		{
			return content.toString();
		}
		
		void write(final String text)
		{
			final String newText = text;
			content.insert(content.length() - Page.TAIL.length(), newText);
		}
	}
	
	public static enum TOPIC
	{
		Error, Grammar, Output, Parser, SemanticStack, SyntaxStack, Tokens
	};
	
	public static final String ApplicationImagePath = GGLLImages.imagePath;
	public final static String DEFAULT_FONT = "Arial";
	public final static String DEFAULT_SIZE = "3";
	
	public final static String HORIZONTAL_LINE = "<hr width=\"100%\" size=\"1\" color=\"gray\" align=\"center\">";
	
	public final static boolean USE_CSSFILE = false;
	private SyntaxGraph activeScene;
	private final StyleSheet cssSheet = new StyleSheet();
	
	private final JEditorPane editorPane = new JEditorPane();
	
	private final HTMLEditorKit kit = new CustomHTMLEditorKit(HtmlViewer.ApplicationImagePath);
	
	public HtmlViewer()
	{
		try
		{
			cssSheet.loadRules(new StringReader(GGLLImages.imagePath + "output.css"), null);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		;
		if (HtmlViewer.USE_CSSFILE)
		{
			kit.setStyleSheet(cssSheet);
		}
		kit.setLinkCursor(new Cursor(Cursor.HAND_CURSOR));
		editorPane.setEditorKit(kit);
		final Document doc = editorPane.getDocument();
		final StringReader reader = new StringReader(new Page().getContent());
		try
		{
			kit.read(reader, doc, 0);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		editorPane.setDoubleBuffered(true);
		editorPane.setEditable(false);
		editorPane.setContentType("text/html");
		editorPane.addHyperlinkListener(this);
	}
	
	public void clear()
	{
		editorPane.setText("");
	}
	
	public void displayHorizontalLineExt(final TOPIC topic)
	{
		displayTextExt(HtmlViewer.HORIZONTAL_LINE, topic);
	}
	
	void displayTextExt(final String st, final String font, final String size, final String cssClass, final TOPIC topic)
	{
	}
	
	public void displayTextExt(final String st, final TOPIC topic)
	{
	}
	
	public SyntaxGraph getActiveScene()
	{
		return activeScene;
	}
	
	public StyleSheet getCssSheet()
	{
		return cssSheet;
	}
	
	public JEditorPane getEditorPane()
	{
		return editorPane;
	}
	
	public HTMLEditorKit getKit()
	{
		return kit;
	}
	
	@Override
	public void hyperlinkUpdate(final HyperlinkEvent e)
	{
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		{
			activeScene.select(e.getDescription());
		}
	}
	
	public void setActiveScene(final SyntaxGraph canvas)
	{
		activeScene = canvas;
	}
}
