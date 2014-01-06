package org.ggll.view.component;


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
		this.textArea = createTextArea();
		this.textArea.setSyntaxEditingStyle(SYNTAX_STYLE_JAVA);
		SetTheme();
		this.jComponent = new RTextScrollPane(this.textArea, true);
	}

	public TextAreaComponent(String path)
	{
		this();
		this.path = path;
		setText(path, true);
		this.textArea.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void changedUpdate(DocumentEvent arg0)
			{
				fireContentChanged();

			}

			@Override
			public void insertUpdate(DocumentEvent arg0)
			{
				fireContentChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0)
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

	private void SetTheme()
	{
		final InputStream in = getClass().getResourceAsStream("/eclipse.xml");
		try
		{
			final Theme theme = Theme.load(in);
			theme.apply(this.textArea);
		}
		catch (final IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	@Override
	public void fireContentChanged()
	{
		for (final ComponentListener listener : this.listeners.getAll())
		{
			listener.ContentChanged(this);
		}
	}

	@Override
	public String getPath()
	{
		return this.path;
	}

	public String getText()
	{
		return this.textArea.getText();
	}

	public JTextArea getTextArea()
	{
		return this.textArea;
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e)
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
				JOptionPane.showMessageDialog(this.textArea, "URL clicked:\n" + url.toString());
			}
		}
	}

	@Override
	public String saveFile()
	{
		final File file = new File(this.path);
		try
		{
			final FileWriter fw = new FileWriter(file);
			fw.write(this.textArea.getText());
			fw.close();
		}
		catch (final IOException e)
		{
			Log.log(Log.ERROR, null, "Could not save file!", e);
		}
		return getPath();
	}

	public void setText(String resource, boolean file)
	{
		if (file)
		{
			BufferedReader r = null;
			try
			{
				r = new BufferedReader(new InputStreamReader(new FileInputStream(resource), "UTF-8"));
				this.textArea.read(r, null);
				r.close();
				this.textArea.setCaretPosition(0);
				this.textArea.discardAllEdits();
			}
			catch (final RuntimeException re)
			{
				throw re;
			}
			catch (final Exception e)
			{
				this.textArea.setText("Type here to see syntax highlighting");
			}
		}
		else
		{
			this.textArea.setText(resource);
		}
	}
}
