package org.ggll.util.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class ClipboardHelper
{
	
	private static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	/**
	 * Gets whatever is in clipboard since it is a WidgetSelection or
	 * StringSelection
	 * 
	 * @return the contents of the clipboard
	 */
	public static Object getClipboardContents()
	{
		Object result = null;
		final Transferable contents = ClipboardHelper.clipboard.getContents(null);
		if (contents != null)
		{
			try
			{
				if (contents.isDataFlavorSupported(DataFlavor.stringFlavor))
				{
					result = contents.getTransferData(DataFlavor.stringFlavor);
				}
				else
				{
					result = contents.getTransferData(null);
				}
			}
			catch (final Exception e)
			{
			}
		}
		return result;
		
	}
	
	/**
	 * Set the content of the clipboard
	 * 
	 * @param contents
	 * @param owner
	 *            the object that is sending the content
	 */
	public static void setClipboardContents(final Transferable contents, final ClipboardOwner owner)
	{
		ClipboardHelper.clipboard.setContents(contents, owner);
	}
}
