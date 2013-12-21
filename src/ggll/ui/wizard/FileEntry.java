package ggll.ui.wizard;

import ggll.ui.file.FileNames;

import javax.swing.ImageIcon;

public class FileEntry
{
	private final FileNames extension;
	private ImageIcon image;
	private final String imagePath;
	private final String title;

	public FileEntry(String title, String imagePath, FileNames extension)
	{
		this.title = title;
		this.imagePath = imagePath;
		this.extension = extension;
	}

	public FileNames getExtension()
	{
		return this.extension;
	}

	public ImageIcon getImage()
	{
		if (this.image == null)
		{
			this.image = new ImageIcon(getClass().getResource(this.imagePath));
		}
		return this.image;
	}

	public String getTitle()
	{
		return this.title;
	}
}