package org.ggll.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ggll.project.Project;
import org.ggll.util.IOUtilities;

public class SemanticFile extends File
{
	private static final String ORG_GRVIEW_PROJECT_EMPTY_SEMMANTIC = "/org/ggll/project/empty_semmantic";
	private static final long serialVersionUID = 1L;

	public SemanticFile(String pathname)
	{
		super(pathname);
	}

	public void create() throws IOException
	{
		if (!this.exists() && !this.createNewFile())
			throw new IOException("Could not create SemanticFile");
		
		String conteudo = readFile(Project.class.getResource(ORG_GRVIEW_PROJECT_EMPTY_SEMMANTIC).getFile());
		conteudo = conteudo.replace("PROJECT_NAME", this.getName().replace(".java", ""));
		FileWriter output = new FileWriter(this);
		output.write(conteudo);
		output.close();
	}
	
	public String readFile(String filename)
	{
	   String content = null;
	   File file = new File(filename); //for ex foo.txt
	   try {
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   return content;
	}
}
