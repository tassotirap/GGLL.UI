/*
 * IOUtilities.java - IO related functions
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2006 Matthieu Casanova
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.ggll.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

import org.ggll.util.Log;

/**
 * IO tools that depends on JDK only.
 * 
 * @author Matthieu Casanova
 * @version $Id$
 * @since 4.3pre5
 */
public class IOHelper
{
	/**
	 * Method that will close an {@link java.io.Closeable} ignoring it if it is
	 * null and ignoring exceptions.
	 * 
	 * @param closeable
	 *            the closeable to close.
	 * @since jEdit 4.3pre8
	 */
	public static void closeQuietly(final Closeable closeable)
	{
		if (closeable != null)
		{
			try
			{
				closeable.close();
			}
			catch (final IOException e)
			{
				// ignore
			}
		}
	} // }}}
	
	// {{{ closeQuietly() methods
	/**
	 * Method that will close an {@link InputStream} ignoring it if it is null
	 * and ignoring exceptions.
	 * 
	 * @param in
	 *            the InputStream to close.
	 */
	public static void closeQuietly(final InputStream in)
	{
		if (in != null)
		{
			try
			{
				in.close();
			}
			catch (final IOException e)
			{
				// ignore
			}
		}
	}
	
	/**
	 * Method that will close an {@link OutputStream} ignoring it if it is null
	 * and ignoring exceptions.
	 * 
	 * @param out
	 *            the OutputStream to close.
	 */
	public static void closeQuietly(final OutputStream out)
	{
		if (out != null)
		{
			try
			{
				out.close();
			}
			catch (final IOException e)
			{
				// ignore
			}
		}
	}
	
	/**
	 * Method that will close an {@link Reader} ignoring it if it is null and
	 * ignoring exceptions.
	 * 
	 * @param r
	 *            the Reader to close.
	 * @since jEdit 4.3pre5
	 */
	public static void closeQuietly(final Reader r)
	{
		if (r != null)
		{
			try
			{
				r.close();
			}
			catch (final IOException e)
			{
				// ignore
			}
		}
	}
	
	// {{{ copyFile() method
	/**
	 * Copies the source file to the destination.
	 * 
	 * If the destination cannot be created or is a read-only file, the method
	 * returns <code>false</code>. Otherwise, the contents of the source are
	 * copied to the destination, and <code>true</code> is returned.
	 * 
	 * @param source
	 *            The source file to copy.
	 * @param dest
	 *            The destination where to copy the file.
	 * @return true on success, false otherwise.
	 * 
	 * @since jEdit 4.3pre9
	 */
	public static boolean copyFile(final File source, final File dest)
	{
		boolean ok = false;
		
		if (dest.exists() && dest.canWrite() || !dest.exists() && dest.getParentFile().canWrite())
		{
			OutputStream fos = null;
			InputStream fis = null;
			try
			{
				fos = new FileOutputStream(dest);
				fis = new FileInputStream(source);
				ok = IOHelper.copyStream(32768, fis, fos, false);
			}
			catch (final IOException ioe)
			{
				Log.Write(ioe.getMessage());
			}
			finally
			{
				IOHelper.closeQuietly(fos);
				IOHelper.closeQuietly(fis);
			}
		}
		return ok;
	} // }}}
	
	public static void copyFileAndUpdateString(final File source, final File dest, final String string, final String string2)
	{
		if (dest.exists() && dest.canWrite() || !dest.exists() && dest.getParentFile().canWrite())
		{
			OutputStream fos = null;
			InputStream fis = null;
			InputStreamReader isr = null;
			OutputStreamWriter osw = null;
			BufferedReader br = null;
			try
			{
				fos = new FileOutputStream(dest);
				fis = new FileInputStream(source);
				isr = new InputStreamReader(fis);
				osw = new OutputStreamWriter(fos);
				br = new BufferedReader(isr);
				String line;
				final StringBuffer sb = new StringBuffer();
				while ((line = br.readLine()) != null)
				{
					line = line.replace(string, string2);
					sb.append(line + "\n");
				}
				osw.write(sb.toString());
			}
			catch (final IOException ioe)
			{
				Log.Write(ioe.getMessage());
			}
			finally
			{
				IOHelper.closeQuietly(br);
				IOHelper.closeQuietly(isr);
				IOHelper.closeQuietly(osw);
				IOHelper.closeQuietly(fos);
				IOHelper.closeQuietly(fis);
			}
		}
		
	}
	
	public static boolean copyFileFromInputSteam(final InputStream in, final File dest)
	{
		boolean ok = false;
		
		if (dest.exists() && dest.canWrite() || !dest.exists() && dest.getParentFile().canWrite())
		{
			OutputStream fos = null;
			final InputStream fis = null;
			try
			{
				fos = new FileOutputStream(dest);
				ok = IOHelper.copyStream(32768, in, fos, false);
			}
			catch (final IOException ioe)
			{
				Log.Write(ioe.getMessage());
			}
			finally
			{
				IOHelper.closeQuietly(fos);
				IOHelper.closeQuietly(fis);
			}
		}
		return ok;
	}
	
	/**
	 * Copy an input stream to an output stream with a buffer of 4096 bytes.
	 * 
	 * @param progress
	 *            the progress observer it could be null
	 * @param in
	 *            the input stream
	 * @param out
	 *            the output stream
	 * @param canStop
	 *            if true, the copy can be stopped by interrupting the thread
	 * @return <code>true</code> if the copy was done, <code>false</code> if it
	 *         was interrupted
	 * @throws IOException
	 *             IOException If an I/O error occurs
	 */
	public static boolean copyStream(final InputStream in, final OutputStream out, final boolean canStop) throws IOException
	{
		return IOHelper.copyStream(4096, in, out, canStop);
	} // }}}
	
	// {{{ copyStream() methods
	/**
	 * Copy an input stream to an output stream.
	 * 
	 * @param bufferSize
	 *            the size of the buffer
	 * @param progress
	 *            the progress observer it could be null
	 * @param in
	 *            the input stream
	 * @param out
	 *            the output stream
	 * @param canStop
	 *            if true, the copy can be stopped by interrupting the thread
	 * @return <code>true</code> if the copy was done, <code>false</code> if it
	 *         was interrupted
	 * @throws IOException
	 *             IOException If an I/O error occurs
	 */
	public static boolean copyStream(final int bufferSize, final InputStream in, final OutputStream out, final boolean canStop) throws IOException
	{
		final byte[] buffer = new byte[bufferSize];
		int n;
		while (-1 != (n = in.read(buffer)))
		{
			out.write(buffer, 0, n);
			if (canStop && Thread.interrupted()) { return false; }
		}
		return true;
	}
	
	// {{{ fileLength() method
	/**
	 * Returns the length of a file. If it is a directory it will calculate
	 * recursively the length.
	 * 
	 * @param file
	 *            the file or directory
	 * @return the length of the file or directory. If the file doesn't exist it
	 *         will return 0
	 * @since 4.3pre10
	 */
	public static long fileLength(final File file)
	{
		long length = 0L;
		if (file.isFile())
		{
			length = file.length();
		}
		else if (file.isDirectory())
		{
			final File[] files = file.listFiles();
			for (final File file2 : files)
			{
				length += IOHelper.fileLength(file2);
			}
		}
		return length;
	} // }}}
	
	// {{{ moveFile() method
	/**
	 * Moves the source file to the destination.
	 * 
	 * If the destination cannot be created or is a read-only file, the method
	 * returns <code>false</code>. Otherwise, the contents of the source are
	 * copied to the destination, the source is deleted, and <code>true</code>
	 * is returned.
	 * 
	 * @param source
	 *            The source file to move.
	 * @param dest
	 *            The destination where to move the file.
	 * @return true on success, false otherwise.
	 * 
	 * @since jEdit 4.3pre9
	 */
	public static boolean moveFile(final File source, final File dest)
	{
		boolean ok = false;
		
		if (dest.exists() && dest.canWrite() || !dest.exists() && dest.getParentFile().canWrite())
		{
			OutputStream fos = null;
			InputStream fis = null;
			try
			{
				fos = new FileOutputStream(dest);
				fis = new FileInputStream(source);
				ok = IOHelper.copyStream(32768, fis, fos, false);
			}
			catch (final IOException ioe)
			{
				Log.Write(ioe.getMessage());
			}
			finally
			{
				IOHelper.closeQuietly(fos);
				IOHelper.closeQuietly(fis);
			}
			
			if (ok)
			{
				source.delete();
			}
		}
		return ok;
	} // }}}
	
	public static String readInputStreamAsString(final InputStream in) throws IOException
	{
		
		final BufferedInputStream bis = new BufferedInputStream(in);
		final ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result = bis.read();
		while (result != -1)
		{
			final byte b = (byte) result;
			buf.write(b);
			result = bis.read();
		}
		return buf.toString();
	}
	
	// {{{ IOUtilities() constructor
	private IOHelper()
	{
	} // }}}
}
