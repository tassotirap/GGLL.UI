package org.ggll.window;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.ggll.director.GGLLDirector;
import org.ggll.parser.semantics.SemanticFileManager;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.widget.MarkedWidget;
import org.ggll.window.component.TextAreaComponent;

public class RoutineWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String INSERT_CODE_HERE = "/* insert code here */";

	private JButton btnCanvel = null;
	private JButton btnInsert = null;
	private JPanel pnlContent = null;
	private JPanel pnlButtons = null;
	private JLabel lblName = null;
	private JTextField txtName = null;
	private TextAreaComponent txtCode = null;

	private final String widgetName;
	private final MarkedWidget widget;
	private final String routineName;
	private final SemanticFileManager semFileManager;

	private final SyntaxGraph canvas;

	public RoutineWindow(String widgetName, MarkedWidget widget, String routineName, SyntaxGraph canvas)
	{
		super();
		this.widgetName = widgetName;
		this.widget = widget;
		this.routineName = routineName;
		this.canvas = canvas;
		this.semFileManager = new SemanticFileManager(GGLLDirector.getProject().getSemanticFile());
		initialize();
		addListener();

		if (routineName != null)
		{
			setTitle("Edit " + routineName);
			this.btnInsert.setText("Edit");
			this.txtName.setEditable(false);
			if (this.semFileManager.getCleanCode(routineName, null) != null)
			{
				this.txtCode.getTextArea().setText(this.semFileManager.getCleanCode(routineName, null));
			}
			getNameTextField().setText(routineName);
		}
		else
		{
			setTitle("Create new semantic routine");
		}
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setVisible(true);
	}

	private void addListener()
	{
		this.btnInsert.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				final String name = RoutineWindow.this.txtName.getText();
				final String code = RoutineWindow.this.txtCode.getText();

				if (name.equals("") || code.equals(""))
				{
					return;
				}

				if (RoutineWindow.this.routineName != null || RoutineWindow.this.semFileManager.canInsert(name))
				{

					if (RoutineWindow.this.routineName != null)
					{
						RoutineWindow.this.semFileManager.editRoutine(name, code);
						RoutineWindow.this.widget.setMark(name);
					}
					else if (RoutineWindow.this.semFileManager.insertRoutine(name, code, RoutineWindow.this.widgetName))
					{
						RoutineWindow.this.widget.setMark(name);
						RoutineWindow.this.canvas.propertyChange(new PropertyChangeEvent(this, "undoable", null, "AddRoutine"));
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Could not create semantic routine.", "An Erro Occurred", JOptionPane.ERROR_MESSAGE);
					}
				}
				else if (!RoutineWindow.this.semFileManager.canInsert(name))
				{
					JOptionPane.showMessageDialog(null, "This semantic routine alrealdy existis in the file.", "An Erro Occurred", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "The semantic routines file is invalid.", "An Erro Occurred", JOptionPane.ERROR_MESSAGE);
				}
				setVisible(false);
			}
		});

		this.btnCanvel.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});

		this.txtCode.getTextArea().addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(FocusEvent arg0)
			{
				if (RoutineWindow.this.txtCode.getText().equals(INSERT_CODE_HERE))
				{
					RoutineWindow.this.txtCode.setText("", false);
				}
			}

			@Override
			public void focusLost(FocusEvent arg0)
			{
				if (RoutineWindow.this.txtCode.getText().equals(""))
				{
					RoutineWindow.this.txtCode.setText(INSERT_CODE_HERE, false);
				}
			}
		});
	}

	private JPanel getButtonsPanel()
	{
		if (this.pnlButtons == null)
		{
			this.pnlButtons = new JPanel();
			this.pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
			this.pnlButtons.add(getCancelButton());
			this.pnlButtons.add(getInsertButton());
		}
		return this.pnlButtons;
	}

	private JButton getCancelButton()
	{
		if (this.btnCanvel == null)
		{
			this.btnCanvel = new JButton();
			this.btnCanvel.setText("Cancel");
		}
		return this.btnCanvel;
	}

	private JTextArea getCodeTextArea()
	{
		if (this.txtCode == null)
		{
			this.txtCode = new TextAreaComponent();
			this.txtCode.setText(INSERT_CODE_HERE, false);

			this.txtCode.getTextArea().addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(FocusEvent arg0)
				{
					if (RoutineWindow.this.txtCode.getText().equals(INSERT_CODE_HERE))
					{
						RoutineWindow.this.txtCode.setText("", false);
					}
				}

				@Override
				public void focusLost(FocusEvent arg0)
				{
					if (RoutineWindow.this.txtCode.getText().equals(""))
					{
						RoutineWindow.this.txtCode.setText(INSERT_CODE_HERE, false);
					}
				}
			});
		}
		return this.txtCode.getTextArea();
	}

	private JButton getInsertButton()
	{
		if (this.btnInsert == null)
		{
			this.btnInsert = new JButton();
			this.btnInsert.setText("Insert");
		}
		return this.btnInsert;
	}

	private JPanel getJContentPane()
	{
		if (this.pnlContent == null)
		{
			final GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 2;
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.insets = new Insets(10, 10, 10, 0);
			final GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridwidth = 2;
			gridBagConstraints2.insets = new Insets(10, 10, 0, 10);
			final GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 0.9;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.insets = new Insets(10, 10, 0, 10);
			final GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(10, 10, 0, 0);
			this.lblName = new JLabel();
			this.lblName.setText("Routine name:");
			this.pnlContent = new JPanel();
			this.pnlContent.setLayout(new GridBagLayout());
			this.pnlContent.add(this.lblName, gridBagConstraints);
			this.pnlContent.add(getNameTextField(), gridBagConstraints1);
			this.pnlContent.add(getCodeTextArea(), gridBagConstraints2);
			this.pnlContent.add(getButtonsPanel(), gridBagConstraints4);
		}
		return this.pnlContent;
	}

	private JTextField getNameTextField()
	{
		if (this.txtName == null)
		{
			this.txtName = new JTextField();
		}
		return this.txtName;
	}

	private void initialize()
	{
		this.setSize(550, 500);
		final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dimension.width - 550) / 2, (dimension.height - 500) / 2);
		setContentPane(getJContentPane());
	}

}
