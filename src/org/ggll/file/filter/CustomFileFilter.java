package org.ggll.file.filter;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;

public class CustomFileFilter extends FileFilter
{
	private String description = null;
	private Hashtable<String, CustomFileFilter> filters = null;
	
	public CustomFileFilter(final String[] filters, final String description)
	{
		this.filters = new Hashtable<String, CustomFileFilter>(filters.length);
		for (final String filter : filters)
		{
			this.filters.put(filter.toLowerCase(), this);
		}
		this.description = description;
	}
	
	@Override
	public boolean accept(final File f)
	{
		if (f != null)
		{
			if (f.isDirectory()) { return true; }
			final String extension = getExtension(f);
			if (extension != null && filters.get(getExtension(f)) != null) { return true; }
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
			final Enumeration<String> extensions = filters.keys();
			if (extensions != null)
			{
				fullDescription += "." + extensions.nextElement();
				while (extensions.hasMoreElements())
				{
					fullDescription += ", " + extensions.nextElement();
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
	
	public String getExtension(final File f)
	{
		if (f != null)
		{
			final String filename = f.getName();
			final int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) { return filename.substring(i + 1).toLowerCase(); }
			;
		}
		return null;
	}
}
