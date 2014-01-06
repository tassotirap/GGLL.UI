package org.ggll.file;


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

import org.ggll.project.Project;
import org.ggll.util.io.IOHelper;

public class SemanticFile extends File
{
	private static final String ORG_GRVIEW_PROJECT_EMPTY_SEMMANTIC = "/org/ggll/file/template/empty_semmantic";
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
		final Pattern pattern = Pattern.compile(ROUTINE_PATTERN);
		final Matcher matcher = pattern.matcher(line);
		if (matcher.find())
		{
			return matcher.group(1);
		}
		return null;
	}

	private String readFile(String filename)
	{
		String content = null;
		final File file = new File(filename); // for ex foo.txt
		try
		{
			final FileReader reader = new FileReader(file);
			final char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		return content;
	}

	public void create() throws IOException
	{
		if (!exists() && !createNewFile())
		{
			throw new IOException("Could not create SemanticFile");
		}

		IOHelper.copyFileFromInputSteam(Project.class.getResourceAsStream(ORG_GRVIEW_PROJECT_EMPTY_SEMMANTIC), this);
		String conteudo = readFile(getPath());
		conteudo = conteudo.replace("PROJECT_NAME", getName().replace(".java", ""));
		final FileWriter output = new FileWriter(this);
		output.write(conteudo);
		output.close();
	}

	public String getCode(String routineName)
	{
		if (routineName != null)
		{
			final HashMap<String, String> routineCode = getRoutineCode();
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
		final HashMap<String, String> routineCode = new HashMap<String, String>();
		try
		{
			final FileInputStream fileInputStream = new FileInputStream(this);
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = bufferedReader.readLine();
			while (line != null)
			{
				if (line.matches(ROUTINE_PATTERN))
				{
					final String name = getRoutineName(line);
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
		catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}

		return routineCode;
	}
}
