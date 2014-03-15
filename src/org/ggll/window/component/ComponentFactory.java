package org.ggll.window.component;

import org.ggll.file.FileNames;

public class ComponentFactory
{
	public static AbstractComponent createFileComponentByName(final String fileName, final String path)
	{
		if (fileName.toLowerCase().endsWith(FileNames.LEX_EXTENSION.toLowerCase()))
		{
			return new TextAreaComponent(path);
		}
		else if (fileName.toLowerCase().endsWith(FileNames.SEM_EXTENSION.toLowerCase()))
		{
			return new TextAreaComponent(path);
		}
		else if (fileName.toLowerCase().endsWith(FileNames.GRAM_EXTENSION.toLowerCase()))
		{
			return new GrammarComponent(path);
		}
		else if (fileName.toLowerCase().endsWith(FileNames.XML_EXTENSION.toLowerCase()))
		{
			return new TextAreaComponent(path);
		}
		else if (fileName.toLowerCase().endsWith(FileNames.JAVA_EXTENSION.toLowerCase()))
		{
			return new TextAreaComponent(path);
		}
		else
		{
			return new TextAreaComponent(path);
		}
	}
	
	public static AbstractComponent createFileComponentByType(final String type, final String path)
	{
		if (type.equalsIgnoreCase(FileNames.GRAM_EXTENSION)) { return new GrammarComponent(path); }
		if (type.equalsIgnoreCase(FileNames.LEX_EXTENSION)) { return new TextAreaComponent(path); }
		if (type.equalsIgnoreCase(FileNames.SEM_EXTENSION)) { return new TextAreaComponent(path); }
		if (type.equalsIgnoreCase(FileNames.XML_EXTENSION)) { return new TextAreaComponent(path); }
		if (type.equalsIgnoreCase(FileNames.TXT_EXTENSION)) { return new SimpleTextAreaComponent(path); }
		if (type.equalsIgnoreCase(FileNames.JAVA_EXTENSION)) { return new TextAreaComponent(); }
		return null;
	}
}
