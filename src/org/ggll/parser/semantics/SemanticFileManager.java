package org.ggll.parser.semantics;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.ggll.file.SemanticFile;
import org.ggll.util.Log;

public class SemanticFileManager
{
	private final SemanticFile file;

	public SemanticFileManager(SemanticFile file)
	{
		this.file = file;
	}

	private boolean addToFile(String name, String code)
	{
		try
		{
			final FileInputStream fileInputStream = new FileInputStream(this.file);
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = bufferedReader.readLine();
			String currentFile = "";
			while (line != null)
			{
				if (line.equals(SemanticFile.END))
				{
					currentFile += String.format(SemanticFile.ROUTINE_FORMAT, name) + "\n";
					currentFile += code + "\n";
				}
				currentFile += line + "\n";
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			final FileWriter fileWriter = new FileWriter(this.file);
			final PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println(currentFile);
			printWriter.close();

		}
		catch (final Exception e)
		{
			Log.log(Log.ERROR, this, "Could not create new routine", e);
			return false;
		}
		return true;
	}

	private String getFormatedCode(String name, String code)
	{
		final String[] codeLines = code.split("\n");
		code = "public void " + name + "()";
		code += "\n";
		code += "{";
		code += "\n";
		for (final String line : codeLines)
		{
			code += "\t" + line + "\n";
		}
		code += "}";
		return code;
	}

	public boolean canInsert(String routine)
	{
		return this.file.getCode(routine) == null;
	}

	public void editRoutine(String name, String code)
	{
		try
		{
			code = getFormatedCode(name, code);
			final FileInputStream fileInputStream = new FileInputStream(this.file);
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = bufferedReader.readLine();
			String currentFile = "";
			final String routine = String.format(SemanticFile.ROUTINE_FORMAT, name);

			while (line != null)
			{
				currentFile += line + "\n";
				if (line.contains(routine))
				{
					currentFile += code + "\n";
					line = bufferedReader.readLine();
					while (line != null)
					{
						if (line.matches(SemanticFile.ROUTINE_PATTERN) || line.contains(SemanticFile.END))
						{
							currentFile += line + "\n";
							break;
						}
						line = bufferedReader.readLine();
					}
				}

				line = bufferedReader.readLine();

			}
			bufferedReader.close();

			final FileWriter fileWriter = new FileWriter(this.file);
			final PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println(currentFile);
			printWriter.close();

		}
		catch (final Exception e)
		{
			Log.log(Log.ERROR, this, "Could not create new routine", e);
		}
	}

	public String getCleanCode(String routine, String code)
	{
		if (code == null)
		{
			code = this.file.getCode(routine);
			if (code == null)
			{
				return null;
			}
		}
		code = code.replaceFirst("(\\s*\\t*)*public void(\\s*\\t*)*" + routine + "(\\s*\\t*)*\\(\\)(\\s*\\t*)*\\{", "");
		code = code.replaceFirst("(\\s*\\t*)*\\n", "");
		if (code.lastIndexOf("}") >= 0)
		{
			code = code.substring(0, code.lastIndexOf("}"));
		}
		if (code.lastIndexOf("\n") >= 0)
		{
			code = code.substring(0, code.lastIndexOf("\n"));
		}
		final String[] lines = code.split("\n");
		code = "";
		for (final String line : lines)
		{
			if (line.startsWith("\t"))
			{
				code += line.replaceFirst("\t", "") + "\n";
			}
			else
			{
				code += line + "\n";
			}
		}
		if (code.lastIndexOf("\n") >= 0)
		{
			code = code.substring(0, code.lastIndexOf("\n"));
		}
		return code;
	}

	public boolean insertRoutine(String name, String code, String widgetName)
	{
		code = getFormatedCode(name, code);
		addToFile(name, code);
		return true;
	}
}
