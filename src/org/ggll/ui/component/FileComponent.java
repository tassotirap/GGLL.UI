package org.ggll.ui.component;

public interface FileComponent
{

	/**
	 * @return the absolute path of the opened file
	 */
	public abstract String getPath();

	/**
	 * Saves this file
	 */
	public abstract void saveFile();
}
