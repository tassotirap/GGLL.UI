package ggll.ui.component;

import ggll.ui.util.Log;

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

public class TextAreaComponent extends AbstractComponent implements HyperlinkListener, SyntaxConstants, FileComponent
{
	private RSyntaxTextArea textArea;
	private String path;

	public TextAreaComponent()
	{
		textArea = createTextArea();
		textArea.setSyntaxEditingStyle(SYNTAX_STYLE_JAVA);
		SetTheme();
		jComponent = new RTextScrollPane(textArea, true);
	}

	public TextAreaComponent(String path)
	{
		this();
		this.path = path;
		setText(path, true);
		textArea.getDocument().addDocumentListener(new DocumentListener()
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
		RSyntaxTextArea textArea = new RSyntaxTextArea(25, 70);
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
		InputStream in = getClass().getResourceAsStream("/eclipse.xml");
		try
		{
			Theme theme = Theme.load(in);
			theme.apply(textArea);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	@Override
	public void fireContentChanged()
	{
		for (ComponentListener listener : listeners.getAll())
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
	public void hyperlinkUpdate(HyperlinkEvent e)
	{
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		{
			URL url = e.getURL();
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
	public void saveFile()
	{
		File file = new File(path);
		try
		{
			FileWriter fw = new FileWriter(file);
			fw.write(textArea.getText());
			fw.close();
		}
		catch (IOException e)
		{
			Log.log(Log.ERROR, null, "Could not save file!", e);
		}
	}

	public void setText(String resource, boolean file)
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
			catch (RuntimeException re)
			{
				throw re;
			}
			catch (Exception e)
			{
				textArea.setText("Type here to see syntax highlighting");
			}
		}
		else
		{
			textArea.setText(resource);
		}
	}
}
