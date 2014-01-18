package org.ggll.file;

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
		for (final String filter : filters)
		{
			this.filters.put(filter.toLowerCase(), this);
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
			final String extension = getExtension(f);
			if (extension != null && this.filters.get(getExtension(f)) != null)
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
		if (this.description == null)
		{
			fullDescription = this.description == null ? "(" : this.description + " (";
			final Enumeration extensions = this.filters.keys();
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
			fullDescription = this.description;
		}
		return fullDescription;
	}

	public String getExtension(File f)
	{
		if (f != null)
		{
			final String filename = f.getName();
			final int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1)
			{
				return filename.substring(i + 1).toLowerCase();
			}
			;
		}
		return null;
	}
}
