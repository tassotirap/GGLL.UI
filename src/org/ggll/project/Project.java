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
	
	public Project(final String projectsRootPath)
	{
		this(projectsRootPath, null);
	}
	
	public Project(final String projectsRootPath, final ExtendedList<File> openedFiles)
	{
		projectDir = new File(projectsRootPath);
		grammarFile = new ExtendedList<GrammarFile>();
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
		return grammarFile;
	}
	
	public LexicalFile getLexicalFile()
	{
		return lexicalFile;
	}
	
	public ExtendedList<File> getOpenedFiles()
	{
		return openedFiles;
	}
	
	public File getProjectDir()
	{
		return projectDir;
	}
	
	public String getProjectsRootPath()
	{
		return projectDir.getAbsolutePath();
	}
	
	public SemanticFile getSemanticFile()
	{
		return semanticFile;
	}
	
	public File getYyLexFile()
	{
		return yyLexFile;
	}
	
	public void setGrammarFile(final ExtendedList<GrammarFile> grammarFiles)
	{
		
		grammarFile = grammarFiles;
	}
	
	public void setGrammarFile(final GrammarFile grammarFile)
	{
		if (!this.grammarFile.contains(grammarFile))
		{
			this.grammarFile.append(grammarFile);
		}
	}
	
	public void setLexicalFile(final LexicalFile lexFile)
	{
		lexicalFile = lexFile;
	}
	
	public void setOpenedFiles(final ExtendedList<File> openedFiles)
	{
		this.openedFiles = openedFiles;
	}
	
	public void setSemamticFile(final SemanticFile semFile)
	{
		semanticFile = semFile;
	}
	
	public void setYyLexFile(final File yyLexFile)
	{
		this.yyLexFile = yyLexFile;
	}
}
