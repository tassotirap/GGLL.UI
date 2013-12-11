package ggll.ui.core.syntax;

import ggll.ui.canvas.Canvas;
import ggll.ui.canvas.state.CanvasState;
import ggll.ui.canvas.state.Node;
import ggll.ui.core.syntax.grammar.model.AbstractNode;
import ggll.ui.core.syntax.grammar.model.Connection;
import ggll.ui.core.syntax.grammar.model.SimpleNode;
import ggll.ui.core.syntax.grammar.model.SyntaxDefinitions;
import ggll.ui.core.syntax.grammar.model.SyntaxElement;
import ggll.ui.core.syntax.grammar.model.SyntaxModel;
import ggll.ui.core.syntax.grammar.model.SyntaxSubpart;

public class CanvasParser
{
	private SyntaxModel syntaxModel = new SyntaxModel();

	private void add(String target, String context)
	{
		if (context.equals(AbstractNode.NTERMINAL) || context.equals(AbstractNode.TERMINAL) || context.equals(AbstractNode.LEFTSIDE) || context.equals(AbstractNode.LAMBDA_ALTERNATIVE) || context.equals(AbstractNode.START))
		{
			SimpleNode node = new SimpleNode(context, target);
			node.setID(target);
			syntaxModel.addChild(node);
		}
	}

	private void add(String target, String name, String context)
	{
		if (context.equals(AbstractNode.NTERMINAL) || context.equals(AbstractNode.TERMINAL) || context.equals(AbstractNode.LEFTSIDE) || context.equals(AbstractNode.LAMBDA_ALTERNATIVE) || context.equals(AbstractNode.START))
		{
			SimpleNode node = new SimpleNode(context, target);
			node.setID(target);
			node.getLabel().setLabelContents(name);
			syntaxModel.addChild(node);
		}
	}

	private void addConnection(CanvasState canvasState, String connection, String type)
	{
		ggll.ui.canvas.state.Connection canvasConnection = canvasState.findConnection(connection);
		if (connection != null)
		{
			connect(canvasConnection.getTarget(), canvasConnection.getSource(), connection, type);
		}
	}

	private void addNode(CanvasState canvasState, String name, String type)
	{
		Node node = canvasState.findNode(name);
		if (node != null)
		{
			String context = type;
			add(name, node.getTitle(), context);
			if (node.getMark() != null && !node.getMark().equals(""))
			{
				addRoutine(name, node.getMark());
			}
		}
	}

	private void addRoutine(String target, String routineName)
	{
		SimpleNode routineNode = new SimpleNode(AbstractNode.SEMANTIC_ROUTINE, routineName);

		String name = target;
		SyntaxElement se = syntaxModel.findElement(name);
		if (syntaxModel.isNode(se) && se instanceof SyntaxModel)
		{
			((SyntaxModel) se).setSemanticNode(routineNode);
		}
	}

	private void connect(String targe, String source, String connector, String type)
	{
		if (type.equals(SyntaxDefinitions.SucConnection) || type.equals(SyntaxDefinitions.AltConnection))
		{
			SyntaxElement sourceElement = syntaxModel.findElement(source);
			SyntaxElement targetElement = syntaxModel.findElement(targe);
			if (syntaxModel.isNode(sourceElement) && syntaxModel.isNode(targetElement))
			{
				SyntaxSubpart sourceSyntaxSubpart = (SyntaxSubpart) sourceElement;
				SyntaxSubpart targetSyntaxSubpart = (SyntaxSubpart) targetElement;
				Connection connection = new Connection(connector);
				connection.setSource(sourceSyntaxSubpart);
				connection.setTarget(targetSyntaxSubpart);
				syntaxModel.addChild(connection);
				connection.attachTarget(type);
				connection.attachSource();
			}
		}
	}

	private void recreateDiagram(Canvas canvas)
	{
		CanvasState canvasState = canvas.getCurrentCanvasState();
		syntaxModel = new SyntaxModel();

		for (String name : canvas.getTerminals().getAll())
		{
			addNode(canvasState, name, SyntaxDefinitions.Terminal);
		}

		for (String name : canvas.getNterminals().getAll())
		{
			addNode(canvasState, name, SyntaxDefinitions.NTerminal);
		}

		for (String name : canvas.getLeftSides().getAll())
		{
			addNode(canvasState, name, SyntaxDefinitions.LeftSide);
		}

		for (String name : canvas.getLambdas().getAll())
		{
			Node node = canvasState.findNode(name);
			if (node != null)
			{
				String context = SyntaxDefinitions.LambdaAlternative;
				add(name, context);

				if (node.getMark() != null && !node.getMark().equals(""))
				{
					addRoutine(name, node.getMark());
				}
			}
		}

		for (String name : canvas.getStart().getAll())
		{
			addNode(canvasState, name, SyntaxDefinitions.Start);
		}

		for (String name : canvas.getSuccessors().getAll())
		{
			addConnection(canvasState, name, SyntaxDefinitions.SucConnection);
		}

		for (String name : canvas.getAlternatives().getAll())
		{
			addConnection(canvasState, name, SyntaxDefinitions.AltConnection);
		}
	}

	public SyntaxModel getLogicDiagram(Canvas canvas)
	{
		recreateDiagram(canvas);
		return syntaxModel;
	}
}
