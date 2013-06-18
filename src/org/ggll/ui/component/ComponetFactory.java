package org.ggll.ui.component;

import org.ggll.model.FileNames;

public class ComponetFactory
{
	public static AbstractComponent createFileComponent(String type)
	{
		if (type.equalsIgnoreCase(FileNames.GRAM_EXTENSION))
			return new GrammarComponent();
		if (type.equalsIgnoreCase(FileNames.LEX_EXTENSION))
			return new NewTextArea();
		if (type.equalsIgnoreCase(FileNames.SEM_EXTENSION))
			return new NewTextArea();
		if (type.equalsIgnoreCase(FileNames.XML_EXTENSION))
			return new NewTextArea();
		if (type.equalsIgnoreCase(FileNames.TXT_EXTENSION))
			return new SimpleTextAreaComponent();
		if (type.equalsIgnoreCase(FileNames.JAVA_EXTENSION))
			return new NewTextArea();
		if (type.equalsIgnoreCase(FileNames.IN_EXTENSION))
			return new InputAdapterComponent();
		return null;
	}
}
