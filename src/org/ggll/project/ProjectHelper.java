package org.ggll.project;

import ggll.core.list.ExtendedList;

import java.io.File;
import java.io.IOException;

import org.ggll.file.FileNames;
import org.ggll.file.GrammarFile;
import org.ggll.file.LexicalFile;
import org.ggll.file.SemanticFile;

public class ProjectHelper
{
	public static void createNewProject(final File projectRoot) throws IOException
	{
		final String basePath = projectRoot.getAbsoluteFile() + "/";
		final String name = projectRoot.getName();
		
		final GrammarFile gramFile = new GrammarFile(basePath + name + FileNames.GRAM_EXTENSION);
		final SemanticFile semFile = new SemanticFile(basePath + name + FileNames.SEM_EXTENSION);
		final LexicalFile lexFile = new LexicalFile(basePath + name + FileNames.LEX_EXTENSION);
		
		gramFile.create();
		semFile.create();
		lexFile.create();
		
		final Project project = new Project(projectRoot.getAbsolutePath());
		project.setGrammarFile(gramFile);
		project.setSemamticFile(semFile);
		project.setLexicalFile(lexFile);
		project.getOpenedFiles().append(gramFile);
	}
	
	public static boolean isProject(final File projectRoot)
	{
		boolean hasGrammarFile = false;
		boolean hasSemanticFile = false;
		boolean hasLexicalFile = false;
		
		for (final File file : projectRoot.listFiles())
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
		final ExtendedList<GrammarFile> gramFiles = new ExtendedList<GrammarFile>();
		SemanticFile semFile = null;
		LexicalFile lexFile = null;
		
		if (!projectRootPath.endsWith("/"))
		{
			projectRootPath += "/";
		}
		
		try
		{
			final File projectRoot = new File(projectRootPath);
			for (final File file : projectRoot.listFiles())
			{
				if (file.getName().endsWith(FileNames.GRAM_EXTENSION))
				{
					gramFiles.append(new GrammarFile(file.getAbsolutePath()));
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
			
			if (gramFiles.count() > 0 && semFile != null && lexFile != null)
			{
				project = new Project(projectRootPath);
				project.setGrammarFile(gramFiles);
				project.setSemamticFile(semFile);
				project.setLexicalFile(lexFile);
				project.getOpenedFiles().append(gramFiles.get(0));
				return project;
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
