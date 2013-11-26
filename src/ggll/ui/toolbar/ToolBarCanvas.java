package ggll.ui.toolbar;

import ggll.ui.canvas.AbstractCanvas;
import ggll.ui.core.syntax.grammar.Controller;
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

public class ToolBarCanvas extends BaseToolBar
{
	private static final long serialVersionUID = 1L;
	private JButton[] buttons;

	String[] names;
	private JButton btnRun, btnZoomIn, btnZoomOut;
	private AbstractCanvas canvas;

	public ToolBarCanvas(AbstractCanvas canvas)
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
		JSeparator jSeparator = new JSeparator(SwingConstants.VERTICAL);
		jSeparator.setMaximumSize(new Dimension(6, 100));
		return jSeparator;
	}

	@Override
	protected void initActions()
	{
		btnZoomIn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
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
			public void actionPerformed(ActionEvent evt)
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
			public void actionPerformed(ActionEvent evt)
			{
				Controller.generateAndParseCurrentGrammar();
			}

		});

	}

	@Override
	protected void initComponets()
	{
		btnRun = new JButton(new ImageIcon(getClass().getResource(imgPath + "application-run.png")));
		btnZoomIn = new JButton(new ImageIcon(getClass().getResource(imgPath + "zoom-in.png")));
		btnZoomOut = new JButton(new ImageIcon(getClass().getResource(imgPath + "zoom-out.png")));
		buttons = new JButton[]{ btnRun, btnZoomIn, btnZoomOut };
		names = new String[]{ LangResource.build, LangResource.zoom_plus, LangResource.zoom_minus };
	}

	@Override
	protected void initLayout()
	{
		for (int i = 0; i < buttons.length; i++)
		{
			JButton button = buttons[i];
			button.setOpaque(false);
			button.setBorder(new EmptyBorder(5, 5, 5, 5));
			button.setRolloverEnabled(true);
			button.setSelectedIcon(new ImageIcon(ColorFilter.createDarkerImage(((ImageIcon) button.getIcon()).getImage())));
			button.setRolloverIcon(new ImageIcon(ColorFilter.createBrighterImage(((ImageIcon) button.getIcon()).getImage())));
			button.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) button.getIcon()).getImage())));
			button.setBackground(this.getBackground());
			button.setToolTipText(names[i]);
		}
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
}
