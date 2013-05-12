package org.ggll.file;

import java.io.File;
import java.io.IOException;

import org.ggll.project.Project;
import org.ggll.util.IOUtilities;

public class LexicalFile extends File
{
	private static final String ORG_GRVIEW_PROJECT_EMPTY_LEX = "/org/ggll/project/empty_lex";
	private static final long serialVersionUID = 1L;

	public LexicalFile(String pathname)
	{
		super(pathname);
	}

	public void create() throws IOException
	{
		if (!this.exists() && !this.createNewFile())
			throw new IOException("Could not create Lexical File");

		IOUtilities.copyFileFromInputSteam(Project.class.getResourceAsStream(ORG_GRVIEW_PROJECT_EMPTY_LEX), this);
	}
}
