package org.ggll.ui.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.ggll.canvas.Canvas;
import org.ggll.canvas.CanvasFactory;
import org.ggll.canvas.state.StaticStateManager;
import org.ggll.ui.toolbar.ToolBarGrammar;
import org.ggll.util.Log;

public class GrammarComponent extends AbstractComponent implements FileComponent, PropertyChangeListener
{

	String path;
	private Canvas canvas;

	public GrammarComponent(String fileName)
	{
		this(CanvasFactory.getCanvasFromFile(fileName));
	}

	public GrammarComponent(Canvas canvas)
	{
		path = CanvasFactory.getCanvasPath();
		this.canvas = canvas;
		JScrollPane jsp = new JScrollPane();
		JComponent view = canvas.createView();
		ToolBarGrammar toolBarGrammar = new ToolBarGrammar(canvas);
		toolBarGrammar.setLayout(new BoxLayout(toolBarGrammar, BoxLayout.PAGE_AXIS));
		JPanel canvasPanel = new JPanel();
		canvasPanel.setLayout(new BorderLayout());
		canvasPanel.add(toolBarGrammar, BorderLayout.WEST);
		jsp.setViewportView(view);
		canvasPanel.add(jsp, BorderLayout.CENTER);
		canvas.setPreferredSize(new Dimension(jsp.getWidth(), jsp.getHeight()));
		CanvasFactory.getVolatileStateManager().getMonitor().addPropertyChangeListener("writing", this);
		GrammarFactory.addGramComponent(this);
		jComponent = canvasPanel;
	}

	@Override
	public void fireContentChanged()
	{
		for (ComponentListener listener : listeners)
		{
			listener.ContentChanged(this, null, null);
		}
	}

	@Override
	public String getPath()
	{
		return path;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals("writing"))
		{
			fireContentChanged();
		}
	}

	@Override
	public void saveFile()
	{
		StaticStateManager staticStateManager = canvas.getStaticStateManager();
		try
		{
			staticStateManager.write();
		}
		catch (IOException e)
		{
			Log.log(Log.ERROR, this, "Could not save file!", e);
		}
	}

	public void setPath(String path)
	{
		canvas = CanvasFactory.getCanvasFromFile(path);
		this.path = path;
	}
}
