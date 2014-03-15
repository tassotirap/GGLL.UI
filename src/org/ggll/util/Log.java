package org.ggll.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Log
{
	public static void write(final String message)
	{
		try
		{
			final FileWriter fw = new FileWriter("ggll.log", true);
			final BufferedWriter bw = new BufferedWriter(fw);
			bw.append("Log: " + new Date().toString() + " - " + message);
			bw.newLine();
			bw.close();
			fw.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
}
