package ggll.ui.project;

import ggll.core.list.ExtendedList;
import ggll.ui.file.GrammarFile;
import ggll.ui.file.LexicalFile;
import ggll.ui.file.SemanticFile;
import ggll.ui.main.ThemeManager.Theme;

import java.io.File;
import java.io.Serializable;

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

	private File projectDir;
	private Theme theme = Theme.DefaultDockingTheme;

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
		catch (Exception e)
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

	public Theme getTheme()
	{
		return this.theme;
	}

	public File getYyLexFile()
	{
		return yyLexFile;
	}

	public void setGrammarFile(ExtendedList<GrammarFile> grammarFiles)
	{
		
		this.grammarFile = grammarFiles;
	}
	
	public void setGrammarFile(GrammarFile grammarFile)
	{
		if(!this.grammarFile.contains(grammarFile))
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

	public void setTheme(Theme theme)
	{
		this.theme = theme;
	}

	public void setYyLexFile(File yyLexFile)
	{
		this.yyLexFile = yyLexFile;
	}
}
