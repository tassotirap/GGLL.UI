package ggll.canvas;

import ggll.canvas.action.GridProvider;
import ggll.canvas.action.WidgetActionFactory;
import ggll.canvas.provider.LineProvider;
import ggll.canvas.provider.NodeSelectProvider;
import ggll.canvas.state.CanvasState;
import ggll.canvas.state.Connection;
import ggll.canvas.state.Node;
import ggll.canvas.widget.GridWidget;
import ggll.canvas.widget.GuideLineWidget;
import ggll.canvas.widget.LabelWidgetExt;
import ggll.canvas.widget.LineWidget;
import ggll.canvas.widget.MarkedWidget;
import ggll.canvas.widget.TypedWidget;
import ggll.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.netbeans.api.visual.anchor.Anchor.Direction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.ConnectionWidget.RoutingPolicy;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

public class Canvas extends AbstractCanvas
{

	private Router activeRouter;
	private LayerWidget backgroundLayer = new LayerWidget(this);
	private LayerWidget connectionLayer = new LayerWidget(this);

	private String connStrategy;
	private String cursor;
	private LayerWidget interractionLayer = new LayerWidget(this);

	private LayerWidget mainLayer = new LayerWidget(this);

	private String moveStrategy;

	private CanvasState state;
	
	private WidgetActionFactory actions;

	public Canvas(String cursor, String connectionStrategy, String movementStrategy, CanvasDecorator decorator)
	{
		super(cursor, connectionStrategy, movementStrategy, decorator);
		this.connStrategy = connectionStrategy;
		this.moveStrategy = movementStrategy;
		this.cursor = cursor;
		this.actions = WidgetActionFactory.getInstance();
	}

	@Override
	protected void attachEdgeSourceAnchor(String edge, String oldSourceNode, String sourceNode)
	{
		Widget w = sourceNode != null ? findWidget(sourceNode) : null;
		ConnectionWidget conn = ((ConnectionWidget) findWidget(edge));
		if (isSuccessor(edge))
		{
			conn.setSourceAnchor(new UnidirectionalAnchor(w, Direction.RIGHT));
		}
		else if (isAlternative(edge))
		{
			conn.setSourceAnchor(new UnidirectionalAnchor(w, Direction.BOTTOM));
		}
		else
		{
			conn.setSourceAnchor(AnchorFactory.createRectangularAnchor(w));
		}
	}

	@Override
	protected void attachEdgeTargetAnchor(String edge, String oldTargetNode, String targetNode)
	{
		Widget w = targetNode != null ? findWidget(targetNode) : null;
		ConnectionWidget conn = ((ConnectionWidget) findWidget(edge));
		if (isSuccessor(edge))
		{
			conn.setTargetAnchor(new UnidirectionalAnchor(w, Direction.LEFT, edge, targetNode, Direction.TOP));
		}
		else if (isAlternative(edge))
		{
			conn.setTargetAnchor(new UnidirectionalAnchor(w, Direction.TOP, edge, targetNode, Direction.LEFT));
		}
		else
		{
			conn.setTargetAnchor(AnchorFactory.createRectangularAnchor(w));
		}
	}

	@Override
	protected Widget attachEdgeWidget(String edge)
	{
		ConnectionWidget connection = null;
		String activeTool = getCanvasActiveTool();

		if (activeTool.equals(CanvasStrings.SUCCESSOR) || activeTool.equals(CanvasStrings.ALTERNATIVE))
		{
			connection = decorator.drawConnection(activeTool, this, edge);
			connection.setRouter(getActiveRouter());
			connection.createActions(CanvasStrings.SELECT).addAction(actions.getAction("ConnSelect", this));
			connection.getActions(CanvasStrings.SELECT).addAction(actions.getAction("Reconnect", this));

			connection.getActions(CanvasStrings.SELECT).addAction(actions.getAction("FreeMoveCP", this));
			connection.getActions(CanvasStrings.SELECT).addAction(actions.getAction("AddRemoveCP", this));
			connectionLayer.addChild(connection);
			if (activeTool.equals(CanvasStrings.SUCCESSOR))
			{
				if (!isSuccessor(edge))
				{
					getSuccessors().add(edge);
				}
			}
			else if (activeTool.equals(CanvasStrings.ALTERNATIVE))
			{
				if (!isAlternative(edge))
				{
					getAlternatives().add(edge);
				}
			}
		}
		return connection;
	}

	@Override
	protected Widget attachNodeWidget(String node)
	{
		Widget widget = null;
		String activeTool = getCanvasActiveTool();

		if (node.startsWith(LineWidget.class.getCanonicalName()))
		{
			LineWidget lWidget = new LineWidget(this);
			backgroundLayer.addChild(lWidget);
			widget = lWidget;
			setShowingLines(true);
			state.getPreferences().setShowLines(true);
		}
		else if (node.startsWith(GuideLineWidget.class.getCanonicalName()))
		{
			GuideLineWidget glWidget = new GuideLineWidget(this);
			backgroundLayer.addChild(glWidget);
			widget = glWidget;
			setShowingGuide(true);
			state.getPreferences().setShowGuide(true);
		}
		else if (node.startsWith(GridWidget.class.getCanonicalName()))
		{
			GridWidget gWidget = new GridWidget(this);
			backgroundLayer.addChild(gWidget);
			widget = gWidget;
			setShowingGrid(true);
			state.getPreferences().setShowGrid(true);
		}
		else if (!activeTool.equals(CanvasStrings.SELECT))
		{
			String tool = activeTool;
			if (tool.equals(CanvasStrings.N_TERMINAL) || tool.equals(CanvasStrings.TERMINAL) || tool.equals(CanvasStrings.LEFT_SIDE) || tool.equals(CanvasStrings.LAMBDA) || tool.equals(CanvasStrings.START))
			{
				try
				{
					widget = decorator.drawIcon(activeTool, this, node);
				}
				catch (Exception e)
				{
					Log.log(Log.ERROR, this, "Could not create widget!", e);
					widget = new LabelWidgetExt(mainLayer.getScene(), node);
				}
				widget.createActions(CanvasStrings.SELECT);
				if (!tool.equals(CanvasStrings.LAMBDA))
				{
					widget.getActions(CanvasStrings.SELECT).addAction(actions.getAction("NodeHover", this));
				}
				widget.getActions(CanvasStrings.SELECT).addAction(actions.getAction("Select", this));
				if (!tool.equals(CanvasStrings.LAMBDA))
				{
					widget.getActions(CanvasStrings.SELECT).addAction(actions.getAction("Editor", this));
				}
				widget.getActions(CanvasStrings.SELECT).addAction(actions.getAction("Move", this));
				if (!tool.equals(CanvasStrings.LAMBDA))
				{
					widget.createActions(CanvasStrings.SUCCESSOR).addAction(actions.getAction("Successor", this));
				}
				if (!tool.equals(CanvasStrings.LAMBDA))
				{
					widget.createActions(CanvasStrings.ALTERNATIVE).addAction(actions.getAction("Alternative", this));
				}
				mainLayer.addChild(widget);
				if (tool.equals(CanvasStrings.LEFT_SIDE))
				{
					leftSides.add(node);
				}
				else if (tool.equals(CanvasStrings.TERMINAL))
				{
					terminals.add(node);
				}
				else if (tool.equals(CanvasStrings.N_TERMINAL))
				{
					nterminals.add(node);
				}
				else if (tool.equals(CanvasStrings.LAMBDA))
				{
					lambdas.add(node);
				}
				else if (tool.equals(CanvasStrings.START))
				{
					start.add(node);
				}
			}
			else if (tool.equals(CanvasStrings.LABEL))
			{
				widget = new LabelWidgetExt(mainLayer.getScene(), "Double Click Here to Edit");
				widget.createActions(CanvasStrings.SELECT).addAction(actions.getAction("LabelHover", this));
				widget.getActions(CanvasStrings.SELECT).addAction(actions.getAction("SelectLabel", this));
				widget.getActions(CanvasStrings.SELECT).addAction(actions.getAction("Editor", this));
				widget.getActions(CanvasStrings.SELECT).addAction(actions.getAction("Move", this));
				labels.add(node);
				mainLayer.addChild(widget);
			}
			if (widget != null)
			{
				if (widget instanceof TypedWidget)
				{
					((TypedWidget) widget).setType(tool);
				}
			}
		}
		return widget;
	}

	@Override
	public Router getActiveRouter()
	{
		return activeRouter;
	}

	@Override
	public LayerWidget getBackgroundLayer()
	{
		return backgroundLayer;
	}

	@Override
	public CanvasDecorator getCanvasDecorator()
	{
		return decorator;
	}

	@Override
	public CanvasState getCanvasState()
	{
		return state;
	}

	@Override
	public LayerWidget getConnectionLayer()
	{
		return connectionLayer;
	}

	@Override
	public String getConnStrategy()
	{
		return this.connStrategy;
	}

	@Override
	public LayerWidget getInterractionLayer()
	{
		return interractionLayer;
	}

	@Override
	public Collection<?> getLabels()
	{
		return labels;
	}

	@Override
	public LayerWidget getMainLayer()
	{
		return mainLayer;
	}

	@Override
	public String getMoveStrategy()
	{
		return moveStrategy;
	}

	@Override
	public String getNodeType(Object node)
	{
		Widget w = findWidget(node);
		if (w != null)
		{
			if (w instanceof LabelWidgetExt)
			{
				return ((LabelWidgetExt) w).getType();
			}
		}
		return null;
	}

	@Override
	public void init(CanvasState state)
	{
		super.init(state);
		this.state = state;

		addChild(backgroundLayer);
		addChild(mainLayer);
		addChild(connectionLayer);
		addChild(interractionLayer);

		createActions(CanvasStrings.SELECT).addAction(actions.getAction(WidgetActionFactory.SELECT, this));
		getActions().addAction(actions.getAction(WidgetActionFactory.SELECT_LABEL, this));
		getActions().addAction(actions.getAction(WidgetActionFactory.CREATE, this));
		getActions().addAction(actions.getAction(WidgetActionFactory.POPUP_MENU_MAIN, this));
		getActions().addAction(actions.getAction(WidgetActionFactory.NODE_HOVER, this));
		getActions().addAction(actions.getAction(WidgetActionFactory.LABEL_HOVER, this));
		getActions().addAction(actions.getAction(WidgetActionFactory.MOUSE_CENTERED_ZOOM, this));
		getActions().addAction(actions.getAction(WidgetActionFactory.PAN, this));
		getActions().addAction(actions.getAction(WidgetActionFactory.RECTANGULAR_SELECT, this));
		getActions().addAction(actions.getAction(WidgetActionFactory.DELETE, this));
		getActions().addAction(actions.getAction(WidgetActionFactory.COPY_PASTE, this));
		setActiveTool(cursor);

		if (state.getPreferences().getConnectionStrategy() != null)
		{
			setConnStrategy(state.getPreferences().getConnectionStrategy());
		}
		else
		{
			setConnStrategy(connStrategy);
		}
		if (state.getPreferences().getMoveStrategy() != null)
		{
			setMoveStrategy(state.getPreferences().getMoveStrategy());
		}
		else
		{
			setMoveStrategy(moveStrategy);
		}
	}

	@Override
	public boolean isLabel(Object o)
	{
		return labels.contains(o);
	}

	public boolean isLambda(Object o)
	{
		return this.lambdas.contains(o);
	}

	public boolean isLeftSide(Object o)
	{
		return this.leftSides.contains(o);
	}

	@Override
	public boolean isNode(Object o)
	{
		if (!isLabel(o) && (isTerminal(o) || isNonTerminal(o) || isLambda(o) || isLeftSide(o) || isStart(o)))
		{
			return super.isNode(o);
		}
		return false;
	}

	public boolean isNonTerminal(Object o)
	{
		return this.nterminals.contains(o);
	}

	public boolean isStart(Object o)
	{
		return this.start.contains(o);
	}

	public boolean isTerminal(Object o)
	{
		return this.terminals.contains(o);
	}

	@Override
	public void removeEdgeSafely(String edge)
	{
		if (isEdge(edge))
		{
			if (getSuccessors().contains(edge))
			{
				getSuccessors().remove(edge);
			}
			if (getAlternatives().contains(edge))
			{
				getAlternatives().remove(edge);
			}
			TargetDirections.removeConnection(edge, (ConnectionWidget) findWidget(edge));
			super.removeEdge(edge);
		}
	}

	@Override
	public void removeNodeSafely(String node)
	{
		if (isLabel(node))
		{
			labels.remove(node);
		}
		super.removeNode(node);
		if (node.startsWith(GridWidget.class.getCanonicalName()))
		{
			setShowingGrid(false);
		}
		else if (node.startsWith(LineWidget.class.getCanonicalName()))
		{
			setShowingLines(false);
		}
		else if (node.startsWith(GuideLineWidget.class.getCanonicalName()))
		{
			setShowingGuide(false);
		}
	}

	/**
	 * Direct select method, not to be called after direct user action
	 */
	@Override
	public void select(Object object)
	{
		select(object, false);
	}

	@Override
	public void select(Object object, boolean invertSelection)
	{
		Widget w;
		if ((w = findWidget(object)) != null)
		{
			NodeSelectProvider nsp = new NodeSelectProvider(this);
			if (!getSelectedObjects().contains(w))
			{
				if (nsp.isSelectionAllowed(w, null, invertSelection))
					nsp.select(w, null, invertSelection);
				validate();
			}
		}
	}

	@Override
	public void setConnStrategy(String policy)
	{
		if (policy != null)
		{
			state.getPreferences().setConnectionStrategy(policy);
			if (policy.equals(CanvasStrings.R_ORTHOGONAL) || policy.equals(CanvasStrings.R_FREE) || policy.equals(CanvasStrings.R_DIRECT))
			{
				if (!policy.equals(connStrategy) || activeRouter == null)
				{
					connStrategy = policy;
					if (policy.equals(CanvasStrings.R_ORTHOGONAL))
					{
						activeRouter = RouterFactory.createOrthogonalSearchRouter(mainLayer);
					}
					else if (policy.equals(CanvasStrings.R_DIRECT))
					{
						activeRouter = RouterFactory.createDirectRouter();
					}
					else if (policy.equals(CanvasStrings.R_FREE))
					{
						activeRouter = RouterFactory.createFreeRouter();
					}
					for (String edge : getEdges())
					{
						ConnectionWidget cw = (ConnectionWidget) findWidget(edge);
						if (cw != null)
						{
							cw.setRouter(activeRouter);
							cw.calculateRouting();
						}
					}
					repaint();
				}
			}
		}
	}

	@Override
	public void setMoveStrategy(String strategy)
	{
		MoveTracker mt = new MoveTracker(this);
		if (strategy != null)
		{
			state.getPreferences().setMoveStrategy(strategy);
			if (strategy.equals(CanvasStrings.M_ALIGN) || strategy.equals(CanvasStrings.M_SNAP) || strategy.equals(CanvasStrings.M_FREE) || strategy.equals(CanvasStrings.M_LINES))
			{
				if (moveStrategy == null || !moveStrategy.equals(strategy))
				{
					moveStrategy = strategy;
					mt.notifyObservers(strategy);
				}
			}
		}
	}

	// /* ### PRIVATE NESTED CLASSES ############################ */

	@Override
	public void updateState(CanvasState state)
	{
		setConnStrategy(state.getPreferences().getConnectionStrategy());
		setMoveStrategy(state.getPreferences().getMoveStrategy());

		ArrayList<String> nodes = new ArrayList<String>();
		nodes.addAll(state.getNodes());
		for (String n : nodes)
		{
			if (!this.getNodes().contains(n))
			{
				Node n1 = state.findNode(n);
				String oldTool = getCanvasActiveTool();
				setActiveTool(state.getType(n));
				Widget w1 = addNode(n);
				if (n1.getLocation() != null)
					w1.setPreferredLocation(mainLayer.convertLocalToScene(n1.getLocation()));
				if (w1 instanceof LabelWidgetExt)
				{
					((LabelWidgetExt) w1).setLabel(n1.getTitle());

				}
				if (w1 instanceof MarkedWidget)
				{
					((MarkedWidget) w1).setMark(n1.getMark());
				}
				if (w1 instanceof TypedWidget)
				{
					((TypedWidget) w1).setType(n1.getType());
				}
				setActiveTool(oldTool);
			}
		}

		List<String> conn = state.getConnections();
		for (String e : conn)
		{
			if (!this.getEdges().contains(e))
			{
				Connection c1 = state.findConnection(e);
				String oldTool = getCanvasActiveTool();
				setActiveTool(state.getType(e));
				ConnectionWidget cnn = (ConnectionWidget) addEdge(e);

				setEdgeSource(e, c1.getSource());
				setEdgeTarget(e, c1.getTarget());

				cnn.setRoutingPolicy(RoutingPolicy.DISABLE_ROUTING_UNTIL_END_POINT_IS_MOVED);
				cnn.setControlPoints(c1.getPoints(), true);
				setActiveTool(oldTool);

			}
		}

		if (state.getPreferences().isShowGrid())
		{
			GridProvider.getInstance(this).setVisible(true);
		}
		if (state.getPreferences().isShowGuide())
		{
			LineProvider.getInstance(this).setGuideVisible(true);
		}
		if (state.getPreferences().isShowLines())
		{
			LineProvider.getInstance(this).populateCanvas();
		}

		removeObjectSceneListener(this.state, ObjectSceneEventType.OBJECT_ADDED);
		removeObjectSceneListener(this.state, ObjectSceneEventType.OBJECT_REMOVED);
		addObjectSceneListener(state, ObjectSceneEventType.OBJECT_ADDED);
		addObjectSceneListener(state, ObjectSceneEventType.OBJECT_REMOVED);
		this.state = state;

		state.update(this);

		validate();
	}
}
