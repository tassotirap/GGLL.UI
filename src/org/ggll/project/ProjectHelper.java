package org.ggll.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.ggll.exceptions.WarningException;
import org.ggll.file.GrammarFile;
import org.ggll.file.LexicalFile;
import org.ggll.file.MetaFile;
import org.ggll.file.SemanticFile;
import org.ggll.model.FileNames;

public class ProjectHelper
{
	public static void createNewProject(File projectRoot) throws IOException
	{
		String basePath = projectRoot.getAbsoluteFile() + "/";
		String name = projectRoot.getName();

		GrammarFile gramFile = new GrammarFile(basePath + name + FileNames.GRAM_EXTENSION);
		SemanticFile semFile = new SemanticFile(basePath + name + FileNames.SEM_EXTENSION);
		LexicalFile lexFile = new LexicalFile(basePath + name + FileNames.LEX_EXTENSION);
		MetaFile metadataFile = new MetaFile(basePath + FileNames.METADATA_FILENAME);

		gramFile.create();
		semFile.create();
		lexFile.create();
		metadataFile.create();

		Project project = new Project(projectRoot.getAbsolutePath());
		project.setGrammarFile(gramFile);
		project.setSemamticFile(semFile);
		project.setLexicalFile(lexFile);
		project.setMetadataFile(metadataFile);
		project.getOpenedFiles().add(gramFile);
		project.save();
	}

	public static Project openProject(String projectRootPath)
	{
		try
		{
			if (!(projectRootPath.endsWith("/") || projectRootPath.endsWith("\\")))
			{
				projectRootPath += "/";
			}
			File metaFile = new File(projectRootPath + FileNames.METADATA_FILENAME);
			FileInputStream fileInputStream = new FileInputStream(metaFile);
			if (metaFile.length() > 0)
			{
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				Object object = objectInputStream.readObject();
				if (object instanceof Project)
				{
					Project result = (Project) object;
					objectInputStream.close();
					return result;
				}
				objectInputStream.close();
			}
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isProject(File projectRoot) throws WarningException
	{
		boolean hasGrammarFile = false;
		boolean hasSemanticFile = false;
		boolean hasLexicalFile = false;
		boolean hasMetaFile = false;

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
			else if (file.getName().equals(FileNames.METADATA_FILENAME))
			{
				hasMetaFile = true;
			}
		}
		return hasGrammarFile && hasSemanticFile && hasLexicalFile && hasMetaFile;
	}
}
