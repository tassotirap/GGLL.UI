package ggll.ui.file;

import ggll.ui.project.Project;
import ggll.ui.util.io.IOHelper;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemanticFile extends File
{
	private static final String ORG_GRVIEW_PROJECT_EMPTY_SEMMANTIC = "/ggll/ui/file/template/empty_semmantic";
	public static final String ROUTINE_FORMAT = "/* [%s] */";
	public static final String ROUTINE_PATTERN = "/\\* \\[(.*)\\] \\*/";
	public static final String END = "/* END */";
	private static final long serialVersionUID = 1L;

	public SemanticFile(String pathname)
	{
		super(pathname);
	}

	private String getRoutineName(String line)
	{
		Pattern pattern = Pattern.compile(ROUTINE_PATTERN);
		Matcher matcher = pattern.matcher(line);
		if (matcher.find())
		{
			return matcher.group(1);
		}
		return null;
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
				if (line.matches(ROUTINE_PATTERN))
				{
					String name = getRoutineName(line);
					String code = "";
					line = bufferedReader.readLine();
					while (line != null)
					{
						if (line.matches(ROUTINE_PATTERN) || line.contains(END))
						{
							routineCode.put(name, code);
							break;
						}
						else
						{
							code += line + "\n";
							line = bufferedReader.readLine();
						}
					}
					continue;
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
