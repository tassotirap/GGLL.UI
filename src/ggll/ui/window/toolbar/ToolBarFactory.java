package ggll.ui.window.toolbar;

import ggll.ui.canvas.Canvas;
import ggll.ui.view.component.AbstractComponent;
import ggll.ui.view.component.GrammarComponent;
import ggll.ui.view.component.TextAreaComponent;

import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ToolBarFactory
{
	private HashMap<AbstractComponent, JComponent> toolBars = new HashMap<AbstractComponent, JComponent>();
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
	private JComponent createToolBarExt(final AbstractComponent component)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		if (component instanceof TextAreaComponent || component instanceof GrammarComponent)
		{
			ToolBarDefault toolBarFile = createToolBarFile(component);
			panel.add(toolBarFile);
		}
		if (component instanceof GrammarComponent)
		{
			GrammarComponent grammarComponent = (GrammarComponent) component;
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

	private JComponent getDefaultToolBar(final AbstractComponent reference, ToolBarFactory toolBarFactory)
	{
		if (defaultToolBar == null)
		{
			defaultToolBar = toolBarFactory.createToolBarExt(reference);
		}
		return defaultToolBar;
	}

	@SuppressWarnings("rawtypes")
	public JComponent createToolBar(final AbstractComponent component, boolean enableToolBarFile, boolean enableToolBarCanvas)
	{
		ToolBarFactory toolBarFactory = new ToolBarFactory();
		if (component == null)
		{
			return getDefaultToolBar(component, toolBarFactory);
		}
		if (!toolBars.containsKey(component))
		{
			toolBars.put(component, toolBarFactory.createToolBarExt(component));
		}
		return toolBars.get(component);
	}
}
