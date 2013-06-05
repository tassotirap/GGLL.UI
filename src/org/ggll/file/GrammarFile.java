package org.ggll.file;

import java.io.File;
import java.io.IOException;

import org.ggll.project.Project;
import org.ggll.util.IOUtilities;

public class GrammarFile extends File
{
	private static final String ORG_GRVIEW_PROJECT_EMPTY_GRAMMAR = "/org/ggll/project/empty_grammar";
	private static final long serialVersionUID = 1L;
	
	private int lastTerminalId = 0;
	private int lastNTerminalId = 0;
	private int lastLeftSides = 0;
	private int lastLAMBDA = 0;
	private int lastSTART = 0;
	private int lastCustomNode = 0;

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

	public int getLastTerminalId()
	{
		return lastTerminalId;
	}

	public void incLastTerminalId()
	{
		this.lastTerminalId++;
	}

	public int getLastNTerminalId()
	{
		return lastNTerminalId;
	}

	public void incLastNTerminalId()
	{
		this.lastNTerminalId++;
	}

	public int getLastLeftSides()
	{
		return lastLeftSides;
	}

	public void incLastLeftSides()
	{
		this.lastLeftSides++;
	}

	public int getLastLAMBDA()
	{
		return lastLAMBDA;
	}

	public void incLastLAMBDA()
	{
		this.lastLAMBDA++;
	}

	public int getLastSTART()
	{
		return lastSTART;
	}

	public void incLastSTART()
	{
		this.lastSTART++;
	}

	public int getLastCustomNode()
	{
		return lastCustomNode;
	}

	public void incLastCustomNode()
	{
		this.lastCustomNode++;
	}
}
