package org.ggll.parser.syntax.grammar;

import ggll.core.list.ExtendedList;

import java.io.File;

import org.ggll.director.GGLLDirector;
import org.ggll.output.AppOutput;
import org.ggll.output.HtmlViewer.TOPIC;
import org.ggll.parser.syntax.CanvasParser;
import org.ggll.parser.syntax.grammar.model.AbstractNode;
import org.ggll.parser.syntax.grammar.model.NodeLabel;
import org.ggll.parser.syntax.grammar.model.SimpleNode;
import org.ggll.parser.syntax.grammar.model.SyntaxDefinitions;
import org.ggll.parser.syntax.grammar.model.SyntaxElement;
import org.ggll.parser.syntax.grammar.model.SyntaxModel;
import org.ggll.parser.syntax.grammar.model.SyntaxSubpart;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.SyntaxGraphRepository;

/** Compiles a grammar textual representation from the designed graph **/
public class GrammarFactory
{
	private Grammar grammar;

	private int cont;

	private String htmlOutput;

	/**
	 * Constructor
	 * 
	 * @param part
	 */
	public GrammarFactory()
	{

	}

	private boolean canPerformAction()
	{
		return true;
	}

	private ExtendedList<AbstractNode> clearAndSetStartNodes(ExtendedList<SyntaxElement> children)
	{
		final ExtendedList<AbstractNode> startNodes = new ExtendedList<AbstractNode>();

		for (final SyntaxElement object : children.getAll())
		{
			if (object instanceof SyntaxSubpart)
			{
				((SyntaxSubpart) object).setFlag(false);
			}
			if (object instanceof AbstractNode && ((AbstractNode) object).getType().equals(AbstractNode.LEFTSIDE))
			{
				startNodes.append((AbstractNode) object);
			}
			if (object instanceof AbstractNode && ((AbstractNode) object).getType().equals(AbstractNode.START))
			{
				startNodes.prepend((AbstractNode) object);
			}
		}
		return startNodes;
	}

	private GrammarData getNameAndSematicRouting(SyntaxModel syntaxModel)
	{
		String name;
		String semanticRoutine;
		String[] stringValue = new String[2];
		final GrammarData returnValue = new GrammarData();
		SimpleNode simpleNode;
		final ExtendedList<NodeLabel> listChildren = syntaxModel.getChildrenAsLabels();
		final NodeLabel label = listChildren.first();
		stringValue = label.getLabelContents().split("#");
		if (syntaxModel instanceof AbstractNode && ((AbstractNode) syntaxModel).getType().equals(AbstractNode.LAMBDA_ALTERNATIVE))
		{
			name = SyntaxDefinitions.EmptyNodeLabel;
		}
		else
		{
			name = stringValue[0];
		}
		if (stringValue.length >= 2)
		{
			semanticRoutine = stringValue[1];
		}
		else if ((simpleNode = syntaxModel.getSemanticNode()) != null)
		{
			semanticRoutine = simpleNode.getLabel().getLabelContents();
		}
		else
		{
			semanticRoutine = "-1";
		}
		returnValue.name = new String(name);
		returnValue.semanticRoutine = new String(semanticRoutine);
		returnValue.id = syntaxModel.getID();
		return returnValue;
	}

	private void writeBegining(NodeLabel label)
	{
		AppOutput.displayText("<a>>>Begining a new leftside..." + label.getLabelContents() + "</a><br>", TOPIC.Output);
	}

	protected boolean calculateEnabled()
	{
		return canPerformAction();
	}

	public Grammar getGrammar()
	{
		return this.grammar;
	}

	public String run() throws Exception
	{
		final StringBuffer returnString = new StringBuffer();
		final CanvasParser ggllTableGenerator = new CanvasParser();

		final ExtendedList<SyntaxElement> children = new ExtendedList<SyntaxElement>();
		final ExtendedList<AbstractNode> startNodes = new ExtendedList<AbstractNode>();

		for (final File grammar : GGLLDirector.getProject().getGrammarFile().getAll())
		{
			final SyntaxGraph cavans = SyntaxGraphRepository.getInstance(grammar.getAbsolutePath());
			children.addAll(ggllTableGenerator.getLogicDiagram(cavans).getChildrenNodes());
		}

		startNodes.addAll(clearAndSetStartNodes(children));

		for (int i = 0; i < startNodes.count(); i++)
		{
			this.cont = 0;
			final SyntaxModel startSyntaxModel = (SyntaxModel) startNodes.get(i);
			if (startSyntaxModel == null)
			{
				throw new Exception("Could not find the grammar start node.");
			}

			final SyntaxModel successorSyntaxSubpart = (SyntaxModel) startSyntaxModel.getSucessor();
			if (successorSyntaxSubpart == null)
			{
				throw new Exception("Could not find the successor node of the grammar head.");
			}
			successorSyntaxSubpart.setNumber(++this.cont);

			final AbstractNode startNode = startNodes.get(i);
			final AbstractNode successorNode = (AbstractNode) successorSyntaxSubpart;

			final ExtendedList listChildren = ((SyntaxModel) startNodes.get(i)).getChildrenAsLabels();
			final NodeLabel label = (NodeLabel) listChildren.first();

			writeBegining(label);

			this.htmlOutput = "<table cellspacing=\"0\" cellpadding=\"0\" border=\"1px\" width=\"100%\">";
			this.htmlOutput += "<tr style=\"background-color: #EEEEEE; font-weight: bold;\"><td></td>";
			this.htmlOutput += "<td>Node</td><td>Number</td><td>Alternative</td><td>Successor</td>";
			this.htmlOutput += "<td>Semantic Rout.</td></tr>";
			if (startNode.getType().equals(AbstractNode.START))
			{
				this.htmlOutput += "<tr><td style=\"background-color: #EEEEEE;\">";
				this.htmlOutput += "<img src=\"images/icon_s2.png\" alt=\"Initial Node\"></td>";
				this.htmlOutput += "<td style=\"font-weight: bold;\" >";
				this.htmlOutput += "<a href=\"" + ((SyntaxModel) startNodes.get(i)).getID() + "\">" + label.getLabelContents() + "</a></td>";
				this.htmlOutput += "<td align=\"center\">-1</td><td align=\"center\">-</td><td align=\"center\">" + successorSyntaxSubpart.getNumber() + "</td>";
				this.htmlOutput += "<td align=\"center\">-</td></tr>";
				final GrammarComponent grammarHead = new GrammarComponent(label.getLabelContents(), startSyntaxModel.getID());
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
				this.htmlOutput += "<tr><td style=\"background-color: #EEEEEE;\">";
				this.htmlOutput += "<img src=\"images/icon_H2.png\" alt=\"Left Side\"></td>";
				this.htmlOutput += "<td style=\"font-weight: bold;\" >";
				this.htmlOutput += "<a href=\"" + ((SyntaxModel) startNodes.get(i)).getID() + "\">" + label.getLabelContents() + "</a>";
				this.htmlOutput += "</td><td align=\"center\">-1</td><td align=\"center\">-</td><td align=\"center\">" + successorSyntaxSubpart.getNumber() + "</td>";
				this.htmlOutput += "<td align=\"center\">-</td></tr>";
				final GrammarComponent grammarLeftside = new GrammarComponent(label.getLabelContents(), startSyntaxModel.getID());
				grammarLeftside.setLeftHand(true);
				if (i == 0)
				{
					this.grammar = new Grammar(grammarLeftside);
				}
				this.grammar.addLeftHand(grammarLeftside);
				this.grammar.setCurrent(grammarLeftside);
			}
			final String stringOut = startSyntaxModel.getID() + " H " + label.getLabelContents() + " -1 -1 " + successorSyntaxSubpart.getNumber() + " -1\n";
			returnString.append(stringOut);
			final GrammarData successorGrammarData = getNameAndSematicRouting(successorSyntaxSubpart);
			final GrammarComponent successorGammarComponent = new GrammarComponent(successorGrammarData.name, successorGrammarData.id);

			if (successorNode.getType().equals(AbstractNode.NTERMINAL))
			{
				successorGammarComponent.setNonterminal(true);
			}
			else if (successorNode.getType().equals(AbstractNode.TERMINAL))
			{
				successorGammarComponent.setTerminal(true);
			}
			else if (successorNode.getType().equals(AbstractNode.LAMBDA_ALTERNATIVE))
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

	public String subpartString(SyntaxSubpart syntaxSubpart)
	{
		final StringBuffer returnString = new StringBuffer();
		GrammarComponent grammarComponent = null;

		final AbstractNode subpartNode = (AbstractNode) syntaxSubpart;

		if (syntaxSubpart instanceof SyntaxModel)
		{
			syntaxSubpart.setFlag(true);
			final SyntaxModel successor = (SyntaxModel) syntaxSubpart.getSucessor();
			final SyntaxModel alternative = (SyntaxModel) syntaxSubpart.getAlternative();

			final AbstractNode successorNode = (AbstractNode) successor;
			final AbstractNode alternativeNode = (AbstractNode) alternative;

			if (successor != null && successor.getFlag() == false)
			{
				successor.setNumber(++this.cont);
			}

			if (alternative != null && alternative.getFlag() == false)
			{
				alternative.setNumber(++this.cont);
			}

			String stringOut = "";
			this.htmlOutput = this.htmlOutput + "<tr>";

			final GrammarData grammarData = getNameAndSematicRouting((SyntaxModel) syntaxSubpart);

			if (subpartNode.getType().equals(AbstractNode.NTERMINAL))
			{
				stringOut = grammarData.id + " N";
				this.htmlOutput = this.htmlOutput + "<td style=\"background-color: #EEEEEE;\"><img src=\"images/icon_nt2.png\" alt=\"Non-Terminal\"></td>";
				grammarComponent = new GrammarComponent(grammarData.name, grammarData.id);
				grammarComponent.setNonterminal(true);
			}
			else if (subpartNode.getType().equals(AbstractNode.TERMINAL))
			{
				stringOut = grammarData.id + " T";
				this.htmlOutput = this.htmlOutput + "<td style=\"background-color: #EEEEEE;\"><img src=\"images/icon_t2.png\" alt=\"Terminal\"></td>";
				grammarComponent = new GrammarComponent(grammarData.name, grammarData.id);
				grammarComponent.setTerminal(true);
			}
			else if (subpartNode.getType().equals(AbstractNode.LAMBDA_ALTERNATIVE))
			{
				stringOut = grammarData.id + " T";
				this.htmlOutput = this.htmlOutput + "<td style=\"background-color: #EEEEEE;\"><img src=\"images/icon_l.png\" alt=\"Lambda Alternative\"></td>";
				grammarComponent = new GrammarComponent(null, grammarData.id);
				grammarComponent.setLambda(true);
			}

			stringOut = stringOut + " " + grammarData.name;
			this.htmlOutput = this.htmlOutput + "<td style=\"font-weight: bold;\"><a href=\"" + grammarData.id + "\">" + grammarData.name + "</a></td>";

			stringOut = stringOut + " " + syntaxSubpart.getNumber();
			this.htmlOutput = this.htmlOutput + "<td align=\"center\">" + syntaxSubpart.getNumber() + "</td>";

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
			if (successor != null && successor.getFlag() == false)
			{
				final GrammarData nextGrammarData = getNameAndSematicRouting(successor);
				final GrammarComponent nextGrammarComponent = new GrammarComponent(nextGrammarData.name, nextGrammarData.id);
				if (successorNode.getType().equals(AbstractNode.NTERMINAL))
				{
					nextGrammarComponent.setNonterminal(true);
				}
				else if (successorNode.getType().equals(AbstractNode.TERMINAL))
				{
					nextGrammarComponent.setTerminal(true);
				}
				else if (successorNode.getType().equals(AbstractNode.LAMBDA_ALTERNATIVE))
				{
					nextGrammarComponent.setLambda(true);
				}
				this.grammar.addSuccessor(nextGrammarComponent);
				returnString.append(subpartString(successor));
			}

			if (alternative != null && alternative.getFlag() == false)
			{
				final GrammarData nextGrammarData = getNameAndSematicRouting(alternative);
				final GrammarComponent nextGuy = new GrammarComponent(nextGrammarData.name, nextGrammarData.id);
				if (alternativeNode.getType().equals(AbstractNode.NTERMINAL))
				{
					nextGuy.setNonterminal(true);
				}
				else if (alternativeNode.getType().equals(AbstractNode.TERMINAL))
				{
					nextGuy.setTerminal(true);
				}
				else if (alternativeNode.getType().equals(AbstractNode.LAMBDA_ALTERNATIVE))
				{
					nextGuy.setLambda(true);
				}
				this.grammar.addAlternative(nextGuy);
				returnString.append(subpartString(alternative));
			}
		}
		return returnString.toString();
	}

}