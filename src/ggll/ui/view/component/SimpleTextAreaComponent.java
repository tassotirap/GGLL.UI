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
		StringBuffer sb = new StringBuffer();
		File f = new File(fileName);
		try
		{
			FileReader fileReader = new FileReader(f);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			bufferedReader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		jComponent = new JScrollPane(new JTextArea(sb.toString()));
	}

	@Override
	public void fireContentChanged()
	{
	}
}
