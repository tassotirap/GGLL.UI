package ggll.ui.view.component;

import ggll.ui.canvas.Canvas;
import ggll.ui.canvas.CanvasRepository;
import ggll.ui.canvas.state.CanvasState;
import ggll.ui.toolbar.ToolBarGrammar;
import ggll.ui.util.Log;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GrammarComponent extends AbstractFileComponent implements PropertyChangeListener
{
	String path;
	private Canvas canvas;

	public GrammarComponent(Canvas canvas)
	{
		path = canvas.getFile();

		this.canvas = canvas;
		JComponent view = canvas.createView();

		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(view);

		JPanel canvasPanel = new JPanel();

		ToolBarGrammar toolBarGrammar = new ToolBarGrammar(canvas);
		toolBarGrammar.setLayout(new BoxLayout(toolBarGrammar, BoxLayout.PAGE_AXIS));

		canvasPanel.setLayout(new BorderLayout());
		canvasPanel.add(toolBarGrammar, BorderLayout.WEST);
		canvasPanel.add(jsp, BorderLayout.CENTER);

		canvas.setPreferredSize(new Dimension(jsp.getWidth(), jsp.getHeight()));
		canvas.getMonitor().addPropertyChangeListener("object_state", this);
		canvas.getMonitor().addPropertyChangeListener("writing", this);

		GrammarFactory.addGramComponent(this, canvas.getFile());
		jComponent = canvasPanel;
	}

	public GrammarComponent(String fileName)
	{
		this(CanvasRepository.getInstance(fileName));
	}

	@Override
	public void fireContentChanged()
	{
		for (ComponentListener listener : listeners.getAll())
		{
			listener.ContentChanged(this);
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
		fireContentChanged();
	}

	@Override
	public String saveFile()
	{
		CanvasState canvasState = canvas.getCurrentCanvasState();
		try
		{
			canvasState.write();
		}
		catch (IOException e)
		{
			Log.log(Log.ERROR, this, "Could not save file!", e);
		}
		return getPath();
	}

	public void setPath(String path)
	{
		canvas = CanvasRepository.getInstance(path);
		this.path = path;
	}

	public Canvas getCanvas()
	{
		return canvas;
	}
}
