package org.ggll.util.print;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.RepaintManager;

import org.netbeans.api.visual.widget.Widget;

/**
 * prints an java.awt.Component, or a org.netbeans.api.visual.widget
 * 
 * @author Gustavo
 * 
 */
public class ComponentPrinter implements Printable
{
	
	public static void disableDoubleBuffering(final Component c)
	{
		final RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}
	
	public static void enableDoubleBuffering(final Component c)
	{
		final RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}
	
	public static void printComponent(final Component c)
	{
		new ComponentPrinter(c).print();
	}
	
	public static void printWidget(final Widget w)
	{
		new ComponentPrinter(w).print();
	}
	
	private Component componentToBePrinted;
	
	private Widget widgetToBePrinted;
	
	public ComponentPrinter(final Component componentToBePrinted)
	{
		this.componentToBePrinted = componentToBePrinted;
	}
	
	public ComponentPrinter(final Widget widgetToBePrinted)
	{
		this.widgetToBePrinted = widgetToBePrinted;
	}
	
	public void print()
	{
		final PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this);
		if (printJob.printDialog())
		{
			try
			{
				printJob.print();
			}
			catch (final PrinterException pe)
			{
				System.out.println("Error printing: " + pe);
			}
		}
	}
	
	@Override
	public int print(final Graphics g, final PageFormat pageFormat, final int pageIndex)
	{
		if (pageIndex > 0)
		{
			return Printable.NO_SUCH_PAGE;
		}
		else
		{
			final Graphics2D g2d = (Graphics2D) g;
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			ComponentPrinter.disableDoubleBuffering(componentToBePrinted);
			if (componentToBePrinted != null)
			{
				componentToBePrinted.paint(g2d);
			}
			else if (widgetToBePrinted != null)
			{
				widgetToBePrinted.getScene().paint(g2d);
			}
			ComponentPrinter.enableDoubleBuffering(componentToBePrinted);
			return Printable.PAGE_EXISTS;
		}
	}
}
