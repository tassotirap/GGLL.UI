package ggll.ui.file;

public class FileNames
{

	public final static String GRAM_EXTENSION = ".gram";
	public final static String JAVA_EXTENSION = ".java";
	public final static String LEX_EXTENSION = ".lex";
	public final static String SEM_EXTENSION = ".java";
	public final static String GGLL_TABLE_EXTENSION = ".ggll";

	public final static String TXT_EXTENSION = ".txt";
	public final static String XML_EXTENSION = ".xml";

	private String extension;

	public FileNames(String extension)
	{
		this.extension = extension;
	}

	public String getExtension()
	{
		return extension;
	}
}
