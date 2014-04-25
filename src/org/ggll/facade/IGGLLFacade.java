package org.ggll.facade;

import ggll.core.list.ExtendedList;

import java.io.File;
import java.io.IOException;

import org.ggll.file.GrammarFile;
import org.ggll.file.LexicalFile;
import org.ggll.file.SemanticFile;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.window.component.AbstractComponent;
import org.ggll.window.view.AbstractView;

public interface IGGLLFacade
{	
	public abstract void closeFile(String fileName);
	
	public abstract void createFile(String name, String extension) throws IOException;
	
	public abstract void exit();
	
	public abstract SyntaxGraph getActiveSyntaxGraph();
	
	public ExtendedList<GrammarFile> getGrammarFile();
	
	public abstract LexicalFile getLexicalFile();
	
	public abstract ExtendedList<File> getOpenedFiles();
	
	public abstract File getProjectDir();
	
	public String getProjectsRootPath();
	
	public abstract SemanticFile getSemanticFile();
	
	public abstract AbstractView getUnsavedView(String file);
	
	public abstract boolean hasUnsavedView(AbstractView view);
	
	public abstract boolean hasUnsavedView(String file);
	
	public abstract boolean isFileOpen(String absolutePath);
	
	public abstract void openFile(String path);
	
	public abstract void openFile(String path, boolean verifyOpen);
	
	public abstract void print(Object object);
	
	public abstract void removeUnsavedView(String path);
	
	public abstract void saveAllFiles();
	
	public abstract void saveFile(AbstractComponent object);
	
	public abstract void setActiveSyntaxGraph(SyntaxGraph syntaxGraph);
	
	public void setGrammarFile(GrammarFile grammarFile);
	
	public abstract void setUnsavedView(String path, AbstractView view);
	
}