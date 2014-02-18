package org.ggll.window.component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.ggll.images.GGLLImages;
import org.ggll.output.Output;
import org.ggll.syntax.graph.SyntaxGraph;

public class OutputComponent extends AbstractComponent
{

	public OutputComponent(final SyntaxGraph canvas)
	{
		final JScrollPane jsp = new JScrollPane();
		final JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		this.jComponent = new JPanel(new BorderLayout());
		jsp.setViewportView(Output.getInstance().getView(canvas));
		final JButton clear = new JButton(new ImageIcon(getClass().getResource(GGLLImages.ICON_ERASE)));
		clear.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Output.getInstance().clear();
			}
		});
		clear.setBorder(new EmptyBorder(0, 0, 0, 0));
		clear.setRolloverEnabled(true);
		clear.setBackground(bar.getBackground());
		clear.setToolTipText("Clear All");
		final JButton output = new JButton(new ImageIcon(getClass().getResource(GGLLImages.ICON_OUTPUT)));
		output.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				jsp.setViewportView(Output.getInstance().getView(canvas));
			}
		});
		output.setBorder(new EmptyBorder(0, 0, 0, 0));
		output.setRolloverEnabled(true);
		output.setBackground(bar.getBackground());
		output.setToolTipText("Output");
		bar.add(output);
		bar.add(clear);
		this.jComponent.add(bar, BorderLayout.NORTH);
		this.jComponent.add(jsp, BorderLayout.CENTER);
	}

	@Override
	public void fireContentChanged()
	{
	}

}
