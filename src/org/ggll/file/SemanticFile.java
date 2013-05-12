package org.ggll.file;

import java.io.File;
import java.io.IOException;

import org.ggll.project.Project;
import org.ggll.util.IOUtilities;

public class SemanticFile extends File
{
	private static final String ORG_GRVIEW_PROJECT_EMPTY_SEMMANTIC = "/org/ggll/project/empty_semmantic";
	private static final long serialVersionUID = 1L;

	public SemanticFile(String pathname)
	{
		super(pathname);
	}

	public void create() throws IOException
	{
		if (!this.exists() && !this.createNewFile())
			throw new IOException("Could not create SemanticFile");

		IOUtilities.copyFileFromInputSteam(Project.class.getResourceAsStream(ORG_GRVIEW_PROJECT_EMPTY_SEMMANTIC), this);
	}
}
