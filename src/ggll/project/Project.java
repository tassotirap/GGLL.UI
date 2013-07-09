package ggll.project;

import ggll.file.GrammarFile;
import ggll.file.LexicalFile;
import ggll.file.MetaFile;
import ggll.file.SemanticFile;
import ggll.ui.ThemeManager.Theme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
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

	private GrammarFile grammarFile;
	private LexicalFile lexicalFile;
	private SemanticFile semanticFile;

	private MetaFile metadataFile;
	private String name;
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

	public GrammarFile getGrammarFile()
	{
		return grammarFile;
	}

	public LexicalFile getLexicalFile()
	{
		return lexicalFile;
	}

	public MetaFile getMetadataFile()
	{
		return metadataFile;
	}

	public String getName()
	{
		return name;
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

	public boolean save()
	{
		try
		{
			FileOutputStream fileOutputStream = new FileOutputStream(getMetadataFile());
			new ObjectOutputStream(fileOutputStream).writeObject(this);
			fileOutputStream.close();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public void setGrammarFile(GrammarFile grammarFile)
	{
		this.grammarFile = grammarFile;
	}

	public void setLexicalFile(LexicalFile lexFile)
	{
		this.lexicalFile = lexFile;
	}

	public void setMetadataFile(MetaFile metadataFile)
	{
		this.metadataFile = metadataFile;
	}

	public void setName(String name)
	{
		this.name = name;
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