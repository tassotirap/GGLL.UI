package org.ggll.project;

import ggll.core.list.ExtendedList;

import java.io.File;
import java.io.Serializable;

import org.ggll.file.GrammarFile;
import org.ggll.file.LexicalFile;
import org.ggll.file.SemanticFile;

/**
 * This class represents a projects and deals with the management of a project.
 * 
 * @author Tasso Tirapani Silva Pinto
 */
public class Project implements Serializable
{
	private static final long serialVersionUID = -6812190878328950994L;

	private ExtendedList<GrammarFile> grammarFile;
	private LexicalFile lexicalFile;
	private SemanticFile semanticFile;

	private ExtendedList<File> openedFiles;

	private final File projectDir;
	// private Theme theme = Theme.DefaultDockingTheme;

	private File yyLexFile;

	public Project(String projectsRootPath)
	{
		this(projectsRootPath, null);
	}

	public Project(String projectsRootPath, ExtendedList<File> openedFiles)
	{
		this.projectDir = new File(projectsRootPath);
		this.grammarFile = new ExtendedList<GrammarFile>();
		try
		{
			if (openedFiles == null)
			{
				this.openedFiles = new ExtendedList<File>();
			}
			else
			{
				this.openedFiles = openedFiles;
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	public ExtendedList<GrammarFile> getGrammarFile()
	{
		return this.grammarFile;
	}

	public LexicalFile getLexicalFile()
	{
		return this.lexicalFile;
	}

	public ExtendedList<File> getOpenedFiles()
	{
		return this.openedFiles;
	}

	public File getProjectDir()
	{
		return this.projectDir;
	}

	public String getProjectsRootPath()
	{
		return this.projectDir.getAbsolutePath();
	}

	public SemanticFile getSemanticFile()
	{
		return this.semanticFile;
	}

	// public Theme getTheme()
	// {
	// return this.theme;
	// }

	public File getYyLexFile()
	{
		return this.yyLexFile;
	}

	public void setGrammarFile(ExtendedList<GrammarFile> grammarFiles)
	{

		this.grammarFile = grammarFiles;
	}

	public void setGrammarFile(GrammarFile grammarFile)
	{
		if (!this.grammarFile.contains(grammarFile))
		{
			this.grammarFile.append(grammarFile);
		}
	}

	public void setLexicalFile(LexicalFile lexFile)
	{
		this.lexicalFile = lexFile;
	}

	public void setOpenedFiles(ExtendedList<File> openedFiles)
	{
		this.openedFiles = openedFiles;
	}

	public void setSemamticFile(SemanticFile semFile)
	{
		this.semanticFile = semFile;
	}

	// public void setTheme(Theme theme)
	// {
	// this.theme = theme;
	// }

	public void setYyLexFile(File yyLexFile)
	{
		this.yyLexFile = yyLexFile;
	}
}
