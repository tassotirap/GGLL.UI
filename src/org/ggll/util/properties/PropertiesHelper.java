package org.ggll.util.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.ggll.util.Log;
import org.ggll.util.io.IOHelper;

public class PropertiesHelper
{
	
	/**
	 * Loads properties from a file
	 * 
	 * @param fileName
	 *            the name of the file containing the properties
	 * @param isXML
	 *            whether is a XML file or not
	 * **/
	public static Properties loadProperties(final String fileName, final boolean isXML)
	{
		final Properties props = new Properties();
		FileInputStream in = null;
		try
		{
			in = new FileInputStream(new File(fileName));
			if (!isXML)
			{
				props.load(in);
			}
			else
			{
				props.loadFromXML(in);
				
			}
		}
		catch (final InvalidPropertiesFormatException e)
		{
			
		}
		catch (final IOException e)
		{
			Log.write(e.getMessage());
		}
		finally
		{
			IOHelper.closeQuietly(in);
		}
		return props;
	}
	
	/**
	 * Store properties in a file
	 * 
	 * @param fileName
	 *            the file that will store the properties
	 * @param properties
	 *            the properties that will be stored
	 * @param comments
	 *            additional and optional comments
	 * @param isXML
	 *            whether is a XML file or not
	 */
	public static void storeProperties(final String fileName, final Properties properties, final String comments, final boolean isXML)
	{
		
		FileOutputStream out = null;
		try
		{
			out = new FileOutputStream(new File(fileName));
			if (!isXML)
			{
				properties.store(out, comments);
			}
			else
			{
				properties.storeToXML(out, comments, "UTF-8");
			}
		}
		catch (final IOException e)
		{
			Log.write(e.getMessage());
		}
		catch (final Exception e)
		{
			
		}
		finally
		{
			IOHelper.closeQuietly(out);
		}
	}
	
}
