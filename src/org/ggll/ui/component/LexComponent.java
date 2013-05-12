package org.ggll.ui.component;

import org.ggll.core.lexical.YyFactory;

public class LexComponent extends AdvancedTextAreaComponent
{

	public LexComponent()
	{
		super("java");
	}

	@Override
	public void saveFile()
	{
		super.saveFile();
		YyFactory.createYylex(rootPath, "generated_code", path);
	}
}
