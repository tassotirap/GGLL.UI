package org.ggll.window.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.SyntaxGraphRepository;
import org.ggll.syntax.graph.state.State;
import org.ggll.util.Log;
import org.ggll.window.toolbar.ToolBarGrammar;

public class GrammarComponent extends AbstractFileComponent implements PropertyChangeListener
{
	String path;
	private SyntaxGraph canvas;
	
	public GrammarComponent(final String fileName)
	{
		this(SyntaxGraphRepository.getInstance(fileName));
	}
	
	public GrammarComponent(final SyntaxGraph canvas)
	{
		path = canvas.getFile();
		
		this.canvas = canvas;
		final JComponent view = canvas.createView();
		
		final JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(view);
		
		final ToolBarGrammar toolBarGrammar = new ToolBarGrammar(canvas);
		toolBarGrammar.setLayout(new BoxLayout(toolBarGrammar, BoxLayout.PAGE_AXIS));
		
		final JPanel canvasPanel = new JPanel();
		canvasPanel.setLayout(new BorderLayout());
		canvasPanel.add(toolBarGrammar, BorderLayout.WEST);
		canvasPanel.add(jsp, BorderLayout.CENTER);
		
		canvas.getMonitor().addPropertyChangeListener("object_state", this);
		canvas.getMonitor().addPropertyChangeListener("writing", this);
		
		GrammarFactory.addGramComponent(this, canvas.getFile());
		view.setSize(new Dimension(1000, 1000));
		jComponent = canvasPanel;
	}
	
	@Override
	public void fireContentChanged()
	{
		for (final ComponentListener listener : listeners.getAll())
		{
			listener.ContentChanged(this);
		}
	}
	
	public SyntaxGraph getCanvas()
	{
		return canvas;
	}
	
	@Override
	public String getPath()
	{
		return path;
	}
	
	@Override
	public void propertyChange(final PropertyChangeEvent evt)
	{
		fireContentChanged();
	}
	
	@Override
	public String saveFile()
	{
		final State canvasState = canvas.getCanvasState();
		try
		{
			canvasState.write();
		}
		catch (final IOException e)
		{
			Log.write("Could not save file!");
		}
		return getPath();
	}
	
	public void setPath(final String path)
	{
		canvas = SyntaxGraphRepository.getInstance(path);
		this.path = path;
	}
}
