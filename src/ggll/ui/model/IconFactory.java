package ggll.ui.model;

import ggll.file.FileNames;
import ggll.images.GGLLImages;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconFactory
{
	public enum IconType
	{
		ACTIVE_OUTPUT_ICON, DIR_ICON, GRAM_ICON, GRAMMAR_ICON, JAVA_ICON, LEX_ICON, OVERVIEW_CON, PARSER_ICON, PROJECT_ICON, PROPERTIES_ICON, SEM_ICON, SEMANTIC_STACK_ICON, SYNTACTIC_STACK_ICON, TXT_ICON, XML_ICON
	}

	public Icon getIcon(IconType type)
	{
		switch (type)
		{
			case ACTIVE_OUTPUT_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_ACTIVE_OUTPUT));
			case DIR_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_PROJECT));
			case GRAM_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_GRAM));
			case GRAMMAR_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_GRAMMAR));
			case JAVA_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_JAVA));
			case LEX_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_LEX));
			case OVERVIEW_CON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_OVERVIEW));
			case PARSER_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_PARSER));
			case PROJECT_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_PROJECT));
			case PROPERTIES_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_PROPERTIES));
			case SEM_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_SEM));
			case SEMANTIC_STACK_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_SEMANTIC_STACK));
			case SYNTACTIC_STACK_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_SYNTACTIC_STACK));
			case TXT_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_TXT));
			case XML_ICON:
				return new ImageIcon(getClass().getResource(GGLLImages.ICON_XML));
			default:
				return null;
		}
	}

	public Icon getIcon(String fileName)
	{
		if (fileName.toLowerCase().endsWith(FileNames.GRAM_EXTENSION.toLowerCase()))
		{
			return getIcon(IconType.GRAM_ICON);
		}
		else if (fileName.toLowerCase().endsWith(FileNames.LEX_EXTENSION.toLowerCase()))
		{
			return getIcon(IconType.LEX_ICON);
		}
		else if (fileName.toLowerCase().endsWith(FileNames.SEM_EXTENSION.toLowerCase()))
		{
			return getIcon(IconType.SEM_ICON);
		}
		else if (fileName.toLowerCase().endsWith(FileNames.TXT_EXTENSION.toLowerCase()))
		{
			return getIcon(IconType.TXT_ICON);
		}
		else if (fileName.toLowerCase().endsWith(FileNames.XML_EXTENSION.toLowerCase()))
		{
			return getIcon(IconType.XML_ICON);
		}
		else if (fileName.toLowerCase().endsWith(FileNames.JAVA_EXTENSION.toLowerCase()))
		{
			return getIcon(IconType.JAVA_ICON);
		}
		return new IconView();
	}
}
