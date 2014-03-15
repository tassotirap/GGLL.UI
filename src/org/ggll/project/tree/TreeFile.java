package org.ggll.project.tree;

import java.io.File;

public class TreeFile extends File
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TreeFile(final File parent, final String child)
	{
		super(parent, child);
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
}
