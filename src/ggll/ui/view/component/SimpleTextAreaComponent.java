package ggll.ui.view.component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SimpleTextAreaComponent extends AbstractComponent
{
	public SimpleTextAreaComponent(String fileName)
	{
		final StringBuffer sb = new StringBuffer();
		final File f = new File(fileName);
		try
		{
			final FileReader fileReader = new FileReader(f);
			final BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			bufferedReader.close();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		this.jComponent = new JScrollPane(new JTextArea(sb.toString()));
	}

	@Override
	public void fireContentChanged()
	{
	}
}
