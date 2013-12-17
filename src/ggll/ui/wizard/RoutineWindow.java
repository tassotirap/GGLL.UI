package ggll.ui.wizard;

import ggll.ui.canvas.Canvas;
import ggll.ui.canvas.widget.MarkedWidget;
import ggll.ui.core.semantics.SemanticFileManager;
import ggll.ui.director.GGLLDirector;
import ggll.ui.view.component.TextAreaComponent;

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
	
	private String widgetName;
	private MarkedWidget widget;
	private String routineName;
	private SemanticFileManager semFileManager;

	private Canvas canvas;
	
	
	public RoutineWindow(String widgetName, MarkedWidget widget, String routineName, Canvas canvas)
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
			btnInsert.setText("Edit");
			txtName.setEditable(false);
			if (semFileManager.getCleanCode(routineName, null) != null)
			{
				txtCode.getTextArea().setText(semFileManager.getCleanCode(routineName, null));
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
		btnInsert.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				String name = txtName.getText();
				String code = txtCode.getText();

				if (name.equals("") || code.equals(""))
					return;

				if (routineName != null || semFileManager.canInsert(name))
				{

					if (routineName != null)
					{
						semFileManager.editRoutine(name, code);
						widget.setMark(name);
					}
					else if (semFileManager.insertRoutine(name, code, widgetName))
					{
						widget.setMark(name);
						canvas.propertyChange(new PropertyChangeEvent(this, "undoable", null, "AddRoutine"));
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Could not create semantic routine.", "An Erro Occurred", JOptionPane.ERROR_MESSAGE);
					}
				}
				else if (!semFileManager.canInsert(name))
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
		
		btnCanvel.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
		
		txtCode.getTextArea().addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(FocusEvent arg0)
			{
				if (txtCode.getText().equals(INSERT_CODE_HERE))
				{
					txtCode.setText("", false);
				}
			}

			@Override
			public void focusLost(FocusEvent arg0)
			{
				if (txtCode.getText().equals(""))
				{
					txtCode.setText(INSERT_CODE_HERE, false);
				}
			}
		});
	}

	private JPanel getButtonsPanel()
	{
		if (pnlButtons == null)
		{
			pnlButtons = new JPanel();
			pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
			pnlButtons.add(getCancelButton());
			pnlButtons.add(getInsertButton());
		}
		return pnlButtons;
	}

	private JPanel getJContentPane()
	{
		if (pnlContent == null)
		{
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 2;
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.insets = new Insets(10, 10, 10, 0);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridwidth = 2;
			gridBagConstraints2.insets = new Insets(10, 10, 0, 10);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 0.9;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.insets = new Insets(10, 10, 0, 10);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(10, 10, 0, 0);
			lblName = new JLabel();
			lblName.setText("Routine name:");
			pnlContent = new JPanel();
			pnlContent.setLayout(new GridBagLayout());
			pnlContent.add(lblName, gridBagConstraints);
			pnlContent.add(getNameTextField(), gridBagConstraints1);
			pnlContent.add(getCodeTextArea(), gridBagConstraints2);
			pnlContent.add(getButtonsPanel(), gridBagConstraints4);
		}
		return pnlContent;
	}

	private void initialize()
	{
		this.setSize(550, 500);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dimension.width - 550) / 2, (dimension.height - 500) / 2);
		this.setContentPane(getJContentPane());
	}

	private JButton getCancelButton()
	{
		if (btnCanvel == null)
		{
			btnCanvel = new JButton();
			btnCanvel.setText("Cancel");
		}
		return btnCanvel;
	}

	private JTextArea getCodeTextArea()
	{
		if (txtCode == null)
		{
			txtCode = new TextAreaComponent();
			txtCode.setText(INSERT_CODE_HERE, false);

			txtCode.getTextArea().addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(FocusEvent arg0)
				{
					if (txtCode.getText().equals(INSERT_CODE_HERE))
					{
						txtCode.setText("", false);
					}
				}

				@Override
				public void focusLost(FocusEvent arg0)
				{
					if (txtCode.getText().equals(""))
					{
						txtCode.setText(INSERT_CODE_HERE, false);
					}
				}
			});
		}
		return txtCode.getTextArea();
	}

	private JButton getInsertButton()
	{
		if (btnInsert == null)
		{
			btnInsert = new JButton();
			btnInsert.setText("Insert");
		}
		return btnInsert;
	}

	private JTextField getNameTextField()
	{
		if (txtName == null)
		{
			txtName = new JTextField();
		}
		return txtName;
	}

}
