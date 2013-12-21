package ggll.ui.view.component;

import ggll.ui.canvas.Canvas;
import ggll.ui.images.GGLLImages;
import ggll.ui.output.Output;
import ggll.ui.output.SyntaxErrorOutput;
import ggll.ui.output.TokenOutput;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class OutputComponent extends AbstractComponent
{

	public OutputComponent(final Canvas canvas)
	{
		final JScrollPane jsp = new JScrollPane();
		final JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		final JButton lastToken = new JButton(new ImageIcon(getClass().getResource(GGLLImages.ICON_TOKEN)));
		this.jComponent = new JPanel(new BorderLayout());
		jsp.setViewportView(Output.getInstance().getView(canvas));
		lastToken.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				jsp.setViewportView(TokenOutput.getInstance().getView(canvas));
			}
		});
		lastToken.setBorder(new EmptyBorder(0, 0, 0, 0));
		lastToken.setRolloverEnabled(true);
		lastToken.setBackground(bar.getBackground());
		lastToken.setToolTipText("Tokens");
		final JButton errorReovery = new JButton(new ImageIcon(getClass().getResource(GGLLImages.ICON_ERROR_RECOVERY)));
		errorReovery.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				jsp.setViewportView(SyntaxErrorOutput.getInstance().getView(canvas));
			}
		});
		errorReovery.setBorder(new EmptyBorder(0, 0, 0, 0));
		errorReovery.setRolloverEnabled(true);
		errorReovery.setBackground(bar.getBackground());
		errorReovery.setToolTipText("Error Recovery");
		final JButton clear = new JButton(new ImageIcon(getClass().getResource(GGLLImages.ICON_ERASE)));
		clear.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Output.getInstance().clear();
				TokenOutput.getInstance().clear();
				SyntaxErrorOutput.getInstance().clear();
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
		final JButton saveReport = new JButton(new ImageIcon(getClass().getResource(GGLLImages.ICON_HTML_REPORT)));
		saveReport.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Output.getInstance().saveReport(bar);
			}
		});
		saveReport.setBorder(new EmptyBorder(0, 0, 0, 0));
		saveReport.setRolloverEnabled(true);
		saveReport.setBackground(bar.getBackground());
		saveReport.setToolTipText("Save an html report.");
		bar.add(output);
		bar.add(errorReovery);
		bar.add(lastToken);
		bar.add(clear);
		bar.add(saveReport);
		this.jComponent.add(bar, BorderLayout.NORTH);
		this.jComponent.add(jsp, BorderLayout.CENTER);
	}

	@Override
	public void fireContentChanged()
	{
	}

}
