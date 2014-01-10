package org.ggll.window.toolbar;

import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.window.component.AbstractComponent;
import org.ggll.window.component.GrammarComponent;
import org.ggll.window.component.TextAreaComponent;

public class ToolBarFactory
{
	private final HashMap<AbstractComponent, JComponent> toolBars = new HashMap<AbstractComponent, JComponent>();
	private JComponent defaultToolBar;

	public ToolBarFactory()
	{
	}

	private ToolBarCanvas createToolBarCanvas(final SyntaxGraph canvas)
	{
		final ToolBarCanvas toolBarCanvas = new ToolBarCanvas(canvas);
		toolBarCanvas.setLayout(new BoxLayout(toolBarCanvas, BoxLayout.LINE_AXIS));
		return toolBarCanvas;
	}

	@SuppressWarnings("rawtypes")
	private JComponent createToolBarExt(final AbstractComponent component)
	{
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		if (component instanceof TextAreaComponent || component instanceof GrammarComponent)
		{
			final ToolBarDefault toolBarFile = createToolBarFile(component);
			panel.add(toolBarFile);
		}
		if (component instanceof GrammarComponent)
		{
			final GrammarComponent grammarComponent = (GrammarComponent) component;
			final ToolBarCanvas toolBarCanvas = createToolBarCanvas(grammarComponent.getCanvas());
			panel.add(toolBarCanvas);
		}
		return panel;

	}

	private ToolBarDefault createToolBarFile(final Object ref)
	{
		final ToolBarDefault toolBarFile = new ToolBarDefault(ref);
		toolBarFile.setLayout(new BoxLayout(toolBarFile, BoxLayout.LINE_AXIS));
		return toolBarFile;
	}

	private JComponent getDefaultToolBar(final AbstractComponent reference, ToolBarFactory toolBarFactory)
	{
		if (this.defaultToolBar == null)
		{
			this.defaultToolBar = toolBarFactory.createToolBarExt(reference);
		}
		return this.defaultToolBar;
	}

	@SuppressWarnings("rawtypes")
	public JComponent createToolBar(final AbstractComponent component, boolean enableToolBarFile, boolean enableToolBarCanvas)
	{
		final ToolBarFactory toolBarFactory = new ToolBarFactory();
		if (component == null)
		{
			return getDefaultToolBar(component, toolBarFactory);
		}
		if (!this.toolBars.containsKey(component))
		{
			this.toolBars.put(component, toolBarFactory.createToolBarExt(component));
		}
		return this.toolBars.get(component);
	}
}
