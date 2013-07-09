package ggll.project.tree;

import ggll.file.FileNames;

import java.io.File;
import java.io.FilenameFilter;

public class TreeFileNameFilter implements FilenameFilter
{
	@Override
	public boolean accept(File dir, String name)
	{
		if (name.endsWith(FileNames.GGLL_TABLE_EXTENSION) || name.endsWith(FileNames.GRAM_EXTENSION) || name.endsWith(FileNames.SEM_EXTENSION) || name.endsWith(FileNames.LEX_EXTENSION) || name.endsWith(FileNames.XML_EXTENSION))
		{
			return true;
		}
		return false;
	}

}