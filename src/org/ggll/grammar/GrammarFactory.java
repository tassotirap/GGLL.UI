package org.ggll.grammar;

import ggll.core.list.ExtendedList;

import org.ggll.output.AppOutput;
import org.ggll.output.HtmlViewer.TOPIC;
import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraphRepository;
import org.ggll.syntax.graph.state.StateNode;

/** Compiles a grammar textual representation from the designed graph **/
public class GrammarFactory
{
	private Grammar grammar;

	private int cont;

	private String htmlOutput;

	public GrammarFactory()
	{
	}

	private boolean canPerformAction()
	{
		return true;
	}

	private GrammarData getNameAndSematicRouting(StateNode syntaxModel)
	{
		final GrammarData returnValue = new GrammarData();
		if (syntaxModel.getType().equals(CanvasResource.LAMBDA))
		{
			returnValue.name = CanvasResource.EMPTY_NODE_LABEL;
		}
		else
		{
			returnValue.name = syntaxModel.getTitle();
		}
		if (syntaxModel.getSemanticRoutine() != null)
		{
			returnValue.semanticRoutine = syntaxModel.getSemanticRoutine();
		}
		else
		{
			returnValue.semanticRoutine = "-1";
		}
		returnValue.id = syntaxModel.getId();
		return returnValue;
	}

	private void writeBegining(StateNode startNode)
	{
		AppOutput.displayText(">>Begining a new leftside..." + startNode.getTitle() + "<br>", TOPIC.Output);
		this.htmlOutput = "<table cellspacing=\"0\" cellpadding=\"0\" border=\"1px\" width=\"100%\">";
		this.htmlOutput += "<tr style=\"background-color: #EEEEEE; font-weight: bold;\"><td></td>";
		this.htmlOutput += "<td>Node</td><td>Number</td><td>Alternative</td><td>Successor</td>";
		this.htmlOutput += "<td>Semantic Rout.</td></tr>";
	}

	private void writeLeftSide(StateNode leftSideNode, StateNode successor)
	{
		this.htmlOutput += "<tr><td style=\"background-color: #EEEEEE;\">";
		this.htmlOutput += "<img src=\"images/icon_H2.png\" alt=\"Left Side\"></td>";
		this.htmlOutput += "<td style=\"font-weight: bold;\" >";
		this.htmlOutput += "<a href=\"" + leftSideNode.getId() + "\">" + leftSideNode.getTitle() + "</a>";
		this.htmlOutput += "</td><td align=\"center\">-1</td><td align=\"center\">-</td><td align=\"center\">" + successor.getNumber() + "</td>";
		this.htmlOutput += "<td align=\"center\">-</td></tr>";
	}

	private void writeStart(StateNode startNode, StateNode successor)
	{
		this.htmlOutput += "<tr><td style=\"background-color: #EEEEEE;\">";
		this.htmlOutput += "<img src=\"images/icon_s2.png\" alt=\"Initial Node\"></td>";
		this.htmlOutput += "<td style=\"font-weight: bold;\" >";
		this.htmlOutput += "<a href='" + startNode.getId() + "'>" + startNode.getTitle() + "</a></td>";
		this.htmlOutput += "<td align=\"center\">-1</td><td align=\"center\">-</td><td align=\"center\">" + successor.getNumber() + "</td>";
		this.htmlOutput += "<td align=\"center\">-</td></tr>";
	}

	protected boolean calculateEnabled()
	{
		return canPerformAction();
	}

	public Grammar getGrammar()
	{
		return this.grammar;
	}

	public String run()
	{
		final StringBuffer returnString = new StringBuffer();
		final ExtendedList<StateNode> startNodes = SyntaxGraphRepository.getLeftSides();
		for (int i = 0; i < startNodes.count(); i++)
		{
			this.cont = 0;

			final StateNode startNode = startNodes.get(i);
			final StateNode successorSyntaxSubpart = SyntaxGraphRepository.findSucessorNode(startNode);
			successorSyntaxSubpart.setNumber(++this.cont);

			writeBegining(startNode);

			if (startNode.getType().equals(CanvasResource.START))
			{
				writeStart(startNode, successorSyntaxSubpart);
				final GrammarComponent grammarHead = new GrammarComponent(startNode.getTitle(), startNode.getId());
				grammarHead.setHead(true);
				if (i == 0)
				{
					this.grammar = new Grammar(grammarHead);
				}
				this.grammar.setHead(grammarHead);
				this.grammar.setCurrent(grammarHead);
			}
			else
			{
				writeLeftSide(startNode, successorSyntaxSubpart);
				final GrammarComponent grammarLeftside = new GrammarComponent(startNode.getTitle(), startNode.getId());
				grammarLeftside.setLeftHand(true);
				if (i == 0)
				{
					this.grammar = new Grammar(grammarLeftside);
				}
				this.grammar.addLeftHand(grammarLeftside);
				this.grammar.setCurrent(grammarLeftside);
			}
			returnString.append(startNode.getId() + " H " + startNode.getTitle() + " -1 -1 " + successorSyntaxSubpart.getNumber() + " -1\n");

			final GrammarData successorGrammarData = getNameAndSematicRouting(successorSyntaxSubpart);
			final GrammarComponent successorGammarComponent = new GrammarComponent(successorGrammarData.name, successorGrammarData.id);

			if (startNode.getType().equals(CanvasResource.N_TERMINAL))
			{
				successorGammarComponent.setNonterminal(true);
			}
			else if (startNode.getType().equals(CanvasResource.TERMINAL))
			{
				successorGammarComponent.setTerminal(true);
			}
			else if (startNode.getType().equals(CanvasResource.LAMBDA))
			{
				successorGammarComponent.setLambda(true);
			}

			this.grammar.addSuccessor(successorGammarComponent);
			returnString.append(subpartString(successorSyntaxSubpart));
			this.htmlOutput += "</table>";
			AppOutput.displayGeneratedGrammar(this.htmlOutput);
		}
		this.grammar.finalize();
		return returnString.toString();
	}

	public String subpartString(StateNode node)
	{
		final StringBuffer returnString = new StringBuffer();
		GrammarComponent grammarComponent = null;

		final StateNode successor = SyntaxGraphRepository.findSucessorNode(node);
		final StateNode alternative = SyntaxGraphRepository.findAlternativeNode(node);

		node.setFlag(true);		
		if (successor != null && successor.isFlag() == false)
		{
			
			successor.setNumber(++this.cont);
		}

		if (alternative != null && alternative.isFlag() == false)
		{
			alternative.setNumber(++this.cont);
		}

		String stringOut = "";
		this.htmlOutput = this.htmlOutput + "<tr>";

		final GrammarData grammarData = getNameAndSematicRouting(node);

		if (node.getType().equals(CanvasResource.N_TERMINAL))
		{
			stringOut = grammarData.id + " N";
			this.htmlOutput = this.htmlOutput + "<td style=\"background-color: #EEEEEE;\"><img src=\"images/icon_nt2.png\" alt=\"Non-Terminal\"></td>";
			grammarComponent = new GrammarComponent(grammarData.name, grammarData.id);
			grammarComponent.setNonterminal(true);
		}
		else if (node.getType().equals(CanvasResource.TERMINAL))
		{
			stringOut = grammarData.id + " T";
			this.htmlOutput = this.htmlOutput + "<td style=\"background-color: #EEEEEE;\"><img src=\"images/icon_t2.png\" alt=\"Terminal\"></td>";
			grammarComponent = new GrammarComponent(grammarData.name, grammarData.id);
			grammarComponent.setTerminal(true);
		}
		else if (node.getType().equals(CanvasResource.LAMBDA))
		{
			stringOut = grammarData.id + " T";
			this.htmlOutput = this.htmlOutput + "<td style=\"background-color: #EEEEEE;\"><img src=\"images/icon_l.png\" alt=\"Lambda Alternative\"></td>";
			grammarComponent = new GrammarComponent(null, grammarData.id);
			grammarComponent.setLambda(true);
		}

		stringOut = stringOut + " " + grammarData.name;
		this.htmlOutput = this.htmlOutput + "<td style=\"font-weight: bold;\"><a href=\"" + grammarData.id + "\">" + grammarData.name + "</a></td>";

		stringOut = stringOut + " " + node.getNumber();
		this.htmlOutput = this.htmlOutput + "<td align=\"center\">" + node.getNumber() + "</td>";

		if (alternative == null)
		{
			stringOut = stringOut + " 0";
			this.htmlOutput = this.htmlOutput + "<td align=\"center\">-</td>";
		}
		else
		{
			stringOut = stringOut + " " + alternative.getNumber();
			this.htmlOutput = this.htmlOutput + "<td align=\"center\">" + alternative.getNumber() + "</td>";
		}

		if (successor == null)
		{
			stringOut = stringOut + " 0";
			this.htmlOutput = this.htmlOutput + "<td align=\"center\">-</td>";
		}
		else
		{
			stringOut = stringOut + " " + successor.getNumber();
			this.htmlOutput = this.htmlOutput + "<td align=\"center\">" + successor.getNumber() + "</td>";
		}

		stringOut = stringOut + " " + grammarData.semanticRoutine + "\n";
		this.htmlOutput = this.htmlOutput + "<td align=\"center\">" + (grammarData.semanticRoutine.equals("-1") ? "-" : "<a href=\"name_smRoutine[1]\">" + grammarData.semanticRoutine + "</a>") + "</td>";

		returnString.append(stringOut);

		this.htmlOutput += "</tr>";

		this.grammar.setCurrent(grammarComponent);
		if (successor != null && successor.isFlag() == false)
		{
			final GrammarData nextGrammarData = getNameAndSematicRouting(successor);
			final GrammarComponent nextGrammarComponent = new GrammarComponent(nextGrammarData.name, nextGrammarData.id);
			if (successor.getType().equals(CanvasResource.N_TERMINAL))
			{
				nextGrammarComponent.setNonterminal(true);
			}
			else if (successor.getType().equals(CanvasResource.TERMINAL))
			{
				nextGrammarComponent.setTerminal(true);
			}
			else if (successor.getType().equals(CanvasResource.LAMBDA))
			{
				nextGrammarComponent.setLambda(true);
			}
			this.grammar.addSuccessor(nextGrammarComponent);
			returnString.append(subpartString(successor));
		}

		if (alternative != null && alternative.isFlag() == false)
		{
			final GrammarData nextGrammarData = getNameAndSematicRouting(alternative);
			final GrammarComponent nextGuy = new GrammarComponent(nextGrammarData.name, nextGrammarData.id);
			if (alternative.getType().equals(CanvasResource.N_TERMINAL))
			{
				nextGuy.setNonterminal(true);
			}
			else if (alternative.getType().equals(CanvasResource.TERMINAL))
			{
				nextGuy.setTerminal(true);
			}
			else if (alternative.getType().equals(CanvasResource.LAMBDA))
			{
				nextGuy.setLambda(true);
			}
			this.grammar.addAlternative(nextGuy);
			returnString.append(subpartString(alternative));
		}

		return returnString.toString();
	}

}