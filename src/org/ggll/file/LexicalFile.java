package org.ggll.file;

import java.io.File;
import java.io.IOException;

import org.ggll.project.Project;
import org.ggll.util.io.IOHelper;

public class LexicalFile extends File
{
	private static final String ORG_GRVIEW_PROJECT_EMPTY_LEX = "/org/ggll/file/template/empty_lex";
	private static final long serialVersionUID = 1L;

	public LexicalFile(String pathname)
	{
		super(pathname);
	}

	public void create() throws IOException
	{
		if (!exists() && !createNewFile())
		{
			throw new IOException("Could not create Lexical File");
		}

		IOHelper.copyFileFromInputSteam(Project.class.getResourceAsStream(ORG_GRVIEW_PROJECT_EMPTY_LEX), this);
	}
}
