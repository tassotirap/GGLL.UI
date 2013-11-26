package ggll.file;

import ggll.project.Project;
import ggll.util.io.IOHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

public class SemanticFile extends File
{
	private static final String ORG_GRVIEW_PROJECT_EMPTY_SEMMANTIC = "/ggll/project/empty_semmantic";
	public static final String BEGIN_ROUTINE = "/* BEGIN ROUTINE: ";
	public static final String BEGIN_SEMANTIC_ROUTINES = "/* BEGIN SEMANTIC ROUTINES */";
	public static final String END_ROUTINE = "/* END ROUTINE: ";
	public static final String END_SEMANTIC_ROUTINES = "/* END SEMANTIC ROUTINES */";

	private static final long serialVersionUID = 1L;

	public SemanticFile(String pathname)
	{
		super(pathname);
	}

	private String getRoutineName(String line)
	{
		return line.substring(line.indexOf(BEGIN_ROUTINE) + BEGIN_ROUTINE.length()).replace("*/", "").trim();
	}

	private String readFile(String filename)
	{
		String content = null;
		File file = new File(filename); // for ex foo.txt
		try
		{
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return content;
	}

	public void create() throws IOException
	{
		if (!this.exists() && !this.createNewFile())
			throw new IOException("Could not create SemanticFile");

		IOHelper.copyFileFromInputSteam(Project.class.getResourceAsStream(ORG_GRVIEW_PROJECT_EMPTY_SEMMANTIC), this);
		String conteudo = readFile(getPath());
		conteudo = conteudo.replace("PROJECT_NAME", this.getName().replace(".java", ""));
		FileWriter output = new FileWriter(this);
		output.write(conteudo);
		output.close();
	}

	public String getCode(String routineName)
	{
		if (routineName != null)
		{
			HashMap<String, String> routineCode = getRoutineCode();
			if (routineCode.containsKey(routineName))
			{
				return routineCode.get(routineName);
			}
		}
		return null;
	}

	public Set<String> getRegRoutines()
	{
		return getRoutineCode().keySet();
	}

	public HashMap<String, String> getRoutineCode()
	{
		HashMap<String, String> routineCode = new HashMap<String, String>();
		try
		{
			FileInputStream fileInputStream = new FileInputStream(this);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = bufferedReader.readLine();
			while (line != null)
			{
				if (line.equals(BEGIN_SEMANTIC_ROUTINES))
				{
					line = bufferedReader.readLine();
					while (line != null)
					{
						if (line.contains(BEGIN_ROUTINE))
						{
							String name = getRoutineName(line);
							String code = "";
							line = bufferedReader.readLine();
							while (line != null)
							{
								if (line.contains((END_ROUTINE + name)))
								{
									break;
								}
								else
								{
									code += line + "\n";
									line = bufferedReader.readLine();
								}
							}
							routineCode.put(name, code);
						}
						if (line.equals(END_SEMANTIC_ROUTINES))
						{
							break;
						}
						line = bufferedReader.readLine();
					}
				}

				line = bufferedReader.readLine();
			}
			bufferedReader.close();

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return routineCode;
	}
}
