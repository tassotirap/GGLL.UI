package org.ggll.parser.syntax;

import ggll.core.syntax.model.TableGraphNode;
import ggll.core.syntax.model.TableNode;

import org.ggll.grammar.GrammarFactory;
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
		final GrammarFactory grammarFactory = new GrammarFactory();
		final String table[][] = grammarFactory.createTable();
		
		this.TableTerminal = new TableNode[table.length];
		this.TableNTerminal = new TableNode[table.length];
		this.TableNodes = new TableGraphNode[table.length];
		
		int line;		
		int firstIndex = 1;
		for (line = 0; line < table.length; line++)
		{
			String flag = table[line][0];
			char type = table[line][1].toCharArray()[0];
			String syntaticSymbol = table[line][2];
			int nodeNumber = Integer.parseInt(table[line][3]);
			int alternativeNumber = Integer.parseInt(table[line][4]);
			int sucessorNumer = Integer.parseInt(table[line][5]);
			String semanticRoutine = table[line][6];			

			if (type == 'H')
			{
				firstIndex = header(firstIndex, table, flag, syntaticSymbol);
			}
			else
			{
				final int I = firstIndex + nodeNumber - 1;
				this.TableNodes[I] = new TableGraphNode();
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

	private void setSemanticRoutine(String semanticRoutine, final int I)
	{
		this.TableNodes[I].setSemanticRoutine(semanticRoutine);
	}

	private void setSucessor(int firstIndex, int sucessorNumer, final int I)
	{
		if (sucessorNumer != 0)
		{
			this.TableNodes[I].setSucessorIndex(firstIndex + sucessorNumer - 1);
		}
		else
		{
			this.TableNodes[I].setSucessorIndex(0);
		}
	}

	private void setAlternative(int firstIndex, int alternativeNumber, final int I)
	{
		if (alternativeNumber != 0)
		{
			this.TableNodes[I].setAlternativeIndex(firstIndex + alternativeNumber - 1);
		}
		else
		{
			this.TableNodes[I].setAlternativeIndex(0);
		}
	}

	private void setReferenceNode(int index, String syntaticSymbol, final int I)
	{
		if (syntaticSymbol.equals("-1") || syntaticSymbol.equals(CanvasResource.EMPTY_NODE_LABEL))
		{
			this.TableNodes[I].setNodeReference(0);
		}
		else
		{
			this.TableNodes[I].setNodeReference(index);
		}
	}

	private int nTerminal(String flag, String syntaticSymbol, final int I)
	{
		int iterator;
		int index = -1;
		iterator = 1;		
		while (this.TableNTerminal[iterator] != null)
		{
			if (this.TableNTerminal[iterator].getName().equals(syntaticSymbol))
			{
				index = iterator;
				break;
			}
			iterator = iterator + 1;
		}
		if (index == -1)
		{
			maxNTerminal = maxNTerminal + 1;
			this.TableNTerminal[maxNTerminal] = new TableNode(flag, syntaticSymbol, 0);
			index = maxNTerminal;
		}
		this.TableNodes[I].setIsTerminal(false);
		return index;
	}

	private int terminal(String flag, String syntaticSymbol, final int I)
	{
		int iterator = 1;
		int index = -1;
		this.TableNodes[I].setIsTerminal(true);

		if (!syntaticSymbol.equals(new String("-1")) && !syntaticSymbol.equals(CanvasResource.EMPTY_NODE_LABEL))
		{
			while (this.TableTerminal[iterator] != null)
			{
				if (this.TableTerminal[iterator].getName().equals(syntaticSymbol))
				{
					index = iterator;
					break;
				}
				iterator = iterator + 1;
			}
			if (index == -1)
			{
				maxTerminal = maxTerminal + 1;
				this.TableTerminal[maxTerminal] = new TableNode(flag, syntaticSymbol);
				index = maxTerminal;
			}
		}
		return index;
	}

	private Integer header(Integer firstIndex, final String[][] table, String flag, String syntaticSymbol)
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
		
		while (this.TableNTerminal[iterator] != null)
		{
			if (this.TableNTerminal[iterator].getName().equals(syntaticSymbol))
			{
				nTerminalIndex[aux] = iterator;
				aux = aux + 1;
			}
			iterator = iterator + 1;
		}

		if (nTerminalIndex[0] == -1)
		{
			maxNTerminal = maxNTerminal + 1;
			this.TableNTerminal[maxNTerminal] = new TableNode(flag, syntaticSymbol, firstIndex);
		}
		else
		{
			for (int j = 0; j < maxNTerminal; j++)
			{
				if (nTerminalIndex[j] != -1)
				{
					if (this.TableNTerminal[nTerminalIndex[j]].getFirstNode() == 0)
					{
						this.TableNTerminal[nTerminalIndex[j]].setFirstNode(firstIndex);
					}
				}
			}
		}
		return firstIndex;
	}

	public TableGraphNode[] getTableNodes()
	{
		return this.TableNodes;
	}

	public TableNode[] getNTerminalTable()
	{
		return this.TableNTerminal;
	}

	public TableNode[] getTerminalTable()
	{
		return this.TableTerminal;
	}
}
