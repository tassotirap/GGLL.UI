package org.ggll.facade;

import ggll.core.list.ExtendedList;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.ggll.file.FileManager;
import org.ggll.file.GrammarFile;
import org.ggll.file.LexicalFile;
import org.ggll.file.SemanticFile;
import org.ggll.project.Project;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.util.print.ComponentPrinter;
import org.ggll.util.print.TextPrinter;
import org.ggll.window.MainWindow;
import org.ggll.window.component.AbstractComponent;
import org.ggll.window.component.GrammarComponent;
import org.ggll.window.component.TextAreaComponent;
import org.ggll.window.view.AbstractView;
import org.ggll.window.view.UnsavedViewRepository;

public final class GGLLFacade implements IGGLLFacade
{
	public static IGGLLFacade getInstance()
	{
		return GGLLFacade.instance;
	}
	
	public static void start(final Project project)
	{
		GGLLFacade.instance = new GGLLFacade();
		GGLLFacade.instance.project = project;
		GGLLFacade.instance.unViewManager = new UnsavedViewRepository();
		GGLLFacade.instance.fileManager = new FileManager();
	}
	
	private Project project;
	private FileManager fileManager;
	
	private UnsavedViewRepository unViewManager;
	
	
	
	private static GGLLFacade instance;
	
	private GGLLFacade()
	{
	}
	

	@Override
	public void closeFile(final String fileName)
	{
		fileManager.closeFile(fileName);
	}
	
	@Override
	public void createFile(final String name, final String extension) throws IOException
	{
		fileManager.createFile(name, extension);
	}
	
	@Override
	public void exit()
	{
		final ExtendedList<AbstractView> unsavedViews = unViewManager.getUnsavedViews();
		
		for (final AbstractView dynamicView : unsavedViews.getAll())
		{
			final int option = JOptionPane.showConfirmDialog(MainWindow.getInstance().getFrame(), "Would you like to save '" + dynamicView.getTitle().replace(MainWindow.UNSAVED_PREFIX, "") + "' before exiting?");
			if (option == JOptionPane.CANCEL_OPTION)
			{
				return;
			}
			else if (option == JOptionPane.YES_OPTION)
			{
				saveFile(dynamicView.getComponentModel());
			}
		}
		System.exit(0);
	}
	
	@Override
	public SyntaxGraph getActiveSyntaxGraph()
	{
		return project.getActiveCanvas();
	}
	
	@Override
	public ExtendedList<GrammarFile> getGrammarFile()
	{
		return project.getGrammarFile();
	}
	
	@Override
	public LexicalFile getLexicalFile()
	{
		return project.getLexicalFile();
	}
	
	
	@Override
	public ExtendedList<File> getOpenedFiles()
	{
		final ExtendedList<File> filesOpened = project.getOpenedFiles();
		if (filesOpened.count() == 0)
		{
			project.getOpenedFiles().append(project.getGrammarFile().get(0));
		}
		return filesOpened;
	}
	
	@Override
	public File getProjectDir()
	{
		return project.getProjectDir();
	}
	
	@Override
	public String getProjectsRootPath()
	{
		return project.getProjectsRootPath();
	}
	
	@Override
	public SemanticFile getSemanticFile()
	{
		return project.getSemanticFile();
	}
	
	@Override
	public AbstractView getUnsavedView(final String file)
	{
		return unViewManager.getUnsavedView(file);
	}
	
	@Override
	public boolean hasUnsavedView(final AbstractView view)
	{
		return unViewManager.hasUnsavedView(view);
	}
	
	@Override
	public boolean hasUnsavedView(final String file)
	{
		return unViewManager.hasUnsavedView(file);
	}
	
	@Override
	public boolean isFileOpen(final String absolutePath)
	{
		return fileManager.isFileOpen(absolutePath);
	}
	
	@Override
	public void openFile(final String path)
	{
		fileManager.openFile(path);
	}
	
	@Override
	public void openFile(final String path, final boolean verifyOpen)
	{
		fileManager.openFile(path, verifyOpen);
	}
	
	@Override
	public void print(final Object object)
	{
		if (object instanceof TextAreaComponent)
		{
			TextPrinter.printText(((TextAreaComponent) object).getText());
		}
		else if (object instanceof GrammarComponent)
		{
			final GrammarComponent grammarComponent = (GrammarComponent) object;
			ComponentPrinter.printWidget(grammarComponent.getCanvas());
		}
	}
	
	@Override
	public void removeUnsavedView(final String path)
	{
		unViewManager.removeUnsavedView(path);
	}
	
	@Override
	public void saveAllFiles()
	{
		fileManager.saveAllFiles(unViewManager.getUnsavedViews());
	}
	
	@Override
	public void saveFile(final AbstractComponent object)
	{
		fileManager.saveFileObject(object);
	}
	
	@Override
	public void setActiveSyntaxGraph(final SyntaxGraph syntaxGraph)
	{
		project.setActiveCanvas(syntaxGraph);
	}
	
	@Override
	public void setGrammarFile(final GrammarFile grammarFile)
	{
		project.setGrammarFile(grammarFile);
		
	}
		
	@Override
	public void setUnsavedView(final String path, final AbstractView view)
	{
		unViewManager.setUnsavedView(path, view);
	}
}
