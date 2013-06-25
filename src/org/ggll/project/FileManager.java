package org.ggll.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.ggll.canvas.Canvas;
import org.ggll.model.FileNames;
import org.ggll.model.ui.IconFactory;
import org.ggll.model.ui.IconFactory.IconType;
import org.ggll.ui.MainWindow;
import org.ggll.ui.TabWindowList.TabPlace;
import org.ggll.ui.component.AbstractComponent;
import org.ggll.ui.component.FileComponent;
import org.ggll.ui.component.GrammarComponent;
import org.ggll.ui.component.GrammarFactory;
import org.ggll.ui.component.NewTextArea;
import org.ggll.ui.component.TextAreaRepo;
import org.ggll.util.Log;
import org.ggll.view.GGLLView;
import org.ggll.view.UnsavedViewManager;

public class FileManager
{
	private Project project;
	private MainWindow mainWindow;
	private UnsavedViewManager viewManager;

	public FileManager()
	{
		this.project = GGLLManager.getProject();
		this.mainWindow = GGLLManager.getMainWindow();
		this.viewManager = GGLLManager.getUnsavedViewManager();
	}

	public void closeFile(String fileName)
	{
		for (File file : project.getOpenedFiles())
		{
			if (file.getAbsolutePath().equals(fileName))
			{
				project.getOpenedFiles().remove(file);
				break;
			}
		}
		project.save();
	}

	public void createFile(String name, FileNames extension) throws IOException
	{
		if (extension.getExtension().equals(FileNames.GRAM_EXTENSION))
		{
			if (project.getGrammarFile() != null)
			{
				JOptionPane.showMessageDialog(null, "Only one grammar file is allowed by project.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else if (extension.getExtension().equals(FileNames.SEM_EXTENSION))
		{
			if (project.getSemanticFile() != null)
			{
				JOptionPane.showMessageDialog(null, "Only one semantic routines file is allowed by project.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else if (extension.getExtension().equals(FileNames.LEX_EXTENSION))
		{
			if (project.getLexicalFile() != null)
			{
				JOptionPane.showMessageDialog(null, "Only one lexical scanner file is allowed by project.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else if (extension.getExtension().equals(FileNames.OUT_EXTENSION))
		{
			if (project.getLexicalFile() != null)
			{
				JOptionPane.showMessageDialog(null, "This kind of file is not supported yet", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			File baseDir = project.getProjectDir();
			File newFile = null;
			if (extension != null)
			{
				if (!name.endsWith(extension.getExtension()))
				{
					name += extension.getExtension();
				}
			}
			if (!name.startsWith(baseDir.getAbsolutePath()))
			{
				newFile = new File(baseDir, name);
			}
			else
			{
				newFile = new File(name);
			}
			try
			{
				newFile.createNewFile();
				openFile(newFile.getAbsolutePath());
			}
			catch (SecurityException e)
			{
				JOptionPane.showMessageDialog(null, "Could not create file. Probably you do not have permission to write on disk.", "Could not create file", JOptionPane.INFORMATION_MESSAGE);
				Log.log(Log.WARNING, mainWindow, "A security exception was thrown while trying to create a new file.", e);
			}
		}
	}

	public boolean isFileOpen(String fileName)
	{
		for (File file : project.getOpenedFiles())
		{
			if (file.getAbsolutePath().equals(fileName))
				return true;
		}
		return false;
	}

	public void openFile(String path)
	{
		IconFactory iconFactory = new IconFactory();
		File file = new File(path);
		if (!isFileOpen(path))
		{
			try
			{
				if (file.getName().toLowerCase().endsWith(FileNames.LEX_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent(new NewTextArea(path), file.getName(), path, iconFactory.getIcon(IconType.LEX_ICON), TabPlace.CENTER_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.SEM_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent( new NewTextArea(path), file.getName(), path, iconFactory.getIcon(IconType.SEM_ICON), TabPlace.CENTER_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.GRAM_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent(new GrammarComponent(path), file.getName(), path, iconFactory.getIcon(IconType.GRAM_ICON), TabPlace.CENTER_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.XML_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent( new NewTextArea(path), file.getName(), path, iconFactory.getIcon(IconType.XML_ICON), TabPlace.CENTER_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.JAVA_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent( new NewTextArea(path), file.getName(), path, iconFactory.getIcon(IconType.JAVA_ICON), TabPlace.CENTER_TABS);
				}
				else
				{
					mainWindow.addComponent(new NewTextArea(path), file.getName(), path, iconFactory.getIcon(IconType.TXT_ICON), TabPlace.CENTER_TABS);
				}
				project.getOpenedFiles().add(new File(path));

				project.save();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public void saveAllFiles(ArrayList<GGLLView> views)
	{
		for (GGLLView dynamicView : views)
		{
			AbstractComponent abstractComponent = dynamicView.getComponentModel();
			if (abstractComponent instanceof NewTextArea)
			{
				NewTextArea advancedTextAreaComponent = (NewTextArea) abstractComponent;
				saveFileObject(TextAreaRepo.getComponent(advancedTextAreaComponent.getTextArea()));
			}
			if (abstractComponent instanceof GrammarComponent)
			{
				saveFileObject(abstractComponent);
			}
		}
	}

	public void saveFileObject(Object object)
	{
		String path = null;
		boolean componentSaved = false;
		if (object != null)
		{
			if (object instanceof NewTextArea)
			{
				path = saveFile((NewTextArea) object);
				componentSaved = true;
			}
			else if (object instanceof FileComponent)
			{
				path = saveFile((FileComponent) object);
				componentSaved = true;
			}
			else if (object instanceof Canvas)
			{
				path = saveFile((Canvas) object);
				componentSaved = true;
			}
			else if (object instanceof GrammarComponent)
			{
				path = saveFile((GrammarComponent) object);
			}
			else if (object instanceof String)
			{
				path = (String) object;
			}
		}

		if (path == null)
			return;

		if (project != null)
		{
			if (mainWindow != null && !componentSaved)
			{
				for (GGLLView dynamicView : viewManager.getUnsavedViews())
				{
					AbstractComponent comp = dynamicView.getComponentModel();
					if (comp instanceof FileComponent && ((FileComponent) comp).getPath().equals(path))
					{
						((FileComponent) comp).saveFile();
					}
				}
			}
			if (mainWindow != null)
			{
				mainWindow.setSaved(path);
			}
			project.save();
		}
	}

	private String saveFile(NewTextArea object)
	{
		if (object != null)
		{
			object.saveFile();
			return object.getPath();
		}
		return null;
	}

	private String saveFile(FileComponent fileComponent)
	{
		fileComponent.saveFile();
		return fileComponent.getPath();
	}

	private String saveFile(Canvas canvas)
	{
		GrammarComponent grammarComponent = GrammarFactory.getGrammarComponent();
		if (viewManager.hasUnsavedView(grammarComponent.getPath()))
		{
			grammarComponent.saveFile();
			return grammarComponent.getPath();
		}
		return null;
	}

	private String saveFile(GrammarComponent grammarComponent)
	{
		grammarComponent.saveFile();
		return grammarComponent.getPath();
	}

}
