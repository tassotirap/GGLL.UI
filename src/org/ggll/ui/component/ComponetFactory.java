package org.ggll.ui.component;

import org.ggll.model.FileNames;

public class ComponetFactory
{
	public static AbstractComponent createFileComponent(String type, String path)
	{
		if (type.equalsIgnoreCase(FileNames.GRAM_EXTENSION))
		{
			return new GrammarComponent(path);
		}
		if (type.equalsIgnoreCase(FileNames.LEX_EXTENSION))
		{
			return new TextAreaComponent(path);
		}
		if (type.equalsIgnoreCase(FileNames.SEM_EXTENSION))
		{
			return new TextAreaComponent(path);
		}
		if (type.equalsIgnoreCase(FileNames.XML_EXTENSION))
		{
			return new TextAreaComponent(path);
		}
		if (type.equalsIgnoreCase(FileNames.TXT_EXTENSION))
		{
			return new SimpleTextAreaComponent(path);
		}
		if (type.equalsIgnoreCase(FileNames.JAVA_EXTENSION))
		{
			return new TextAreaComponent();
		}
		return null;
	}
}
