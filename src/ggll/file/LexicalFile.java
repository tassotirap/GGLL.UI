package ggll.file;

import ggll.project.Project;
import ggll.util.io.IOHelper;

import java.io.File;
import java.io.IOException;

public class LexicalFile extends File
{
	private static final String ORG_GRVIEW_PROJECT_EMPTY_LEX = "/ggll/project/empty_lex";
	private static final long serialVersionUID = 1L;

	public LexicalFile(String pathname)
	{
		super(pathname);
	}

	public void create() throws IOException
	{
		if (!this.exists() && !this.createNewFile())
			throw new IOException("Could not create Lexical File");

		IOHelper.copyFileFromInputSteam(Project.class.getResourceAsStream(ORG_GRVIEW_PROJECT_EMPTY_LEX), this);
	}
}
