package org.ggll.grammar;

import ggll.core.list.ExtendedList;

import org.ggll.output.AppOutput;
import org.ggll.output.HtmlViewer.TOPIC;
import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.state.StateNode;
import org.ggll.syntax.graph.state.StateHelper;

public class GrammarFactory
{
	private int cont;

	private String htmlOutput;

	private String table[][];
	
	private int line;

	public GrammarFactory()
	{
	}

	private void htmlBegining(StateNode startNode)
	{
		AppOutput.displayText(">>Begining a new leftside..." + startNode.getTitle() + "<br>", TOPIC.Output);
		this.htmlOutput = "<table cellspacing='0' cellpadding='0' border='1px' width='100%'>";
		this.htmlOutput += "<tr style='background-color: #EEEEEE; font-weight: bold;'><td></td>";
		this.htmlOutput += "<th>Node</th><th>Number</th><th>Alternative</th><th>Successor</th>";
		this.htmlOutput += "<th>Semantic Rout.</th></tr>";
	}

	private void htmlLeftSide(StateNode leftSideNode, StateNode successor)
	{
		this.htmlOutput += "<tr><td style='background-color: #EEEEEE;' width='35' align='center'>";
		this.htmlOutput += "<img src='icon_H2.png' alt='Left Side'></td>";
		this.htmlOutput += "<td style='font-weight: bold;' align='center'>";
		this.htmlOutput += "<a href='Id|" + leftSideNode.getId() + "'>" + leftSideNode.getTitle() + "</a>";
		this.htmlOutput += "</td><td align='center'>-1</td><td align='center'>-</td><td align='center'>" + successor.getNumber() + "</td>";
		this.htmlOutput += "<td align='center'>-</td></tr>";
	}

	private void htmlNodeIcon(StateNode node, StateNode successor, StateNode alternative)
	{
		this.htmlOutput += "<tr>";
		switch (node.getType())
		{
			case CanvasResource.N_TERMINAL:
				this.htmlOutput += "<td style='background-color: #EEEEEE;' align='center' width='35'><img src='icon_nt2.png' alt='Non-Terminal'></td>";
				break;
			case CanvasResource.TERMINAL:
				this.htmlOutput += "<td style='background-color: #EEEEEE;' align='center' width='35'><img src='icon_t2.png' alt='Terminal'></td>";
				break;
			case CanvasResource.LAMBDA:
				this.htmlOutput += "<td style='background-color: #EEEEEE;' align='center' width='35'><img src='icon_l.png' alt='Lambda Alternative'></td>";
				break;
		}

		this.htmlOutput += "<td style='font-weight: bold;' align='center'><a href='Id|" + node.getId() + "'>" + nodeTitle(node) + "</a></td>";
		this.htmlOutput += "<td align='center'>" + node.getNumber() + "</td>";
		this.htmlOutput += alternative == null ? "<td align='center'>-</td>" : "<td align='center'>" + alternative.getNumber() + "</td>";
		this.htmlOutput += successor == null ? "<td align='center'>-</td>" : "<td align='center'>" + successor.getNumber() + "</td>";
		this.htmlOutput += "<td align='center'>" + (node.getSemanticRoutine() == null ? "-" : node.getSemanticRoutine()) + "</td>";
		this.htmlOutput += "</tr>";
	}

	private void htmlStart(StateNode startNode, StateNode successor)
	{
		this.htmlOutput += "<tr><td style='background-color: #EEEEEE;' width='35' align='center'>";
		this.htmlOutput += "<img src='icon_s2.png' alt='Initial Node'></td>";
		this.htmlOutput += "<td style='font-weight: bold;' align='center'>";
		this.htmlOutput += "<a href='Id|" + startNode.getTitle() + "'>" + startNode.getTitle() + "</a></td>";
		this.htmlOutput += "<td align='center'>-1</td><td align='center'>-</td><td align='center'>" + successor.getNumber() + "</td>";
		this.htmlOutput += "<td align='center'>-</td></tr>";
	}

	private String nodeSemanticRoutine(StateNode node)
	{
		if (node.getSemanticRoutine() == null)
		{
			return "-1";
		}
		return node.getSemanticRoutine();
	}

	private String nodeTitle(StateNode node)
	{
		if (node.getTitle() == null)
		{
			return CanvasResource.EMPTY_NODE_LABEL;
		}
		return node.getTitle();
	}

	private void stringSubpart(StateNode node, StateNode successor, StateNode alternative)
	{	
		line++;
		table[line][0] = node.getId();
		switch (node.getType())
		{
			case CanvasResource.N_TERMINAL:
				table[line][1] = "N";
				break;
			case CanvasResource.TERMINAL:
			case CanvasResource.LAMBDA:
				table[line][1] = "T";
				break;
		}
		
		table[line][2] = nodeTitle(node);
		table[line][3] = ""+node.getNumber();
		table[line][4] = alternative == null ? "0" : "" + alternative.getNumber();
		table[line][5] = successor == null ? "0" : "" + successor.getNumber();
		table[line][6] = nodeSemanticRoutine(node);
	}

	public void subpart(StateNode node)
	{
		final StateNode successor = StateHelper.findSucessorNode(node);
		final StateNode alternative = StateHelper.findAlternativeNode(node);

		node.setFlag(true);
		if (successor != null && successor.isFlag() == false)
		{
			successor.setNumber(++this.cont);
		}

		if (alternative != null && alternative.isFlag() == false)
		{
			alternative.setNumber(++this.cont);
		}

		htmlNodeIcon(node, successor, alternative);
		stringSubpart(node, successor, alternative);

		if (successor != null && successor.isFlag() == false)
		{
			subpart(successor);
		}

		if (alternative != null && alternative.isFlag() == false)
		{
			subpart(alternative);
		}
	}

	public String[][] createTable()
	{
		for (StateNode stateNode : StateHelper.getStateNodes().getAll())
		{
			stateNode.setFlag(false);
			stateNode.setNumber(0);
		}
		final ExtendedList<StateNode> startNodes = StateHelper.getLeftSides();
		table = new String[StateHelper.getNodesCount()][7];
		line = -1;

		for (int i = 0; i < startNodes.count(); i++)
		{
			this.cont = 0;
			final StateNode startNode = startNodes.get(i);
			final StateNode successorSyntaxSubpart = StateHelper.findSucessorNode(startNode);
			successorSyntaxSubpart.setNumber(++this.cont);

			htmlBegining(startNode);

			if (startNode.getType().equals(CanvasResource.START))
			{
				htmlStart(startNode, successorSyntaxSubpart);
			}
			else
			{
				htmlLeftSide(startNode, successorSyntaxSubpart);
			}
			line++;
			table[line][0] = startNode.getId();
			table[line][1] = "H";
			table[line][2] = startNode.getTitle();
			table[line][3] = "-1";
			table[line][4] = "-1";
			table[line][5] = successorSyntaxSubpart.getNumber() + "";
			table[line][6] = "-1";
			subpart(successorSyntaxSubpart);
			this.htmlOutput += "</table>";
			AppOutput.displayGeneratedGrammar(this.htmlOutput);
		}
		
		return table;
	}
}