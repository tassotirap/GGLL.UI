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

import org.ggll.images.ImageResource;
import org.ggll.resource.CanvasResource;
import org.ggll.resource.LangResource;
import org.ggll.syntax.graph.SyntaxGraph;

public class ToolBarGrammar extends BaseToolBar
{
	private static final long serialVersionUID = 1L;
	
	JButton[] buttons;
	String[] names;
	SyntaxGraph canvas;
	private JButton btnNTerminal, btnTerminal, btnLambdaAlternative, btnStart;
	private JButton btnSelect, btnSucessor, btnAlternative, btnLeftHand,
			btnLabel;
	
	public ToolBarGrammar(final SyntaxGraph canvas)
	{
		super(canvas);
		this.canvas = canvas;
		for (int i = 0; i < buttons.length; i++)
		{
			buttons[i].setName(names[i]);
		}
		this.add(btnSelect);
		this.add(createJSeparator());
		this.add(btnSucessor);
		this.add(btnAlternative);
		this.add(createJSeparator());
		this.add(btnStart);
		this.add(btnLeftHand);
		this.add(btnNTerminal);
		this.add(btnTerminal);
		this.add(btnLambdaAlternative);
		this.add(createJSeparator());
		this.add(btnLabel);
	}
	
	private JSeparator createJSeparator()
	{
		final JSeparator jSeparator = new JSeparator(SwingConstants.HORIZONTAL);
		jSeparator.setMaximumSize(new Dimension(100, 6));
		return jSeparator;
	}
	
	@Override
	protected void initActions()
	{
		
		btnSelect.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				canvas.setActiveTool(CanvasResource.SELECT);
			}
			
		});
		btnSucessor.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				canvas.setActiveTool(CanvasResource.SUCCESSOR);
			}
			
		});
		btnAlternative.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				canvas.setActiveTool(CanvasResource.ALTERNATIVE);
			}
			
		});
		btnLeftHand.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				canvas.setActiveTool(CanvasResource.LEFT_SIDE);
			}
			
		});
		btnNTerminal.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				canvas.setActiveTool(CanvasResource.N_TERMINAL);
			}
			
		});
		btnTerminal.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				canvas.setActiveTool(CanvasResource.TERMINAL);
			}
			
		});
		btnLambdaAlternative.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				canvas.setActiveTool(CanvasResource.LAMBDA);
			}
			
		});
		btnLabel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				canvas.setActiveTool(CanvasResource.LABEL);
			}
			
		});
		btnStart.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				canvas.setActiveTool(CanvasResource.START);
			}
			
		});
	}
	
	@Override
	protected void initComponets()
	{
		btnSelect = new JButton(new ImageIcon(ImageResource.imagePath + "select.png"));
		btnSucessor = new JButton(new ImageIcon(ImageResource.imagePath + "successor.png"));
		btnAlternative = new JButton(new ImageIcon(ImageResource.imagePath + "alternative.png"));
		btnLeftHand = new JButton(new ImageIcon(ImageResource.imagePath + "left_hand.png"));
		btnNTerminal = new JButton(new ImageIcon(ImageResource.imagePath + "icon_nt.png"));
		btnTerminal = new JButton(new ImageIcon(ImageResource.imagePath + "icon_t.png"));
		btnLambdaAlternative = new JButton(new ImageIcon(ImageResource.imagePath + "icon_l.png"));
		btnLabel = new JButton(new ImageIcon(ImageResource.imagePath + "label.png"));
		btnStart = new JButton(new ImageIcon(ImageResource.imagePath + "icon_s.png"));
		
		buttons = new JButton[]
		{ btnSelect, btnSucessor, btnAlternative, btnLeftHand, btnNTerminal, btnTerminal, btnLambdaAlternative, btnLabel, btnStart };
		names = new String[]
		{ LangResource.select, LangResource.successor, LangResource.alternative, LangResource.left_hand, LangResource.n_terminal, LangResource.terminal, LangResource.lambda_alternative, LangResource.label, LangResource.start };
	}
	
	@Override
	protected void initLayout()
	{
		for (int i = 0; i < buttons.length; i++)
		{
			final JButton bt = buttons[i];
			bt.setOpaque(false);
			bt.setBorder(new EmptyBorder(1, 1, 1, 1));
			bt.setRolloverEnabled(true);
			bt.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) bt.getIcon()).getImage())));
			bt.setBackground(getBackground());
			bt.setToolTipText(names[i]);
		}
	}
}