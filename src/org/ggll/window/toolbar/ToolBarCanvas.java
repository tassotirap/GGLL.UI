package org.ggll.window.toolbar;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.ggll.parser.syntax.grammar.Controller;
import org.ggll.resource.LangResource;
import org.ggll.syntax.graph.SyntaxGraph;

public class ToolBarCanvas extends BaseToolBar
{
	private static final long serialVersionUID = 1L;
	private JButton[] buttons;

	String[] names;
	private JButton btnRun, btnZoomIn, btnZoomOut;
	private final SyntaxGraph canvas;

	public ToolBarCanvas(SyntaxGraph canvas)
	{
		super(canvas);
		this.canvas = canvas;
		for (int i = 0; i < this.buttons.length; i++)
		{
			this.buttons[i].setName(this.names[i]);
		}
		this.add(this.btnRun);
		this.add(createJSeparator());
		this.add(this.btnZoomIn);
		this.add(this.btnZoomOut);
	}

	private JSeparator createJSeparator()
	{
		final JSeparator jSeparator = new JSeparator(SwingConstants.VERTICAL);
		jSeparator.setMaximumSize(new Dimension(6, 100));
		return jSeparator;
	}

	@Override
	protected void initActions()
	{
		this.btnZoomIn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				if (ToolBarCanvas.this.canvas.canZoomIn())
				{
					ToolBarCanvas.this.canvas.setZoomFactor(ToolBarCanvas.this.canvas.getZoomFactor() * 1.1);
					ToolBarCanvas.this.canvas.validate();
				}
			}

		});

		this.btnZoomOut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				if (ToolBarCanvas.this.canvas.canZoomOut())
				{
					ToolBarCanvas.this.canvas.setZoomFactor(ToolBarCanvas.this.canvas.getZoomFactor() / 1.1);
					ToolBarCanvas.this.canvas.validate();
				}
			}

		});

		this.btnRun.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				Controller.generateAndParseCurrentGrammar();
			}

		});

	}

	@Override
	protected void initComponets()
	{
		this.btnRun = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "application-run.png")));
		this.btnZoomIn = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "zoom-in.png")));
		this.btnZoomOut = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "zoom-out.png")));
		this.buttons = new JButton[]{ this.btnRun, this.btnZoomIn, this.btnZoomOut };
		this.names = new String[]{ LangResource.build, LangResource.zoom_plus, LangResource.zoom_minus };
	}

	@Override
	protected void initLayout()
	{
		for (int i = 0; i < this.buttons.length; i++)
		{
			final JButton button = this.buttons[i];
			button.setOpaque(false);
			button.setBorder(new EmptyBorder(5, 5, 5, 5));
			button.setRolloverEnabled(true);
			button.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) button.getIcon()).getImage())));
			button.setBackground(getBackground());
			button.setToolTipText(this.names[i]);
		}
	}

	public JButton getBtnRun()
	{
		return this.btnRun;
	}

	public JButton getBtnZoomIn()
	{
		return this.btnZoomIn;
	}

	public JButton getBtnZoomOut()
	{
		return this.btnZoomOut;
	}

	public JButton[] getButtons()
	{
		return this.buttons;
	}

	public String[] getNames()
	{
		return this.names;
	}
}
