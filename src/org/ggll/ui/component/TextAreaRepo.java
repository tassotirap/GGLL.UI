package org.ggll.ui.component;

import java.util.HashMap;

import javax.swing.JTextArea;

public class TextAreaRepo
{
	private static HashMap<JTextArea, FileComponent> componentByTextArea = new HashMap<JTextArea, FileComponent>();
	private static HashMap<AbstractComponent, JTextArea> textAreaByComponent = new HashMap<AbstractComponent, JTextArea>();

	public static FileComponent getComponent(JTextArea ta)
	{
		if (componentByTextArea.containsKey(ta))
		{
			return componentByTextArea.get(ta);
		}
		return null;
	}

	public static JTextArea getTextArea(AbstractComponent comp)
	{
		if (textAreaByComponent.containsKey(comp))
		{
			return textAreaByComponent.get(comp);
		}
		return null;
	}

	public static void register(AbstractComponent component, JTextArea textArea)
	{
		textAreaByComponent.put(component, textArea);
		if (component instanceof FileComponent)
		{
			componentByTextArea.put(textArea, (FileComponent) component);
		}
	}

	public static void remove(AbstractComponent comp)
	{
		if (textAreaByComponent.containsKey(comp))
		{
			JTextArea ta = textAreaByComponent.get(comp);
			textAreaByComponent.remove(comp);
			componentByTextArea.remove(ta);
		}
	}

	public static void remove(JTextArea ta)
	{
		if (componentByTextArea.containsKey(ta))
		{
			FileComponent comp = componentByTextArea.get(ta);
			componentByTextArea.remove(ta);
			textAreaByComponent.remove(comp);
		}
	}

}
