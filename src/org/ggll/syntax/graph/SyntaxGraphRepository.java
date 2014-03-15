package org.ggll.syntax.graph;

import java.util.Collection;
import java.util.Hashtable;

import org.ggll.facade.GGLLFacade;
import org.ggll.file.GrammarFile;
import org.ggll.resource.CanvasResource;

public class SyntaxGraphRepository
{
	private static String connStrategy = CanvasResource.R_ORTHOGONAL;
	private static String defaultCursor = CanvasResource.SELECT;
	private static String moveStrategy = CanvasResource.M_ALIGN;
	
	private static Hashtable<String, SyntaxGraph> instances;
	
	public static SyntaxGraph getInstance(final String file)
	{
		if (SyntaxGraphRepository.instances == null)
		{
			SyntaxGraphRepository.instances = new Hashtable<String, SyntaxGraph>();
		}
		
		if (!SyntaxGraphRepository.instances.containsKey(file))
		{
			final SyntaxGraph canvasFactory = new SyntaxGraph(SyntaxGraphRepository.defaultCursor, SyntaxGraphRepository.connStrategy, SyntaxGraphRepository.moveStrategy, file);
			GGLLFacade.getInstance().setGrammarFile(new GrammarFile(file));
			SyntaxGraphRepository.instances.put(file, canvasFactory);
		}
		
		return SyntaxGraphRepository.instances.get(file);
	}
	
	public static Collection<SyntaxGraph> getInstances()
	{
		SyntaxGraphRepository.refresh();
		return SyntaxGraphRepository.instances.values();
	}
	
	public static void refresh()
	{
		for (final GrammarFile grammar : GGLLFacade.getInstance().getGrammarFile().getAll())
		{
			SyntaxGraphRepository.getInstance(grammar.getAbsolutePath());
		}
	}
}
