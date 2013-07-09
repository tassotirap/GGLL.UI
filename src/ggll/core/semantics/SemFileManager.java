package ggll.core.semantics;

import ggll.core.syntax.command.CommandFactory;
import ggll.file.SemanticFile;
import ggll.util.Log;

import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class SemFileManager
{

	private SemanticFile file;
	private PropertyChangeSupport monitor;

	public SemFileManager(SemanticFile file, PropertyChangeSupport monitor)
	{
		this.file = file;
		this.monitor = monitor;
	}

	private boolean addToFile(String name, String code)
	{
		try
		{
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = bufferedReader.readLine();
			String currentFile = "";
			while (line != null)
			{
				currentFile += line + "\n";
				if (line.equals(SemanticFile.BEGIN_SEMANTIC_ROUTINES))
				{
					currentFile += SemanticFile.BEGIN_ROUTINE + name + " */ \n";
					currentFile += code + "\n";
					currentFile += SemanticFile.END_ROUTINE + name + " */ \n";
				}
				line = bufferedReader.readLine();
			}
			bufferedReader.close();

			FileWriter fileWriter = new FileWriter(file);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println(currentFile);
			printWriter.close();

		}
		catch (Exception e)
		{
			Log.log(Log.ERROR, this, "Could not create new routine", e);
			return false;
		}
		return true;
	}

	private String getFormatedCode(String name, String code)
	{
		if (!code.trim().startsWith("public void " + "name"))
		{
			String[] codeLines = code.split("\n");
			code = "public void " + name + "() {\n";
			for (String line : codeLines)
			{
				code += "\t" + line + "\n";
			}
			code += "}";
		}
		return code;
	}

	public boolean canInsert(String routine)
	{
		return file.getCode(routine) == null;
	}

	public void editRouine(String name, String code)
	{
		try
		{
			code = getFormatedCode(name, code);
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = bufferedReader.readLine();
			String currentFile = "";

			// HEAD
			while (line != null)
			{
				currentFile += line + "\n";
				if (line.equals(SemanticFile.BEGIN_SEMANTIC_ROUTINES))
				{
					line = bufferedReader.readLine();
					break;
				}
				line = bufferedReader.readLine();
			}

			//
			while (line != null)
			{
				currentFile += line + "\n";
				if (line.contains(SemanticFile.BEGIN_ROUTINE + name))
				{
					currentFile += code + "\n";
					line = bufferedReader.readLine();
					while (line != null)
					{
						if (line.contains(SemanticFile.END_ROUTINE + name))
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

			FileWriter fileWriter = new FileWriter(file);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println(currentFile);
			printWriter.close();

		}
		catch (Exception e)
		{
			Log.log(Log.ERROR, this, "Could not create new routine", e);
		}
	}

	public String getCleanCode(String routine, String code)
	{
		if (code == null)
		{
			code = file.getCode(routine);
			if (code == null)
			{
				return null;
			}
		}
		code = code.replaceFirst("(\\s*\\t*)*public void(\\s*\\t*)*" + routine + "(\\s*\\t*)*\\(\\)(\\s*\\t*)*\\{", "");
		code = code.replaceFirst("(\\s*\\t*)*\\n", "");
		if (code.lastIndexOf("}") >= 0)
			code = code.substring(0, code.lastIndexOf("}"));
		if (code.lastIndexOf("\n") >= 0)
			code = code.substring(0, code.lastIndexOf("\n"));
		String[] lines = code.split("\n");
		code = "";
		for (String line : lines)
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
			code = code.substring(0, code.lastIndexOf("\n"));
		return code;
	}

	public boolean InsertRoutine(String name, String code, String widgetName)
	{
		monitor.firePropertyChange("undoable", null, CommandFactory.createAddRoutineCommand());
		code = getFormatedCode(name, code);
		addToFile(name, code);
		return true;
	}

	/**
	 * Validates the content of the NewRoutineWizard form.
	 * 
	 * @return true if the content is in a valid format, false otherwise.
	 */
	public boolean isValid()
	{
		return true;
	}
}
