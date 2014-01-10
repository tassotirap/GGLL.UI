package org.ggll.parser.syntax;

import org.ggll.parser.syntax.grammar.model.AbstractNode;
import org.ggll.parser.syntax.grammar.model.Connection;
import org.ggll.parser.syntax.grammar.model.SimpleNode;
import org.ggll.parser.syntax.grammar.model.SyntaxDefinitions;
import org.ggll.parser.syntax.grammar.model.SyntaxElement;
import org.ggll.parser.syntax.grammar.model.SyntaxModel;
import org.ggll.parser.syntax.grammar.model.SyntaxSubpart;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.state.State;
import org.ggll.syntax.graph.state.StateNode;

public class CanvasParser
{
	private SyntaxModel syntaxModel = new SyntaxModel();

	private void add(String target, String context)
	{
		if (context.equals(AbstractNode.NTERMINAL) || context.equals(AbstractNode.TERMINAL) || context.equals(AbstractNode.LEFTSIDE) || context.equals(AbstractNode.LAMBDA_ALTERNATIVE) || context.equals(AbstractNode.START))
		{
			final SimpleNode node = new SimpleNode(context, target);
			node.setID(target);
			this.syntaxModel.addChild(node);
		}
	}

	private void add(String target, String name, String context)
	{
		if (context.equals(AbstractNode.NTERMINAL) || context.equals(AbstractNode.TERMINAL) || context.equals(AbstractNode.LEFTSIDE) || context.equals(AbstractNode.LAMBDA_ALTERNATIVE) || context.equals(AbstractNode.START))
		{
			final SimpleNode node = new SimpleNode(context, target);
			node.setID(target);
			node.getLabel().setLabelContents(name);
			this.syntaxModel.addChild(node);
		}
	}

	private void addConnection(State canvasState, String connection, String type)
	{
		final org.ggll.syntax.graph.state.StateConnection canvasConnection = canvasState.findConnection(connection);
		if (connection != null)
		{
			connect(canvasConnection.getTarget(), canvasConnection.getSource(), connection, type);
		}
	}

	private void addNode(State canvasState, String name, String type)
	{
		final StateNode node = canvasState.findNode(name);
		if (node != null)
		{
			final String context = type;
			add(name, node.getTitle(), context);
			if (node.getSemanticRoutine() != null && !node.getSemanticRoutine().equals(""))
			{
				addRoutine(name, node.getSemanticRoutine());
			}
		}
	}

	private void addRoutine(String target, String routineName)
	{
		final SimpleNode routineNode = new SimpleNode(AbstractNode.SEMANTIC_ROUTINE, routineName);

		final String name = target;
		final SyntaxElement se = this.syntaxModel.findElement(name);
		if (this.syntaxModel.isNode(se) && se instanceof SyntaxModel)
		{
			((SyntaxModel) se).setSemanticNode(routineNode);
		}
	}

	private void connect(String targe, String source, String connector, String type)
	{
		if (type.equals(SyntaxDefinitions.SucConnection) || type.equals(SyntaxDefinitions.AltConnection))
		{
			final SyntaxElement sourceElement = this.syntaxModel.findElement(source);
			final SyntaxElement targetElement = this.syntaxModel.findElement(targe);
			if (this.syntaxModel.isNode(sourceElement) && this.syntaxModel.isNode(targetElement))
			{
				final SyntaxSubpart sourceSyntaxSubpart = (SyntaxSubpart) sourceElement;
				final SyntaxSubpart targetSyntaxSubpart = (SyntaxSubpart) targetElement;
				final Connection connection = new Connection(connector);
				connection.setSource(sourceSyntaxSubpart);
				connection.setTarget(targetSyntaxSubpart);
				this.syntaxModel.addChild(connection);
				connection.attachTarget(type);
				connection.attachSource();
			}
		}
	}

	private void recreateDiagram(SyntaxGraph canvas)
	{
		final State canvasState = canvas.getCanvasState();
		this.syntaxModel = new SyntaxModel();

		for (final String name : canvas.getTerminals().getAll())
		{
			addNode(canvasState, name, SyntaxDefinitions.Terminal);
		}

		for (final String name : canvas.getNterminals().getAll())
		{
			addNode(canvasState, name, SyntaxDefinitions.NTerminal);
		}

		for (final String name : canvas.getLeftSides().getAll())
		{
			addNode(canvasState, name, SyntaxDefinitions.LeftSide);
		}

		for (final String name : canvas.getLambdas().getAll())
		{
			final StateNode node = canvasState.findNode(name);
			if (node != null)
			{
				final String context = SyntaxDefinitions.LambdaAlternative;
				add(name, context);

				if (node.getSemanticRoutine() != null && !node.getSemanticRoutine().equals(""))
				{
					addRoutine(name, node.getSemanticRoutine());
				}
			}
		}

		for (final String name : canvas.getStart().getAll())
		{
			addNode(canvasState, name, SyntaxDefinitions.Start);
		}

		for (final String name : canvas.getSuccessors().getAll())
		{
			addConnection(canvasState, name, SyntaxDefinitions.SucConnection);
		}

		for (final String name : canvas.getAlternatives().getAll())
		{
			addConnection(canvasState, name, SyntaxDefinitions.AltConnection);
		}
	}

	public SyntaxModel getLogicDiagram(SyntaxGraph canvas)
	{
		recreateDiagram(canvas);
		return this.syntaxModel;
	}
}
