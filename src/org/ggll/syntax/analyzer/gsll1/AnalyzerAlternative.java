package org.ggll.syntax.analyzer.gsll1;

import org.ggll.syntax.model.GrViewStack;
import org.ggll.syntax.model.NTerminalStack;

public class AnalyzerAlternative
{
	AnalyzerTableRepository analyzerTabs;
	
	private static AnalyzerAlternative instance;
	
	public static AnalyzerAlternative getInstance()
	{
		return instance;
	}
	
	public static AnalyzerAlternative setInstance()
	{
		instance = new AnalyzerAlternative();
		return instance;
	}	
	
	private AnalyzerAlternative()
	{
		this.analyzerTabs = AnalyzerTableRepository.getInstance();
	}
	
	public int findAlternative(int indexNode, NTerminalStack nTermStack, GrViewStack grViewStack)
	{
		int alternative = 0;
		alternative = analyzerTabs.getGraphNode(indexNode).getAlternativeIndex();
		while (alternative == 0 && !nTermStack.empty())
		{
			grViewStack.pop();
			alternative = analyzerTabs.getGraphNode(nTermStack.pop()).getAlternativeIndex();
		}
		return alternative;
	}
}
