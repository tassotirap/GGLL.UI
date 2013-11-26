package ggll.ui.file;

import ggll.ui.project.Project;
import ggll.ui.util.io.IOHelper;

import java.io.File;
import java.io.IOException;

public class GrammarFile extends File
{
	private static final String ORG_GRVIEW_PROJECT_EMPTY_GRAMMAR = "/ggll/ui/file/template/empty_grammar";
	private static final long serialVersionUID = 1L;

	public GrammarFile(String pathname)
	{
		super(pathname);
	}

	public void create() throws IOException
	{
		if (!this.exists() && !this.createNewFile())
			throw new IOException("Could not create Grammar File");

		IOHelper.copyFileFromInputSteam(Project.class.getResourceAsStream(ORG_GRVIEW_PROJECT_EMPTY_GRAMMAR), this);
	}
}
