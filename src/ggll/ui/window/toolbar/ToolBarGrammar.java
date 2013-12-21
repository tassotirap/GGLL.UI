package ggll.ui.window.toolbar;

import ggll.ui.canvas.Canvas;
import ggll.ui.resource.CanvasResource;
import ggll.ui.resource.LangResource;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.jidesoft.icons.ColorFilter;

public class ToolBarGrammar extends BaseToolBar
{
	private static final long serialVersionUID = 1L;

	JButton[] buttons;
	String[] names;
	Canvas canvas;
	private JButton btnNTerminal, btnTerminal, btnLambdaAlternative, btnStart;
	private JButton btnSelect, btnSucessor, btnAlternative, btnLeftHand, btnLabel;

	public ToolBarGrammar(Canvas canvas)
	{
		super(canvas);
		this.canvas = canvas;
		for (int i = 0; i < this.buttons.length; i++)
		{
			this.buttons[i].setName(this.names[i]);
		}
		this.add(this.btnSelect);
		this.add(createJSeparator());
		this.add(this.btnSucessor);
		this.add(this.btnAlternative);
		this.add(createJSeparator());
		this.add(this.btnStart);
		this.add(this.btnLeftHand);
		this.add(this.btnNTerminal);
		this.add(this.btnTerminal);
		this.add(this.btnLambdaAlternative);
		this.add(createJSeparator());
		this.add(this.btnLabel);
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

		this.btnSelect.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				ToolBarGrammar.this.canvas.setActiveTool(CanvasResource.SELECT);
			}

		});
		this.btnSucessor.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				ToolBarGrammar.this.canvas.setActiveTool(CanvasResource.SUCCESSOR);
			}

		});
		this.btnAlternative.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				ToolBarGrammar.this.canvas.setActiveTool(CanvasResource.ALTERNATIVE);
			}

		});
		this.btnLeftHand.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				ToolBarGrammar.this.canvas.setActiveTool(CanvasResource.LEFT_SIDE);
			}

		});
		this.btnNTerminal.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				ToolBarGrammar.this.canvas.setActiveTool(CanvasResource.N_TERMINAL);
			}

		});
		this.btnTerminal.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				ToolBarGrammar.this.canvas.setActiveTool(CanvasResource.TERMINAL);
			}

		});
		this.btnLambdaAlternative.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				ToolBarGrammar.this.canvas.setActiveTool(CanvasResource.LAMBDA);
			}

		});
		this.btnLabel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				ToolBarGrammar.this.canvas.setActiveTool(CanvasResource.LABEL);
			}

		});
		this.btnStart.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				ToolBarGrammar.this.canvas.setActiveTool(CanvasResource.START);
			}

		});
	}

	@Override
	protected void initComponets()
	{
		this.btnSelect = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "select.png")));
		this.btnSucessor = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "successor.png")));
		this.btnAlternative = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "alternative.png")));
		this.btnLeftHand = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "left_hand.png")));
		this.btnNTerminal = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "icon_nt.png")));
		this.btnTerminal = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "icon_t.png")));
		this.btnLambdaAlternative = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "icon_l.png")));
		this.btnLabel = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "label.png")));
		this.btnStart = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "icon_s.png")));

		this.buttons = new JButton[]{ this.btnSelect, this.btnSucessor, this.btnAlternative, this.btnLeftHand, this.btnNTerminal, this.btnTerminal, this.btnLambdaAlternative, this.btnLabel, this.btnStart };
		this.names = new String[]{ LangResource.select, LangResource.successor, LangResource.alternative, LangResource.left_hand, LangResource.n_terminal, LangResource.terminal, LangResource.lambda_alternative, LangResource.label, LangResource.start };
	}

	@Override
	protected void initLayout()
	{
		for (int i = 0; i < this.buttons.length; i++)
		{
			final JButton bt = this.buttons[i];
			bt.setOpaque(false);
			bt.setBorder(new EmptyBorder(1, 1, 1, 1));
			bt.setRolloverEnabled(true);
			bt.setSelectedIcon(new ImageIcon(ColorFilter.createDarkerImage(((ImageIcon) bt.getIcon()).getImage())));
			bt.setRolloverIcon(new ImageIcon(ColorFilter.createBrighterImage(((ImageIcon) bt.getIcon()).getImage())));
			bt.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) bt.getIcon()).getImage())));
			bt.setBackground(getBackground());
			bt.setToolTipText(this.names[i]);
		}
	}
}