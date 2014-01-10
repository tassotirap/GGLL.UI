package org.ggll.window.component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.ggll.images.GGLLImages;
import org.ggll.parser.ParsingEditor;

public class ParserComponent extends AbstractComponent
{

	private JPanel btBar;
	private JPanel btBarRight;
	private JButton open;
	private JButton parse;
	private JButton parseNextStep;
	private TextAreaComponent textArea;

	public ParserComponent(String rootPath)
	{
		final ParsingEditor parser;
		createOpenButton();
		createParseButton();
		createNextStepButton();

		if (ParsingEditor.getInstance() == null)
		{
			parser = new ParsingEditor(null, rootPath);
		}
		else
		{
			parser = ParsingEditor.getInstance();
		}
		createListener(parser);
		createLayout(parser);
	}

	private void createLayout(final ParsingEditor parser)
	{
		this.btBarRight = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.btBarRight.add(this.open);
		this.btBarRight.add(this.parseNextStep);
		this.btBarRight.add(this.parse);
		this.btBar = new JPanel(new BorderLayout());
		this.btBar.add(this.btBarRight, BorderLayout.EAST);
		this.jComponent = new JPanel(new BorderLayout());
		this.jComponent.add(this.btBar, BorderLayout.NORTH);

		this.textArea = new TextAreaComponent();
		this.jComponent.add(this.textArea.getJComponent(), BorderLayout.CENTER);
	}

	private void createListener(final ParsingEditor parser)
	{
		this.open.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				open();
			}
		});
		this.parse.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				parser.run(false, ParserComponent.this.textArea.getText());
			}
		});
		this.parseNextStep.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				parser.stepRun(ParserComponent.this.textArea.getText());
			}
		});
	}

	private void createNextStepButton()
	{
		this.parseNextStep = new JButton(new ImageIcon(getClass().getResource(GGLLImages.ICON_PARSER_STEP)));
		this.parseNextStep.setOpaque(false);
		this.parseNextStep.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.parseNextStep.setRolloverEnabled(true);
		this.parseNextStep.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) this.parseNextStep.getIcon()).getImage())));
		this.parseNextStep.setToolTipText("Parse Next Step");
	}

	private void createOpenButton()
	{
		this.open = new JButton(new ImageIcon(getClass().getResource(GGLLImages.ICON_PARSER_OPEN)));
		this.open.setOpaque(false);
		this.open.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.open.setRolloverEnabled(true);
		this.open.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) this.open.getIcon()).getImage())));
		this.open.setToolTipText("Open File With Expression");
	}

	private void createParseButton()
	{
		this.parse = new JButton(new ImageIcon(getClass().getResource(GGLLImages.ICON_PARSER_PARSE)));
		this.parse.setOpaque(false);
		this.parse.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.parse.setRolloverEnabled(true);
		this.parse.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) this.parse.getIcon()).getImage())));
		this.parse.setToolTipText("Parse Expression");
	}

	@Override
	public void fireContentChanged()
	{
	}

	public TextAreaComponent getTextArea()
	{
		return this.textArea;
	}

	public void open()
	{
		final JFileChooser c = new JFileChooser();
		final int rVal = c.showOpenDialog(this.jComponent);
		if (rVal == JFileChooser.APPROVE_OPTION)
		{
			final File file = c.getSelectedFile();
			this.textArea.setText(file.getAbsolutePath(), true);
		}
		else if (rVal == JFileChooser.CANCEL_OPTION)
		{
			c.setVisible(false);
		}
	}
}
