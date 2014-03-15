package org.ggll.parser.syntax;

import ggll.core.syntax.model.TableGraphNode;
import ggll.core.syntax.model.TableNode;

import org.ggll.grammar.GrammarTableBuilder;
import org.ggll.resource.CanvasResource;

public class SyntacticLoader
{
	
	TableGraphNode TableNodes[];
	TableNode TableNTerminal[];
	TableNode TableTerminal[];
	
	int maxTerminal = 0;
	int maxNTerminal = 0;
	int maxNodes = 0;
	
	public SyntacticLoader()
	{
		final GrammarTableBuilder grammarFactory = new GrammarTableBuilder();
		final String table[][] = grammarFactory.createTable();
		
		TableTerminal = new TableNode[table.length];
		TableNTerminal = new TableNode[table.length];
		TableNodes = new TableGraphNode[table.length];
		
		int line;
		int firstIndex = 1;
		for (line = 0; line < table.length; line++)
		{
			final String flag = table[line][0];
			final char type = table[line][1].toCharArray()[0];
			final String syntaticSymbol = table[line][2];
			final int nodeNumber = Integer.parseInt(table[line][3]);
			final int alternativeNumber = Integer.parseInt(table[line][4]);
			final int sucessorNumer = Integer.parseInt(table[line][5]);
			final String semanticRoutine = table[line][6];
			
			if (type == 'H')
			{
				firstIndex = header(firstIndex, table, flag, syntaticSymbol);
			}
			else
			{
				final int I = firstIndex + nodeNumber - 1;
				TableNodes[I] = new TableGraphNode();
				int index = -1;
				if (type == 'T')
				{
					index = terminal(flag, syntaticSymbol, I);
				}
				else if (type == 'N')
				{
					index = nTerminal(flag, syntaticSymbol, I);
				}
				setReferenceNode(index, syntaticSymbol, I);
				setAlternative(firstIndex, alternativeNumber, I);
				setSucessor(firstIndex, sucessorNumer, I);
				setSemanticRoutine(semanticRoutine, I);
				
				if (maxNodes < nodeNumber)
				{
					maxNodes = nodeNumber;
				}
			}
		}
	}
	
	public TableNode[] getNTerminalTable()
	{
		return TableNTerminal;
	}
	
	public TableGraphNode[] getTableNodes()
	{
		return TableNodes;
	}
	
	public TableNode[] getTerminalTable()
	{
		return TableTerminal;
	}
	
	private Integer header(Integer firstIndex, final String[][] table, final String flag, final String syntaticSymbol)
	{
		int iterator = 1;
		firstIndex = firstIndex + maxNodes;
		maxNodes = 0;
		int aux = 0;
		
		final int nTerminalIndex[] = new int[table.length];
		for (int i = 0; i < nTerminalIndex.length; i++)
		{
			nTerminalIndex[i] = -1;
		}
		
		while (TableNTerminal[iterator] != null)
		{
			if (TableNTerminal[iterator].getName().equals(syntaticSymbol))
			{
				nTerminalIndex[aux] = iterator;
				aux = aux + 1;
			}
			iterator = iterator + 1;
		}
		
		if (nTerminalIndex[0] == -1)
		{
			maxNTerminal = maxNTerminal + 1;
			TableNTerminal[maxNTerminal] = new TableNode(flag, syntaticSymbol, firstIndex);
		}
		else
		{
			for (int j = 0; j < maxNTerminal; j++)
			{
				if (nTerminalIndex[j] != -1)
				{
					if (TableNTerminal[nTerminalIndex[j]].getFirstNode() == 0)
					{
						TableNTerminal[nTerminalIndex[j]].setFirstNode(firstIndex);
					}
				}
			}
		}
		return firstIndex;
	}
	
	private int nTerminal(final String flag, final String syntaticSymbol, final int I)
	{
		int iterator;
		int index = -1;
		iterator = 1;
		while (TableNTerminal[iterator] != null)
		{
			if (TableNTerminal[iterator].getName().equals(syntaticSymbol))
			{
				index = iterator;
				break;
			}
			iterator = iterator + 1;
		}
		if (index == -1)
		{
			maxNTerminal = maxNTerminal + 1;
			TableNTerminal[maxNTerminal] = new TableNode(flag, syntaticSymbol, 0);
			index = maxNTerminal;
		}
		TableNodes[I].setIsTerminal(false);
		return index;
	}
	
	private void setAlternative(final int firstIndex, final int alternativeNumber, final int I)
	{
		if (alternativeNumber != 0)
		{
			TableNodes[I].setAlternativeIndex(firstIndex + alternativeNumber - 1);
		}
		else
		{
			TableNodes[I].setAlternativeIndex(0);
		}
	}
	
	private void setReferenceNode(final int index, final String syntaticSymbol, final int I)
	{
		if (syntaticSymbol.equals("-1") || syntaticSymbol.equals(CanvasResource.EMPTY_NODE_LABEL))
		{
			TableNodes[I].setNodeReference(0);
		}
		else
		{
			TableNodes[I].setNodeReference(index);
		}
	}
	
	private void setSemanticRoutine(final String semanticRoutine, final int I)
	{
		TableNodes[I].setSemanticRoutine(semanticRoutine);
	}
	
	private void setSucessor(final int firstIndex, final int sucessorNumer, final int I)
	{
		if (sucessorNumer != 0)
		{
			TableNodes[I].setSucessorIndex(firstIndex + sucessorNumer - 1);
		}
		else
		{
			TableNodes[I].setSucessorIndex(0);
		}
	}
	
	private int terminal(final String flag, final String syntaticSymbol, final int I)
	{
		int iterator = 1;
		int index = -1;
		TableNodes[I].setIsTerminal(true);
		
		if (!syntaticSymbol.equals(new String("-1")) && !syntaticSymbol.equals(CanvasResource.EMPTY_NODE_LABEL))
		{
			while (TableTerminal[iterator] != null)
			{
				if (TableTerminal[iterator].getName().equals(syntaticSymbol))
				{
					index = iterator;
					break;
				}
				iterator = iterator + 1;
			}
			if (index == -1)
			{
				maxTerminal = maxTerminal + 1;
				TableTerminal[maxTerminal] = new TableNode(flag, syntaticSymbol);
				index = maxTerminal;
			}
		}
		return index;
	}
}
