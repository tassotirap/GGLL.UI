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

import org.ggll.grammar.GrammarParser;
import org.ggll.images.ImageResource;
import org.ggll.resource.LangResource;
import org.ggll.syntax.graph.SyntaxGraph;

public class ToolBarCanvas extends BaseToolBar
{
	private static final long serialVersionUID = 1L;
	private JButton[] buttons;
	
	String[] names;
	private JButton btnRun, btnZoomIn, btnZoomOut;
	private final SyntaxGraph canvas;
	
	public ToolBarCanvas(final SyntaxGraph canvas)
	{
		super(canvas);
		this.canvas = canvas;
		for (int i = 0; i < buttons.length; i++)
		{
			buttons[i].setName(names[i]);
		}
		this.add(btnRun);
		this.add(createJSeparator());
		this.add(btnZoomIn);
		this.add(btnZoomOut);
	}
	
	private JSeparator createJSeparator()
	{
		final JSeparator jSeparator = new JSeparator(SwingConstants.VERTICAL);
		jSeparator.setMaximumSize(new Dimension(6, 100));
		return jSeparator;
	}
	
	public JButton getBtnRun()
	{
		return btnRun;
	}
	
	public JButton getBtnZoomIn()
	{
		return btnZoomIn;
	}
	
	public JButton getBtnZoomOut()
	{
		return btnZoomOut;
	}
	
	public JButton[] getButtons()
	{
		return buttons;
	}
	
	public String[] getNames()
	{
		return names;
	}
	
	@Override
	protected void initActions()
	{
		btnZoomIn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				if (canvas.canZoomIn())
				{
					canvas.setZoomFactor(canvas.getZoomFactor() * 1.1);
					canvas.validate();
				}
			}
			
		});
		
		btnZoomOut.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				if (canvas.canZoomOut())
				{
					canvas.setZoomFactor(canvas.getZoomFactor() / 1.1);
					canvas.validate();
				}
			}
			
		});
		
		btnRun.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				final GrammarParser syntaxParser = new GrammarParser();
				syntaxParser.parseGrammar();
			}
		});
		
	}
	
	@Override
	protected void initComponets()
	{
		btnRun = new JButton(new ImageIcon(ImageResource.imagePath + "application-run.png"));
		btnZoomIn = new JButton(new ImageIcon(ImageResource.imagePath + "zoom-in.png"));
		btnZoomOut = new JButton(new ImageIcon(ImageResource.imagePath + "zoom-out.png"));
		buttons = new JButton[]
		{ btnRun, btnZoomIn, btnZoomOut };
		names = new String[]
		{ LangResource.build, LangResource.zoom_plus, LangResource.zoom_minus };
	}
	
	@Override
	protected void initLayout()
	{
		for (int i = 0; i < buttons.length; i++)
		{
			final JButton button = buttons[i];
			button.setOpaque(false);
			button.setBorder(new EmptyBorder(5, 5, 5, 5));
			button.setRolloverEnabled(true);
			button.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) button.getIcon()).getImage())));
			button.setBackground(getBackground());
			button.setToolTipText(names[i]);
		}
	}
}
