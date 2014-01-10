package org.ggll.output;

import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.util.html.CustomHTMLEditorKit;

public abstract class HtmlViewer implements HyperlinkListener
{

	protected class Page
	{
		final static String HEAD = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + "<html>" + "<head>" + "<meta http-equiv=\"Content-Type\" content=\"text/html\">" + "<title>Gr View gerenerated page</title>" + "</head>";
		final static String TAIL = "</body></html>";
		StringBuffer content = new StringBuffer(HEAD + TAIL);

		String getContent()
		{
			return this.content.toString();
		}

		void write(String text)
		{
			final String newText = text;
			this.content.insert(this.content.length() - TAIL.length(), newText);
		}
	}

	public static enum TOPIC
	{
		Error, Grammar, Output, Parser, SemanticStack, SyntaxStack, Tokens
	};

	public static final String ApplicationImagePath = new File("resources").getAbsolutePath();
	public final static String DEFAULT_FONT = "Arial";
	public final static String DEFAULT_SIZE = "3";

	public final static String HORIZONTAL_LINE = "<hr width=\"100%\" size=\"1\" color=\"gray\" align=\"center\">";
	public static final String SystemImagePath = new File("resources/images").getAbsolutePath();
	public static final String SystemImagePathKey = "system.image.path.key";

	public final static boolean USE_CSSFILE = false;
	private SyntaxGraph activeScene;
	private final StyleSheet cssSheet = new StyleSheet();

	private final JEditorPane editorPane = new JEditorPane();

	private final HTMLEditorKit kit = new CustomHTMLEditorKit(ApplicationImagePath);

	public HtmlViewer()
	{
		try
		{
			this.cssSheet.loadRules(new InputStreamReader(HtmlViewer.class.getResourceAsStream("/org/ggll/output/output.css")), null);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		;
		if (USE_CSSFILE)
		{
			this.kit.setStyleSheet(this.cssSheet);
		}
		this.kit.setLinkCursor(new Cursor(Cursor.HAND_CURSOR));
		setSystemImagePath(SystemImagePath);
		this.editorPane.setEditorKit(this.kit);
		final Document doc = this.editorPane.getDocument();
		final StringReader reader = new StringReader(new Page().getContent());
		try
		{
			this.kit.read(reader, doc, 0);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		this.editorPane.setDoubleBuffered(true);
		this.editorPane.setEditable(false);
		this.editorPane.setContentType("text/html");
		this.editorPane.addHyperlinkListener(this);
	}

	void displayTextExt(String st, String font, String size, String cssClass, TOPIC topic)
	{
	}

	public void clear()
	{
		this.editorPane.setText("");
	}

	public void displayHorizontalLineExt(TOPIC topic)
	{
		displayTextExt(HORIZONTAL_LINE, topic);
	}

	public void displayTextExt(String st, TOPIC topic)
	{
	}

	public SyntaxGraph getActiveScene()
	{
		return this.activeScene;
	}

	public StyleSheet getCssSheet()
	{
		return this.cssSheet;
	}

	public JEditorPane getEditorPane()
	{
		return this.editorPane;
	}

	public HTMLEditorKit getKit()
	{
		return this.kit;
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e)
	{
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		{
			this.activeScene.select(e.getDescription());
		}
	}

	public void setActiveScene(SyntaxGraph canvas)
	{
		this.activeScene = canvas;
	}

	public void setSystemImagePath(String path)
	{
		System.setProperty(SystemImagePathKey, path);
	}
}
