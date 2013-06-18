package org.ggll.ui.component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.ggll.util.Log;

public class NewTextArea extends AbstractComponent implements HyperlinkListener, SyntaxConstants, FileComponent
{
	private RSyntaxTextArea textArea;
	private RTextScrollPane scrollPane;
	private String path;

	public NewTextArea()
	{
		textArea = createTextArea();
		textArea.setSyntaxEditingStyle(SYNTAX_STYLE_JAVA);
		SetTheme();
		scrollPane = new RTextScrollPane(textArea, true);
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

	public JComponent getComponent()
	{
		return scrollPane;
	}

	public String getText()
	{
		return textArea.getText();
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
		// textArea.setWhitespaceVisible(true);
		// textArea.setPaintMatchedBracketPair(true);
		return textArea;
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
				throw re; // FindBugs
			}
			catch (Exception e)
			{ // Never happens
				textArea.setText("Type here to see syntax highlighting");
			}
		}
		else
		{
			textArea.setText(resource);
		}
	}

	@Override
	public JComponent create(Object param) throws BadParameterException
	{
		if (param != null)
		{
			path = (String) param;
			setText(path, true);
		}
		return scrollPane;
	}

	@Override
	public void fireContentChanged()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getPath()
	{
		return path;
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
}
