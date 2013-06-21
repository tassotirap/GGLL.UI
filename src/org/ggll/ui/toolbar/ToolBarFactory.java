package org.ggll.ui.toolbar;

import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.ggll.canvas.Canvas;
import org.ggll.ui.component.AdapterComponent;

public class ToolBarFactory
{
	private HashMap<Object, JComponent> toolBars = new HashMap<Object, JComponent>();
	private JComponent defaultToolBar;

	public ToolBarFactory()
	{
	}

	private ToolBarCanvas createToolBarCanvas(final Canvas canvas)
	{
		ToolBarCanvas toolBarCanvas = new ToolBarCanvas(canvas);
		toolBarCanvas.setLayout(new BoxLayout(toolBarCanvas, BoxLayout.LINE_AXIS));
		return toolBarCanvas;
	}

	@SuppressWarnings("rawtypes")
	private JComponent createToolBarExt(final Object acContextHolder, boolean enableToolBarFile, boolean enableToolBarCanvas)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		if (enableToolBarFile)
		{
			ToolBarDefault toolBarFile = createToolBarFile(acContextHolder);
			panel.add(toolBarFile);
		}
		if (enableToolBarCanvas)
		{
			ToolBarCanvas toolBarCanvas = createToolBarCanvas((Canvas) acContextHolder);
			panel.add(toolBarCanvas);
		}
		return panel;

	}

	private ToolBarDefault createToolBarFile(final Object ref)
	{
		ToolBarDefault toolBarFile = new ToolBarDefault(ref);
		toolBarFile.setLayout(new BoxLayout(toolBarFile, BoxLayout.LINE_AXIS));
		return toolBarFile;
	}


	@SuppressWarnings("rawtypes")
	public JComponent createToolBar(final Object reference, boolean enableToolBarFile, boolean enableToolBarCanvas)
	{
		ToolBarFactory toolBarFactory = new ToolBarFactory();
		if (reference == null)
		{
			if (defaultToolBar == null)
			{
				defaultToolBar = toolBarFactory.createToolBarExt(reference, false, false);
				return defaultToolBar;
			}
		}
		if (!toolBars.containsKey(reference) || reference instanceof AdapterComponent)
		{
			toolBars.put(reference, toolBarFactory.createToolBarExt(reference, enableToolBarFile, enableToolBarCanvas));
		}
		return toolBars.get(reference);
	}
}
