package org.ggll.view.component;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class EmptyComponent extends AbstractComponent
{
	public EmptyComponent()
	{
		this.jComponent = new JPanel(new BorderLayout());
		final JLabel label = new JLabel();
		this.jComponent.add(label, BorderLayout.CENTER);
	}

	@Override
	public void fireContentChanged()
	{
	}
}
