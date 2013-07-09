package ggll.project;

import ggll.canvas.Canvas;
import ggll.file.FileNames;
import ggll.ui.MainWindow;
import ggll.ui.TabWindowList.TabPlace;
import ggll.ui.component.AbstractComponent;
import ggll.ui.component.FileComponent;
import ggll.ui.component.GrammarComponent;
import ggll.ui.component.GrammarFactory;
import ggll.ui.component.TextAreaComponent;
import ggll.ui.model.IconFactory;
import ggll.ui.model.IconFactory.IconType;
import ggll.util.Log;
import ggll.view.AbstractView;
import ggll.view.UnsavedViewRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class FileManager
{
	private Project project;
	private MainWindow mainWindow;
	private UnsavedViewRepository viewManager;

	public FileManager()
	{
		this.project = GGLLManager.getProject();
		this.mainWindow = GGLLManager.getMainWindow();
		this.viewManager = GGLLManager.getUnsavedViewManager();
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

	private String saveFile(FileComponent fileComponent)
	{
		fileComponent.saveFile();
		return fileComponent.getPath();
	}

	private String saveFile(GrammarComponent grammarComponent)
	{
		grammarComponent.saveFile();
		return grammarComponent.getPath();
	}

	private String saveFile(TextAreaComponent object)
	{
		if (object != null)
		{
			object.saveFile();
			return object.getPath();
		}
		return null;
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
					mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.LEX_ICON), TabPlace.CENTER_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.SEM_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.SEM_ICON), TabPlace.CENTER_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.GRAM_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent(new GrammarComponent(path), file.getName(), path, iconFactory.getIcon(IconType.GRAM_ICON), TabPlace.CENTER_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.XML_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.XML_ICON), TabPlace.CENTER_TABS);
				}
				else if (file.getName().toLowerCase().endsWith(FileNames.JAVA_EXTENSION.toLowerCase()))
				{
					mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.JAVA_ICON), TabPlace.CENTER_TABS);
				}
				else
				{
					mainWindow.addComponent(new TextAreaComponent(path), file.getName(), path, iconFactory.getIcon(IconType.TXT_ICON), TabPlace.CENTER_TABS);
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

	public void saveAllFiles(ArrayList<AbstractView> views)
	{
		for (AbstractView dynamicView : views)
		{
			AbstractComponent abstractComponent = dynamicView.getComponentModel();
			saveFileObject(abstractComponent);
		}
	}

	public void saveFileObject(Object object)
	{
		String path = null;
		boolean componentSaved = false;
		if (object != null)
		{
			if (object instanceof TextAreaComponent)
			{
				path = saveFile((TextAreaComponent) object);
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
				for (AbstractView dynamicView : viewManager.getUnsavedViews())
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

}