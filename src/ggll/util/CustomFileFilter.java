package ggll.util;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;

public class CustomFileFilter extends FileFilter
{
	private String description = null;
	private Hashtable filters = null;

	public CustomFileFilter(String[] filters, String description)
	{
		this.filters = new Hashtable(filters.length);
		for (int i = 0; i < filters.length; i++)
		{
			this.filters.put(filters[i].toLowerCase(), this);
		}
		this.description = description;
	}

	@Override
	public boolean accept(File f)
	{
		if (f != null)
		{
			if (f.isDirectory())
			{
				return true;
			}
			String extension = getExtension(f);
			if (extension != null && filters.get(getExtension(f)) != null)
			{
				return true;
			}
			;
		}
		return false;
	}

	@Override
	public String getDescription()
	{
		String fullDescription;
		if (description == null)
		{
			fullDescription = description == null ? "(" : description + " (";
			Enumeration extensions = filters.keys();
			if (extensions != null)
			{
				fullDescription += "." + (String) extensions.nextElement();
				while (extensions.hasMoreElements())
				{
					fullDescription += ", " + (String) extensions.nextElement();
				}
			}
			fullDescription += ")";
		}
		else
		{
			fullDescription = description;
		}
		return fullDescription;
	}

	public String getExtension(File f)
	{
		if (f != null)
		{
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1)
			{
				return filename.substring(i + 1).toLowerCase();
			}
			;
		}
		return null;
	}
}
