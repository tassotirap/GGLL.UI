package org.ggll.ui.component;

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

import org.ggll.parser.ParsingEditor;

import com.jidesoft.icons.ColorFilter;

public class ParserComponent extends AbstractComponent
{

	public final static String ICONS_PATH = "/org/ggll/images/";
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
	
	public void open()
	{
		JFileChooser c = new JFileChooser();
		int rVal = c.showOpenDialog(jComponent);
		if (rVal == JFileChooser.APPROVE_OPTION)
		{
			File file = c.getSelectedFile();
			textArea.setText(file.getAbsolutePath(), true);
		}
		else if (rVal == JFileChooser.CANCEL_OPTION)
		{
			c.setVisible(false);
		}
	}

	private void createLayout(final ParsingEditor parser)
	{
		btBarRight = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btBarRight.add(open);
		btBarRight.add(parseNextStep);
		btBarRight.add(parse);
		btBar = new JPanel(new BorderLayout());
		btBar.add(btBarRight, BorderLayout.EAST);
		jComponent = new JPanel(new BorderLayout());
		jComponent.add(btBar, BorderLayout.NORTH);

		textArea = new TextAreaComponent();
		jComponent.add(textArea.getJComponent(), BorderLayout.CENTER);
	}

	private void createListener(final ParsingEditor parser)
	{
		open.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				open();
			}
		});
		parse.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				parser.run(false, textArea.getText());
			}
		});
		parseNextStep.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				parser.stepRun(textArea.getText());
			}
		});
	}

	private void createNextStepButton()
	{
		parseNextStep = new JButton(new ImageIcon(getClass().getResource(ICONS_PATH + "parser-step.png")));
		parseNextStep.setOpaque(false);
		parseNextStep.setBorder(new EmptyBorder(0, 0, 0, 0));
		parseNextStep.setRolloverEnabled(true);
		parseNextStep.setSelectedIcon(new ImageIcon(ColorFilter.createDarkerImage(((ImageIcon) parseNextStep.getIcon()).getImage())));
		parseNextStep.setRolloverIcon(new ImageIcon(ColorFilter.createBrighterImage(((ImageIcon) parseNextStep.getIcon()).getImage())));
		parseNextStep.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) parseNextStep.getIcon()).getImage())));
		parseNextStep.setToolTipText("Parse Next Step");
	}

	private void createOpenButton()
	{
		open = new JButton(new ImageIcon(getClass().getResource(ICONS_PATH + "parser-open.png")));
		open.setOpaque(false);
		open.setBorder(new EmptyBorder(0, 0, 0, 0));
		open.setRolloverEnabled(true);
		open.setSelectedIcon(new ImageIcon(ColorFilter.createDarkerImage(((ImageIcon) open.getIcon()).getImage())));
		open.setRolloverIcon(new ImageIcon(ColorFilter.createBrighterImage(((ImageIcon) open.getIcon()).getImage())));
		open.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) open.getIcon()).getImage())));
		open.setToolTipText("Open File With Expression");
	}

	private void createParseButton()
	{
		parse = new JButton(new ImageIcon(getClass().getResource(ICONS_PATH + "parser-parse.png")));
		parse.setOpaque(false);
		parse.setBorder(new EmptyBorder(0, 0, 0, 0));
		parse.setRolloverEnabled(true);
		parse.setSelectedIcon(new ImageIcon(ColorFilter.createDarkerImage(((ImageIcon) parse.getIcon()).getImage())));
		parse.setRolloverIcon(new ImageIcon(ColorFilter.createBrighterImage(((ImageIcon) parse.getIcon()).getImage())));
		parse.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) parse.getIcon()).getImage())));
		parse.setToolTipText("Parse Expression");
	}

	@Override
	public void fireContentChanged()
	{
	}

	public TextAreaComponent getTextArea()
	{
		return textArea;
	}
}
