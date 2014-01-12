//package org.ggll.parser.syntax;
//
//import org.ggll.grammar.model.Connection;
//import org.ggll.grammar.model.SimpleNode;
//import org.ggll.grammar.model.SyntaxElement;
//import org.ggll.grammar.model.SyntaxModel;
//import org.ggll.resource.CanvasResource;
//import org.ggll.syntax.graph.state.State;
//import org.ggll.syntax.graph.state.StateConnection;
//import org.ggll.syntax.graph.state.StateNode;
//
//public class CanvasStateParser
//{
//	private SyntaxModel syntaxModel = new SyntaxModel();
//
//	private void add(String id, String label, String type)
//	{
//		if (type.equals(CanvasResource.N_TERMINAL) || type.equals(CanvasResource.TERMINAL) || type.equals(CanvasResource.LEFT_SIDE) || type.equals(CanvasResource.LAMBDA) || type.equals(CanvasResource.START))
//		{
//			final SimpleNode node = new SimpleNode(type, id);
//			node.setID(id);
//			if(label != null)
//			{
//				node.setLabel(label);
//			}			
//			this.syntaxModel.addChild(node);
//		}
//	}
//
//	private void addConnection(StateConnection connection)
//	{
//		if (connection != null)
//		{
//			connect(connection);
//		}
//	}
//
//	private void addNode(StateNode node)
//	{
//		add(node.getId(), node.getTitle(), node.getType());
//		if (node.getSemanticRoutine() != null && !node.getSemanticRoutine().equals(""))
//		{
//			addRoutine(node.getId(), node.getSemanticRoutine());
//		}
//	}
//
//	private void addRoutine(String target, String routineName)
//	{
//		final SimpleNode routineNode = new SimpleNode(CanvasResource.SEMANTIC_ROUTINE, routineName);
//		final String name = target;
//		final SyntaxElement syntaxElement = this.syntaxModel.findElement(name);
//		if (this.syntaxModel.isNode(syntaxElement) && syntaxElement instanceof SimpleNode)
//		{
//			((SimpleNode) syntaxElement).setSemanticNode(routineNode);
//		}
//	}
//
//	private void connect(StateConnection stateConnection)
//	{
//		final SyntaxElement sourceElement = this.syntaxModel.findElement(stateConnection.getSource());
//		final SyntaxElement targetElement = this.syntaxModel.findElement(stateConnection.getTarget());
//		if (this.syntaxModel.isNode(sourceElement) && this.syntaxModel.isNode(targetElement))
//		{
//			final SimpleNode sourceSyntaxSubpart = (SimpleNode) sourceElement;
//			final SimpleNode targetSyntaxSubpart = (SimpleNode) targetElement;
//			final Connection connection = new Connection(stateConnection.getId());
//			connection.setSource(sourceSyntaxSubpart);
//			connection.setTarget(targetSyntaxSubpart);
//			this.syntaxModel.addChild(connection);
//			connection.attachTarget(stateConnection.getType());
//			connection.attachSource();
//		}
//	}
//
//	private void recreateDiagram(State state)
//	{
//		this.syntaxModel = new SyntaxModel();
//
//		for (final StateNode node : state.getTerminal().getAll())
//		{
//			addNode(node);
//		}
//
//		for (final StateNode node : state.getNTerminal().getAll())
//		{
//			addNode(node);
//		}
//
//		for (final StateNode node : state.getLeftSide().getAll())
//		{
//			addNode(node);
//		}
//
//		for (final StateNode node : state.getLambda().getAll())
//		{
//			addNode(node);
//		}
//
//		for (final StateNode node : state.getStarts().getAll())
//		{
//			addNode(node);
//		}
//
//		for (final StateConnection connection : state.getSucessors().getAll())
//		{
//			addConnection(connection);
//		}
//
//		for (final StateConnection connection : state.getAlternatives().getAll())
//		{
//			addConnection(connection);
//		}
//	}
//
//	public SyntaxModel getLogicDiagram(State canvas)
//	{
//		recreateDiagram(canvas);
//		return this.syntaxModel;
//	}
//}
