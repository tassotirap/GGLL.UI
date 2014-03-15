package org.ggll.window.component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.ggll.util.Log;

public class TextAreaComponent extends AbstractFileComponent implements HyperlinkListener, SyntaxConstants
{
	private final RSyntaxTextArea textArea;
	private String path;
	
	public TextAreaComponent()
	{
		textArea = createTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		SetTheme();
		jComponent = new RTextScrollPane(textArea, true);
	}
	
	public TextAreaComponent(final String path)
	{
		this();
		this.path = path;
		setText(path, true);
		textArea.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void changedUpdate(final DocumentEvent arg0)
			{
				fireContentChanged();
				
			}
			
			@Override
			public void insertUpdate(final DocumentEvent arg0)
			{
				fireContentChanged();
			}
			
			@Override
			public void removeUpdate(final DocumentEvent arg0)
			{
				fireContentChanged();
			}
		});
	}
	
	private RSyntaxTextArea createTextArea()
	{
		final RSyntaxTextArea textArea = new RSyntaxTextArea(25, 70);
		textArea.setTabSize(3);
		textArea.setCaretPosition(0);
		textArea.addHyperlinkListener(this);
		textArea.requestFocusInWindow();
		textArea.setMarkOccurrences(true);
		textArea.setCodeFoldingEnabled(true);
		textArea.setClearWhitespaceLinesEnabled(false);
		return textArea;
	}
	
	@Override
	public void fireContentChanged()
	{
		for (final ComponentListener listener : listeners.getAll())
		{
			listener.ContentChanged(this);
		}
	}
	
	@Override
	public String getPath()
	{
		return path;
	}
	
	public String getText()
	{
		return textArea.getText();
	}
	
	public JTextArea getTextArea()
	{
		return textArea;
	}
	
	@Override
	public void hyperlinkUpdate(final HyperlinkEvent e)
	{
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		{
			final URL url = e.getURL();
			if (url == null)
			{
				UIManager.getLookAndFeel().provideErrorFeedback(null);
			}
			else
			{
				JOptionPane.showMessageDialog(textArea, "URL clicked:\n" + url.toString());
			}
		}
	}
	
	@Override
	public String saveFile()
	{
		final File file = new File(path);
		try
		{
			final FileWriter fw = new FileWriter(file);
			fw.write(textArea.getText());
			fw.close();
		}
		catch (final IOException e)
		{
			Log.Write("Could not save file!");
		}
		return getPath();
	}
	
	public void setText(final String resource, final boolean file)
	{
		if (file)
		{
			BufferedReader r = null;
			try
			{
				r = new BufferedReader(new InputStreamReader(new FileInputStream(resource), "UTF-8"));
				textArea.read(r, null);
				r.close();
				textArea.setCaretPosition(0);
				textArea.discardAllEdits();
			}
			catch (final RuntimeException re)
			{
				throw re;
			}
			catch (final Exception e)
			{
				textArea.setText("Type here to see syntax highlighting");
			}
		}
		else
		{
			textArea.setText(resource);
		}
	}
	
	private void SetTheme()
	{
		final InputStream in = getClass().getResourceAsStream("/eclipse.xml");
		try
		{
			final Theme theme = Theme.load(in);
			theme.apply(textArea);
		}
		catch (final IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
