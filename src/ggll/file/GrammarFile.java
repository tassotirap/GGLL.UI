package ggll.file;

import ggll.project.Project;
import ggll.util.IOUtilities;

import java.io.File;
import java.io.IOException;

public class GrammarFile extends File
{
	private static final String ORG_GRVIEW_PROJECT_EMPTY_GRAMMAR = "/ggll/project/empty_grammar";
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

	public int getLastCustomNode()
	{
		return lastCustomNode;
	}

	public int getLastLAMBDA()
	{
		return lastLAMBDA;
	}

	public int getLastLeftSides()
	{
		return lastLeftSides;
	}

	public int getLastNTerminalId()
	{
		return lastNTerminalId;
	}

	public int getLastSTART()
	{
		return lastSTART;
	}

	public int getLastTerminalId()
	{
		return lastTerminalId;
	}

	public void incLastCustomNode()
	{
		this.lastCustomNode++;
	}

	public void incLastLAMBDA()
	{
		this.lastLAMBDA++;
	}

	public void incLastLeftSides()
	{
		this.lastLeftSides++;
	}

	public void incLastNTerminalId()
	{
		this.lastNTerminalId++;
	}

	public void incLastSTART()
	{
		this.lastSTART++;
	}

	public void incLastTerminalId()
	{
		this.lastTerminalId++;
	}
}
