package ggll.ui.project;

import ggll.ui.file.GrammarFile;
import ggll.ui.file.LexicalFile;
import ggll.ui.file.SemanticFile;
import ggll.ui.main.ThemeManager.Theme;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a projects and deals with the management of a project.
 * 
 * @author Tasso Tirapani Silva Pinto
 */
public class Project implements Serializable
{
	private static final long serialVersionUID = -6812190878328950994L;

	private ArrayList<GrammarFile> grammarFile;
	private LexicalFile lexicalFile;
	private SemanticFile semanticFile;

	private List<File> openedFiles;

	private File projectDir;
	private Theme theme = Theme.ShapedGradientDockingTheme;

	private File yyLexFile;

	public Project(String projectsRootPath)
	{
		this(projectsRootPath, null);
	}

	public Project(String projectsRootPath, ArrayList<File> openedFiles)
	{		
		this.projectDir = new File(projectsRootPath);
		this.grammarFile = new ArrayList<GrammarFile>();
		try
		{
			if (openedFiles == null)
			{
				this.openedFiles = new ArrayList<File>();
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

	public ArrayList<GrammarFile> getGrammarFile()
	{
		return grammarFile;
	}

	public LexicalFile getLexicalFile()
	{
		return lexicalFile;
	}


	public List<File> getOpenedFiles()
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

	public void setGrammarFile(ArrayList<GrammarFile> grammarFiles)
	{
		
		this.grammarFile.addAll(grammarFiles);
	}
	
	public void setGrammarFile(GrammarFile grammarFile)
	{
		if(!this.grammarFile.contains(grammarFile))
		{
			this.grammarFile.add(grammarFile);
		}
	}

	public void setLexicalFile(LexicalFile lexFile)
	{
		this.lexicalFile = lexFile;
	}

	public void setOpenedFiles(List<File> openedFiles)
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
