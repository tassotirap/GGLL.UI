package org.ggll.file;

import java.io.File;
import java.io.IOException;

import org.ggll.project.Project;
import org.ggll.util.IOUtilities;

public class GrammarFile extends File
{
	private static final String ORG_GRVIEW_PROJECT_EMPTY_GRAMMAR = "/org/ggll/project/empty_grammar";
	private static final long serialVersionUID = 1L;

	public GrammarFile(String pathname)
	{
		super(pathname);
	}

	public void create() throws IOException
	{
		if (!this.exists() && !this.createNewFile())
			throw new IOException("Could not create Grammar File");

		IOUtilities.copyFileFromInputSteam(Project.class.getResourceAsStream(ORG_GRVIEW_PROJECT_EMPTY_GRAMMAR), this);
	}
}
