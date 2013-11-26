package ggll.project;

import ggll.file.FileNames;
import ggll.file.GrammarFile;
import ggll.file.LexicalFile;
import ggll.file.SemanticFile;
import ggll.ui.exceptions.WarningException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectHelper
{
	public static void createNewProject(File projectRoot) throws IOException
	{
		String basePath = projectRoot.getAbsoluteFile() + "/";
		String name = projectRoot.getName();

		GrammarFile gramFile = new GrammarFile(basePath + name + FileNames.GRAM_EXTENSION);
		SemanticFile semFile = new SemanticFile(basePath + name + FileNames.SEM_EXTENSION);
		LexicalFile lexFile = new LexicalFile(basePath + name + FileNames.LEX_EXTENSION);

		gramFile.create();
		semFile.create();
		lexFile.create();

		Project project = new Project(projectRoot.getAbsolutePath());
		project.setGrammarFile(gramFile);
		project.setSemamticFile(semFile);
		project.setLexicalFile(lexFile);
		project.getOpenedFiles().add(gramFile);
	}

	public static boolean isProject(File projectRoot) throws WarningException
	{
		boolean hasGrammarFile = false;
		boolean hasSemanticFile = false;
		boolean hasLexicalFile = false;

		for (File file : projectRoot.listFiles())
		{
			if (file.getName().endsWith(FileNames.GRAM_EXTENSION))
			{
				hasGrammarFile = true;
			}
			else if (file.getName().endsWith(FileNames.SEM_EXTENSION))
			{
				hasSemanticFile = true;
			}
			else if (file.getName().endsWith(FileNames.LEX_EXTENSION))
			{
				hasLexicalFile = true;
			}
		}
		return hasGrammarFile && hasSemanticFile && hasLexicalFile;
	}

	public static Project openProject(String projectRootPath)
	{
		Project project = null;
		ArrayList<GrammarFile> gramFiles = new ArrayList<GrammarFile>();
		SemanticFile semFile = null;
		LexicalFile lexFile = null;
		
		if(!projectRootPath.endsWith("/"))
		{
			projectRootPath += "/";
		}			
		
		try
		{
			File projectRoot = new File(projectRootPath);
			for (File file : projectRoot.listFiles())
			{
				if (file.getName().endsWith(FileNames.GRAM_EXTENSION))
				{
					gramFiles.add(new GrammarFile(file.getAbsolutePath()));
				}
				else if (file.getName().endsWith(FileNames.SEM_EXTENSION))
				{
					semFile = new SemanticFile(file.getAbsolutePath());
				}
				else if (file.getName().endsWith(FileNames.LEX_EXTENSION))
				{
					lexFile = new LexicalFile(file.getAbsolutePath());
				}
			}

			if (gramFiles.size() > 0 && semFile != null && lexFile != null)
			{
				project = new Project(projectRootPath);
				project.setGrammarFile(gramFiles);
				project.setSemamticFile(semFile);
				project.setLexicalFile(lexFile);
				project.getOpenedFiles().add(gramFiles.get(0));
				return project;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
