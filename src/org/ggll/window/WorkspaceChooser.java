package org.ggll.window;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.ggll.exceptions.WarningException;
import org.ggll.project.ProjectHelper;
import org.ggll.util.Log;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
 * 
 * @author Gusga
 * @author Tasso Tirapani Silva Pinto Refactory: OK
 */
public class WorkspaceChooser extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String LIST_FILE = "workspace";
	private static final String PROJECTS_SCREEN_PNG = "/org/ggll/images/projects_screen.png";

	private JButton btnBrowse;
	private JButton btnCancel;
	private JButton btnOk;
	private JComboBox<String> ckbWorkspace;
	private JLabel imgWorkspace;
	private JLabel lblWorkspace;
	private String workspaceDir;

	private boolean canceled;
	private boolean done;

	public WorkspaceChooser()
	{
		setPanelProperties();
		setPanelLocation();
		initComponents();
		readDirsFromList();
	}

	private void addDirToList(String filename)
	{
		final File file = new File(System.getProperty("java.io.tmpdir"), LIST_FILE);

		try
		{
			if (!file.exists())
			{
				file.createNewFile();
			}
			final FileReader fileReader = new FileReader(file);
			final BufferedReader bufferedReader = new BufferedReader(fileReader);

			final StringBuffer oldText = new StringBuffer();

			String line = "";
			while ((line = bufferedReader.readLine()) != null)
			{
				if (!line.equalsIgnoreCase(filename))
				{
					oldText.append(line + "\n");
				}
			}
			bufferedReader.close();
			final PrintWriter printWriter = new PrintWriter(file);
			printWriter.println(filename);
			printWriter.print(oldText.toString());
			printWriter.close();
		}
		catch (final IOException e)
		{
			Log.log(Log.ERROR, this, "Could not load workspace list!", e);
		}
	}

	private void btnBrowseActionPerformed()
	{
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final int dialogReturn = fileChooser.showOpenDialog(this);
		if (dialogReturn == JFileChooser.APPROVE_OPTION)
		{
			this.ckbWorkspace.setSelectedItem(fileChooser.getSelectedFile().getAbsolutePath());
		}
		if (dialogReturn == JFileChooser.CANCEL_OPTION)
		{
			this.ckbWorkspace.setSelectedItem("");
		}
	}

	private void btnCancelActionPerformed()
	{
		dispose();
		this.canceled = true;
	}

	private void btnOkActionPerformed()
	{
		final String directory = this.ckbWorkspace.getSelectedItem().toString();
		final File file = new File(directory);
		boolean newDirectory = false;
		try
		{
			if (!directory.equals(""))
			{
				if (file.exists())
				{
					setupProject(directory, file);
				}
				else
				{
					newDirectory = createDirectory(directory, file);
					if (newDirectory)
					{
						setupProject(directory, file);
					}
				}

			}
		}
		catch (final WarningException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage(), "Worksapce Loader", JOptionPane.ERROR_MESSAGE);

			if (newDirectory)
			{
				file.delete();
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	private boolean createDirectory(String directory, File file) throws Exception
	{
		final int option = JOptionPane.showConfirmDialog(this, "This directory does not exist.\nIf you continue this directory will be created and a new project will be created on this directory.\nProceed?", "Directory not found", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (option == JOptionPane.NO_OPTION)
		{
			return false;
		}
		if (file.mkdir())
		{
			return true;
		}
		else
		{
			throw new WarningException("Could not find or create this directory in disk!");
		}
	}

	private void initComponents()
	{
		this.imgWorkspace = new JLabel();
		this.lblWorkspace = new JLabel();
		this.ckbWorkspace = new JComboBox<String>();
		this.btnBrowse = new JButton();
		this.btnCancel = new JButton();
		this.btnOk = new javax.swing.JButton();

		this.imgWorkspace.setIcon(new ImageIcon(getClass().getResource(PROJECTS_SCREEN_PNG))); // NOI18N
		this.lblWorkspace.setText("Please inform a workspace to continue:");
		this.btnBrowse.setText("Browse");

		this.btnBrowse.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				btnBrowseActionPerformed();
			}
		});

		this.btnCancel.setText("OK");
		this.btnCancel.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				btnOkActionPerformed();
			}
		});

		this.btnOk.setText("Cancel");
		this.btnOk.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				btnCancelActionPerformed();
			}
		});

		this.ckbWorkspace.setEditable(true);

		final GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(GroupLayout.TRAILING).add(this.ckbWorkspace, GroupLayout.PREFERRED_SIZE, 305, GroupLayout.PREFERRED_SIZE).add(this.btnCancel, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(layout.createParallelGroup(GroupLayout.TRAILING).add(this.btnOk, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE).add(this.btnBrowse, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)).addContainerGap()).add(this.imgWorkspace).add(layout.createSequentialGroup().addContainerGap().add(this.lblWorkspace)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(layout.createSequentialGroup().add(this.imgWorkspace).addPreferredGap(LayoutStyle.UNRELATED).add(this.lblWorkspace).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(GroupLayout.BASELINE).add(this.ckbWorkspace, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(this.btnBrowse)).addPreferredGap(LayoutStyle.RELATED).add(layout.createParallelGroup(GroupLayout.BASELINE).add(this.btnOk).add(this.btnCancel)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		getContentPane().setLayout(layout);
		pack();
	}

	private void readDirsFromList()
	{
		final File file = new File(System.getProperty("java.io.tmpdir"), LIST_FILE);
		try
		{
			if (!file.exists())
			{
				file.createNewFile();
			}
			final FileReader fileReader = new FileReader(file);
			final BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = "";
			while ((line = bufferedReader.readLine()) != null)
			{
				if (!line.equals(""))
				{
					this.ckbWorkspace.addItem(line);
				}
			}
			bufferedReader.close();
		}
		catch (final IOException e)
		{
			Log.log(Log.ERROR, this, "Could not load workspace list!", e);
		}
	}

	private void setPanelLocation()
	{
		final Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenDim.width - 428) / 2, (screenDim.height - 230) / 2);
		setResizable(false);
	}

	private void setPanelProperties()
	{
		setTitle("GGLL");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		setSize(428, 230);
	}

	private void setupProject(String directory, File file) throws Exception
	{
		this.workspaceDir = directory;
		verifyOrCreateProject(file);
		addDirToList(directory);
		setVisible(false);
		this.done = true;
	}

	private void verifyOrCreateProject(File file) throws Exception
	{
		if (!ProjectHelper.isProject(file))
		{
			if (file.listFiles().length > 0)
			{
				throw new WarningException("Must be a new, empty, or existing project directory!");
			}
			ProjectHelper.createNewProject(file);
		}
	}

	public String getWorkspaceDir()
	{
		return this.workspaceDir;
	}

	public boolean isCanceled()
	{
		return this.canceled;
	}

	public boolean isDone()
	{
		return this.done;
	}

}
