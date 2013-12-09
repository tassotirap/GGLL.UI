package ggll.ui.toolbar;

import ggll.ui.canvas.Canvas;
import ggll.ui.view.component.AbstractComponent;
import ggll.ui.view.component.GrammarComponent;

import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

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
			GrammarComponent grammarComponent = (GrammarComponent)acContextHolder;
			ToolBarCanvas toolBarCanvas = createToolBarCanvas(grammarComponent.getCanvas());
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
	public JComponent createToolBar(final AbstractComponent reference, boolean enableToolBarFile, boolean enableToolBarCanvas)
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
		if (!toolBars.containsKey(reference))
		{
			toolBars.put(reference, toolBarFactory.createToolBarExt(reference, enableToolBarFile, enableToolBarCanvas));
		}
		return toolBars.get(reference);
	}
}
